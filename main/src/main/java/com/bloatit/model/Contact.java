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
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if (dao == null) {
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

    public void setName(final String name) {
        getDao().setName(name);
    }

    public String getName() {
        return getDao().getName();
    }

    /**
     * @param taxIdentification
     * @see com.bloatit.data.DaoContact#setTaxIdentification(java.lang.String)
     */
    public void setTaxIdentification(final String taxIdentification) {
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
    public void setStreet(final String street) {
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
    public void setExtras(final String extras) {
        dao.setExtras(extras);
    }

    public void setIsCompany(boolean isCompany) {
        dao.setIsCompany(isCompany);
    }

    public boolean isCompany() {
        return dao.isCompany();
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
    public void setPostalCode(final String postalCode) {
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
    public void setCity(final String city) {
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
    public void setCountry(final String country) {
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
    public void setLegalId(final String legalId) {
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
    public void setTaxRate(final BigDecimal taxRate) {
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
    public void setInvoiceIdTemplate(final String invoiceIdTemplate) {
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
    public void setInvoiceIdNumber(final BigDecimal invoiceIdNumber) {
        dao.setInvoiceIdNumber(invoiceIdNumber);
    }

    /**
     * Return the invoice id template with the following fields replaced by the
     * right values and increment the next id number <code>
     * {ID|x} : id number on x characters
     * {YEAR|2} : year on 2 characters
     * {YEAR|4} : year on 4 characters
     * {MONTH} : month on 4 characters
     * {DAY} : day of the month on 2 characters
     * {YDAY} : day of the year on 2 characters
     * {WEEK} : week of the year on 2 characters
     * 
     * Exemple:
     * 
     * "ELVEOS-{YEAR|4}{MONTH}{DAY}-F{ID|4}" 
     * 
     * The 2011/09/08 with next id number at 42 give ELVEOS-20110809-F0042
     * </code>
     * 
     * @return invoice id
     */
    public String pickNextInvoiceId() {

        InvoiceIdFormatter formatter = new InvoiceIdFormatter(getInvoiceIdTemplate());
        String invoiceId = formatter.format(getInvoiceIdNumber());

        setInvoiceIdNumber(getInvoiceIdNumber().add(BigDecimal.ONE));
        return invoiceId;

    }

    private static class InvoiceIdFormatter {
        private final String template;
        private String output;

        public InvoiceIdFormatter(String template) {
            this.template = template;

        }

        /*
         * {ID|x} : id number on x characters {YEAR} : year on 4 characters
         * {MONTH} : month on 4 characters {DAY} : day of the month on 2
         * characters {YDAY} : day of the year on 2 characters {WEEK} : week of
         * the year on 2 characters
         */
        public String format(BigDecimal invoiceIdNumber) {

            this.output = this.template;

            GregorianCalendar gregorianCalendar = new GregorianCalendar();

            replaceNumber("YEAR", 4, gregorianCalendar.get(Calendar.YEAR));
            replaceNumber("MONTH", 2, gregorianCalendar.get(Calendar.MONTH));
            replaceNumber("DAY", 2, gregorianCalendar.get(Calendar.DAY_OF_MONTH));
            replaceNumber("YDAY", 2, gregorianCalendar.get(Calendar.DAY_OF_YEAR));
            replaceNumber("WEEK", 2, gregorianCalendar.get(Calendar.WEEK_OF_YEAR));
            replaceNumber("ID", 4, invoiceIdNumber.intValue());

            return this.output;
        }

        private void replaceNumber(String token, int defaultLength, int value) {

            Pattern pattern = Pattern.compile("^(.*)(\\{" + token + "(\\|([0-9]+))?\\})(.*)");

            Matcher matcher = pattern.matcher(this.output);

            while (matcher.find()) {

                int length = defaultLength;
                if (matcher.group(4) != null) {
                    length = Integer.parseInt(matcher.group(4));
                }

                DecimalFormat df = new DecimalFormat(multiply("0", length));

                this.output = matcher.group(1) + df.format(value) + matcher.group(5);
            }

        }

        private String multiply(String string, int count) {
            StringBuffer out = new StringBuffer();
            for (int i = 0; i < count; i++) {
                out.append(string);
            }
            return out.toString();
        }

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
    // final Actor<?> getActorUnprotected() {
    // return (Actor<?>) getDao().getActor().accept(new
    // DataVisitorConstructor());
    // }

    private DaoContact getDao() {
        return dao;
    }
}
