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
package com.bloatit.web.actions;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.framework.Demand;
import com.bloatit.web.server.Action;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.RequestParam;
import com.bloatit.web.utils.TestQueryAnnotation.DemandLoader;
import com.bloatit.web.utils.tr;

public class OfferAction extends Action {

    /**
     * The idea on which the author wants to create a new offer
     */
    @RequestParam(name = "idea", loader = DemandLoader.class, message = @tr("Invalid idea"))
    private Demand targetIdea = null;
    /**
     * The desired price for the offer
     */
    @RequestParam(name = "offer_price")
    private BigDecimal price;
    /**
     * The expiration date for the offer
     */
    @RequestParam(name = "offer_expiry_date")
    private Date expiryDate;
    /**
     * The title of the offer
     */
    @RequestParam(name = "offer_title")
    private String title;
    /**
     * The short description of the offer
     */
    @RequestParam(name = "offer_description")
    private String description;

    public OfferAction(Session session) {
        super(session, new HashMap<String, String>());
    }

    public OfferAction(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    /**
     * @return the code used to generate the form input field for the price
     */
    public String getPriceCode() {
        return "offer_price";
    }

    /**
     * @return the code used to generate the form input field for the expiration date
     */
    public String getExpiryDateCode() {
        return "offer_expiry_date";
    }

    /**
     * @return the code used to generate the form input field for the offer title
     */
    public String getTitleCode() {
        return "offer_title";
    }

    /**
     * @return the code used to generate the form input field for description of the offer
     */
    public String getDescriptionCode() {
        return "offer_description";
    }

    @Override
    public String getCode() {
        return "offer";
    }

    @Override
    protected void process() {
        // Handle errors here


        // targetIdea.addOffer(price, , expiryDate);
        
    }
}
