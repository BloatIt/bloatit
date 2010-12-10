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
package test.actions;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.framework.Demand;
import test.Action;
import com.bloatit.web.utils.RequestParam;
import com.bloatit.web.utils.TestQueryAnnotation.DemandLoader;
import com.bloatit.web.utils.tr;
import test.Request;

public class OfferAction extends Action {

    public final static String IDEA_CODE = "offer_idea";
    public final static String PRICE_CODE = "offer_price";
    public final static String EXPIRY_CODE = "offer_expiry";
    public final static String TITLE_CODE = "offer_title";
    public final static String DESCRIPTION_CODE = "offer_description";
    
    /**
     * The idea on which the author wants to create a new offer
     */
    @RequestParam(name = IDEA_CODE, loader = DemandLoader.class, message = @tr("Invalid idea"))
    private Demand targetIdea = null;

    /**
     * The desired price for the offer
     */
    @RequestParam(name = PRICE_CODE)
    private BigDecimal price;

    /**
     * The expiration date for the offer
     */
    @RequestParam(name = EXPIRY_CODE)
    private Date expiryDate;

    /**
     * The title of the offer
     */
    @RequestParam(name = TITLE_CODE)
    private String title;

    /**
     * The short description of the offer
     */
    @RequestParam(name = DESCRIPTION_CODE)
    private String description;

    public OfferAction(Request request) {
        super(request);
    }

    @Override
    public String process() {
        // Handle errors here


        // targetIdea.addOffer(price, , expiryDate);
        return null;
        
    }
}
