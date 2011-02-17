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
package com.bloatit.web.pages.demand;

import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.model.Demand;

public class DemandBugListComponent extends HtmlDiv {

    private final Demand demand;

    public DemandBugListComponent(final Demand demand) {
        super();
        this.demand = demand;
        /*try {

            PageIterable<Bug> bugs = new NullCollection<Bug>();
            bugs = demand.getBugs();
            int unselectedOfferCount = offers.size();

            final Offer selectedOffer = demand.getSelectedOffer();
            if (selectedOffer != null) {
                unselectedOfferCount--;
            }

            final HtmlDiv offersBlock = new HtmlDiv("offers_block");

            if (selectedOffer != null) {
                HtmlTitle selectedOfferTitle = new HtmlTitle(Context.tr("Selected offer"), 1);
                offersBlock.add(selectedOfferTitle);
                offersBlock.add(generateSelectedOfferTypeBlock(selectedOffer));
            } else {
                HtmlTitle selectedOfferTitle = new HtmlTitle(Context.tr("No selected offer"), 1);
                offersBlock.add(selectedOfferTitle);
            }

            HtmlTitle unselectedOffersTitle = new HtmlTitle(Context.trn("Unselected offer ({0})", "Unselected offers ({0})", unselectedOfferCount,
                    unselectedOfferCount), 1);
            offersBlock.add(unselectedOffersTitle);

            offersBlock.add(generateUnselectedOffersTypeBlock(offers, selectedOffer));


            add(offersBlock);

        } catch (final UnauthorizedOperationException e) {
            // No right no current offer.
        }*/
    }


}
