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
package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import java.math.BigDecimal;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.XmlText;
import com.bloatit.model.Feature;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.url.CheckContributionPageUrl;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.FeaturePageUrl;

/**
 * A page that hosts the form used to check the contribution on a Feature
 */
@ParamContainer("contribute/check")
public final class CheckContributionPage extends LoggedPage {

    public static final String AMOUNT_CODE = "contributionAmount";
    public static final String COMMENT_CODE = "comment";
    public static final String TARGET_FEATURE = "targetFeature";

    @RequestParam(name = TARGET_FEATURE)
    private final Feature targetFeature;

    @RequestParam(name = COMMENT_CODE, role = Role.GET)
    @ParamConstraint(max = "140", maxErrorMsg = @tr("Your comment is too long. It must be less than 140 char long."))
    @Optional
    private final String comment;

    @RequestParam(name = AMOUNT_CODE, role = Role.GET)
    @ParamConstraint(min = "0", minIsExclusive = true, minErrorMsg = @tr("Amount must be superior to 0."),//
    max = "1000000000", maxErrorMsg = @tr("We cannot accept such a generous offer!"),//
    precision = 0, precisionErrorMsg = @tr("Please do not use Cents."))
    private final BigDecimal amount;

    private final CheckContributionPageUrl url;

    public CheckContributionPage(final CheckContributionPageUrl url) {
        super(url);
        this.url = url;
        targetFeature = url.getTargetFeature();
        amount = url.getAmount();
        comment = url.getComment();
    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {
        addNotifications(url.getMessages());
        if (url.getMessages().hasMessage()) {
            session.notifyList(url.getMessages());
            throw new RedirectException(Context.getSession().getLastStablePage());
        }

        final TwoColumnLayout layout = new TwoColumnLayout(true);
        layout.addLeft(generateCheckContributeForm());

        layout.addRight(new SideBarFeatureBlock(targetFeature));

        return layout;
    }

    public HtmlElement generateCheckContributeForm() throws RedirectException {
        final HtmlTitleBlock group = new HtmlTitleBlock("Check contribution", 1);

        if(comment != null) {
            group.add(new XmlText(tr("Contribute for {0} with comment : \"{1}\".", Context.getLocalizator().getCurrency(amount).toString(), comment)));
        } else {
            group.add(new XmlText(tr("Contribute for {0}.", Context.getLocalizator().getCurrency(amount).toString())));
        }

        try {
            BigDecimal account = Context.getSession().getAuthToken().getMember().getInternalAccount().getAmount();

            if(amount.compareTo(account) <= 0) {
                //enought money
                group.add(new HtmlParagraph(tr("You have enought money on your internal account.")));

                BigDecimal accountAfter = account.subtract(amount);

                group.add(new HtmlParagraph(tr("Money before the contribution: {0}.", Context.getLocalizator().getCurrency(account))));
                group.add(new HtmlParagraph(tr("Money after the contribution: {0}.", Context.getLocalizator().getCurrency(accountAfter))));

                ContributionActionUrl contributionActionUrl = new ContributionActionUrl(targetFeature, amount);
                contributionActionUrl.setComment(comment);
                HtmlLink confirmContributionLink = contributionActionUrl.getHtmlLink(tr("Confirm contribution"));
                confirmContributionLink.setCssClass("button");
                group.add(confirmContributionLink);

            } else {
                group.add(new HtmlParagraph("You haven't enought money on your internal account."));
            }


        } catch (UnauthorizedOperationException e) {
            Log.web().error("Fail to check contribution",e);
            throw new RedirectException(new FeaturePageUrl(targetFeature));
        }

        //Modify contribution button
        ContributePageUrl contributePageUrl = new ContributePageUrl(targetFeature);
        session.addParameter(url.getAmountParameter());
        session.addParameter(url.getCommentParameter());
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
        return CheckContributionPage.generateBreadcrumb(targetFeature);
    }

    public static Breadcrumb generateBreadcrumb(Feature feature) {
        Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbContributions(feature);

        breadcrumb.pushLink(new ContributePageUrl(feature).getHtmlLink(tr("Contribute - Check")));

        return breadcrumb;
    }
}
