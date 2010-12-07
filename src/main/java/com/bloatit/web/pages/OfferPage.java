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

package com.bloatit.web.pages;

import com.bloatit.framework.Demand;
import com.bloatit.web.actions.OfferAction;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlButton;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlDateField;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlForm;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlText;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTextArea;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTextField;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTitle;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.RequestParam;
import com.bloatit.web.utils.TestQueryAnnotation.DemandLoader;
import com.bloatit.web.utils.tr;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class OfferPage extends LoggedPage {

    /**
     * The idea on which the author wants to create a new offer
     */
    @RequestParam(name = OfferAction.IDEA_CODE, loader = DemandLoader.class, message = @tr("Invalid idea"))
    private Demand targetIdea = null;

    /**
     * The desired price for the offer
     */
    @RequestParam(name = OfferAction.PRICE_CODE)
    private BigDecimal price;

    /**
     * The expiration date for the offer
     */
    @RequestParam(name = OfferAction.EXPIRY_CODE)
    private Date expiryDate;

    /**
     * The title of the offer
     */
    @RequestParam(name = OfferAction.TITLE_CODE)
    private String title;

    /**
     * The short description of the offer
     */
    @RequestParam(name = OfferAction.DESCRIPTION_CODE)
    private String description;

    public OfferPage(Session session) {
        this(session, new HashMap<String, String>());
    }

    public OfferPage(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    @Override
    public String getCode() {
        return "offer";
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
    public HtmlComponent generateRestrictedContent() {

        if (getParameters().getMessages().size() > 0) {
            session.notifyList(getParameters().getMessages());
            // return null;
        }

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

        HtmlTitle offerPageContainer = new HtmlTitle(this.session.tr("Make an offer"), "");
        final OfferAction offerAction = new OfferAction(this.session, this.getParameters());
        HtmlForm offerForm = new HtmlForm(offerAction);

        HtmlText t = new HtmlText(this.targetIdea.getTitle());
        offerPageContainer.add(t);


        HtmlTextField priceField = new HtmlTextField(price == null ? "" : price.toPlainString()); // TODO
        priceField.setLabel(this.session.tr("Offer price : "));
        priceField.setName(offerAction.getPriceCode());
        offerForm.add(priceField);

        HtmlDateField dateField = new HtmlDateField(this.expiryDate);
        dateField.setLabel(this.session.tr("Expiration date : "));
        dateField.setName(offerAction.getExpiryDateCode());
        offerForm.add(dateField);

        HtmlTextField titleField = new HtmlTextField(this.title); // TODO
        titleField.setLabel(this.session.tr("Add a title to the offer : "));
        titleField.setName(offerAction.getTitleCode());
        offerForm.add(titleField);

        HtmlTextArea descriptionField = new HtmlTextArea(this.description); // TODO
        descriptionField.setLabel(this.session.tr("Enter the description of the offer : "));
        descriptionField.setName(offerAction.getDescriptionCode());
        offerForm.add(descriptionField);

        HtmlButton offerButton = new HtmlButton(session.tr("Make an offer"));
        offerForm.add(offerButton);

        offerPageContainer.add(offerForm);
        return offerPageContainer;
    }

}
