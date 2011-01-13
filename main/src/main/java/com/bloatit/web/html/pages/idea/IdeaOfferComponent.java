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

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.Offer;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlImage;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.components.standard.HtmlSpan;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.i18n.DateLocale.FormatStyle;

public final class IdeaOfferComponent extends HtmlPageComponent {

    private final Offer offer;
    private final boolean currentOffer;

    public IdeaOfferComponent(Offer offer, boolean b) {
        super();
        this.offer = offer;
        this.currentOffer = b;

        add(produce());
    }

    protected HtmlElement produce() {

        HtmlParagraph author = null;
        try {
            author = new HtmlParagraph(Context.tr("Author : ") + offer.getAuthor().getDisplayName(), "offer_author");
        } catch (UnauthorizedOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HtmlParagraph price = new HtmlParagraph(Context.tr("Price : ") + Context.getLocalizator().getCurrency(offer.getAmount()).getLocaleString(),
                "offer_price");
        HtmlParagraph expirationDate = new HtmlParagraph(Context.tr("Expiration date : ")
                + Context.getLocalizator().getDate(offer.getDateExpire()).toDateTimeString(FormatStyle.LONG, FormatStyle.MEDIUM), "offer_expiry_date");
        HtmlImage authorAvatar = new HtmlImage(offer.getAuthor().getAvatar(), "offer_avatar");
        HtmlParagraph creationDate = new HtmlParagraph(Context.tr("Creation Date : ")
                + Context.getLocalizator().getDate(offer.getCreationDate()).toDateTimeString(FormatStyle.LONG, FormatStyle.MEDIUM),
                "offer_creation_date");

        HtmlParagraph title = new HtmlParagraph(offer.getDescription().getDefaultTranslation().getTitle(), "offer_title");
        HtmlParagraph description = new HtmlParagraph(offer.getDescription().getDefaultTranslation().getTitle(), "offer_description");

        final HtmlDiv offerBlock = new HtmlDiv("offer_block");
        {

            if(this.currentOffer){
                offerBlock.add(new HtmlSpan().addText(Context.tr("Currently favored offer")).setCssClass("offer_validated_info"));
                offerBlock.setCssClass("offer_block_validated");
            }

            HtmlDiv offerMainBlock = new HtmlDiv("offer_main_block");
            offerBlock.add(offerMainBlock);

            offerMainBlock.add(authorAvatar);

            final HtmlDiv offerInfoBlock = new HtmlDiv("offer_info_block");
            {
                offerInfoBlock.add(author);
                offerInfoBlock.add(price);
                offerInfoBlock.add(expirationDate);
                offerInfoBlock.add(creationDate);
            }

            offerMainBlock.add(offerInfoBlock);
            offerMainBlock.add(title);
            offerMainBlock.add(description);

        }
        return offerBlock;
    }
}
