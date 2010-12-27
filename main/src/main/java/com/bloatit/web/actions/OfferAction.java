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
package com.bloatit.web.actions;

import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.utils.url.OfferActionUrl;

@ParamContainer("action/offer")
public class OfferAction extends Action {

    public final static String IDEA_CODE = "offer_idea";
    public final static String PRICE_CODE = "offer_price";
    public final static String EXPIRY_CODE = "offer_expiry";
    public final static String TITLE_CODE = "offer_title";
    public final static String DESCRIPTION_CODE = "offer_description";

    public OfferAction(final OfferActionUrl url) {
        super(url);
    }

    @Override
    public String doProcess() {
        // Handle errors here

        // targetIdea.addOffer(price, , expiryDate);
        return null;

    }
}
