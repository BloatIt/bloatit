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
package com.bloatit.web.linkable.features.create;

import java.math.BigDecimal;
import java.util.Locale;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.data.exceptions.UniqueNameExpectedException;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.utils.FileConstraintChecker;
import com.bloatit.framework.utils.FileConstraintChecker.SizeUnit;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.utils.i18n.DateLocale;
import com.bloatit.framework.utils.i18n.Language;
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
import com.bloatit.model.Feature;
import com.bloatit.model.FeatureFactory;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.Offer;
import com.bloatit.model.Software;
import com.bloatit.model.Team;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.linkable.usercontent.UserContentAction;
import com.bloatit.web.url.CreateFeatureAndOfferActionUrl;
import com.bloatit.web.url.CreateFeatureAndOfferPageUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.MakeOfferPageUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("feature/%process%/docreatewithoffer")
public final class CreateFeatureAndOfferAction extends UserContentAction {
    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You forgot to write a title"))
    @MinConstraint(min = 10, message = @tr("The title must have at least %constraint% chars."))
    @MaxConstraint(max = 80, message = @tr("The title must be %constraint% chars length max."))
    private final String description;

    @NonOptional(@tr("You forgot to write a specification"))
    @MinConstraint(min = 10, message = @tr("The specification must have at least %constraint% chars."))
    @MaxConstraint(max = 800000, message = @tr("The specification must be %constraint% chars length max."))
    @RequestParam(role = Role.POST)
    private final String specification;

    @RequestParam(role = Role.POST, message = @tr("Invalid value for price field."))
    @NonOptional(@tr("You must set a price to your offer."))
    @MinConstraint(min = 1, message = @tr("The price must be greater to %constraint%."))
    private final BigDecimal price;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You must set an expiration date."))
    private final DateLocale expiryDate;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You must add a license to your offer."))
    private final String license;

    @RequestParam(role = Role.POST, suggestedValue = "7")
    @NonOptional(@tr("You must set a days count for validation."))
    @MinConstraint(min = 1, message = @tr("The validation time must be greater to %constraint%."))
    private final Integer daysBeforeValidation;

    @Optional
    @RequestParam(role = Role.POST, suggestedValue = "100")
    @MinConstraint(min = 0, message = @tr("''%paramName%'' is a percent, and must be greater or equal to %constraint%."))
    @MaxConstraint(max = 100, message = @tr("''%paramName%'' is a percent, and must be lesser or equal to %constraint%."))
    private final Integer percentFatal;

    @RequestParam(role = Role.POST, suggestedValue = "0")
    @Optional
    @MinConstraint(min = 0, message = @tr("''%paramName%'' is a percent, and must be greater or equal to %constraint%."))
    @MaxConstraint(max = 100, message = @tr("''%paramName%'' is a percent, and must be lesser or equal to %constraint%."))
    private final Integer percentMajor;

    @RequestParam(role = Role.POST, suggestedValue = "true")
    private final Boolean isFinished;
    
    
    @Optional
    @RequestParam(role = Role.POST)
    private final Software software;

    @Optional
    @RequestParam(role = Role.POST)
    private final String newSoftwareName;
    
    @NonOptional(@tr("The process is closed, expired, missing or invalid."))
    @RequestParam(role = Role.PAGENAME)
    private final CreateFeatureProcess process;
    
    private final CreateFeatureAndOfferActionUrl url;

    public CreateFeatureAndOfferAction(final CreateFeatureAndOfferActionUrl url) {
        super(url, UserTeamRight.TALK);
        this.url = url;

        this.description = url.getDescription();
        this.specification = url.getSpecification();
        this.software = url.getSoftware();
        this.newSoftwareName = url.getNewSoftwareName();
        this.process = url.getProcess();
        this.license = url.getLicense();
        this.expiryDate = url.getExpiryDate();
        this.price = url.getPrice();
        this.daysBeforeValidation = url.getDaysBeforeValidation();
        this.percentFatal = url.getPercentFatal();
        this.percentMajor = url.getPercentMajor();
        this.isFinished = url.getIsFinished();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        if (getLocale() == null) {
            session.notifyError(Context.tr("You have to specify a valid language."));
            if(process == null) {
                return new IndexPageUrl();
            } else {
                return new CreateFeaturePageUrl(process);
            }
            
        }
        
        if(software == null && newSoftwareName != null &&  newSoftwareName.equals("--invalid--")) {
            session.notifyError(Context.tr("You have to specify a valid software."));
            if (process == null) {
                return new IndexPageUrl();
            } else {
                return new CreateFeaturePageUrl(process);
            }
        }
        return NO_ERROR;
    }

    @Override
    public Url doDoProcessRestricted(final Member me, final Team asTeam) {
        
        Software softwareToUse = software;

        if (software == null && newSoftwareName != null && !newSoftwareName.isEmpty()) {
            try {
                softwareToUse = new Software(newSoftwareName, me, Locale.ENGLISH, "No description yet.");
            } catch (UniqueNameExpectedException e) {
                softwareToUse = SoftwareManager.getByName(newSoftwareName);
            }

        }
        // Create feature
        final Feature feature = FeatureFactory.createFeature(me, asTeam, Language.fromLocale(getLocale()), description, specification, softwareToUse);
        propagateAttachedFileIfPossible(feature);
        
        // Create offer
        Offer constructingOffer;
        try {
            Milestone constructingMilestone;
            constructingOffer = feature.addOffer(price, specification, license, Language.fromLocale(getLocale()), expiryDate.getJavaDate(), daysBeforeValidation
                    * DateUtils.SECOND_PER_DAY);
            constructingMilestone = constructingOffer.getMilestones().iterator().next();
            
            if (percentFatal != null && percentMajor != null) {
                constructingMilestone.updateMajorFatalPercent(percentFatal, percentMajor);
            }
            if (isFinished) {
                constructingOffer.setDraftFinished();

                final FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature, FeatureTabKey.offers);
                return featurePageUrl;
            }

        } catch (final UnauthorizedOperationException e) {
            Context.getSession().notifyError(Context.tr("Error creating an offer. Please notify us."));
            throw new ShallNotPassException("Error creating an offer", e);
        }
        
        if (isFinished) {
            process.close();
            return new FeaturePageUrl(feature, FeatureTabKey.description);    
        }
        
        final MakeOfferPageUrl returnUrl = new MakeOfferPageUrl(feature);
        returnUrl.setOffer(constructingOffer);
        process.close();
        return returnUrl;
        
        
    }

    @Override
    protected Url doProcessErrors() {
        if(process == null) {
            return new IndexPageUrl();
        } else {
            return new CreateFeatureAndOfferPageUrl(process);
        }
    }

    @Override
    protected String getRefusalReason() {
        return "You have to be logged to create a new feature request.";
    }

    @Override
    protected void doTransmitParameters() {
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getSpecificationParameter());
        session.addParameter(url.getSoftwareParameter());
        session.addParameter(url.getExpiryDateParameter());
        session.addParameter(url.getPriceParameter());
        session.addParameter(url.getDaysBeforeValidationParameter());
        session.addParameter(url.getPercentFatalParameter());
        session.addParameter(url.getPercentMajorParameter());
        session.addParameter(url.getIsFinishedParameter());
        session.addParameter(url.getLicenseParameter());
        session.addParameter(url.getNewSoftwareNameParameter());
    }

    @Override
    protected boolean verifyFile(final String filename) {
        return new FileConstraintChecker(filename).isFileSmaller(CreateFeaturePage.FILE_MAX_SIZE_MIO, SizeUnit.MBYTE);
    }
}
