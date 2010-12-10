/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with
 * BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package test.pages.demand;

import test.Context;
import test.html.HtmlElement;
import test.html.components.standard.HtmlDiv;
import test.html.components.standard.HtmlImage;
import test.html.components.standard.HtmlText;

import test.Request;

import com.bloatit.framework.Offer;
import com.bloatit.web.server.Session;

public class DemandOfferComponent extends HtmlElement {

    // private Offer offer;
    private HtmlText description;
    private HtmlText title;
    private HtmlText price;
    private HtmlText expirationDate;
    private HtmlText creationDate;
    private HtmlImage authorAvatar;
    private HtmlText author;
    private Offer offer;

    public DemandOfferComponent(Request request, Offer offer) {
        super();
        this.offer = offer;
        extractData(request);
        add(produce(request));
    }

    protected HtmlElement produce(Request request) {
        HtmlDiv offerBlock = new HtmlDiv("offer_block");
        {
            offerBlock.add(authorAvatar);

            HtmlDiv offerInfoBlock = new HtmlDiv("offer_info_block");
            {
                offerInfoBlock.add(author);
                offerInfoBlock.add(price);
                offerInfoBlock.add(expirationDate);
                offerInfoBlock.add(creationDate);
            }

            offerBlock.add(offerInfoBlock);
            offerBlock.add(title);
            offerBlock.add(description);

        }
        return offerBlock;
    }

    protected void extractData(Request request) {

        Session session = Context.getSession();
        author = new HtmlText(session.tr("Author : ") + offer.getAuthor().getFullname(), "offer_author");
        price = new HtmlText(session.tr("Price : ") + "Unknown yet", "offer_price");
        expirationDate = new HtmlText(session.tr("Expiration date : ") + offer.getDateExpire().toString(), "offer_expiry_date");
        authorAvatar = new HtmlImage(offer.getAuthor().getAvatar(), "offer_avatar");
        creationDate = new HtmlText(session.tr("Creation Date : ") + offer.getCreationDate().toString(), "offer_creation_date");

        title = new HtmlText(offer.getDescription().getDefaultTranslation().getTitle(), "offer_title");
        description = new HtmlText(offer.getDescription().getDefaultTranslation().getTitle(), "offer_description");

    }
}
