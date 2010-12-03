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
package com.bloatit.web.pages.demand;

import com.bloatit.web.pages.demand.DemandOfferComponent;
import com.bloatit.common.PageIterable;
import com.bloatit.framework.Offer;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.pages.components.PageComponent;
import com.bloatit.web.pages.demand.DemandPage;

public class DemandOfferListComponent extends PageComponent {

    private final DemandPage demandPage;
    private PageIterable<Offer> offers;

    public DemandOfferListComponent(DemandPage demandPage) {
        super(demandPage.getSession());
        this.demandPage = demandPage;
    }


    /**
     * Creates the block that will be displayed in the offer tab.
     */
    @Override
    protected HtmlComponent produce() {
        
        HtmlBlock offersBlock = new HtmlBlock("offers_block");

        for (Offer offer : offers) {
            
            offersBlock.add(new DemandOfferComponent(demandPage, offer));
        }

        return offersBlock;
    }

    @Override
    protected void extractData() {
        offers = this.demandPage.getDemand().getOffers();
    }
}
