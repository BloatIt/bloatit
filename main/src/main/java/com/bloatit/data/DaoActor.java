//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import org.hibernate.Query;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.common.Log;
import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * DaoActor is the base class of any user that can make money transaction. Each
 * actor has a unique name, an email, and an internalAccount.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
// @formatter:off
@NamedQueries(value = { @NamedQuery(
                           name = "actor.byLogin.size",
                           query = "select count(*) from DaoActor where login = :login"),
                       @NamedQuery(
                           name = "actor.byEmail.size",
                           query = "select count(*) from DaoActor where email = :email"),
                       @NamedQuery(
                           name = "actor.getBankTransaction",
                           query = "from DaoBankTransaction where author = :author order by creationDate DESC"),
                       @NamedQuery(
                           name = "actor.getBankTransaction.size",
                           query = "select count(*) from DaoBankTransaction where author = :author order by creationDate DESC") 
                     }
             )
// @formatter:on
public abstract class DaoActor extends DaoIdentifiable {

    /**
     * The login represent the user login and the group name. It must be unique
     * (means that a group cannot have the same name as a user)
     */
    @Basic(optional = false)
    @Column(unique = true, updatable = false)
    private String login;

    @Basic(optional = false)
    private Date dateCreation;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @Cascade(value = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private DaoInternalAccount internalAccount;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @Cascade(value = { CascadeType.ALL })
    private DaoExternalAccount externalAccount;

    // ======================================================================
    // HQL static requests.
    // ======================================================================

    /**
     * This method use a HQL request. If you intend to use "getByLogin" or
     * "getByName", "exist" is useless. (In that case you'd better test if
     * getByLogin != null, to minimize the number of HQL request).
     */
    public static boolean loginExists(final String login) {
        final Query q = SessionManager.getNamedQuery("actor.byLogin.size").setString("login", login);
        return ((Long) q.uniqueResult()) > 0;
    }

    /**
     * This method use a HQL request.
     */
    public static boolean emailExists(final String email) {
        final Query q = SessionManager.getNamedQuery("actor.byEmail.size").setString("email", email);
        return ((Long) q.uniqueResult()) > 0;
    }

    // ======================================================================
    // Construction.
    // ======================================================================

    /**
     * Create a new DaoActor. Initialize the creation date to now. Create a new
     * {@link DaoInternalAccount} and a new {@link DaoExternalAccount}.
     * 
     * @param login is the login or name of this actor. It must be non null, and
     *            unique.
     * @throws NonOptionalParameterException if login or mail is null.
     */
    protected DaoActor(final String login) {
        super();
        if (login == null) {
            Log.data().fatal("Login null!");
            throw new NonOptionalParameterException();
        }
        if (login.isEmpty()) {
            Log.data().fatal("Login empty!");
            throw new NonOptionalParameterException("login cannot be empty");
        }
        this.dateCreation = new Date();
        this.login = login;
        this.internalAccount = new DaoInternalAccount(this);
        this.externalAccount = new DaoExternalAccount(this);
    }

    /**
     * @return the email of this actor.
     */
    public abstract String getContact();

    /**
     * No check is performed on the correctness of the new email.
     * 
     * @param email the new email.
     */
    public abstract void setContact(String email);

    // ======================================================================
    // Getters.
    // ======================================================================

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

    public String getLogin() {
        return this.login;
    }

    public Date getDateCreation() {
        return (Date) this.dateCreation.clone();
    }

    public DaoInternalAccount getInternalAccount() {
        return this.internalAccount;
    }

    public DaoExternalAccount getExternalAccount() {
        return this.externalAccount;
    }

    /**
     * @return all the <code>DaoBankTransaction</code> created by
     *         <code>this</code>, order by <code>creationDate</code>, most
     *         recent first.
     */
    public PageIterable<DaoBankTransaction> getBankTransactions() {
        return new QueryCollection<DaoBankTransaction>("actor.getBankTransactions").setEntity("author", this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * For hibernate mapping. Do not use it.
     */
    protected DaoActor() {
        super();
    }

    // ======================================================================
    // equals and hashCode.
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.login == null) ? 0 : this.login.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj.getClass().equals(getClass()))) {
            return false;
        }
        final DaoActor other = (DaoActor) obj;
        if (this.login == null) {
            if (other.login != null) {
                return false;
            }
        } else if (!this.login.equals(other.login)) {
            return false;
        }
        return true;
    }
}
