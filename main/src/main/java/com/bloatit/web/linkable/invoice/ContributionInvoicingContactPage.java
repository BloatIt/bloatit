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

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ContributionInvoicingContactPageUrl;

/**
 * A page used choose or create the invoicing contact to use for the comission
 * invoice.
 */
@ParamContainer(value = "invoicing/invoicing_contact", protocol = Protocol.HTTPS)
public final class ContributionInvoicingContactPage extends LoggedPage {



    @RequestParam()
    private final ContributionInvoicingProcess process;

    private final ContributionInvoicingContactPageUrl url;

    public ContributionInvoicingContactPage(final ContributionInvoicingContactPageUrl url) {
        super(url);
        this.url = url;
        this.process = url.getProcess();
    }

    @Override
    public HtmlElement createBodyContentOnParameterError(final ElveosUserToken userToken) throws RedirectException {
        if (url.getMessages().hasMessage()) {
            throw new RedirectException(Context.getSession().pickPreferredPage());
        }
        return createBodyContent(userToken);
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateInvoicingContactForm(loggedUser));

        return layout;
    }

    private HtmlElement generateInvoicingContactForm(final Member member) {
        final HtmlTitleBlock group = new HtmlTitleBlock("plop",1);
        /*if (process.getActor().isTeam()) {
            group = new HtmlTitleBlock(tr("Invoicing informations of {0}", process.getActor().getDisplayName()), 1);
        } else {
            group = new HtmlTitleBlock(tr("Your invoicing informations"), 1);
        }

        try {
            if (getActor(member).getInvoicingContacts().size() > 0) {
                group.add(generateSelectInvoicingContactForm(member));
            }
        } catch (UnauthorizedOperationException e) {
            throw new ShallNotPassException("Fail to get invoicing contacts", e);
        }

        group.add(generateNewInvoicingContactForm(member));
*/
        return group;
    }

//    private HtmlElement generateNewInvoicingContactForm(final Member member) {
//
//        // Create contact form
//        final CreateInvoicingContactActionUrl createInvoicingContextActionUrl = new CreateInvoicingContactActionUrl(process);
//        final HtmlForm newContactForm = new HtmlForm(createInvoicingContextActionUrl.urlString());
//
//        // Name
//        final FieldData nameData = createInvoicingContextActionUrl.getNameParameter().pickFieldData();
//
//        String name = "";
//
//        if (team == null) {
//            name = Context.tr("Name");
//        } else {
//            name = Context.tr("Organisation name");
//        }
//
//        final HtmlTextField nameInput = new HtmlTextField(nameData.getName(), name);
//        nameInput.setDefaultValue(nameData.getSuggestedValue());
//        nameInput.addErrorMessages(nameData.getErrorMessages());
//        if (team == null) {
//            nameInput.setComment(Context.tr("Your full name"));
//        } else {
//            nameInput.setComment(Context.tr("The name of your company or your association."));
//        }
//        newContactForm.add(nameInput);
//
//        final FieldData addressData = createInvoicingContextActionUrl.getAddressParameter().pickFieldData();
//
//        final HtmlTextArea addressInput = new HtmlTextArea(addressData.getName(), Context.tr("Address"), 10, 80);
//        addressInput.setDefaultValue(addressData.getSuggestedValue());
//        addressInput.addErrorMessages(addressData.getErrorMessages());
//        addressInput.setComment(Context.tr("The full address, including the city and the country."));
//        newContactForm.add(addressInput);
//
//        final HtmlSubmit newContactButton = new HtmlSubmit(Context.tr("New invoicing contact"));
//        newContactForm.add(newContactButton);
//
//
//        return newContactForm;
//    }
//
//    private HtmlElement generateSelectInvoicingContactForm(final Member member) throws UnauthorizedOperationException {
//     // Create contact form
//        final ChooseInvoicingContactActionUrl chooseInvoicingContextActionUrl = new ChooseInvoicingContactActionUrl(process);
//        final HtmlForm chooseContactForm = new HtmlForm(chooseInvoicingContextActionUrl.urlString());
//
//        // Name
//
//
//        // Linked contacts
//        final FieldData contactData = chooseInvoicingContextActionUrl.getInvoicingContactParameter().pickFieldData();
//        final HtmlDropDown contactInput = new HtmlDropDown(contactData.getName(), Context.tr("Invoicing contact"));
//        for (final InvoicingContact contact: getActor(member).getInvoicingContacts()) {
//            contactInput.addDropDownElement(String.valueOf(contact.getId()), contact.getName() + " - "+ contact.getAddress());
//        }
//        chooseContactForm.add(contactInput);
//
//
//
//
//
//        final HtmlSubmit newContactButton = new HtmlSubmit(Context.tr("Select"));
//        chooseContactForm.add(newContactButton);
//
//        return chooseContactForm;
//    }


    @Override
    protected String createPageTitle() {
        return tr("Contribution invoicing informations");
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

        Url url = new ContributionInvoicingContactPageUrl(process);

        breadcrumb.pushLink(url.getHtmlLink(tr("Contribution Invoicing contact")));
        return breadcrumb;
    }

}
