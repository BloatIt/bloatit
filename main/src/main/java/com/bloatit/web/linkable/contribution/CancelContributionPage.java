package com.bloatit.web.linkable.contribution;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Contribution;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.pages.LoggedElveosPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CancelContributionActionUrl;
import com.bloatit.web.url.CancelContributionPageUrl;
import com.bloatit.web.url.IndexPageUrl;

@ParamContainer("contributions/cancel/%contribution%")
public class CancelContributionPage extends LoggedElveosPage {
    private CancelContributionPageUrl url;

    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the contribution number: ''%value%''."))
    @NonOptional(@tr("You have to specify a contribution number."))
    private Contribution contribution;

    public CancelContributionPage(CancelContributionPageUrl url) {
        super(url);
        this.url = url;
        this.contribution = url.getContribution();
    }

    @Override
    public HtmlElement createRestrictedContent(Member loggedUser) throws RedirectException {
        if (!checkRightsAndEverything(loggedUser)) {
            getSession().notifyWarning(Context.tr("You cannot cancel a contribution you didn't make."));
            throw new RedirectException(new IndexPageUrl());
        }

        TwoColumnLayout master = new TwoColumnLayout(true, url);

        HtmlParagraph info = new HtmlParagraph(Context.tr("Are you sure you want to cancel your contribution ?"));
        master.addLeft(info);

        CancelContributionActionUrl targetUrl = new CancelContributionActionUrl(getSession().getShortKey(), contribution);
        HtmlForm form = new HtmlForm(targetUrl.urlString());
        master.addLeft(form);

        form.add(new HtmlSubmit(Context.tr("Cancel contribution")));

        return master;
    }

    /**
     * Checks if everything is valid in the information provided to the page.
     * 
     * @return <i>true</i> if the user can access this page, <i>false</i>
     *         otherwise
     */
    private boolean checkRightsAndEverything(Member loggedUser) {
        if (!contribution.getAuthor().equals(loggedUser)) {
            return false;
        }

        return true;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to cancel a contribution");
    }

    @Override
    protected Breadcrumb createBreadcrumb(Member loggedUser) {
        return generateBreadcrumb(contribution);
    }

    public static Breadcrumb generateBreadcrumb(final Contribution contribution) {
        Breadcrumb breadcrumb = FeaturePage.generateBreadcrumb(contribution.getFeature());
        breadcrumb.pushLink(new CancelContributionPageUrl(contribution).getHtmlLink(Context.tr("Cancel contribution")));
        return breadcrumb;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Cancel contribution");
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
