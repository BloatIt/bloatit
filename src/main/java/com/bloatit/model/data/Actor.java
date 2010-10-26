package com.bloatit.model.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.bloatit.model.data.util.SessionManger;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Actor {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Basic(optional = false)
	@Column(unique = true, updatable = false)
	private String login;
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

	
	protected Actor() {
	    super();
    }

	protected Actor(String login, String email) {
		super();
		this.dateJoin = new Date();
		this.login = login;
		this.email = email;
		this.internalAccount = new InternalAccount(this);
	}

	/**
	 * This method use a HQL request. If you intend to use "getByLogin", "exist"
	 * is useless. (In that case you'd better test if getByLogin != null, to
	 * minimize the number of HQL request).
	 */
	public static boolean exist(String login) {
		Session session = SessionManger.getSessionFactory().getCurrentSession();
		// TODO use the count() in HQL
		Query q = session.createQuery("from com.bloatit.model.data.Actor as m where login = :login");
		q.setString("login", login);
		return (q.uniqueResult() != null);
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
		Query q = SessionManger.getSessionFactory().getCurrentSession()
		        .createQuery("from com.bloatit.model.data." + className + " as x where x.actor = :author");
		q.setEntity("author", this);
		return new QueryCollection<T>(q);
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

	public Integer getId() {
		return id;
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

	protected void setId(Integer id) {
		this.id = id;
	}

}
