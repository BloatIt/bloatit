/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.invoice;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableCell;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableLine;
import com.bloatit.framework.webprocessor.components.form.HtmlCheckbox;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlString;
import com.bloatit.model.Actor;
import com.bloatit.model.Contact;
import com.bloatit.model.Contribution;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.MilestoneContributionAmount;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.model.right.UnauthorizedPublicReadOnlyAccessException;
import com.bloatit.web.components.HtmlAuthorLink;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.components.MoneyDisplayComponent;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.HtmlDefineParagraph;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.url.ContributionInvoicePreviewDataUrl;
import com.bloatit.web.url.ContributionInvoicingInformationsActionUrl;
import com.bloatit.web.url.ContributionInvoicingInformationsPageUrl;
import com.bloatit.web.url.ModifyInvoicingContactProcessUrl;

/**
 * A page used choose or create the invoicing contact to use for the comission
 * invoice.
 */
@ParamContainer(value = "invoicing/contribution_invoicing_informations", protocol = Protocol.HTTPS)
public final class ContributionInvoicingInformationsPage extends LoggedElveosPage {

    @RequestParam(message = @tr("The process is closed, expired, missing or invalid."))
    @NonOptional(@tr("The process is closed, expired, missing or invalid."))
    private final ContributionInvoicingProcess process;

    @RequestParam(name = "applyVAT", role = Role.GET)
    @Optional
    private final List<String> applyVAT;

    @RequestParam(name = "preview", role = Role.GET)
    @NonOptional(@tr("You must indicate the preview mode"))
    private final Boolean preview;

    private final ContributionInvoicingInformationsPageUrl url;

    private ContributionInvoicingInformationsActionUrl generateInvoiceActionUrl;

    public ContributionInvoicingInformationsPage(final ContributionInvoicingInformationsPageUrl url) {
        super(url);
        this.url = url;
        this.applyVAT = url.getApplyVAT();
        this.process = url.getProcess();
        this.preview = url.getPreview();
    }

    @Override
    public HtmlElement createBodyContentOnParameterError() throws RedirectException {
        if (url.getMessages().hasMessage()) {
            throw new RedirectException(Context.getSession().pickPreferredPage());
        }
        return createBodyContent();
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {

        // TODO: mutualise css
        final HtmlDiv layout = new HtmlDiv("translate_page");

        final HtmlDiv modify = new HtmlDiv("float_right");
        layout.add(modify);

        Actor<?> actor = process.getActor();

        ModifyInvoicingContactProcessUrl modifyInvoicingContactProcessUrl = new ModifyInvoicingContactProcessUrl(actor, process);
        modifyInvoicingContactProcessUrl.setNeedAllInfos(true);
        modify.add(modifyInvoicingContactProcessUrl.getHtmlLink(Context.tr("Change invoicing informations")));

        // Contact reminder
        layout.add(generateContactInformations());

        layout.add(generateInvoicingGenerationForm(loggedUser));

        return layout;
    }

    private HtmlElement generateInvoicingGenerationForm(final Member member) {
        // new HtmlTitleBlock("Additional invoicing informations", 1);
        generateInvoiceActionUrl = new ContributionInvoicingInformationsActionUrl(getSession().getShortKey(), process);
        final HtmlElveosForm form = new HtmlElveosForm(generateInvoiceActionUrl.urlString());

        Milestone milestone = process.getMilestone();
        HtmlTable.HtmlLineTableModel model = new HtmlTable.HtmlLineTableModel();
        model.setHeaderLine(new HtmlTableLine() {

            {
                addTextCell(Context.tr("Contributor"));
                addTextCell(Context.tr("Amount"));
                addTextCell(Context.tr("Country"));
                addTextCell(Context.tr("Company?"));
                addTextCell(Context.tr("VAT Number"));
                addTextCell(Context.tr("Apply VAT ?"));
                addTextCell(Context.tr("Preview"));
            }

        });

        try {
            BigDecimal invoiceIdNumberCount = process.getActor().getContact().getInvoiceIdNumber();

            for (MilestoneContributionAmount milestoneContributionAmount : milestone.getContributionAmounts()) {
                model.addLine(new InvoiceLine(milestoneContributionAmount, invoiceIdNumberCount));
                invoiceIdNumberCount = invoiceIdNumberCount.add(BigDecimal.ONE);
            }

        } catch (UnauthorizedPrivateAccessException e) {
            throw new BadProgrammerException("Fail to generate contributors list", e);
        }

        form.add(new HtmlTable(model));

        final HtmlSubmit previewButton = new HtmlSubmit(Context.tr("Update previews"));
        previewButton.addAttribute("name", "preview");
        form.addSubmit(previewButton);

        final HtmlSubmit generateButton = new HtmlSubmit(Context.tr("Generate invoices"));
        generateButton.addAttribute("name", "generate");
        form.addSubmit(generateButton);

        return form;
    }

    public HtmlElement generateContactInformations() {
        final HtmlList memberIdList = new HtmlList();

        // Name
        final String name;
        Actor<?> actor = process.getActor();
        Contact contact;
        try {
            contact = actor.getContact();
        } catch (UnauthorizedPrivateAccessException e) {
            throw new BadProgrammerException(e);
        }
        if (actor.isTeam()) {
            name = Context.tr("Organisation name: ");
        } else {
            name = Context.tr("Name: ");
        }
        memberIdList.add(new HtmlDefineParagraph(name, emptyIfNull(contact.getName())));

        // Street
        final String street;
        street = Context.tr("Street: ");
        memberIdList.add(new HtmlDefineParagraph(street, emptyIfNull(contact.getStreet())));

        // Extras
        final String extras;
        extras = Context.tr("Extras adress: ");
        memberIdList.add(new HtmlDefineParagraph(extras, emptyIfNull(contact.getExtras())));

        // City
        final String city;
        city = Context.tr("City: ");
        memberIdList.add(new HtmlDefineParagraph(city, emptyIfNull(contact.getCity())));

        // Postal code
        final String postalCode;
        postalCode = Context.tr("Postcode: ");
        memberIdList.add(new HtmlDefineParagraph(postalCode, emptyIfNull(contact.getPostalCode())));

        // Country
        final String country;
        country = Context.tr("Country: ");

        if (contact.getCountry() == null) {
            memberIdList.add(new HtmlDefineParagraph(country, ""));
        } else {
            memberIdList.add(new HtmlDefineParagraph(country, new Locale("en", contact.getCountry()).getDisplayCountry(Context.getLocalizator()
                                                                                                                              .getLocale())));
        }

        // Is company

        final String isCompany;
        isCompany = Context.tr("Company: ");
        memberIdList.add(new HtmlDefineParagraph(isCompany, (contact.isCompany() ? Context.tr("Yes") : Context.tr("No"))));

        if (contact.isCompany()) {

            // Tax identification
            final String taxIdentification;
            taxIdentification = Context.tr("Tax identification: ");
            memberIdList.add(new HtmlDefineParagraph(taxIdentification, emptyIfNull(contact.getTaxIdentification())));

        }

        // Invoicing id template
        final String invoicingIdTemplate;
        invoicingIdTemplate = Context.tr("Invoicing id template: ");
        memberIdList.add(new HtmlDefineParagraph(invoicingIdTemplate, emptyIfNull(contact.getInvoiceIdTemplate())));

        // Invoicing id number
        final String invoicingIdNumber;
        invoicingIdNumber = Context.tr("Invoicing id next number: ");
        if (contact.getInvoiceIdNumber() != null) {
            memberIdList.add(new HtmlDefineParagraph(invoicingIdNumber, "" + contact.getInvoiceIdNumber().intValue()));
        } else {
            memberIdList.add(new HtmlDefineParagraph(invoicingIdNumber, ""));
        }

        // Legal Id
        final String legalId;
        legalId = Context.tr("Legal identification: ");
        memberIdList.add(new HtmlDefineParagraph(legalId, emptyIfNull(contact.getLegalId())));

        // Tax rate
        final String taxRate;
        taxRate = Context.tr("Tax rate: ");
        if (contact.getTaxRate() != null) {
            memberIdList.add(new HtmlDefineParagraph(taxRate, contact.getTaxRate().multiply(new BigDecimal("100")).floatValue() + " %"));
        } else {
            memberIdList.add(new HtmlDefineParagraph(taxRate, ""));
        }

        return memberIdList;
    }

    @Override
    protected String createPageTitle() {
        return tr("Contribution invoices generation");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to add invoicing informations");
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return generateBreadcrumb(process);
    }

    protected static Breadcrumb generateBreadcrumb(final ContributionInvoicingProcess process) {
        final Breadcrumb breadcrumb;
        breadcrumb = FeaturePage.generateBreadcrumb(process.getMilestone().getOffer().getFeature());

        final Url url = new ContributionInvoicingInformationsPageUrl(false, process);

        breadcrumb.pushLink(url.getHtmlLink(tr("Contribution invoices generation")));
        return breadcrumb;
    }

    private class InvoiceLine extends HtmlTableLine {

        public InvoiceLine(final MilestoneContributionAmount milestoneContributionAmount, final BigDecimal invoiceIdNumber) throws UnauthorizedPrivateAccessException {
            final Contribution contribution = milestoneContributionAmount.getContribution();
            Actor<?> author = contribution.getAuthor();
            final Contact contact = author.getContactUnprotected();

            addCell(new HtmlTableCell("") {
                @Override
                public HtmlNode getBody() {
                    return new HtmlAuthorLink(contribution);
                }
            });

            addCell(new HtmlTableCell("") {
                @Override
                public HtmlNode getBody() {
                    try {
                        return new MoneyDisplayComponent(milestoneContributionAmount.getAmount(), Context.getLocalizator());
                    } catch (UnauthorizedPublicReadOnlyAccessException e) {
                        throw new BadProgrammerException("fail to read the amount of a contribution milestone");
                    }
                }
            });
            addTextCell(new Locale("en", contact.getCountry()).getDisplayCountry(Context.getLocalizator().getLocale()));

            addTextCell((contact.isCompany() ? Context.tr("Yes") : Context.tr("No")));

            addCell(new HtmlTableCell("") {
                @Override
                public HtmlNode getBody() {

                    HtmlDiv div = new HtmlDiv();
                    String taxIdentification = contact.getTaxIdentification();
                    if (taxIdentification != null) {
                        div.add(new HtmlText(taxIdentification));
                        UrlString urlString = new UrlString("http://ec.europa.eu/taxation_customs/vies/viesquer.do?ms="
                                + taxIdentification.substring(0, 2) + "&vat=" + taxIdentification.substring(2));
                        HtmlLink link = urlString.getHtmlLink(Context.tr("Check VAT number"));
                        link.setOpenInNewPage();

                        div.add(new HtmlDiv("vat_check").add(link));
                    }
                    return div;
                }
            });

            addCell(new HtmlTableCell("") {
                @Override
                public HtmlNode getBody() {
                    final HtmlCheckbox box = new HtmlCheckbox(generateInvoiceActionUrl.getApplyVATParameter().pickFieldData().getName(),
                                                              LabelPosition.AFTER);
                    String id = milestoneContributionAmount.getId().toString();
                    box.addAttribute("value", id);
                    if (applyVAT.contains(id) || preview == false) {
                        box.addAttribute("checked", "checked");
                    }

                    return box;
                }
            });

            addCell(new HtmlTableCell("") {
                @Override
                public HtmlNode getBody() {
                    ContributionInvoicePreviewDataUrl dataUrl = new ContributionInvoicePreviewDataUrl(process.getActor(),
                                                                                                      milestoneContributionAmount,
                                                                                                      invoiceIdNumber);
                    String id = milestoneContributionAmount.getId().toString();
                    if (applyVAT.contains(id) || preview == false) {
                        dataUrl.setApplyVAT(true);
                    } else {
                        dataUrl.setApplyVAT(false);
                    }
                    try {
                        return dataUrl.getHtmlLink(process.getActor().getContact().getInvoiceId(invoiceIdNumber));
                    } catch (UnauthorizedPrivateAccessException e) {
                        throw new BadProgrammerException("fail to read the contact informations for the member");
                    }
                }
            });

        }
    }

    private String emptyIfNull(final String input) {
        if (input == null) {
            return "";
        }
        return input;
    }
}
