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

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.i18n.DateLocale;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Demand;
import com.bloatit.model.Offer;
import com.bloatit.web.url.DemandPageUrl;
import com.bloatit.web.url.OfferActionUrl;
import com.bloatit.web.url.OfferPageUrl;

/**
 * Class that will create a new offer based on data received from a form.
 */
@ParamContainer("action/offer")
public final class OfferAction extends LoggedAction {
    public static final String PRICE_CODE = "offer_price";
    public static final String EXPIRY_CODE = "offer_expiry";
    public static final String TITLE_CODE = "offer_title";
    public static final String DESCRIPTION_CODE = "offer_description";

    @RequestParam(level = Level.ERROR, role = Role.GET, conversionErrorMsg = @tr("The target idea is mandatory to make an offer."))
    private Demand targetIdea = null;

    @RequestParam(name = PRICE_CODE, role = Role.POST, conversionErrorMsg = @tr("Invalid or missing value for price field."))
    private final BigDecimal price;

    @RequestParam(name = EXPIRY_CODE, role = Role.POST)
    private final DateLocale expiryDate;

    @RequestParam(name = TITLE_CODE, role = Role.POST)
    private final String title;

    @RequestParam(name = DESCRIPTION_CODE, role = Role.POST)
    private final String description;

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
    public Url doProcessRestricted() {
        try {
            targetIdea.authenticate(session.getAuthToken());
            final Offer offer = new Offer(session.getAuthToken().getMember(), targetIdea, price, description, title, Locale.FRENCH,
                    expiryDate.getJavaDate());
            targetIdea.addOffer(offer);
        } catch (final UnauthorizedOperationException e) {
            session.notifyBad(Context.tr("For obscure reasons, you are not allowed to make an offer on this idea."));
            return session.pickPreferredPage();
        }
        return new DemandPageUrl(targetIdea);
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        session.notifyList(url.getMessages());

        if (targetIdea != null) {

            final OfferPageUrl redirectUrl = new OfferPageUrl(targetIdea);
            session.addParameter(url.getDescriptionParameter());
            session.addParameter(url.getPriceParameter());
            session.addParameter(url.getTitleParameter());

            if (expiryDate != null) {
                session.addParameter(url.getExpiryDateParameter());
            }
            return redirectUrl;
        }
        return session.pickPreferredPage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to make an offer.");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getPriceParameter());
        session.addParameter(url.getTitleParameter());

        if (expiryDate != null) {
            session.addParameter(url.getExpiryDateParameter());
        }
    }
}
