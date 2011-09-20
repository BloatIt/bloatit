package com.bloatit.web.linkable.contribution;

import com.bloatit.data.DaoContribution;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Contribution;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.CancelContributionActionUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.IndexPageUrl;

@ParamContainer("contribution/docancel/%contribution%")
public class CancelContributionAction extends LoggedElveosAction {

    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the contribution number: ''%value%''."))
    @NonOptional(@tr("You have to specify a contribution number."))
    private Contribution contribution;

    public CancelContributionAction(CancelContributionActionUrl url) {
        super(url);
        this.contribution = url.getContribution();
    }

    @Override
    protected Url doProcessRestricted(Member me) {
        contribution.cancel();

        HtmlMixedText mixed;
        try {
            mixed = new HtmlMixedText(Context.tr("Your {0} contribution on the feature <0::> has been canceled.",
                                                 Context.getLocalizator().getCurrency(contribution.getAmount()).getSimpleEuroString()),
                                      new FeaturePageUrl(contribution.getFeature(), FeatureTabKey.description).getHtmlLink(Context.tr(contribution.getFeature()
                                                                                                                                      .getDescription().getTranslationOrDefault(Language.fromLocale(Context.getLocalizator().getLocale())).getTitle())));
        } catch (UnauthorizedOperationException e) {
            throw new ShallNotPassException("User get info on his contribution");
        }

        session.notifyGood(mixed);
        return new FeaturePageUrl(contribution.getFeature(), FeatureTabKey.contributions);
    }

    @Override
    protected Url doProcessErrors() {
        if (contribution != null) {
            return new FeaturePageUrl(contribution.getFeature(), FeatureTabKey.description);
        }
        return new IndexPageUrl();
    }

    @Override
    protected Url checkRightsAndEverything(Member me) {
        if (!contribution.getAuthor().equals(me)) {
            Context.getSession().notifyWarning(Context.tr("You cannot cancel a contribution you didn't make."));
            return new FeaturePageUrl(contribution.getFeature(), FeatureTabKey.description);
        }
        if (contribution.getState() != DaoContribution.ContributionState.PENDING) {
            Context.getSession().notifyWarning(Context.tr("You cannot cancel an already canceled contribution."));
            return new FeaturePageUrl(contribution.getFeature(), FeatureTabKey.description);
        }
        switch (contribution.getFeature().getFeatureState()) {
            case DEVELOPPING:
                Context.getSession().notifyWarning(Context.tr("You cannot cancel a contribution when development started."));
                return new FeaturePageUrl(contribution.getFeature(), FeatureTabKey.description);
            case DISCARDED:
                Context.getSession().notifyWarning(Context.tr("You cannot cancel a contribution on a discarded feature."));
                return new FeaturePageUrl(contribution.getFeature(), FeatureTabKey.description);
            case FINISHED:
                Context.getSession().notifyWarning(Context.tr("You cannot cancel a contribution on a finished feature."));
                return new FeaturePageUrl(contribution.getFeature(), FeatureTabKey.description);
        }
        return NO_ERROR;
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to cancel a contribution");
    }

    @Override
    protected void transmitParameters() {
    }
}
