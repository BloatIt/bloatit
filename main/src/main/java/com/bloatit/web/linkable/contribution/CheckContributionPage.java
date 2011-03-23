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
package com.bloatit.web.linkable.contribution;

import static com.bloatit.framework.webserver.Context.tr;

import java.math.BigDecimal;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.XmlText;
import com.bloatit.model.Feature;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.url.AccountChargingPageUrl;
import com.bloatit.web.url.CheckContributionPageUrl;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.FeaturePageUrl;

/**
 * A page that hosts the form used to check the contribution on a Feature
 */
@ParamContainer("contribute/check")
public final class CheckContributionPage extends LoggedPage {

    @RequestParam
    @ParamConstraint(optionalErrorMsg=@tr("The process is closed, expired, missing or invalid."))
    private final ContributionProcess process;


    private final CheckContributionPageUrl url;

    public CheckContributionPage(final CheckContributionPageUrl url) {
        super(url);
        this.url = url;
        process = url.getProcess();

    }


    @Override
    public void processErrors() throws RedirectException {
        addNotifications(url.getMessages());
        if (url.getMessages().hasMessage()) {
            session.notifyList(url.getMessages());
            throw new RedirectException(Context.getSession().getLastStablePage());
        }

    }


    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {


        final TwoColumnLayout layout = new TwoColumnLayout(true);
        layout.addLeft(generateCheckContributeForm());

        layout.addRight(new SideBarFeatureBlock(process.getFeature()));

        return layout;
    }

    public HtmlElement generateCheckContributeForm() throws RedirectException {
        final HtmlTitleBlock group = new HtmlTitleBlock("Check contribution", 1);

        if(process.getComment() != null) {
            group.add(new XmlText(tr("Contribute for {0} with comment : \"{1}\".", Context.getLocalizator().getCurrency(process.getAmount()).toString(), process.getComment())));
        } else {
            group.add(new XmlText(tr("Contribute for {0}.", Context.getLocalizator().getCurrency(process.getAmount()).toString())));
        }

        try {
            BigDecimal account = Context.getSession().getAuthToken().getMember().getInternalAccount().getAmount();

            if(process.getAmount().compareTo(account) <= 0) {
                //enought money
                group.add(new HtmlParagraph(tr("You have enought money on your internal account.")));

                BigDecimal accountAfter = account.subtract(process.getAmount());

                group.add(new HtmlParagraph(tr("Money in internal account before the contribution: {0}.", Context.getLocalizator().getCurrency(account))));
                group.add(new HtmlParagraph(tr("Money in internal account after the contribution: {0}.", Context.getLocalizator().getCurrency(accountAfter))));

                ContributionActionUrl contributionActionUrl = new ContributionActionUrl(process);
                HtmlLink confirmContributionLink = contributionActionUrl.getHtmlLink(tr("Confirm contribution"));
                confirmContributionLink.setCssClass("button");
                group.add(confirmContributionLink);

            } else {
                group.add(new HtmlParagraph("You haven't enought money on your internal account."));

                BigDecimal missingAmount = process.getAmount().subtract(account);

                group.add(new HtmlParagraph(tr("Missing amount: {0}.", Context.getLocalizator().getCurrency(missingAmount))));

                session.setTargetPage(url);


                AccountChargingPageUrl accountChargingPageUrl = new AccountChargingPageUrl();
                accountChargingPageUrl.setAmount(missingAmount);
                HtmlLink chargeAccountLink = accountChargingPageUrl.getHtmlLink(tr("Charge account"));
                chargeAccountLink.setCssClass("button");
                group.add(chargeAccountLink);
            }


        } catch (UnauthorizedOperationException e) {
            Log.web().error("Fail to check contribution",e);
            throw new RedirectException(new FeaturePageUrl(process.getFeature()));
        }

        //Modify contribution button
        ContributePageUrl contributePageUrl = new ContributePageUrl(process);
        HtmlLink modifyContributionLink = contributePageUrl.getHtmlLink(tr("Modify contribution"));
        modifyContributionLink.setCssClass("button");


        group.add(modifyContributionLink);

        return group;
    }

    @Override
    protected String getPageTitle() {
        return tr("Contribute to a feature - check");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to contribute");
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return CheckContributionPage.generateBreadcrumb(process.getFeature(), process);
    }

    public static Breadcrumb generateBreadcrumb(Feature feature, ContributionProcess process) {
        Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbContributions(feature);

        breadcrumb.pushLink(new CheckContributionPageUrl(process).getHtmlLink(tr("Contribute - Check")));

        return breadcrumb;
    }


}
