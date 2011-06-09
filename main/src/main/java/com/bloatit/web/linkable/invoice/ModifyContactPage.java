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

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.web.linkable.contribution.CheckContributionPage;
import com.bloatit.web.linkable.contribution.ContributionProcess;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.money.AccountChargingPage;
import com.bloatit.web.linkable.money.AccountChargingProcess;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ModifyContactPageUrl;
import com.bloatit.web.url.ModifyInvoicingContactActionUrl;

/**
 * A page used choose or create the invoicing contact to use for the comission
 * invoice.
 */
@ParamContainer(value = "account/charging/invoicing_contact", protocol = Protocol.HTTPS)
public final class ModifyContactPage extends LoggedPage {

    @RequestParam(conversionErrorMsg = @tr("The process is closed, expired, missing or invalid."))
    @ParamConstraint(optionalErrorMsg = @tr("The process is closed, expired, missing or invalid."))
    private final ModifyInvoicingContactProcess process;

    private final ModifyContactPageUrl url;

    public ModifyContactPage(final ModifyContactPageUrl url) {
        super(url);
        this.url = url;
        process = url.getProcess();
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
        layout.addRight(new SideBarDocumentationBlock("account_charging"));
        return layout;
    }

    private HtmlElement generateInvoicingContactForm(final Member member) {
        final HtmlTitleBlock group;
        if (process.getActor().isTeam()) {
            group = new HtmlTitleBlock(tr("Invoicing informations of {0}", process.getActor().getDisplayName()), 1);
        } else {
            group = new HtmlTitleBlock(tr("Your invoicing informations"), 1);
        }

        group.add(generateNewInvoicingContactForm(member));

        return group;
    }

    private HtmlElement generateNewInvoicingContactForm(final Member member) {

        // Create contact form
        final ModifyInvoicingContactActionUrl modifyInvoicingContextActionUrl = new ModifyInvoicingContactActionUrl(process);
        final HtmlForm newContactForm = new HtmlForm(modifyInvoicingContextActionUrl.urlString());

        // Name
        final FieldData nameData = modifyInvoicingContextActionUrl.getNameParameter().pickFieldData();

        String name = "";

        if (process.getActor().isTeam()) {
            name = Context.tr("Organisation name");
        } else {
            name = Context.tr("Name");
        }

        final HtmlTextField nameInput = new HtmlTextField(nameData.getName(), name);
        if(nameData.getSuggestedValue() == null) {
            try {
                nameInput.setDefaultValue(process.getActor().getContact().getName());
            } catch (UnauthorizedPrivateAccessException e) {
                throw new ShallNotPassException("The user is not allowed to access to his contact informations");
            }
        } else {
            nameInput.setDefaultValue(nameData.getSuggestedValue());    
        }
        nameInput.addErrorMessages(nameData.getErrorMessages());
        if (process.getActor().isTeam()) {
            nameInput.setComment(Context.tr("The name of your company or your association."));
        } else {
            nameInput.setComment(Context.tr("Your full name"));
        }
        newContactForm.add(nameInput);

        final FieldData addressData = modifyInvoicingContextActionUrl.getStreetParameter().pickFieldData();

        final HtmlTextArea addressInput = new HtmlTextArea(addressData.getName(), Context.tr("Street"), 10, 80);
        
        if(addressData.getSuggestedValue() == null) {
            try {
                addressInput.setDefaultValue(process.getActor().getContact().getCity());
            } catch (UnauthorizedPrivateAccessException e) {
                throw new ShallNotPassException("The user is not allowed to access to his contact informations");
            }
        } else {
            addressInput.setDefaultValue(addressData.getSuggestedValue());    
        }
        addressInput.addErrorMessages(addressData.getErrorMessages());
        newContactForm.add(addressInput);

        final HtmlSubmit newContactButton = new HtmlSubmit(Context.tr("Update invoicing contact"));
        newContactForm.add(newContactButton);


        return newContactForm;
    }



    @Override
    protected String createPageTitle() {
        return tr("Invoicing informations");
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
        return generateBreadcrumb(member, (process.getActor().isTeam() ? (Team) process.getActor(): null) , process);
    }

    protected static Breadcrumb generateBreadcrumb(final Member member, final Team asTeam, final ModifyInvoicingContactProcess process) {
        final Breadcrumb breadcrumb;

        if(process.getFather() instanceof AccountChargingProcess) {
            breadcrumb = AccountChargingPage.generateBreadcrumb(member, asTeam, (AccountChargingProcess) process.getFather());
        } else if(process.getFather() instanceof ContributionProcess) {
            ContributionProcess process2 = (ContributionProcess) process.getFather() ;
            breadcrumb = CheckContributionPage.generateBreadcrumb(process2.getFeature(), process2);
        } else {
            breadcrumb = IndexPage.generateBreadcrumb();
        }

        Url url = new ModifyContactPageUrl(process);

        breadcrumb.pushLink(url.getHtmlLink(tr("Invoicing contact")));
        return breadcrumb;
    }

}
