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

import com.bloatit.common.Log;
import com.bloatit.data.DaoGroupRight.UserGroupRight;
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
import com.bloatit.model.Demand;
import com.bloatit.model.Group;
import com.bloatit.model.Member;
import com.bloatit.model.Offer;
import com.bloatit.model.right.Action;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.components.SideBarDemandBlock;
import com.bloatit.web.linkable.demands.DemandOfferListComponent;
import com.bloatit.web.pages.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.url.OfferActionUrl;
import com.bloatit.web.url.OfferPageUrl;

@ParamContainer("offer")
public final class OfferPage extends LoggedPage {

    @RequestParam
    @ParamConstraint(optionalErrorMsg = @tr("The demand id is not optional !"))
    private final Demand demand;

    @RequestParam
    @Optional
    private final Offer offer;

    public OfferPage(final OfferPageUrl url) {
        super(url);
        this.demand = url.getDemand();
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
    public HtmlElement createRestrictedContent() {

        TwoColumnLayout layout = new TwoColumnLayout(true);
        layout.addLeft(generateOfferForm());

        layout.addRight(new SideBarDemandBlock(demand));
        layout.addRight(new SideBarDocumentationBlock("markdown"));

        return layout;
    }

    public HtmlTitleBlock generateOfferForm() {
        final HtmlTitleBlock offerPageContainer = new HtmlTitleBlock(Context.tr("Make an offer"), 1);

        try {
            if (offer != null) {
                offerPageContainer.add(new DemandOfferListComponent.OfferBlock(offer, true));
            }
        } catch (UnauthorizedOperationException e1) {
            offerPageContainer.addText(Context.tr("For an unknown raison you have not the right to see the Offer you are constructing..."));
        }

        // Create offer form
        final OfferActionUrl offerActionUrl = new OfferActionUrl(demand);
        offerActionUrl.setDraftOffer(offer);
        final HtmlForm offerForm = new HtmlForm(offerActionUrl.urlString());

        // Offering on the behalf of
        final Member me = session.getAuthToken().getMember();
        if (me.canAccessGroups(Action.READ)) {
            try {
                final PageIterable<Group> groups = me.getGroups();
                final FieldData groupField = offerActionUrl.getGroupParameter().fieldData();
                final HtmlDropDown groupDropDown = new HtmlDropDown(groupField, Context.tr("On the behalf of"));
                groupDropDown.setComment("If you make an offer on the behalf of a team, this teamwill get the money instead of you");
                groupDropDown.addDropDownElement("", Context.tr("Myself"));
                int nbGroup = 0;
                for (final Group group : groups) {
                    if (group.getUserGroupRight(me).contains(UserGroupRight.TALK)) {
                        groupDropDown.addDropDownElement(group.getId().toString(), group.getLogin());
                        nbGroup++;
                    }
                }
                if (nbGroup > 0) {
                    offerForm.add(groupDropDown);
                }
            } catch (final UnauthorizedOperationException e) {
                // Shouldn't happen
                Log.web().error("Can't access current user groups (I check before tho)", e);
            }
        }

        // Price field
        final FieldData priceFieldData = offerActionUrl.getPriceParameter().fieldData();
        final HtmlMoneyField priceField = new HtmlMoneyField(priceFieldData, Context.tr("Offer price"));
        priceField.setComment(Context.tr("The price must be in euros (€) and can't contains cents."));
        offerForm.add(priceField);

        // Date field
        final FieldData dateFieldData = offerActionUrl.getExpiryDateParameter().fieldData();
        final HtmlDateField dateField = new HtmlDateField(dateFieldData, Context.tr("Release date"));
        dateField.setComment(Context.tr("You will have to release this feature before the release date."));
        offerForm.add(dateField);

        // Description
        final FieldData descriptionFieldData = offerActionUrl.getDescriptionParameter().fieldData();
        final HtmlTextArea descriptionField = new HtmlTextArea(descriptionFieldData, Context.tr("Description"), 10, 80);
        offerForm.add(descriptionField);

        // locale
        final FieldData localeFieldData = offerActionUrl.getLocaleParameter().fieldData();
        final LanguageSelector localeField = new LanguageSelector(localeFieldData, Context.tr("description langue"));
        localeField.setComment(Context.tr("The language in which you have maid the description."));
        offerForm.add(localeField);

        // days before validation
        final FieldData nbDaysFiledData = offerActionUrl.getDaysBeforeValidationParameter().fieldData();
        final HtmlTextField nbDaysField = new HtmlTextField(nbDaysFiledData, Context.tr("Days before validation"));
        nbDaysField.setComment(Context.tr("The number of days to wait before this offer is can be validated. "
                + "During this time users can add bugs un the bug tracker. Fatal bugs have to be colsed before the validation."));
        offerForm.add(nbDaysField);

        // percent Fatal
        final FieldData percentFatalFiledData = offerActionUrl.getPercentFatalParameter().fieldData();
        final HtmlTextField percentFatalField = new HtmlTextField(percentFatalFiledData, Context.tr("Percent gained when no FATAL bugs"));
        percentFatalField.setComment(Context.tr("If you want to add some warenty to the contributor you can say that you want to gain less than 100% "
                + "of the amount on this feature request when all the FATAL bugs are closed. "
                + "The money left will be transfered when all the MAJOR bugs are closed. If you specify this field, you have to specify the next one on MAJOR bug percent. "
                + "By default, all the money on this feature request is transfered when all the FATAL bugs are closed."));
        offerForm.add(percentFatalField);

        // percent Major
        final FieldData percentMajorFiledData = offerActionUrl.getPercentMajorParameter().fieldData();
        final HtmlTextField percentMajorField = new HtmlTextField(percentMajorFiledData, Context.tr("Percent gained when no MAJOR bugs"));
        percentMajorField.setComment(Context.tr("If you specified a value for the 'FATAL bugs percent', you have to also specify one for the MAJOR bugs. "
                + "You can say that you want to gain less than 100% of the amount on this offer when all the MAJOR bugs are closed. "
                + "The money left will be transfered when all the MINOR bugs are closed. Make sure that (FATAL percent + MAJOR percent) <= 100."));
        offerForm.add(percentMajorField);

        // Is finished
        final FieldData isFinishedFiledData = offerActionUrl.getIsFinishedParameter().fieldData();
        final HtmlRadioButtonGroup isFinishedField = new HtmlRadioButtonGroup(isFinishedFiledData);
        isFinishedField.addRadioButton("true", Context.tr("Finish your Offer"));
        isFinishedField.addRadioButton("false", Context.tr("Add an other lot"));
        offerForm.add(isFinishedField);

        final HtmlSubmit offerButton = new HtmlSubmit(Context.tr("Make an offer"));
        offerForm.add(offerButton);

        offerPageContainer.add(offerForm);
        return offerPageContainer;
    }
}
