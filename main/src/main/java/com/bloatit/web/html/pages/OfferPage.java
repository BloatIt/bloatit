/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.html.pages;

import java.math.BigDecimal;
import java.util.Date;


import com.bloatit.framework.Demand;
import com.bloatit.web.actions.OfferAction;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.HtmlButton;
import com.bloatit.web.html.components.standard.form.HtmlDateField;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlTextArea;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.utils.BloatitDate;
import com.bloatit.web.utils.annotations.RequestParam;
import com.bloatit.web.utils.url.Request;
import com.bloatit.web.utils.url.UrlBuilder;

public class OfferPage extends LoggedPage {

    @RequestParam(name = "idea")
    private final Demand targetIdea = null;

    @RequestParam(name = "price", defaultValue="vide")
    private BigDecimal price;

    @RequestParam(name = "expiry", defaultValue="vide")
    private Date expiryDate;

    @RequestParam(name = "title", defaultValue="vide")
    private String title;

    @RequestParam(name = "description", defaultValue="vide")
    private String description;

    public OfferPage(final Request request) throws RedirectException {
        super(request);
        this.request.setValues(this);
        addNotifications(request.getMessages());
    }

    @Override
    protected String getTitle() {
        return session.tr("Make an offer");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public String getRefusalReason() {
        return session.tr("You must be logged to make an offer");
    }

    @Override
    public HtmlElement generateRestrictedContent() {

        // TODO : remove and replace with parameter loading machanism
        /*
         * int ideaId = -1;
         * if(this.parameters.containsKey("idea")){
         * try{
         * ideaId = Integer.parseInt(this.parameters.get("idea"));
         * targetIdea = DemandManager.getDemandById(ideaId);
         * } catch (NumberFormatException nfe){
         * }
         * }
         * 
         * if (ideaId == -1 ){
         * session.notifyBad(session.tr(
         * "You need to choose an idea on which you'll contribute"));
         * htmlResult.setRedirect(new DemandsPage(session));
         * return null;
         * }
         * 
         * if (targetIdea == null){
         * session.notifyBad(session.tr("The idea you chose does not exists (id :"
         * +ideaId+")"));
         * htmlResult.setRedirect(new DemandsPage(session));
         * return null;
         * }
         */
        // !TODO

        final HtmlTitleBlock offerPageContainer = new HtmlTitleBlock(this.session.tr("Make an offer"),2);

        // Create offer form
        final UrlBuilder offerActionUrlBuilder = new UrlBuilder(OfferAction.class);
        offerActionUrlBuilder.addParameter("idea", targetIdea);
        final HtmlForm offerForm = new HtmlForm(offerActionUrlBuilder.buildUrl());

        // Idea title
        final HtmlText t = new HtmlText(this.targetIdea.getTitle());
        offerPageContainer.add(t);

        // Title field
        final HtmlTextField titleField = new HtmlTextField(this.title); // TODO
        titleField.setLabel(this.session.tr("Add a title to the offer : "));
        titleField.setName(OfferAction.TITLE_CODE);
        offerForm.add(titleField);

        // Price field
        final HtmlTextField priceField = new HtmlTextField(price == null ? "" : price.toPlainString()); // TODO
        priceField.setLabel(this.session.tr("Offer price : "));
        priceField.setName(OfferAction.PRICE_CODE);
        offerForm.add(priceField);

        // Date field
        final BloatitDate bd = new BloatitDate(this.expiryDate, session.getLanguage().getLocale());
        // TODO : create a constructor with the language
        final HtmlDateField dateField = new HtmlDateField(OfferAction.EXPIRY_CODE);
        dateField.setDefaultValue(bd);
        dateField.setLabel(this.session.tr("Expiration date : "));
        offerForm.add(dateField);

        final HtmlTextArea descriptionField = new HtmlTextArea(this.description, 10, 20); // TODO
        descriptionField.setLabel(this.session.tr("Enter the description of the offer : "));
        descriptionField.setName(OfferAction.DESCRIPTION_CODE);
        offerForm.add(descriptionField);

        final HtmlButton offerButton = new HtmlButton(session.tr("Make an offer"));
        offerForm.add(offerButton);

        offerPageContainer.add(offerForm);
        return offerPageContainer;
    }

}
