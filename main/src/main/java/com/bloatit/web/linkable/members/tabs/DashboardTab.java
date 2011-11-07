package com.bloatit.web.linkable.members.tabs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock.HtmlTab;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.Feature;
import com.bloatit.model.Follow;
import com.bloatit.model.lists.FollowList;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.linkable.members.tabs.dashboard.Dashboard;
import com.bloatit.web.linkable.members.tabs.dashboard.DashboardEntry;
import com.bloatit.web.linkable.members.tabs.dashboard.DashboardRenderer;
import com.bloatit.web.linkable.members.tabs.dashboard.DashboardStep;
import com.bloatit.web.linkable.members.tabs.dashboard.DashboardStep.StepState;
import com.bloatit.web.url.ContributionProcessUrl;
import com.bloatit.web.url.MakeOfferPageUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.StopFollowActionUrl;

@ParamContainer(value = "dashboardTab", isComponent = true)
public class DashboardTab extends HtmlTab {
    private final Actor<?> actor;
    private final MemberPageUrl url;

    public DashboardTab(final Actor<?> actor, final String title, final String tabKey, final MemberPageUrl url) {
        super(title, tabKey);
        this.actor = actor;
        this.url = url;
    }

    @Override
    public HtmlNode generateBody() {
        final HtmlDiv master = new HtmlDiv("tab_pane");

        // Displaying list of user recent activity
        final HtmlTitleBlock followed = new HtmlTitleBlock(Context.tr("Content you follow"), 1);
        master.add(followed);

        FollowList followeds = actor.getFollowedContent();
        Dashboard dashboard = new Dashboard();
        for (Follow follow : followeds) {
            Feature f = follow.getFollowed();

            HtmlElement nextStep = null;

            // ///////////////////////
            // GENERATING NEXT STEP
            switch (f.getFeatureState()) {
                case PENDING:
                    // Waiting for offer and money
                    nextStep = new HtmlMixedText(Context.tr("No offer yet, <0::make an offer>"), new MakeOfferPageUrl(f).getHtmlLink());
                    break;
                case PREPARING:
                    // One offer, waiting for more money
                    if (f.getContributionOf(AuthToken.getMember()).compareTo(BigDecimal.ZERO) > 0) {
                        nextStep = new HtmlMixedText(Context.tr("{0}€ missing, <0::contribute> again"), new ContributionProcessUrl(f).getHtmlLink());
                    } else {
                        nextStep = new HtmlMixedText(Context.tr("{0}€ missing, <0::contribute>"), new ContributionProcessUrl(f).getHtmlLink());
                    }
                    break;
                case DEVELOPPING:
                    // One offer validated, waiting for releases
                    if (f.getSelectedOffer().getCurrentMilestone().getReleases().size() >= 1) {
                        if (f.getSelectedOffer().getMember().equals(AuthToken.getMember())) {
                            nextStep = new HtmlMixedText(Context.tr("Developing, <0::make a release>"), new MakeOfferPageUrl(f).getHtmlLink());
                        } else {
                            nextStep = new HtmlMixedText(Context.tr("Development ongoing, <0::make a release>"),
                                                         new MakeOfferPageUrl(f).getHtmlLink());
                        }
                    } else {
                        if (f.getSelectedOffer().getMember().equals(AuthToken.getMember())) {
                            nextStep = new HtmlMixedText(Context.tr("Developing, <0::make a release>"), new MakeOfferPageUrl(f).getHtmlLink());
                        } else {
                            nextStep = new HtmlMixedText(Context.tr("Development ongoing, <0::make a release>"),
                                                         new MakeOfferPageUrl(f).getHtmlLink());
                        }
                    }
                    break;
                case FINISHED:
                    // FINISHED !!!
                    nextStep = new HtmlMixedText(Context.tr("Feature finished ! <0::TODO stop following>"),
                                                 new StopFollowActionUrl(Context.getSession().getShortKey(), follow).getHtmlLink());
                    break;
                case DISCARDED:
                    // Discarded :'( very very sad
                    nextStep = new HtmlMixedText(Context.tr("Feature canceled ! <0::TODO stop following>"),
                                                 new StopFollowActionUrl(Context.getSession().getShortKey(), follow).getHtmlLink());
                    break;
            }
            DashboardEntry entry = new DashboardEntry(f, nextStep, "TODO : Use me or remove me ?", new Date());

            // ///////////////////////
            // GENERATING STEPS

            ArrayList<DashboardStep> steps = new ArrayList<DashboardStep>();
            switch (f.getFeatureState()) {
                case PENDING:
                    contributionStep(steps, f);
                    break;
                case PREPARING:
                    // One offer, waiting for more money
                    offerStep(steps, f);
                    break;
                case DEVELOPPING:
                    // One offer validated, waiting for releases
                    if (f.getSelectedOffer().getCurrentMilestone().getReleases().size() >= 1) {
                        validateStep(steps, f);
                    } else {
                        developpingStep(steps, f);
                    }
                    break;
                case FINISHED:
                    // FINISHED !!!
                    finishedStep(steps, f);
                    break;
                case DISCARDED:
                    // Discarded :'( very very sad
                    discardedStep(steps, f);
                    break;
            }

            for (DashboardStep step : steps) {
                entry.addStep(step);
            }

            dashboard.addEntry(entry);
        }
        followed.add(new DashboardRenderer(dashboard));

        return master;
    }

    private ArrayList<DashboardStep> creationStep(ArrayList<DashboardStep> steps, Feature feature) {
        if (AuthToken.getMember().equals(feature.getMember())) {
            DashboardStep creation = new DashboardStep(Context.tr("Creation"), StepState.COMPLETE, Context.tr("You created this feature"));
            steps.add(creation);
        }
        return steps;
    }

    private ArrayList<DashboardStep> contributionStep(ArrayList<DashboardStep> steps, Feature feature) {
        creationStep(steps, feature);
        DashboardStep contribution = null;
        BigDecimal contributionOf = feature.getContributionOf(AuthToken.getMember());
        if (contributionOf.compareTo(BigDecimal.ZERO) > 0) {
            contribution = new DashboardStep(Context.tr("Contribution"), StepState.COMPLETE, Context.tr("You contributed {0}",
                                                                                                        Context.getLocalizator()
                                                                                                               .getCurrency(contributionOf)
                                                                                                               .getLocaleString()));
        } else {
            if (feature.getFeatureState() == FeatureState.PENDING || feature.getFeatureState() == FeatureState.PREPARING) {
                contribution = new DashboardStep(Context.tr("Contribution"), StepState.COMPLETE, Context.tr("TODO contribute on this feature"));
            }
        }
        if (contribution != null) {
            steps.add(contribution);
        }
        return steps;
    }

    private ArrayList<DashboardStep> offerStep(ArrayList<DashboardStep> steps, Feature feature) {
        contributionStep(steps, feature);
        DashboardStep offer = null;
        return steps;
    }

    private ArrayList<DashboardStep> developpingStep(ArrayList<DashboardStep> steps, Feature feature) {
        offerStep(steps, feature);
        return steps;
    }

    private ArrayList<DashboardStep> validateStep(ArrayList<DashboardStep> steps, Feature feature) {
        developpingStep(steps, feature);
        return steps;
    }

    private ArrayList<DashboardStep> discardedStep(ArrayList<DashboardStep> steps, Feature feature) {
        validateStep(steps, feature);
        return steps;
    }

    private ArrayList<DashboardStep> finishedStep(ArrayList<DashboardStep> steps, Feature feature) {
        validateStep(steps, feature);
        return steps;
    }
}
