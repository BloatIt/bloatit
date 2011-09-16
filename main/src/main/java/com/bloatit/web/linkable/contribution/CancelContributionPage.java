package com.bloatit.web.linkable.contribution;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;

import com.bloatit.data.DaoContribution;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.Contribution;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.HtmlDefineParagraph;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.members.MembersTools;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
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
            throw new RedirectException(new IndexPageUrl());
        }

        TwoColumnLayout master = new TwoColumnLayout(true, url);

        SideBarFeatureBlock sbfb;
        try {
            // TODO doesn't work now
            sbfb = new SideBarFeatureBlock(contribution.getFeature(), new BigDecimal(-(contribution.getAmount().doubleValue())));
        } catch (UnauthorizedOperationException e) {
            throw new ShallNotPassException("Woopsie");
        }
        master.addRight(sbfb);

        HtmlTitle title = new HtmlTitle(Context.tr("Cancel contribution"), 1);
        master.addLeft(title);

        try {
            master.addLeft(contributionCancelSummary(contribution.getFeature(), loggedUser));
        } catch (UnauthorizedOperationException e) {
            throw new ShallNotPassException("Cannot access some contribution info.", e);
        }

        CancelContributionActionUrl targetUrl = new CancelContributionActionUrl(getSession().getShortKey(), contribution);
        HtmlForm form = new HtmlForm(targetUrl.urlString());
        master.addLeft(form);

        form.add(new HtmlSubmit(Context.tr("Cancel contribution")));

        return master;
    }

    /**
     * Display a block summarizing the effect of the cancelation of a
     * contribution on both the user account and the feature
     * 
     * @param feature the feature on which the contribution happened
     * @param actor the author of the contribution
     * @return the element containing the suymmary
     * @throws UnauthorizedOperationException
     */
    private HtmlElement contributionCancelSummary(final Feature feature, final Actor<?> actor) throws UnauthorizedOperationException {
        PlaceHolderElement elem = new PlaceHolderElement();

        final HtmlDiv contributionSummaryDiv = new HtmlDiv("contribution_summary");
        {
            contributionSummaryDiv.add(generateFeatureSummary(feature));
            final HtmlDiv authorContributionSummary = new HtmlDiv("author_contribution_summary");
            {

                if (contribution.getAsTeam() != null) {
                    authorContributionSummary.add(new HtmlTitle(tr("Account of {0}", actor.getDisplayName()), 2));
                } else {
                    authorContributionSummary.add(new HtmlTitle(tr("Your account"), 2));
                }

                try {
                    final HtmlDiv changeLine = new HtmlDiv("change_line");
                    {
                        changeLine.add(new MoneyVariationBlock(actor.getInternalAccount().getAmount(), actor.getInternalAccount()
                                                                                                            .getAmount()
                                                                                                            .add(contribution.getAmount())));
                        changeLine.add(MembersTools.getMemberAvatar(actor));
                        authorContributionSummary.add(changeLine);
                        authorContributionSummary.add(new HtmlDefineParagraph(tr("Author: "), actor.getDisplayName()));
                    }
                } catch (final UnauthorizedOperationException e) {
                    getSession().notifyError(Context.tr("An error prevented us from accessing user's info. Please notify us."));
                    throw new ShallNotPassException("User cannot access user information", e);
                }
            }
            contributionSummaryDiv.add(authorContributionSummary);

        }
        elem.add(contributionSummaryDiv);

        return elem;
    }

    /**
     * Display a small block with the summary of the feature to be displayed in
     * the contribution cancelation summary block
     * 
     * @param feature
     * @return
     * @throws UnauthorizedOperationException
     */
    private HtmlDiv generateFeatureSummary(final Feature feature) throws UnauthorizedOperationException {
        final HtmlDiv featureContributionSummary = new HtmlDiv("feature_contribution_summary");
        {
            featureContributionSummary.add(new HtmlTitle(tr("The feature"), 2));

            final HtmlDiv changeLine = new HtmlDiv("change_line");
            {
                changeLine.add(new SoftwaresTools.Logo(feature.getSoftware()));
                changeLine.add(new MoneyVariationBlock(feature.getContribution(), feature.getContribution().subtract(contribution.getAmount())));
            }
            featureContributionSummary.add(changeLine);
            featureContributionSummary.add(new HtmlDefineParagraph(tr("Target feature: "), FeaturesTools.getTitle(feature)));
        }
        return featureContributionSummary;
    }

    /**
     * Checks if everything is valid in the information provided to the page.
     * 
     * @return <i>true</i> if the user can access this page, <i>false</i>
     *         otherwise
     */
    private boolean checkRightsAndEverything(Member loggedUser) {
        if (!contribution.getAuthor().equals(loggedUser)) {
            getSession().notifyWarning(Context.tr("You cannot cancel a contribution you didn't make."));
            return false;
        }
        if(contribution.getState() != DaoContribution.State.PENDING){
            getSession().notifyWarning(Context.tr("You cannot cancel an already canceled contribution."));
            return false;
        }
        switch (contribution.getFeature().getFeatureState()) {
            case DEVELOPPING:
                getSession().notifyWarning(Context.tr("You cannot cancel a contribution when development started."));
                return false;
            case DISCARDED:
                getSession().notifyWarning(Context.tr("You cannot cancel a contribution on a discarded feature."));
                return false;
            case FINISHED:
                getSession().notifyWarning(Context.tr("You cannot cancel a contribution on a finished feature."));
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
