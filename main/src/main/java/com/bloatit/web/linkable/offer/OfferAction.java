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

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.utils.i18n.DateLocale;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.Offer;
import com.bloatit.model.Team;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.features.FeatureTabPane;
import com.bloatit.web.linkable.usercontent.UserContentAction;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.MakeOfferPageUrl;
import com.bloatit.web.url.OfferActionUrl;

/**
 * Class that will create a new offer based on data received from a form.
 */
@ParamContainer("offer/docreate")
public final class OfferAction extends UserContentAction {

    @RequestParam(role = Role.GET, message = @tr("The target feature is mandatory to make an offer."))
    private final Feature feature;

    @RequestParam(role = Role.GET)
    @Optional
    private final Offer draftOffer;

    @RequestParam(role = Role.POST, message = @tr("Invalid value for price field."))
    @NonOptional(@tr("You must set a price to your offer."))
    @MinConstraint(min = 1, message = @tr("The price must be greater to 0."))
    private final BigDecimal price;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You must set an expiration date."))
    private final DateLocale expiryDate;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You must add a description to your offer."))
    private final String description;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You must add a license to your offer."))
    private final String license;

    @RequestParam(role = Role.POST, suggestedValue = "7")
    @NonOptional(@tr("You must set a days count for validation."))
    @MinConstraint(min = 1, message = @tr("The validation time must be greater to 0."))
    private final Integer daysBeforeValidation;

    @Optional
    @RequestParam(role = Role.POST, suggestedValue = "100")
    @MinConstraint(min = 0, message = @tr("''%paramName%'' is a percent, and must be greater or equal to 0."))
    @MaxConstraint(max = 0, message = @tr("''%paramName%'' is a percent, and must be lesser or equal to 100."))
    private final Integer percentFatal;

    @RequestParam(role = Role.POST, suggestedValue = "0")
    @Optional
    @MinConstraint(min = 0, message = @tr("''%paramName%'' is a percent, and must be greater or equal to 0."))
    @MaxConstraint(max = 0, message = @tr("''%paramName%'' is a percent, and must be lesser or equal to 100."))
    private final Integer percentMajor;

    @RequestParam(role = Role.POST, suggestedValue = "true")
    private final Boolean isFinished;

    private final OfferActionUrl url;

    public OfferAction(final OfferActionUrl url) {
        super(url, UserTeamRight.TALK);
        this.url = url;
        this.description = url.getDescription();
        this.license = url.getLicense();
        this.expiryDate = url.getExpiryDate();
        this.price = url.getPrice();
        this.feature = url.getFeature();
        this.draftOffer = url.getDraftOffer();
        this.daysBeforeValidation = url.getDaysBeforeValidation();
        this.percentFatal = url.getPercentFatal();
        this.percentMajor = url.getPercentMajor();
        this.isFinished = url.getIsFinished();
    }

    @Override
    public Url doDoProcessRestricted(final Member me, final Team asTeam) {

        Offer constructingOffer;
        try {
            Milestone constructingMilestone;
            if (draftOffer == null) {
                constructingOffer = feature.addOffer(price, description, license, getLocale(), expiryDate.getJavaDate(), daysBeforeValidation
                        * DateUtils.SECOND_PER_DAY);
                constructingMilestone = constructingOffer.getMilestones().iterator().next();
            } else {
                constructingOffer = draftOffer;
                constructingMilestone = draftOffer.addMilestone(price, description, getLocale(), expiryDate.getJavaDate(), daysBeforeValidation
                        * DateUtils.SECOND_PER_DAY);
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
            Context.getSession().notifyError(Context.tr("Error creating an offer. Please notify us."));
            throw new ShallNotPassException("Error creating an offer", e);
        }

        final MakeOfferPageUrl returnUrl = new MakeOfferPageUrl(feature);
        returnUrl.setOffer(constructingOffer);
        return returnUrl;
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {

        boolean everythingIsRight = true;

        if ((percentFatal != null && percentMajor == null) || (percentFatal == null && percentMajor != null)) {
            session.notifyBad(Context.tr("You have to specify both the Major and Fatal percent."));
            url.getPercentMajorParameter().addErrorMessage(Context.tr("You have to specify both the Major and Fatal percent."));
            url.getPercentFatalParameter().addErrorMessage(Context.tr("You have to specify both the Major and Fatal percent."));
            everythingIsRight = false;
        }
        if (percentFatal != null && percentFatal + percentMajor > 100) {
            session.notifyBad(Context.tr("Major + Fatal percent cannot be > 100 !!"));
            url.getPercentMajorParameter().addErrorMessage(Context.tr("Major + Fatal percent cannot be > 100 !!"));
            url.getPercentFatalParameter().addErrorMessage(Context.tr("Major + Fatal percent cannot be > 100 !!"));
            everythingIsRight = false;
        }
        if (draftOffer != null && !draftOffer.isDraft()) {
            session.notifyBad(Context.tr("The specified offer is not modifiable. You cannot add a lot in it."));
            everythingIsRight = false;
        }
        if (!expiryDate.isFuture()) {
            session.notifyBad(Context.tr("The date must be in the future."));
            url.getExpiryDateParameter().addErrorMessage(Context.tr("The date must be in the future."));
            everythingIsRight = false;
        }

        if (!everythingIsRight) {
            return new MakeOfferPageUrl(feature);
        }

        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors(final ElveosUserToken userToken) {
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
    protected void doTransmitParameters() {
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getExpiryDateParameter());
        session.addParameter(url.getPriceParameter());
        session.addParameter(url.getDaysBeforeValidationParameter());
        session.addParameter(url.getPercentFatalParameter());
        session.addParameter(url.getPercentMajorParameter());
        session.addParameter(url.getIsFinishedParameter());
        session.addParameter(url.getLicenseParameter());
    }

    @Override
    protected boolean verifyFile(final String filename) {
        return true;
    }
}
