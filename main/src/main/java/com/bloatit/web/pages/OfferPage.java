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
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Demand;
import com.bloatit.model.Group;
import com.bloatit.model.Member;
import com.bloatit.model.Offer;
import com.bloatit.model.right.Action;
import com.bloatit.web.components.HtmlDemandSumary;
import com.bloatit.web.components.HtmlDemandSumary.Compacity;
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
        final HtmlTitleBlock offerPageContainer = new HtmlTitleBlock(Context.tr("Make an offer"), 1);

        offerPageContainer.add(new HtmlDemandSumary(demand, Compacity.NORMAL));

        // Create offer form
        final OfferActionUrl offerActionUrl = new OfferActionUrl(demand);
        final HtmlForm offerForm = new HtmlForm(offerActionUrl.urlString());
        offerForm.setCssClass("padding_box");

        // Offering on the behalf of
        final Member me = session.getAuthToken().getMember();
        if (me.canAccessGroups(Action.READ)) {
            try {
                final PageIterable<Group> groups = me.getGroups();
                final FieldData groupField = offerActionUrl.getGroupParameter().fieldData();
                final HtmlDropDown groupDropDown = new HtmlDropDown(groupField, Context.tr("On the behalf of"));
                groupDropDown.setComment("If you make an offer on the behalf of a team, this teamwill get the money instead of you");
                groupDropDown.addDropDownElement("-1", Context.tr("Myself"));
                for (final Group group : groups) {
                    groupDropDown.addDropDownElement(group.getId().toString(), group.getLogin());
                }
                offerForm.add(groupDropDown);
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
        final HtmlDateField dateField = new HtmlDateField(dateFieldData, Context.tr("Expiration date"));
        dateField.setComment(Context.tr("Date formatted in ISO format. Example: 2012/03/15 for March 15, 2012."));
        offerForm.add(dateField);

        final FieldData descriptionFieldData = offerActionUrl.getExpiryDateParameter().fieldData();
        final HtmlTextArea descriptionField = new HtmlTextArea(descriptionFieldData, Context.tr("Description of the offer"), 10, 80);
        offerForm.add(descriptionField);

        final HtmlSubmit offerButton = new HtmlSubmit(Context.tr("Make an offer"));
        offerForm.add(offerButton);

        offerPageContainer.add(offerForm);
        return offerPageContainer;
    }
}
