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

import com.bloatit.common.Log;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FieldData;
import com.bloatit.framework.webserver.components.form.HtmlDateField;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlMoneyField;
import com.bloatit.framework.webserver.components.form.HtmlRadioButtonGroup;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.model.Offer;
import com.bloatit.model.Team;
import com.bloatit.model.right.Action;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeatureOfferListComponent;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.pages.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.url.MakeOfferPageUrl;
import com.bloatit.web.url.OfferActionUrl;

@ParamContainer("offer/create")
public final class MakeOfferPage extends LoggedPage {

    @RequestParam
    @ParamConstraint(optionalErrorMsg = @tr("The feature id is not optional !"))
    private final Feature feature;

    @RequestParam
    @Optional
    private final Offer offer;

    public MakeOfferPage(final MakeOfferPageUrl url) {
        super(url);
        this.feature = url.getFeature();
        this.offer = url.getOffer();
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Make an offer");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to make an offer");
    }


    @Override
    public void processErrors() throws RedirectException {
    }

    @Override
    public HtmlElement createRestrictedContent() {

        final TwoColumnLayout layout = new TwoColumnLayout(true);
        layout.addLeft(generateOfferForm());

        layout.addRight(new SideBarFeatureBlock(feature));
        layout.addRight(new SideBarDocumentationBlock("markdown"));

        return layout;
    }

    public HtmlTitleBlock generateOfferForm() {
        final HtmlTitleBlock offerPageContainer = new HtmlTitleBlock(Context.tr("Make an offer"), 1);

        try {
            if (offer != null) {
                offerPageContainer.add(new FeatureOfferListComponent.OfferBlock(offer, true));
            }
        } catch (final UnauthorizedOperationException e1) {
            offerPageContainer.addText(Context.tr("For an unknown raison you have not the right to see the Offer you are constructing..."));
        }

        // Create offer form
        final OfferActionUrl offerActionUrl = new OfferActionUrl(feature);
        offerActionUrl.setDraftOffer(offer);
        final HtmlForm offerForm = new HtmlForm(offerActionUrl.urlString());

        // Offering on the behalf of
        final Member me = session.getAuthToken().getMember();
        if (me.canAccessTeams(Action.READ)) {
            try {
                final PageIterable<Team> teams = me.getTeams();
                final FieldData teamData = offerActionUrl.getTeamParameter().pickFieldData();
                final HtmlDropDown teamInput = new HtmlDropDown(teamData.getName(), Context.tr("On the behalf of"));
                teamInput.setDefaultValue(teamData.getSuggestedValue());
                teamInput.addErrorMessages(teamData.getErrorMessages());
                teamInput.setComment("If you make an offer on the behalf of a team, this teamwill get the money instead of you");
                teamInput.addDropDownElement("", Context.tr("Myself"));
                int nbTeam = 0;
                for (final Team team : teams) {
                    if (team.getUserTeamRight(me).contains(UserTeamRight.TALK)) {
                        teamInput.addDropDownElement(team.getId().toString(), team.getLogin());
                        nbTeam++;
                    }
                }
                if (nbTeam > 0) {
                    offerForm.add(teamInput);
                }
            } catch (final UnauthorizedOperationException e) {
                // Shouldn't happen
                Log.web().error("Can't access current user teams (I check before tho)", e);
            }
        }

        // Price field
        final FieldData priceData = offerActionUrl.getPriceParameter().pickFieldData();
        final HtmlMoneyField priceInput = new HtmlMoneyField(priceData.getName(), Context.tr("Offer price"));
        priceInput.setDefaultValue(priceData.getSuggestedValue());
        priceInput.addErrorMessages(priceData.getErrorMessages());
        priceInput.setComment(Context.tr("The price must be in euros (€) and can't contains cents."));
        offerForm.add(priceInput);

        // Date field
        final FieldData dateData = offerActionUrl.getExpiryDateParameter().pickFieldData();
        final HtmlDateField dateInput = new HtmlDateField(dateData.getName(), Context.tr("Release date"), Context.getLocalizator().getLocale());
        dateInput.setDefaultValue(dateData.getSuggestedValue());
        dateInput.addErrorMessages(dateData.getErrorMessages());
        dateInput.setComment(Context.tr("You will have to release this feature before the release date."));
        offerForm.add(dateInput);

        // Description
        final FieldData descriptionData = offerActionUrl.getDescriptionParameter().pickFieldData();
        final HtmlTextArea descriptionInput = new HtmlTextArea(descriptionData.getName(), Context.tr("Description"), 10, 80);
        descriptionInput.setDefaultValue(descriptionData.getSuggestedValue());
        descriptionInput.addErrorMessages(descriptionData.getErrorMessages());
        offerForm.add(descriptionInput);

        // locale
        final FieldData localeData = offerActionUrl.getLocaleParameter().pickFieldData();
        final LanguageSelector localeInput = new LanguageSelector(localeData.getName(), Context.tr("description langue"));
        localeInput.setDefaultValue(localeData.getSuggestedValue());
        localeInput.addErrorMessages(localeData.getErrorMessages());
        localeInput.setComment(Context.tr("The language in which you have maid the description."));
        offerForm.add(localeInput);

        // days before validation
        final FieldData nbDaysData = offerActionUrl.getDaysBeforeValidationParameter().pickFieldData();
        final HtmlTextField nbDaysInput = new HtmlTextField(nbDaysData.getName(), Context.tr("Days before validation"));
        nbDaysInput.setDefaultValue(nbDaysData.getSuggestedValue());
        nbDaysInput.addErrorMessages(nbDaysData.getErrorMessages());
        nbDaysInput.setComment(Context.tr("The number of days to wait before this offer is can be validated. "
                + "During this time users can add bugs un the bug tracker. Fatal bugs have to be colsed before the validation."));
        offerForm.add(nbDaysInput);

        // percent Fatal
        final FieldData percentFatalData = offerActionUrl.getPercentFatalParameter().pickFieldData();
        final HtmlTextField percentFatalInput = new HtmlTextField(percentFatalData.getName(), Context.tr("Percent gained when no FATAL bugs"));
        percentFatalInput.setDefaultValue(percentFatalData.getSuggestedValue());
        percentFatalInput.addErrorMessages(percentFatalData.getErrorMessages());
        percentFatalInput.setComment(Context.tr("If you want to add some warenty to the contributor you can say that you want to gain less than 100% "
                + "of the amount on this feature request when all the FATAL bugs are closed. "
                + "The money left will be transfered when all the MAJOR bugs are closed. If you specify this field, you have to specify the next one on MAJOR bug percent. "
                + "By default, all the money on this feature request is transfered when all the FATAL bugs are closed."));
        offerForm.add(percentFatalInput);

        // percent Major
        final FieldData percentMajorData = offerActionUrl.getPercentMajorParameter().pickFieldData();
        final HtmlTextField percentMajorInput = new HtmlTextField(percentMajorData.getName(), Context.tr("Percent gained when no MAJOR bugs"));
        percentMajorInput.setDefaultValue(percentMajorData.getSuggestedValue());
        percentMajorInput.addErrorMessages(percentMajorData.getErrorMessages());
        percentMajorInput.setComment(Context.tr("If you specified a value for the 'FATAL bugs percent', you have to also specify one for the MAJOR bugs. "
                + "You can say that you want to gain less than 100% of the amount on this offer when all the MAJOR bugs are closed. "
                + "The money left will be transfered when all the MINOR bugs are closed. Make sure that (FATAL percent + MAJOR percent) <= 100."));
        offerForm.add(percentMajorInput);

        // Is finished
        final FieldData isFinishedData = offerActionUrl.getIsFinishedParameter().pickFieldData();
        final HtmlRadioButtonGroup isFinishedInput = new HtmlRadioButtonGroup(isFinishedData.getName());
        isFinishedInput.setDefaultValue(isFinishedData.getSuggestedValue());
        // isFinishedInput.addErrorMessages(isFinishedData.getErrorMessages());
        isFinishedInput.addRadioButton("true", Context.tr("Finish your Offer"));
        isFinishedInput.addRadioButton("false", Context.tr("Add an other lot"));
        offerForm.add(isFinishedInput);

        final HtmlSubmit offerButton = new HtmlSubmit(Context.tr("Validate !"));
        offerForm.add(offerButton);

        offerPageContainer.add(offerForm);
        return offerPageContainer;
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return MakeOfferPage.generateBreadcrumb(feature);
    }

    public static Breadcrumb generateBreadcrumb(Feature feature) {
        Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbContributions(feature);

        breadcrumb.pushLink(new MakeOfferPageUrl(feature).getHtmlLink(tr("Make an offer")));

        return breadcrumb;
    }

}
