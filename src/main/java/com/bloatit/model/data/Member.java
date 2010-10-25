package com.bloatit.model.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.model.data.util.SessionManger;

// member is a SQL keyword (in some specific implementations)
@Entity(name = "bloatit_member")
@NamedQuery(name = "getGroups", query = "select g from com.bloatit.model.data.Member m " + "join m.groupMembership as gm " + "join gm.group as g "
        + "where m = :member")
public class Member extends Identifiable {

    @Basic(optional = false)
    @Column(unique = true, updatable = false)
    private String login;
    @Basic(optional = false)
    private String password;
    private String firstname;
    private String lastname;
    @Basic(optional = false)
    private String email;
    @Basic(optional = false)
    private Date dateJoin;

    @OneToOne(optional = false)
    @Cascade(value = { CascadeType.ALL })
    private InternalAccount internalAccount;

    @OneToOne
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private ExternalAccount externalAccount;

    // this property is for hibernate mapping.
    @OneToMany(mappedBy = "member")
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private Set<GroupMembership> groupMembership = new HashSet<GroupMembership>(0);

    /**
     * Create a member. The member login must be unique, and you cannot change
     * it.
     * 
     * @param login The login of the member.
     * @param password The password of the member (md5 ??)
     * @return The newly created Member
     * @throws HibernateException If there is any problem connecting to the db. Or if the
     *             member as a non unique login. If an exception is thrown then
     *             the transaction is rolled back and reopened.
     * 
     */
    public static Member createAndPersist(String login, String password, String email) throws HibernateException {
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        Member theMember = new Member(login, password, email);
        try {
            session.save(theMember);
        } catch (HibernateException e) {
            System.out.println(e);
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return theMember;
    }

    /**
     * Find a Member using its login.
     * 
     * @param login the member login.
     * @return null if not found.
     */
    public static Member getByLogin(String login) {
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        Query q = session.createQuery("from com.bloatit.model.data.Member where login = :login");
        q.setString("login", login);
        return (Member) q.uniqueResult();
    }

    /**
     * This method use a HQL request. If you intend to use "getByLogin", "exist"
     * is useless. (In that case you'd better test if getByLogin != null, to
     * minimize the number of HQL request).
     */
    public static boolean exist(String login) {
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        // TODO use the count() in HQL
        Query q = session.createQuery("from com.bloatit.model.data.Member as m where login = :login");
        q.setString("login", login);
        return (q.uniqueResult() != null);
    }

    /**
     * @param aGroup the group in which this member is added.
     * @param isAdmin tell if the member is an admin of the group 'aGroup'
     */
    public void addToGroup(Group aGroup, boolean isAdmin) {
        groupMembership.add(new GroupMembership(this, aGroup, isAdmin));
    }

    /**
     * @param aGroup the group from which this member is removed.
     */
    public void removeFromGroup(Group aGroup) {
        GroupMembership link = GroupMembership.get(aGroup, this);
        groupMembership.remove(link);
        aGroup.getGroupMembership().remove(link);
    }

    public QueryCollection<Group> getGroups() {
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        Query q = session.getNamedQuery("getGroups");
        q.setParameter("member", this);
        return new QueryCollection<Group>(q);
    }

    public QueryCollection<Demand> getDemands() {
        return getUserContent(Demand.class, "Demand");
    }

    public QueryCollection<Kudos> getKudos() {
        return getUserContent(Kudos.class, "Kudos");
    }

    public QueryCollection<Specification> getSpecifications() {
        return getUserContent(Specification.class, "Specification");
    }

    public QueryCollection<Contribution> getTransactions() {
        return getUserContent(Contribution.class, "Transaction");
    }

    public QueryCollection<Comment> getComments() {
        return getUserContent(Comment.class, "Comment");
    }

    public QueryCollection<Offer> getOffers() {
        return getUserContent(Offer.class, "Offer");
    }

    public QueryCollection<Translation> getTranslations() {
        return getUserContent(Translation.class, "Translation");
    }

    private <T> QueryCollection<T> getUserContent(Class<T> theClass, String className) {
        Query q = SessionManger.getSessionFactory()
                               .getCurrentSession()
                               .createQuery("from com.bloatit.model.data." + className + " as x where x.author = :author");
        q.setEntity("author", this);
        return new QueryCollection<T>(q);
    }

    protected Member() {
        super();
    }

    protected Member(String login, String password, String email) {
        super();
        this.login = login;
        this.password = password;
        this.email = email;
        this.internalAccount = new InternalAccount(this);
        this.dateJoin = new Date();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public Date getDateJoin() {
        return dateJoin;
    }

    public InternalAccount getInternalAccount() {
        return internalAccount;
    }

    public ExternalAccount getExternalAccount() {
        return externalAccount;
    }

    public void setExternalAccount(ExternalAccount externalAccount) {
        this.externalAccount = externalAccount;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setInternalAccount(InternalAccount internalAccount) {
        this.internalAccount = internalAccount;
    }

    protected void setLogin(String login) {
        this.login = login;
    }

    protected void setDateJoin(Date dateJoin) {
        this.dateJoin = dateJoin;
    }

    protected void setGroupMembership(Set<GroupMembership> groupMembership) {
        this.groupMembership = groupMembership;
    }

    protected Set<GroupMembership> getGroupMembership() {
        return groupMembership;
    }

}
