package com.bloatit.data;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.metadata.ClassMetadata;

import com.bloatit.common.Log;
import com.bloatit.data.DaoJoinGroupInvitation.State;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * Ok if you need a comment to understand what is a member, then I cannot do anything for
 * you ...
 */
@Entity
public final class DaoMember extends DaoActor {

    public enum Role {
        NORMAL, PRIVILEGED, REVIEWER, MODERATOR, ADMIN
    }

    private String fullname;
    @Basic(optional = false)
    private String password;
    @Basic(optional = false)
    private Integer karma;
    @Basic(optional = false)
    @Enumerated
    private Role role;

    @Basic(optional = false)
    @Column(unique = true)
    private String email;

    @Basic(optional = false)
    private Locale locale;

    // this property is for hibernate mapping.
    @OneToMany(mappedBy = "member")
    @Cascade(value = { CascadeType.ALL })
    private final Set<DaoGroupMembership> groupMembership = new HashSet<DaoGroupMembership>(0);

    /**
     * Create a member. The member login must be unique, and you cannot change it.
     *
     * @param login The login of the member.
     * @param password The password of the member (md5 ??)
     * @param locale the locale of the user.
     * @return The newly created DaoMember
     * @throws HibernateException If there is any problem connecting to the db. Or if the
     *         member as a non unique login. If an exception is thrown then the
     *         transaction is rolled back and reopened.
     */
    public static DaoMember createAndPersist(final String login, final String password, final String email, final Locale locale) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoMember theMember = new DaoMember(login, password, email, locale);
        try {
            session.save(theMember);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return theMember;
    }

    /**
     * Find a DaoMember using its login.
     *
     * @param login the member login.
     * @return null if not found. (or if login == null)
     */
    public static DaoMember getByLogin(final String login) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query q = session.createQuery("from com.bloatit.data.DaoMember where login = :login");
        q.setString("login", login);
        return (DaoMember) q.uniqueResult();
    }

    /**
     * Find a DaoMember using its login, and password. This method can be use to
     * authenticate a use.
     *
     * @param login the member login.
     * @param password the password of the member "login". It is a string corresponding to
     *        the string in the database. This method does not perform any sha1 or md5
     *        transformation.
     * @return null if not found. (or if login == null or password == null)
     */
    public static DaoMember getByLoginAndPassword(final String login, final String password) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query q = session.createQuery("from com.bloatit.data.DaoMember where login = :login and password = :password");
        q.setString("login", login);
        q.setString("password", password);
        return (DaoMember) q.uniqueResult();
    }

    /**
     * You have to use CreateAndPersist instead of this constructor
     *
     * @param locale is the locale in which this user is. (The country and language.)
     * @see DaoMember#createAndPersist(String, String, String, Locale)
     */
    private DaoMember(final String login, final String password, final String email, final Locale locale) {
        super(login);
        if (locale == null) {
            throw new NonOptionalParameterException("locale cannot be null!");
        }
        if (email == null) {
            throw new NonOptionalParameterException("email cannot be null!");
        }
        if (email.isEmpty()) {
            throw new NonOptionalParameterException("email cannot be empty!");
        }
        if (password == null) {
            throw new NonOptionalParameterException("Password cannot be null!");
        }
        if (password.isEmpty()) {
            throw new NonOptionalParameterException("Password cannot be empty!");
        }
        setLocale(locale);
        this.email = email;
        this.role = Role.NORMAL;
        this.password = password;
        this.karma = 0;
        this.fullname = "";
    }

    /**
     * @param aGroup the group in which this member is added.
     * @param isAdmin tell if the member is an admin of the group 'aGroup'
     */
    public void addToGroup(final DaoGroup aGroup, final boolean isAdmin) {
        DaoGroupMembership daoGroupMembership = new DaoGroupMembership(this, aGroup, isAdmin);
        if (groupMembership.contains(daoGroupMembership)) {
            throw new FatalErrorException("This member is already in the group: " + aGroup.getId());
        }
        groupMembership.add(daoGroupMembership);
    }

    /**
     * @param aGroup the group from which this member is removed.
     */
    public void removeFromGroup(final DaoGroup aGroup) {
        final DaoGroupMembership link = DaoGroupMembership.get(aGroup, this);
        if (link != null) {
            groupMembership.remove(link);
            aGroup.getGroupMembership().remove(link);
            SessionManager.getSessionFactory().getCurrentSession().delete(link);
        } else {
            Log.data().error("Try to remove a non existing DaoGroupMembership: group = " + aGroup.getId() + " member = " + getId());
        }
    }

    /**
     * [ Maybe it could be cool to have a parameter to list all the PUBLIC or PROTECTED
     * groups. ]
     *
     * @return All the groups this member is in. (Use a HQL query)
     */
    public PageIterable<DaoGroup> getGroups() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query filter = session.createFilter(getGroupMembership(), "select this.bloatitGroup order by login");
        final Query count = session.createFilter(getGroupMembership(), "select count(*)");
        return new QueryCollection<DaoGroup>(filter, count);
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(final String firstname) {
        fullname = firstname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * @return All the demands created by this member.
     */
    public PageIterable<DaoDemand> getDemands() {
        return getUserContent(DaoDemand.class);
    }

    /**
     * @return All the kudos created by this member.
     */
    public PageIterable<DaoKudos> getKudos() {
        return getUserContent(DaoKudos.class);
    }

    /**
     * @return All the Transactions created by this member.
     */
    public PageIterable<DaoContribution> getTransactions() {
        return getUserContent(DaoContribution.class);
    }

    /**
     * @return All the Comments created by this member.
     */
    public PageIterable<DaoComment> getComments() {
        return getUserContent(DaoComment.class);
    }

    /**
     * @return All the Offers created by this member.
     */
    public PageIterable<DaoOffer> getOffers() {
        return getUserContent(DaoOffer.class);
    }

    /**
     * @return All the Translations created by this member.
     */
    public PageIterable<DaoTranslation> getTranslations() {
        return getUserContent(DaoTranslation.class);
    }

    /**
     * @return All the received invitation to join a group which are in a specified state
     */
    public PageIterable<DaoJoinGroupInvitation> getReceivedInvitation(final State state) {
        return new QueryCollection<DaoJoinGroupInvitation>(
                "from com.bloatit.data.JoinGroupInvitation as j where j.reciever = :reciever and j.state = :state  ").setEntity("reciever", this)
                .setEntity("state", state);
    }

    /**
     * @return All the sent invitation to join a group which are in a specified state
     */
    public PageIterable<DaoJoinGroupInvitation> getSentInvitation(final State state) {
        return new QueryCollection<DaoJoinGroupInvitation>(
                "from com.bloatit.data.JoinGroupInvitation as j where j.sender = :sender and j.state = :state").setEntity("sender", this)
                .setEntity("state", state);
    }

    /**
     * Base method to all the get something created by the user.
     */
    private <T extends DaoUserContent> PageIterable<T> getUserContent(final Class<T> theClass) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(theClass);
        final QueryCollection<T> q = new QueryCollection<T>("from " + meta.getEntityName() + " as x where x.member = :author");
        q.setEntity("author", this);
        return q;
    }

    /**
     * @return if the current member is in the "group".
     */
    public boolean isInGroup(final DaoGroup group) {
        final Query q = SessionManager.getSessionFactory().getCurrentSession().createQuery( //
        "SELECT count(*) " + //
                "FROM com.bloatit.data.DaoMember m " + //
                "JOIN m.groupMembership AS gm " + //
                "JOIN gm.bloatitGroup AS g " + //
                "WHERE m = :member AND g = :group");
        q.setEntity("member", this);
        q.setEntity("group", group);
        return ((Long) q.uniqueResult()) >= 1;
    }

    public void addToKarma(final int value) {
        karma += value;
    }

    public Integer getKarma() {
        return karma;
    }

    public void setRole(final Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * used by DaoGroup
     */
    protected Set<DaoGroupMembership> getGroupMembership() {
        return groupMembership;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoMember() {
        super();
    }
}
