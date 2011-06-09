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

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.bloatit.model.Actor;

/**
 * Represent a invoicing.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DaoInvoice extends DaoIdentifiable {

    // Seller

    /**
     * Full name or company name
     */
    @Column(columnDefinition = "TEXT")
    @Basic(optional = false)
    private String sellerName;

    /**
     * Invoicing adress
     */
    @Column(columnDefinition = "TEXT")
    @Basic(optional = false)
    private String sellerAddress;

    /**
     * Tax identification, VAT ...
     */
    @Column(columnDefinition = "TEXT")
    @Basic(optional = true)
    private String sellerTaxIdentification;

    // Contributor

    @ManyToOne(optional = false)
    private DaoActor receivertActor;


    /**
     * Full name or company name
     */
    @Column(columnDefinition = "TEXT")
    @Basic(optional = false)
    private String receiverName;

    /**
     * Invoicing adress
     */
    @Column(columnDefinition = "TEXT")
    @Basic(optional = false)
    private String contributorAdress;

    /**
     * Tax identification, VAT ...
     */
    @Column(columnDefinition = "TEXT")
    @Basic(optional = true)
    private String contributorTaxIdentification;

    // Commons

    /**
     * Name of the delivery
     */
    @Column(columnDefinition = "TEXT")
    @Basic(optional = false)
    private String deliveryName;

    /**
     * Name of the delivery
     */
    @Basic(optional = false)
    private BigDecimal priceExcludingTax;

    /**
     * Name of the delivery
     */
    @Basic(optional = false)
    private BigDecimal totalPrice;

    /**
     * Pdf of the invoice
     */
    @Basic(optional = false)
    private String invoiceFile;

    /**
     * The ID of the invoice
     */
    @Basic(optional = false)
    private String invoiceId;

    @Basic(optional = false)
    private String invoiceType;

    @Basic(optional = false)
    private String sellerStreet;

    @Basic(optional = false)
    private String sellerExtras;

    @Basic(optional = false)
    private String sellerCity;

    @Basic(optional = false)
    private String sellerCountry;

    @Basic(optional = false)
    private String receiverStreet;

    @Basic(optional = false)
    private String receiverExtras;

    @Basic(optional = false)
    private String receiverCity;

    @Basic(optional = false)
    private String receiverCountry;

    @Basic(optional = false)
    private Date invoiceDate;

    @Basic(optional = false)
    private BigDecimal taxRate;

    @Basic(optional = false)
    private BigDecimal taxAmount;

    @Basic(optional = false)
    private String sellerLegalId;

    @Basic(optional = true)
    private BigDecimal internalInvoiceNumber;


    protected DaoInvoice(final DaoActor recipientActor,
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
                         final BigDecimal internalInvoiceNumber,
                         final String sellerLegalId,
                         final String sellerTaxIdentification) {
        super();
        this.invoiceFile = invoiceFile;
        this.invoiceType = invoiceType;
        this.invoiceId = invoiceId;
        this.sellerName = sellerName;
        this.sellerStreet = sellerStreet;
        this.sellerExtras = sellerExtras;
        this.sellerCity = sellerCity;
        this.sellerCountry = sellerCountry;
        this.receiverName = receiverName;
        this.receiverStreet = receiverStreet;
        this.receivertActor = recipientActor;
        this.receiverExtras = receiverExtras;
        this.receiverCity = receiverCity;
        this.receiverCountry = receiverCountry;
        this.invoiceDate = invoiceDate;
        this.deliveryName = deliveryName;
        this.priceExcludingTax = priceExcludingTax;
        this.taxRate = taxRate;
        this.taxAmount = taxAmount;
        this.totalPrice = totalPrice;
        this.internalInvoiceNumber = internalInvoiceNumber;
        this.sellerLegalId = sellerLegalId;
        this.sellerTaxIdentification = sellerTaxIdentification;

        checkOptionnal(recipientActor,
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
                       sellerTaxIdentification);

        
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
    public static DaoInvoice createAndPersist(final DaoActor recipientActor,
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
                                              final BigDecimal internalInvoiceNumber,
                                              final String sellerLegalId,
                                              final String sellerTaxId) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoInvoice invoice = new DaoInvoice(recipientActor,
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
                                                  internalInvoiceNumber,
                                                  sellerLegalId,
                                                  sellerTaxId);
        try {
            session.save(invoice);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return invoice;
    }



    public DaoActor getRecipientActor() {
        return receivertActor;
    }


    public String getFile() {
        return invoiceFile;
    }

    public String getInvoiceNumber() {
        return invoiceId;
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
    protected DaoInvoice() {
        super();
    }

    // ======================================================================
    // equals hashcode.
    // ======================================================================



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contributorAdress == null) ? 0 : contributorAdress.hashCode());
        result = prime * result + ((receiverName == null) ? 0 : receiverName.hashCode());
        result = prime * result + ((contributorTaxIdentification == null) ? 0 : contributorTaxIdentification.hashCode());
        result = prime * result + ((deliveryName == null) ? 0 : deliveryName.hashCode());
        result = prime * result + ((invoiceFile == null) ? 0 : invoiceFile.hashCode());
        result = prime * result + ((invoiceId == null) ? 0 : invoiceId.hashCode());
        result = prime * result + ((priceExcludingTax == null) ? 0 : priceExcludingTax.hashCode());
        result = prime * result + ((sellerAddress == null) ? 0 : sellerAddress.hashCode());
        result = prime * result + ((sellerName == null) ? 0 : sellerName.hashCode());
        result = prime * result + ((sellerTaxIdentification == null) ? 0 : sellerTaxIdentification.hashCode());
        result = prime * result + ((totalPrice == null) ? 0 : totalPrice.hashCode());
        return result;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DaoInvoice other = (DaoInvoice) obj;

        if (contributorAdress == null) {
            if (other.contributorAdress != null)
                return false;
        } else if (!contributorAdress.equals(other.contributorAdress))
            return false;
        if (receiverName == null) {
            if (other.receiverName != null)
                return false;
        } else if (!receiverName.equals(other.receiverName))
            return false;
        if (contributorTaxIdentification == null) {
            if (other.contributorTaxIdentification != null)
                return false;
        } else if (!contributorTaxIdentification.equals(other.contributorTaxIdentification))
            return false;
        if (deliveryName == null) {
            if (other.deliveryName != null)
                return false;
        } else if (!deliveryName.equals(other.deliveryName))
            return false;
        if (invoiceFile == null) {
            if (other.invoiceFile != null)
                return false;
        } else if (!invoiceFile.equals(other.invoiceFile))
            return false;
        if (invoiceId == null) {
            if (other.invoiceId != null)
                return false;
        } else if (!invoiceId.equals(other.invoiceId))
            return false;
        if (priceExcludingTax == null) {
            if (other.priceExcludingTax != null)
                return false;
        } else if (!priceExcludingTax.equals(other.priceExcludingTax))
            return false;
        if (sellerAddress == null) {
            if (other.sellerAddress != null)
                return false;
        } else if (!sellerAddress.equals(other.sellerAddress))
            return false;
        if (sellerName == null) {
            if (other.sellerName != null)
                return false;
        } else if (!sellerName.equals(other.sellerName))
            return false;
        if (sellerTaxIdentification == null) {
            if (other.sellerTaxIdentification != null)
                return false;
        } else if (!sellerTaxIdentification.equals(other.sellerTaxIdentification))
            return false;
        if (totalPrice == null) {
            if (other.totalPrice != null)
                return false;
        } else if (!totalPrice.equals(other.totalPrice))
            return false;
        return true;
    }



    public static BigDecimal getLastInvoiceNumber() {
        // TODO Auto-generated method stub
        return null;
    }




   








}
