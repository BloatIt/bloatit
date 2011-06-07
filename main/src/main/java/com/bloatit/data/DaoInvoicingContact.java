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

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

/**
 * Represent a invoicing contact for an actor.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DaoInvoicingContact extends DaoIdentifiable {

    /**
     * Full name or company name
     */
    @Column(columnDefinition = "TEXT")
    @Basic(optional = false)
    public String name;

    /**
     * Invoicing address
     */
    @Column(columnDefinition = "TEXT")
    @Basic(optional = false)
    public String address;

    /**
     * Tax identification, VAT ...
     */
    @Column(columnDefinition = "TEXT")
    @Basic(optional = true)
    public String taxIdentification;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DaoActor actor;

    private DaoInvoicingContact(final String name, final String address, final DaoActor actor) {
        if (name == null || address == null || name.isEmpty() || address.isEmpty() || actor == null) {
            throw new NonOptionalParameterException();
        }
        this.name = name;
        this.address = address;
        this.actor = actor;
    }

    /**
     * Creates the invoice contact and persist it.
     * 
     * @param name the full name or company name
     * @param address the invoicing address.
     * @return the new dao invoicing contact
     */
    public static DaoInvoicingContact createAndPersist(final String name, final String address, final DaoActor actor) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoInvoicingContact invoicingContact = new DaoInvoicingContact(name, address, actor);

        actor.addInvoicingContact(invoicingContact);

        try {
            session.save(invoicingContact);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return invoicingContact;
    }

    /**
     * Sets the tax identification.
     * 
     * @param taxIdentification tax identification
     */
    public void setTaxIdentification(final String taxIdentification) {
        this.taxIdentification = taxIdentification;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the address.
     * 
     * @return the address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Gets the tax identification.
     * 
     * @return the tax identification
     */
    public String getTaxIdentification() {
        return this.taxIdentification;
    }

    /**
     * Gets the actor.
     * 
     * @return the actor
     */
    public DaoActor getActor() {
        return this.actor;
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.data.DaoIdentifiable#accept(com.bloatit.data.DataClassVisitor
     * )
     */
    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // Hibernate mapping
    // ======================================================================

    /**
     * Instantiates a new dao bug.
     */
    protected DaoInvoicingContact() {
        super();
    }

    // ======================================================================
    // equals hashcode.
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((taxIdentification == null) ? 0 : taxIdentification.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoInvoicingContact other = (DaoInvoicingContact) obj;
        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (taxIdentification == null) {
            if (other.taxIdentification != null) {
                return false;
            }
        } else if (!taxIdentification.equals(other.taxIdentification)) {
            return false;
        }
        return true;
    }

}
