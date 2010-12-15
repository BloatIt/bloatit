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
package com.bloatit.web.html.pages.demand;


import com.bloatit.common.PageIterable;
import com.bloatit.framework.Demand;
import com.bloatit.framework.Offer;
import com.bloatit.web.html.components.standard.HtmlDiv;

public class DemandOfferListComponent extends HtmlDiv {

    private final PageIterable<Offer> offers;

    public DemandOfferListComponent(final Demand demand) {
        super();
        offers = demand.getOffers();
        final HtmlDiv offersBlock = new HtmlDiv("offers_block");

        for (final Offer offer : offers) {
            offersBlock.add(new DemandOfferComponent(offer));
        }

        add(offersBlock);
    }

}
