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

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DaoActor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

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
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private DaoExternalAccount externalAccount;

    protected DaoActor() {
        super();
    }

    protected DaoActor(String login, String email) {
        super();
        dateCreation = new Date();
        this.login = login;
        this.email = email;
        internalAccount = new DaoInternalAccount(this);
    }

    /**
     * This method use a HQL request. If you intend to use "getByLogin", "exist"
     * is useless. (In that case you'd better test if getByLogin != null, to
     * minimize the number of HQL request).
     */
    public static boolean exist(String login) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        // TODO use the count() in HQL
        final Query q = session.createQuery("select count(*) from com.bloatit.model.data.DaoActor as m where login = :login");
        q.setString("login", login);
        return ((Long) q.uniqueResult()) > 0;
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

    public Date getDateCreation() {
        return dateCreation;
    }

    public DaoInternalAccount getInternalAccount() {
        return internalAccount;
    }

    public DaoExternalAccount getExternalAccount() {
        return externalAccount;
    }

    public void setExternalAccount(DaoExternalAccount externalAccount) {
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

    protected void setInternalAccount(DaoInternalAccount InternalAccount) {
        internalAccount = InternalAccount;
    }

    protected void setLogin(String login) {
        this.login = login;
    }

    protected void setDateCreation(Date dateJoin) {
        dateCreation = dateJoin;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

}
