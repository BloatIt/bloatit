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

import java.math.BigDecimal;
import java.util.Locale;

import com.bloatit.framework.Demand;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.i18n.DateLocale;
import com.bloatit.web.utils.url.IdeaPageUrl;
import com.bloatit.web.utils.url.OfferActionUrl;
import com.bloatit.web.utils.url.Url;

@ParamContainer("action/offer")
public class OfferAction extends Action {
    public final static String PRICE_CODE = "offer_price";
    public final static String EXPIRY_CODE = "offer_expiry";
    public final static String TITLE_CODE = "offer_title";
    public final static String DESCRIPTION_CODE = "offer_description";

    @RequestParam(level=Level.ERROR, role=Role.GET)
    private Demand targetIdea = null;

    @RequestParam(name = PRICE_CODE, role = Role.POST)
    private BigDecimal price;

    @RequestParam(name = EXPIRY_CODE, role = Role.POST)
    private DateLocale expiryDate;

    @RequestParam(name = TITLE_CODE, role = Role.POST)
    private String title;
    
    @RequestParam(name = DESCRIPTION_CODE, role = Role.POST)
    private String description;
    
    @SuppressWarnings("unused")
    private final OfferActionUrl url;

    public OfferAction(final OfferActionUrl url) {
        super(url);

        this.url = url;
        this.description = url.getDescription();
        this.title = url.getTitle();
        this.expiryDate = url.getExpiryDate();
        this.price = url.getPrice();
        this.targetIdea = url.getTargetIdea();
    }

    @Override
    public Url doProcess() {
        targetIdea.authenticate(session.getAuthToken());
        targetIdea.addOffer(price, Locale.FRENCH, title, description, expiryDate.getJavaDate());
        return new IdeaPageUrl(targetIdea);
    }
}
