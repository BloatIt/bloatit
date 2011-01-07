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
package com.bloatit.web.html.pages;

import java.math.BigDecimal;

import com.bloatit.framework.Demand;
import com.bloatit.web.actions.OfferAction;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.HtmlDateField;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextArea;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.i18n.DateLocale;
import com.bloatit.web.utils.url.OfferActionUrl;
import com.bloatit.web.utils.url.OfferPageUrl;

@ParamContainer("offer")
public class OfferPage extends LoggedPage {

    @RequestParam(level=Level.ERROR)
    private Demand targetIdea = null;

    @RequestParam(name = OfferAction.PRICE_CODE, level = Level.INFO, role = Role.SESSION)
    private final BigDecimal price;

    @RequestParam(name = OfferAction.EXPIRY_CODE, level = Level.INFO, role = Role.SESSION)
    private final DateLocale expiryDate;

    @RequestParam(name = OfferAction.TITLE_CODE, level = Level.INFO, role = Role.SESSION)
    private final String title;
    
    @RequestParam(name = OfferAction.DESCRIPTION_CODE, level = Level.INFO, role = Role.SESSION)
    private final String description;

    public OfferPage(final OfferPageUrl url) throws RedirectException {
        super(url);
        this.targetIdea = url.getTargetIdea();
        this.price = url.getPrice();
        this.expiryDate = url.getExpiryDate();
        this.title = url.getTitle();
        this.description = url.getDescription();
    }

    @Override
    protected String getTitle() {
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
        final HtmlTitleBlock offerPageContainer = new HtmlTitleBlock(Context.tr("Make an offer"), 2);

        // Create offer form
        final OfferActionUrl offerActionUrl = new OfferActionUrl(targetIdea);
        final HtmlForm offerForm = new HtmlForm(offerActionUrl.urlString());

        // Idea title
        final HtmlText t = new HtmlText(this.targetIdea.getTitle());
        offerPageContainer.add(t);

        // Title field
        final HtmlTextField titleField = new HtmlTextField(OfferAction.TITLE_CODE, Context.tr("Title to the offer"));
        titleField.setDefaultValue(title);
        offerForm.add(titleField);

        // Price field
        final HtmlTextField priceField = new HtmlTextField(OfferAction.PRICE_CODE, Context.tr("Offer price"));
        if(price!=null) priceField.setDefaultValue(price.toPlainString());
        offerForm.add(priceField);

        // Date field
        final HtmlDateField dateField = new HtmlDateField(OfferAction.EXPIRY_CODE, Context.tr("Expiration date"));
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
