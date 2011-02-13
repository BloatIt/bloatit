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

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * DaoActor is the base class of any user that can make money transaction. Each actor has
 * a unique name, an email, and an internalAccount.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DaoActor implements IdentifiableInterface {

    /**
     * Because of the different inheritance strategy we cannot inherit from identifiable.
     * So we have to have an id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * The login represent the user login and the group name. It must be unique (means
     * that a group cannot have the same name as a user)
     */
    @Basic(optional = false)
    @Column(unique = true, updatable = false)
    private String login;

    @Basic(optional = false)
    private Date dateCreation;

    @OneToOne(optional = false)
    @Cascade(value = { CascadeType.ALL })
    private DaoInternalAccount internalAccount;

    @OneToOne(optional = false)
    @Cascade(value = { CascadeType.ALL })
    private DaoExternalAccount externalAccount;

    // ======================================================================
    // HQL static requests.
    // ======================================================================

    /**
     * This method use a HQL request. If you intend to use "getByLogin" or "getByName",
     * "exist" is useless. (In that case you'd better test if getByLogin != null, to
     * minimize the number of HQL request).
     */
    public static boolean exist(final String login) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query q = session.createQuery("select count(*) from com.bloatit.data.DaoActor as m where login = :login");
        q.setString("login", login);
        return ((Long) q.uniqueResult()) > 0;
    }

    // ======================================================================
    // Construction.
    // ======================================================================

    /**
     * Create a new DaoActor. Initialize the creation date to now. Create a new
     * {@link DaoInternalAccount} and a new {@link DaoExternalAccount}.
     * 
     * @param login is the login or name of this actor. It must be non null, and unique.
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
    public abstract String getEmail();

    /**
     * No check is performed on the correctness of the new email.
     * 
     * @param email the new email.
     */
    public abstract void setEmail(final String email);

    // ======================================================================
    // Getters.
    // ======================================================================

    /**
     * Set the external account for this actor.
     * 
     * @param externalAccount the new external account for this actor
     * @throws FatalErrorException if the externalAccount.getActor() != this
     */
    public final void setExternalAccount(final DaoExternalAccount externalAccount) {
        if (externalAccount.getActor() != this) {
            throw new FatalErrorException("Add an external account to the wrong user.", null);
        }
        this.externalAccount = externalAccount;
    }

    public final String getLogin() {
        return login;
    }

    public final Date getDateCreation() {
        return (Date) dateCreation.clone();
    }

    public final DaoInternalAccount getInternalAccount() {
        return internalAccount;
    }

    public final DaoExternalAccount getExternalAccount() {
        return externalAccount;
    }

    /**
     * @return all the <code>DaoBankTransaction</code> created by <code>this</code>, order
     * by <code>creationDate</code>, most recent first.
     */
    public final PageIterable<DaoBankTransaction> getBankTransactions() {
        return new QueryCollection<DaoBankTransaction>(SessionManager.createQuery("from DaoBankTransaction where author = :author order by creationDate DESC"),
                                                       SessionManager.createQuery("select count(*) from DaoBankTransaction where author = :author")).setEntity("author",
                                                                                                                                                               this);
    }

    @Override
    public final Integer getId() {
        return id;
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
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((login == null) ? 0 : login.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
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
        if (login == null) {
            if (other.login != null) {
                return false;
            }
        } else if (!login.equals(other.login)) {
            return false;
        }
        return true;
    }
}
