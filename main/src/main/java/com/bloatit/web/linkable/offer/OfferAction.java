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
package com.bloatit.web.linkable.offer;

import java.math.BigDecimal;
import java.util.Locale;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.utils.i18n.DateLocale;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.Offer;
import com.bloatit.model.Team;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.linkable.features.FeatureTabPane;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.MakeOfferPageUrl;
import com.bloatit.web.url.OfferActionUrl;

/**
 * Class that will create a new offer based on data received from a form.
 */
@ParamContainer("action/offer")
public final class OfferAction extends LoggedAction {

    @RequestParam(role = Role.GET, conversionErrorMsg = @tr("The target feature is mandatory to make an offer."))
    private final Feature feature;

    @RequestParam(role = Role.GET)
    @Optional
    private final Offer draftOffer;

    @RequestParam(role = Role.POST, conversionErrorMsg = @tr("Invalid or missing value for price field."))
    private final BigDecimal price;

    @RequestParam(role = Role.POST)
    private final DateLocale expiryDate;

    @RequestParam(role = Role.POST)
    private final String description;

    @RequestParam(role = Role.POST)
    private final String locale;

    @RequestParam(role = Role.POST)
    private final Integer daysBeforeValidation;

    @RequestParam(role = Role.POST)
    @Optional
    @ParamConstraint(min = "0", minErrorMsg = @tr("''%param'' is a percent, and must be greater or equal to 0."), //
    max = "100", maxErrorMsg = @tr("''%param'' is a percent, and must be lesser or equal to 100."))
    private final Integer percentFatal;

    @RequestParam(role = Role.POST)
    @Optional
    @ParamConstraint(min = "0", minErrorMsg = @tr("''%param'' is a percent, and must be greater or equal to 0."), //
    max = "100", maxErrorMsg = @tr("''%param'' is a percent, and must be lesser or equal to 100."))
    private final Integer percentMajor;

    @RequestParam(role = Role.POST, suggestedValue = "true")
    private final Boolean isFinished;

    @RequestParam(role = Role.POST)
    @Optional
    private final Team team;

    private final OfferActionUrl url;

    public OfferAction(final OfferActionUrl url) {
        super(url);
        this.url = url;
        this.description = url.getDescription();
        this.locale = url.getLocale();
        this.expiryDate = url.getExpiryDate();
        this.price = url.getPrice();
        this.feature = url.getFeature();
        this.draftOffer = url.getDraftOffer();
        this.team = url.getTeam();
        this.daysBeforeValidation = url.getDaysBeforeValidation();
        this.percentFatal = url.getPercentFatal();
        this.percentMajor = url.getPercentMajor();
        this.isFinished = url.getIsFinished();
    }

    @Override
    public Url doProcessRestricted(final Member authenticatedMember) {

        Offer constructingOffer;
        try {
            Milestone constructingMilestone;
            if (draftOffer == null) {
                constructingOffer = feature.addOffer(session.getAuthToken().getMember(),
                                                     price,
                                                     description,
                                                     new Locale(locale),
                                                     expiryDate.getJavaDate(),
                                                     daysBeforeValidation * DateUtils.SECOND_PER_DAY);
                if (team != null) {
                    constructingOffer.setAsTeam(team);
                }
                constructingMilestone = constructingOffer.getMilestonees().iterator().next();
            } else {
                constructingOffer = draftOffer;
                constructingMilestone = draftOffer.addMilestone(price,
                                                                description,
                                                                new Locale(locale),
                                                                expiryDate.getJavaDate(),
                                                                daysBeforeValidation * DateUtils.SECOND_PER_DAY);
            }
            if (percentFatal != null && percentMajor != null) {
                constructingMilestone.updateMajorFatalPercent(percentFatal, percentMajor);
            }
            if (isFinished) {
                constructingOffer.setDraftFinished();

                final FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature);
                featurePageUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.OFFERS_TAB);
                return featurePageUrl;
            }

        } catch (final UnauthorizedOperationException e) {
            Context.getSession().notifyError(Context.tr("Error creating an offer. Please notify us"));
            throw new ShallNotPassException("Error creating an offer", e);
        }

        final MakeOfferPageUrl returnUrl = new MakeOfferPageUrl(feature);
        returnUrl.setOffer(constructingOffer);
        return returnUrl;
    }

    @Override
    protected Url doCheckRightsAndEverything(final Member authenticatedMember) {
        if ((percentFatal != null && percentMajor == null) || (percentFatal == null && percentMajor != null)) {
            session.notifyBad(Context.tr("You have to specify both the Major and Fatal percent."));
            return session.pickPreferredPage();
        }
        if (percentFatal != null && percentFatal + percentMajor > 100) {
            session.notifyBad(Context.tr("Major + Fatal percent cannot be > 100 !!"));
            return session.pickPreferredPage();
        }
        if (draftOffer != null && !draftOffer.isDraft()) {
            session.notifyBad(Context.tr("The specified offer is not editable. You cannot add a lot in it."));
            return session.pickPreferredPage();
        }
        if (team != null && !team.getUserTeamRight(authenticatedMember).contains(UserTeamRight.TALK)) {
            session.notifyBad(Context.tr("You cannot talk on the behalf of this team."));
            return session.pickPreferredPage();
        }
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        if (feature != null) {
            transmitParameters();
            final MakeOfferPageUrl redirectUrl = new MakeOfferPageUrl(feature);
            redirectUrl.setOffer(draftOffer);
            return redirectUrl;
        }
        return session.pickPreferredPage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to make an offer.");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getLocaleParameter());
        session.addParameter(url.getExpiryDateParameter());
        session.addParameter(url.getPriceParameter());
        session.addParameter(url.getTeamParameter());
        session.addParameter(url.getDaysBeforeValidationParameter());
        session.addParameter(url.getPercentFatalParameter());
        session.addParameter(url.getPercentMajorParameter());
        session.addParameter(url.getIsFinishedParameter());
    }
}
