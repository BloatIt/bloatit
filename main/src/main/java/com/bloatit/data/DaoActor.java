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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.MalformedArgumentException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
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
                           name = "actor.getBankTransactions",
                           query = "from DaoBankTransaction as t where t.author = :this order by t.creationDate DESC"),
                       @NamedQuery(
                           name = "actor.getBankTransactions.size",
                           query = "select count(*) from DaoBankTransaction where author = :this"),
                       @NamedQuery(
                           name = "actor.getFollowedContent",
                           query = "from DaoFollow where actor = :this order by creationDate DESC"),
                       @NamedQuery(
                           name = "actor.getFollowedContent.size",
                           query = "select count(*) from DaoFollow where actor = :this"),
})
// @formatter:on
public abstract class DaoActor extends DaoIdentifiable {

    /**
     * The login represent the user login and the team name. It must be unique
     * (means that a team cannot have the same name as a user)
     */
    @Basic(optional = false)
    @Column(unique = true)
    private String login;

    @Basic(optional = false)
    private Date dateCreation;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private DaoInternalAccount internalAccount;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
    private DaoExternalAccount externalAccount;

    @Embedded
    private DaoContact contact;

    @OneToMany(mappedBy = "followed")
    private final List<DaoFollowActor> followers = new ArrayList<DaoFollowActor>();
    @SuppressWarnings("unused")
    @OneToMany(mappedBy = "actor")
    private final List<DaoEvent> events = new ArrayList<DaoEvent>();

    // ======================================================================
    // HQL static requests.
    // ======================================================================

    /**
     * This method use a HQL request. If you intend to use "getByLogin" or
     * "getByName", "exist" is useless. (In that case you'd better test if
     * getByLogin != null, to minimize the number of HQL request).
     *
     * @param login the login we are looking for.
     * @return true if found.
     */
    public static boolean loginExists(final String login) {
        if (login == null) {
            return false;
        }
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Criteria c = session.createCriteria(DaoActor.class)
                                  .setProjection(Projections.rowCount())
                                  .add(Restrictions.like("login", login).ignoreCase());
        return ((Long) c.uniqueResult()) > 0;
    }

    // ======================================================================
    // Construction.
    // ======================================================================

    /**
     * Create a new DaoActor. Initialize the creation date to now. Create a new
     * {@link DaoInternalAccount} and a new {@link DaoExternalAccount}.
     *
     * @param login is the login or name of this actor. It must be non null,
     *            unique, longer than 2 chars and do not contains space chars
     *            ("[^\\p{Space}]+").
     * @throws NonOptionalParameterException if login or mail is null.
     * @throws MalformedArgumentException if the login is to small or contain
     *             space chars.
     */
    protected DaoActor(final String login) {
        super();
        if (login == null) {
            throw new NonOptionalParameterException("login cannot be null");
        }
        if (login.length() < 2) {
            throw new MalformedArgumentException("login length must be > 1");
        }
        if (!login.trim().equals(login)) {
            throw new MalformedArgumentException("The login cannot begin or end with spaces.");
        }

        this.dateCreation = new Date();
        this.login = login;
        this.internalAccount = new DaoInternalAccount(this);
        this.externalAccount = new DaoExternalAccount(this);
        this.contact = new DaoContact();
    }

    public void setLogin(final String login) {
        if (login.length() < 3) {
            throw new MalformedArgumentException("login length must be > 2");
        }
        if (!login.trim().equals(login)) {
            throw new MalformedArgumentException("The login cannot begin or end with spaces.");
        }
        this.login = login;
    }

    // ======================================================================
    // Getters.
    // ======================================================================

    /**
     * Set the external account for this actor.
     *
     * @param externalAccount the new external account for this actor
     * @throws BadProgrammerException if the externalAccount.getActor() != this
     */
    public void setExternalAccount(final DaoExternalAccount externalAccount) {
        if (externalAccount.getActor() != this) {
            throw new BadProgrammerException("Add an external account to the wrong user.", null);
        }
        this.externalAccount = externalAccount;
    }

    /**
     * @return the login of the actor
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * @return the creation date.
     */
    public Date getDateCreation() {
        return (Date) this.dateCreation.clone();
    }

    /**
     * @return the internal account of this actor
     */
    public DaoInternalAccount getInternalAccount() {
        return this.internalAccount;
    }

    /**
     * @return the external account of this actor
     */
    public DaoExternalAccount getExternalAccount() {
        return this.externalAccount;
    }

    /**
     * @return all the <code>DaoBankTransaction</code> created by
     *         <code>this</code>, order by <code>creationDate</code>, most
     *         recent first.
     */
    public PageIterable<DaoBankTransaction> getBankTransactions() {
        return new QueryCollection<DaoBankTransaction>("actor.getBankTransactions").setEntity("this", this);
    }

    public PageIterable<DaoFollow> getFollowedContent() {
        // return new MappedList<DaoFollow>(followedContents);
        return new QueryCollection<DaoFollow>("actor.getFollowedContent").setEntity("this", this);
    }

    public DaoContact getContact() {
        if (contact == null) {
            contact = new DaoContact();
        }
        return contact;
    }

    public List<DaoFollowActor> getFollowers() {
        return followers;
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
