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
package com.bloatit.model;

import java.math.BigDecimal;

import com.bloatit.data.DaoContact;

/**
 * This is a invoicing contact.
 */
public final class Contact {

    private final DaoContact dao;

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////



    /**
     * Find a bug in the cache or create an new one.
     *
     * @param dao the dao
     * @return null if dao is null. Else return the new invoicing contact.
     */
    @SuppressWarnings("synthetic-access")
    public static Contact create(final DaoContact dao) {
        if(dao == null) {
            return null;
        }
        return new Contact(dao);
    }

    /**
     * Instantiates a new invoicing contact.
     *
     * @param dao the dao
     */
    private Contact(final DaoContact dao) {
        this.dao = dao;
    }


    public void setName(String name) {
        getDao().setName(name);
    }

    public String getName() {
        return getDao().getName();
    }

    
    
    
    /**
     * @param taxIdentification
     * @see com.bloatit.data.DaoContact#setTaxIdentification(java.lang.String)
     */
    public void setTaxIdentification(String taxIdentification) {
        dao.setTaxIdentification(taxIdentification);
    }

    /**
     * @return
     * @see com.bloatit.data.DaoContact#getTaxIdentification()
     */
    public String getTaxIdentification() {
        return dao.getTaxIdentification();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoContact#getStreet()
     */
    public String getStreet() {
        return dao.getStreet();
    }

    /**
     * @param street
     * @see com.bloatit.data.DaoContact#setStreet(java.lang.String)
     */
    public void setStreet(String street) {
        dao.setStreet(street);
    }

    /**
     * @return
     * @see com.bloatit.data.DaoContact#getExtras()
     */
    public String getExtras() {
        return dao.getExtras();
    }

    /**
     * @param extras
     * @see com.bloatit.data.DaoContact#setExtras(java.lang.String)
     */
    public void setExtras(String extras) {
        dao.setExtras(extras);
    }

    /**
     * @return
     * @see com.bloatit.data.DaoContact#getPostalCode()
     */
    public String getPostalCode() {
        return dao.getPostalCode();
    }

    /**
     * @param postalCode
     * @see com.bloatit.data.DaoContact#setPostalCode(java.lang.String)
     */
    public void setPostalCode(String postalCode) {
        dao.setPostalCode(postalCode);
    }

    /**
     * @return
     * @see com.bloatit.data.DaoContact#getCity()
     */
    public String getCity() {
        return dao.getCity();
    }

    /**
     * @param city
     * @see com.bloatit.data.DaoContact#setCity(java.lang.String)
     */
    public void setCity(String city) {
        dao.setCity(city);
    }

    /**
     * @return
     * @see com.bloatit.data.DaoContact#getCountry()
     */
    public String getCountry() {
        return dao.getCountry();
    }

    /**
     * @param country
     * @see com.bloatit.data.DaoContact#setCountry(java.lang.String)
     */
    public void setCountry(String country) {
        dao.setCountry(country);
    }

    /**
     * @return
     * @see com.bloatit.data.DaoContact#getLegalId()
     */
    public String getLegalId() {
        return dao.getLegalId();
    }

    /**
     * @param legalId
     * @see com.bloatit.data.DaoContact#setLegalId(java.lang.String)
     */
    public void setLegalId(String legalId) {
        dao.setLegalId(legalId);
    }

    /**
     * @return
     * @see com.bloatit.data.DaoContact#getTaxRate()
     */
    public BigDecimal getTaxRate() {
        return dao.getTaxRate();
    }

    /**
     * @param taxRate
     * @see com.bloatit.data.DaoContact#setTaxRate(java.math.BigDecimal)
     */
    public void setTaxRate(BigDecimal taxRate) {
        dao.setTaxRate(taxRate);
    }

    /**
     * @return
     * @see com.bloatit.data.DaoContact#getInvoiceIdTemplate()
     */
    public String getInvoiceIdTemplate() {
        return dao.getInvoiceIdTemplate();
    }

    /**
     * @param invoiceIdTemplate
     * @see com.bloatit.data.DaoContact#setInvoiceIdTemplate(java.lang.String)
     */
    public void setInvoiceIdTemplate(String invoiceIdTemplate) {
        dao.setInvoiceIdTemplate(invoiceIdTemplate);
    }

    /**
     * @return
     * @see com.bloatit.data.DaoContact#getInvoiceIdNumber()
     */
    public BigDecimal getInvoiceIdNumber() {
        return dao.getInvoiceIdNumber();
    }

    /**
     * @param invoiceIdNumber
     * @see com.bloatit.data.DaoContact#setInvoiceIdNumber(java.math.BigDecimal)
     */
    public void setInvoiceIdNumber(BigDecimal invoiceIdNumber) {
        dao.setInvoiceIdNumber(invoiceIdNumber);
    }

    public String pickNextInvoiceId() {
        // TODO Auto-generated method stub
        return null;
    }
    
    // ///////////////////////////
    // Unprotected methods

    /**
     * This method is used only in the authentication process. You should never
     * used it anywhere else.
     *
     * @return the actor unprotected
     * @see #getActor()
     */
//    final Actor<?> getActorUnprotected() {
//        return (Actor<?>) getDao().getActor().accept(new DataVisitorConstructor());
//    }

    private DaoContact getDao() {
        return dao;
    }

    

    


}
