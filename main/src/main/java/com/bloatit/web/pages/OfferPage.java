/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.pages;

import java.math.BigDecimal;

import com.bloatit.framework.utils.i18n.DateLocale;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FormFieldData;
import com.bloatit.framework.webserver.components.form.HtmlDateField;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlMoneyField;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Demand;
import com.bloatit.web.actions.OfferAction;
import com.bloatit.web.components.HtmlDemandSumary;
import com.bloatit.web.url.OfferActionUrl;
import com.bloatit.web.url.OfferPageUrl;

@ParamContainer("offer")
public final class OfferPage extends LoggedPage {

    @RequestParam(level = Level.ERROR)
    private Demand targetIdea = null;

    @RequestParam(name = OfferAction.PRICE_CODE, level = Level.INFO, role = Role.SESSION)
    private final BigDecimal price;

    @RequestParam(name = OfferAction.EXPIRY_CODE, level = Level.INFO, role = Role.SESSION)
    private final DateLocale expiryDate;

    @RequestParam(name = OfferAction.TITLE_CODE, level = Level.INFO, role = Role.SESSION)
    private final String title;

    @RequestParam(name = OfferAction.DESCRIPTION_CODE, level = Level.INFO, role = Role.SESSION)
    private final String description;

    public OfferPage(final OfferPageUrl url) {
        super(url);
        this.targetIdea = url.getTargetIdea();
        this.price = url.getPrice();
        this.expiryDate = url.getExpiryDate();
        this.title = url.getTitle();
        this.description = url.getDescription();
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

        offerPageContainer.add(new HtmlDemandSumary(targetIdea, false));

        // Create offer form
        final OfferActionUrl offerActionUrl = new OfferActionUrl(targetIdea);
        final HtmlForm offerForm = new HtmlForm(offerActionUrl.urlString());
        offerForm.setCssClass("padding_box");

        // Title field
        FormFieldData<String> titleFieldData = offerActionUrl.getTitleParameter().createFormFieldData();
        final HtmlTextField titleField = new HtmlTextField(titleFieldData, Context.tr("Title to the offer"));
        offerForm.add(titleField);

        // Price field
        FormFieldData<BigDecimal> priceFieldData = offerActionUrl.getPriceParameter().createFormFieldData();
        final HtmlMoneyField priceField = new HtmlMoneyField(priceFieldData, Context.tr("Offer price"));
        priceField.setComment(Context.tr("The price must be in euros (€) and can't contains cents."));
        if (price != null) {
            priceField.setDefaultValue(price);
        }
        offerForm.add(priceField);

        // Date field
        final HtmlDateField dateField = new HtmlDateField(OfferAction.EXPIRY_CODE, Context.tr("Expiration date"));
        dateField.setComment(Context.tr("Date formatted in ISO format. Example: 2012/03/15 for March 15, 2012."));
        dateField.setDefaultValue(expiryDate);
        offerForm.add(dateField);

        final HtmlTextArea descriptionField = new HtmlTextArea(OfferAction.DESCRIPTION_CODE, Context.tr("Description of the offer"), 10, 80);
        descriptionField.setDefaultValue(description);
        offerForm.add(descriptionField);

        final HtmlSubmit offerButton = new HtmlSubmit(Context.tr("Make an offer"));
        offerForm.add(offerButton);

        offerPageContainer.add(offerForm);
        return offerPageContainer;
    }
}
