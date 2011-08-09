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

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Represent a invoicing.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DaoContributionInvoice extends DaoInvoice {

    @ManyToOne(optional = true)
    public DaoActor sellerActor;

    /**
     * Corresponding contribution. null if commission invoice
     */
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    public DaoContribution contribution;

    /**
     * Corresponding contribution. null if commission invoice
     */
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    public DaoMilestone milestone;

    private DaoContributionInvoice(final DaoActor emitterActor,
                                   final DaoActor recipientActor,
                                   final String invoiceFile,
                                   final String invoiceType,
                                   final String invoiceId,
                                   final String sellerName,
                                   final String sellerStreet,
                                   final String sellerExtras,
                                   final String sellerCity,
                                   final String sellerCountry,
                                   final String receiverName,
                                   final String receiverStreet,
                                   final String receiverExtras,
                                   final String receiverCity,
                                   final String receiverCountry,
                                   final Date invoiceDate,
                                   final String deliveryName,
                                   final BigDecimal priceExcludingTax,
                                   final BigDecimal taxRate,
                                   final BigDecimal taxAmount,
                                   final BigDecimal totalPrice,
                                   final String sellerLegalId,
                                   final String sellerTaxIdentification,
                                   final DaoMilestone milestone,
                                   final DaoContribution contribution) {
        super(recipientActor,
              invoiceFile,
              invoiceType,
              invoiceId,
              sellerName,
              sellerStreet,
              sellerExtras,
              sellerCity,
              sellerCountry,
              receiverName,
              receiverStreet,
              receiverExtras,
              receiverCity,
              receiverCountry,
              invoiceDate,
              deliveryName,
              priceExcludingTax,
              taxRate,
              taxAmount,
              totalPrice,
              null,
              sellerLegalId,
              sellerTaxIdentification);

        checkOptionnal(emitterActor, milestone, contribution);

        this.sellerActor = emitterActor;
        this.milestone = milestone;
        this.contribution = contribution;
    }

    /**
     * Creates the bug and persist it.
     * 
     * @param member the author
     * @param team the as Team property. can be null.
     * @param milestone the milestone on which there is a bug.
     * @param title the title of the bug
     * @param description the description of the bug
     * @param locale the locale in which this bug has been written
     * @param level the level of the bug
     * @return the new dao bug
     */
    public static DaoContributionInvoice createAndPersist(final DaoActor emitterActor,
                                                          final DaoActor recipientActor,
                                                          final String invoiceFile,
                                                          final String invoiceType,
                                                          final String invoiceId,
                                                          final String sellerName,
                                                          final String sellerStreet,
                                                          final String sellerExtras,
                                                          final String sellerCity,
                                                          final String sellerCountry,
                                                          final String receiverName,
                                                          final String receiverStreet,
                                                          final String receiverExtras,
                                                          final String receiverCity,
                                                          final String receiverCountry,
                                                          final Date invoiceDate,
                                                          final String deliveryName,
                                                          final BigDecimal priceExcludingTax,
                                                          final BigDecimal taxRate,
                                                          final BigDecimal taxAmount,
                                                          final BigDecimal totalPrice,
                                                          final String sellerLegalId,
                                                          final String sellerTaxIdentification,
                                                          final DaoMilestone milestone,
                                                          final DaoContribution contribution) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoContributionInvoice invoice = new DaoContributionInvoice(emitterActor,
                                                                          recipientActor,
                                                                          invoiceFile,
                                                                          invoiceType,
                                                                          invoiceId,
                                                                          sellerName,
                                                                          sellerStreet,
                                                                          sellerExtras,
                                                                          sellerCity,
                                                                          sellerCountry,
                                                                          receiverName,
                                                                          receiverStreet,
                                                                          receiverExtras,
                                                                          receiverCity,
                                                                          receiverCountry,
                                                                          invoiceDate,
                                                                          deliveryName,
                                                                          priceExcludingTax,
                                                                          taxRate,
                                                                          taxAmount,
                                                                          totalPrice,
                                                                          sellerLegalId,
                                                                          sellerTaxIdentification,
                                                                          milestone,
                                                                          contribution);
        try {
            session.save(invoice);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return invoice;
    }

    public DaoActor getEmitterActor() {
        return sellerActor;
    }

    public DaoMilestone getMilestone() {
        return milestone;
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
    protected DaoContributionInvoice() {
        super();
    }

    // ======================================================================
    // equals hashcode.
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((contribution == null) ? 0 : contribution.hashCode());
        result = prime * result + ((milestone == null) ? 0 : milestone.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoContributionInvoice other = (DaoContributionInvoice) obj;
        if (contribution == null) {
            if (other.contribution != null) {
                return false;
            }
        } else if (!contribution.equals(other.contribution)) {
            return false;
        }
        if (milestone == null) {
            if (other.milestone != null) {
                return false;
            }
        } else if (!milestone.equals(other.milestone)) {
            return false;
        }
        return true;
    }

}
