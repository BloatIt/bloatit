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

import com.bloatit.common.FatalErrorException;
import com.bloatit.model.data.util.SessionManager;

/**
 * DaoActor is the base class of any user that can make money transaction. Each
 * actor has
 * a unique name, an email, and an internalAccount.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DaoActor {

    /**
     * Because of the different inheritance strategy we cannot inherit from
     * identifiable.
     * So we have to have an id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * The login represent the user login and the group name. It must be unique
     * (means
     * that a group cannot have the same name as a user)
     */
    @Basic(optional = false)
    @Column(unique = true, updatable = false)
    private String login;
    @Basic(optional = false)
    private String email;
    @Basic(optional = false)
    private Date dateCreation;

    @OneToOne(optional = false)
    @Cascade(value = { CascadeType.ALL })
    private DaoInternalAccount internalAccount;

    @OneToOne
    @Cascade(value = { CascadeType.ALL })
    private DaoExternalAccount externalAccount;

    /**
     * Initialize the creation date to now.
     * 
     * @param login is the login or name of this actor
     * @param email is the email of this actor. (No check is performed on the
     *        correctness
     *        of this email address)
     * @throws NullPointerException if login or mail is null.
     */
    protected DaoActor(final String login, final String email) {
        super();
        if (login == null || email == null) {
            throw new NullPointerException();
        }
        dateCreation = new Date();
        this.login = login;
        this.email = email;
        internalAccount = new DaoInternalAccount(this);
    }

    /**
     * This method use a HQL request. If you intend to use "getByLogin" or
     * "getByName",
     * "exist" is useless. (In that case you'd better test if getByLogin !=
     * null, to
     * minimize the number of HQL request).
     */
    public static boolean exist(final String login) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query q = session.createQuery("select count(*) from com.bloatit.model.data.DaoActor as m where login = :login");
        q.setString("login", login);
        return ((Long) q.uniqueResult()) > 0;
    }

    public String getEmail() {
        return email;
    }

    /**
     * This method is used by hibernate. You can use it if you want to change
     * the email.
     * (No check is performed on the correctness of the new email)
     * 
     * @param email the new email.
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public DaoInternalAccount getInternalAccount() {
        return internalAccount;
    }

    public DaoExternalAccount getExternalAccount() {
        return externalAccount;
    }

    /**
     * Set the external account for this actor.
     * 
     * @param externalAccount the new external account for this actor
     * @throws FatalErrorException if the externalAccount.getActor() != this
     */
    public void setExternalAccount(final DaoExternalAccount externalAccount) {
        if (externalAccount.getActor() != this) {
            throw new FatalErrorException("Add an external account to the wrong user.", null);
        }
        this.externalAccount = externalAccount;
    }

    public Integer getId() {
        return id;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected DaoActor() {
        super();
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setInternalAccount(final DaoInternalAccount InternalAccount) {
        internalAccount = InternalAccount;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setLogin(final String login) {
        this.login = login;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setDateCreation(final Date dateJoin) {
        dateCreation = dateJoin;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setId(final Integer id) {
        this.id = id;
    }
}
