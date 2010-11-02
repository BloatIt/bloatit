package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.bloatit.common.PageIterable;
import com.bloatit.model.data.DaoJoinGroupInvitation.State;
import com.bloatit.model.data.util.SessionManager;

// member is a SQL keyword (in some specific implementations)
@Entity
public class DaoMember extends DaoActor {

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

	// this property is for hibernate mapping.
	@OneToMany(mappedBy = "member")
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	private Set<DaoGroupMembership> groupMembership = new HashSet<DaoGroupMembership>(0);

	/**
	 * Create a member. The member login must be unique, and you cannot change
	 * it.
	 * 
	 * @param login
	 *            The login of the member.
	 * @param password
	 *            The password of the member (md5 ??)
	 * @return The newly created DaoMember
	 * @throws HibernateException
	 *             If there is any problem connecting to the db. Or if the
	 *             member as a non unique login. If an exception is thrown then
	 *             the transaction is rolled back and reopened.
	 * 
	 */
	public static DaoMember createAndPersist(String login, String password, String email) throws HibernateException {
		final Session session = SessionManager.getSessionFactory().getCurrentSession();
		final DaoMember theMember = new DaoMember(login, password, email);
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
	 * @param login
	 *            the member login.
	 * @return null if not found.
	 */
	public static DaoMember getByLogin(String login) {
		final Session session = SessionManager.getSessionFactory().getCurrentSession();
		final Query q = session.createQuery("from com.bloatit.model.data.DaoMember where login = :login");
		q.setString("login", login);
		return (DaoMember) q.uniqueResult();
	}

	public static DaoMember getByLoginAndPassword(String login, String password) {
		final Session session = SessionManager.getSessionFactory().getCurrentSession();
		final Query q = session.createQuery("from com.bloatit.model.data.DaoMember where login = :login and password = :password");
		q.setString("login", login);
		q.setString("password", password);
		return (DaoMember) q.uniqueResult();
	}

	protected DaoMember() {
		super();
	}

	protected DaoMember(String login, String password, String email) {
		super(login, email);
		setRole(Role.NORMAL);
		this.password = password;
		this.karma = 0;
	}

	/**
	 * @param aGroup
	 *            the group in which this member is added.
	 * @param isAdmin
	 *            tell if the member is an admin of the group 'aGroup'
	 */
	public void addToGroup(DaoGroup aGroup, boolean isAdmin) {
		groupMembership.add(new DaoGroupMembership(this, aGroup, isAdmin));
	}

	/**
	 * @param aGroup
	 *            the group from which this member is removed.
	 */
	public void removeFromGroup(DaoGroup aGroup) {
		final DaoGroupMembership link = DaoGroupMembership.get(aGroup, this);
		groupMembership.remove(link);
		aGroup.getGroupMembership().remove(link);
	}

	public PageIterable<DaoGroup> getGroups() {
		final Session session = SessionManager.getSessionFactory().getCurrentSession();
		Query filter = session.createFilter(getGroupMembership(), "select this.group order by login");
		final Query count = session.createFilter(getGroupMembership(), "select count(*)");
		return new QueryCollection<DaoGroup>(filter, count);
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String firstname) {
		this.fullname = firstname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PageIterable<DaoDemand> getDemands() {
		return getUserContent(DaoDemand.class, "DaoDemand");
	}

	public PageIterable<DaoKudos> getKudos() {
		return getUserContent(DaoKudos.class, "DaoKudos");
	}

	public PageIterable<DaoSpecification> getSpecifications() {
		return getUserContent(DaoSpecification.class, "DaoSpecification");
	}

	public PageIterable<DaoContribution> getTransactions() {
		return getUserContent(DaoContribution.class, "DaoTransaction");
	}

	public PageIterable<DaoComment> getComments() {
		return getUserContent(DaoComment.class, "DaoComment");
	}

	public PageIterable<DaoOffer> getOffers() {
		return getUserContent(DaoOffer.class, "DaoOffer");
	}

	public PageIterable<DaoTranslation> getTranslations() {
		return getUserContent(DaoTranslation.class, "DaoTranslation");
	}

	// TODO test
	public PageIterable<DaoJoinGroupInvitation> getRecievedInvitation(State state) {
		final Query q = SessionManager.getSessionFactory().getCurrentSession()
		        .createQuery("from com.bloatit.model.data.JoinGroupInvitation as j where j.reciever = :reciever and j.state = :state");
		q.setEntity("reciever", this);
		q.setEntity("state", state);
		return new QueryCollection<DaoJoinGroupInvitation>(q);
	}

	// TODO test
	public PageIterable<DaoJoinGroupInvitation> getSentInvitation(State state) {
		final Query q = SessionManager.getSessionFactory().getCurrentSession()
		        .createQuery("from com.bloatit.model.data.JoinGroupInvitation as j where j.sender = :sender and j.state = :state");
		q.setEntity("sender", this);
		q.setEntity("state", state);
		return new QueryCollection<DaoJoinGroupInvitation>(q);
	}

	private <T> PageIterable<T> getUserContent(Class<T> theClass, String className) {
		final Query q = SessionManager.getSessionFactory().getCurrentSession()
		        .createQuery("from com.bloatit.model.data." + className + " as x where x.member = :author");
		q.setEntity("author", this);
		return new QueryCollection<T>(q);
	}

	public boolean isInGroup(DaoGroup group) {
		final Query q = SessionManager
		        .getSessionFactory()
		        .getCurrentSession()
		        .createQuery("select count(*) from com.bloatit.model.data.DaoMember m join m.groupMembership as gm "
		                + "join gm.group as g where m = :member and g = :group");
		q.setEntity("member", this);
		q.setEntity("group", group);
		return ((Long) q.uniqueResult()) >= 1;
	}

	public void addToKarma(int value) {
		karma += value;
	}

	public Integer getKarma() {
		return karma;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

	// ======================================================================
	// For hibernate mapping
	// ======================================================================

	protected void setKarma(Integer karama) {
		this.karma = karama;
	}

	protected void setGroupMembership(Set<DaoGroupMembership> GroupMembership) {
		this.groupMembership = GroupMembership;
	}

	protected Set<DaoGroupMembership> getGroupMembership() {
		return groupMembership;
	}

}
