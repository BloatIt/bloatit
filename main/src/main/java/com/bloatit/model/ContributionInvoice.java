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

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoContributionInvoice;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.model.invoicePdf.InvoicePdfGenerator;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.RgtInvoice;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.model.visitor.ModelClassVisitor;

/**
 * This is a invoice.
 */
public final class ContributionInvoice extends Identifiable<DaoContributionInvoice> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This class implements the method pattern, implementing the doCreate
     * method. See the base class for more informations: {@link Creator}.
     */
    private static final class MyCreator extends Creator<DaoContributionInvoice, ContributionInvoice> {

        /*
         * (non-Javadoc)
         * @see
         * com.bloatit.model.Creator#doCreate(com.bloatit.data.DaoIdentifiable)
         */
        @SuppressWarnings("synthetic-access")
        @Override
        public ContributionInvoice doCreate(final DaoContributionInvoice dao) {
            return new ContributionInvoice(dao);
        }
    }

    /**
     * Find a bug in the cache or create an new one.
     * 
     * @param dao the dao
     * @return null if dao is null. Else return the new invoice.
     */
    @SuppressWarnings("synthetic-access")
    public static ContributionInvoice create(final DaoContributionInvoice dao) {
        return new MyCreator().create(dao);
    }

    /**
     * Instantiates a new invoice.
     * 
     * @param dao the dao
     */
    private ContributionInvoice(final DaoContributionInvoice dao) {
        super(dao);
    }

    /**
     * Create a new Invoice.
     * 
     * @param member is the author of the bug.
     * @param milestone is the milestone on which this bug has been set.
     * @param title is the title of the bug.
     * @param description is a complete description of the bug.
     * @param locale is the language in which this description has been written.
     * @param errorLevel is the estimated level of the bug. see {@link Level}.
     * @throws UnauthorizedPrivateAccessException
     */
    public ContributionInvoice(final Actor<?> emitterActor,
                               final Actor<?> recipientActor,
                               final String invoiceType,
                               final String deliveryName,
                               final BigDecimal totalPrice,
                               final Milestone milestone,
                               final Contribution contribution,
                               final boolean applyVAT) throws UnauthorizedPrivateAccessException {
        super(generateInvoice(emitterActor, recipientActor, invoiceType, deliveryName, totalPrice, milestone, contribution, applyVAT, null));
    }

    public static class PreviewSettings {
        private final OutputStream stream;
        private final BigDecimal invoiceNumber;

        public PreviewSettings(final OutputStream stream, final BigDecimal invoiceNumber) {
            this.stream = stream;
            this.invoiceNumber = invoiceNumber;
        }

        public BigDecimal getInvoiceNumber() {
            return invoiceNumber;
        }

        public OutputStream getStream() {
            return stream;
        }
    }

    public static DaoContributionInvoice generateInvoice(final Actor<?> emitterActor,
                                                         final Actor<?> recipientActor,
                                                         final String invoiceType,
                                                         final String deliveryName,
                                                         final BigDecimal totalPrice,
                                                         final Milestone milestone,
                                                         final Contribution contribution,
                                                         final boolean applyVAT,
                                                         final PreviewSettings preview) throws UnauthorizedPrivateAccessException {

        final String invoiceId;
        if (preview == null) {
            invoiceId = emitterActor.getContact().pickNextInvoiceId();
        } else {
            invoiceId = emitterActor.getContact().getInvoiceId(preview.getInvoiceNumber());
        }
        final String sellerName = emitterActor.getContact().getName();
        final String sellerStreet = emitterActor.getContact().getStreet();
        final String sellerExtras = emitterActor.getContact().getExtras();
        final String sellerCity = emitterActor.getContact().getPostalCode() + " " + emitterActor.getContact().getCity();
        final String sellerCountry = new Locale("en", emitterActor.getContact().getCountry()).getDisplayCountry(Locale.ENGLISH);
        final String sellerLegalId = emitterActor.getContact().getLegalId();
        final String sellerTaxIdentification = emitterActor.getContact().getTaxIdentification();
        final Contact recipientContact = recipientActor.getContactUnprotected();
        final String receiverName = recipientContact.getName();
        final String receiverStreet = recipientContact.getStreet();
        final String receiverExtras = recipientContact.getExtras();
        final String receiverCity = recipientContact.getPostalCode() + " " + recipientContact.getCity();
        final String receiverCountry = new Locale("en", recipientContact.getCountry()).getDisplayCountry(Locale.ENGLISH);
        final String receiverTaxIdentification = recipientContact.getTaxIdentification();
        final Date invoiceDate = DateUtils.now();

        BigDecimal taxRate = emitterActor.getContact().getTaxRate();

        final BigDecimal priceExcludingTax = totalPrice.divide(BigDecimal.ONE.add(taxRate), BigDecimal.ROUND_HALF_EVEN);
        final BigDecimal taxAmount = totalPrice.subtract(priceExcludingTax);

        if (!applyVAT) {
            taxRate = null;
        }

        final InvoicePdfGenerator pdfGenerator = new InvoicePdfGenerator(invoiceType,
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
                                                                         receiverTaxIdentification,
                                                                         invoiceDate,
                                                                         deliveryName,
                                                                         priceExcludingTax,
                                                                         taxRate,
                                                                         taxAmount,
                                                                         totalPrice,
                                                                         sellerLegalId,
                                                                         sellerTaxIdentification,
                                                                         null);

        if (preview == null) {
            return DaoContributionInvoice.createAndPersist(emitterActor.getDao(),
                                                           recipientActor.getDao(),
                                                           pdfGenerator.getPdfUrl(),
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
                                                           receiverTaxIdentification,
                                                           invoiceDate,
                                                           deliveryName,
                                                           priceExcludingTax,
                                                           taxRate.multiply(BigDecimal.valueOf(100)),
                                                           taxAmount,
                                                           totalPrice,
                                                           sellerLegalId,
                                                           sellerTaxIdentification,
                                                           milestone.getDao(),
                                                           contribution.getDao());
        } else {
            pdfGenerator.write(preview.getStream());
            return null;
        }
    }

    public String getFile() throws UnauthorizedPrivateAccessException {
        tryAccess(new RgtInvoice.File(), Action.READ);
        return getDao().getFile();
    }

    public String getInvoiceNumber() throws UnauthorizedPrivateAccessException {
        tryAccess(new RgtInvoice.InvoiceNumber(), Action.READ);
        return getDao().getInvoiceNumber();
    }

    public Milestone getMilestone() {
        return Milestone.create(getDao().getMilestone());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
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
    final Actor<?> getRecipientActorUnprotected() {
        return (Actor<?>) getDao().getRecipientActor().accept(new DataVisitorConstructor());
    }

    /**
     * This method is used only in the authentication process. You should never
     * used it anywhere else.
     * 
     * @return the actor unprotected
     * @see #getActor()
     */
    final Actor<?> getEmitterActorUnprotected() {
        return (Actor<?>) getDao().getEmitterActor().accept(new DataVisitorConstructor());
    }
}
