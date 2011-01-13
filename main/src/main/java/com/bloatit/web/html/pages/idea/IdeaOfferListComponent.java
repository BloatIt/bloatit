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
package com.bloatit.web.html.pages.idea;

import com.bloatit.common.PageIterable;
import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.Demand;
import com.bloatit.framework.Offer;
import com.bloatit.model.data.NullCollection;
import com.bloatit.web.html.components.standard.HtmlDiv;

public class IdeaOfferListComponent extends HtmlDiv {

    public IdeaOfferListComponent(final Demand demand) {
        super();
        PageIterable<Offer> offers = new NullCollection<Offer>();
        try {
            offers = demand.getOffers();
        } catch (UnauthorizedOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final HtmlDiv offersBlock = new HtmlDiv("offers_block");

        Offer currentOffer = demand.getCurrentOffer();
        offersBlock.add(new IdeaOfferComponent(currentOffer, true));

        for (final Offer offer : offers) {
            if (!offer.equals(currentOffer)) {
                offersBlock.add(new IdeaOfferComponent(offer, false));
            }
        }

        add(offersBlock);
    }

}
