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
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ContributionInvoicingInformationsActionUrl;
import com.bloatit.web.url.ContributionInvoicingInformationsPageUrl;

/**
 * A page used choose or create the invoicing contact to use for the comission
 * invoice.
 */
@ParamContainer(value = "invoicing/contribution_invoicing_informations", protocol = Protocol.HTTPS)
public final class ContributionInvoicingInformationsPage extends LoggedPage {

    @RequestParam()
    private final ContributionInvoicingProcess process;

    private final ContributionInvoicingInformationsPageUrl url;

    public ContributionInvoicingInformationsPage(final ContributionInvoicingInformationsPageUrl url) {
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
        layout.addLeft(generateInvoicingInformationsForm(loggedUser));

        return layout;
    }

    private HtmlElement generateInvoicingInformationsForm(final Member member) {
        final HtmlTitleBlock group = new HtmlTitleBlock("Additional invoicing informations", 1);

        // Create contact form
        final ContributionInvoicingInformationsActionUrl contributionInvoicingInformationActionUrl = new ContributionInvoicingInformationsActionUrl(process);
        final HtmlForm form = new HtmlForm(contributionInvoicingInformationActionUrl.urlString());


        final HtmlSubmit newContactButton = new HtmlSubmit(Context.tr("Generate invoices"));
        form.add(newContactButton);

        return form;
    }

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

        final Url url = new ContributionInvoicingInformationsPageUrl(process);

        breadcrumb.pushLink(url.getHtmlLink(tr("Contribution Invoicing contact")));
        return breadcrumb;
    }

}
