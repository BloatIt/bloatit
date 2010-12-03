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
import com.bloatit.framework.Comment;
import com.bloatit.framework.Offer;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTitle;
import com.bloatit.web.pages.components.PageComponent;
import com.bloatit.web.pages.demand.DemandPage;

public class DemandCommentListComponent extends PageComponent {

    private final DemandPage demandPage;
    private PageIterable<Comment> comments;

    public DemandCommentListComponent(DemandPage demandPage) {
        super(demandPage.getSession());
        this.demandPage = demandPage;
    }

    /**
     * Creates the block that will be displayed in the offer tab.
     */
    @Override
    protected HtmlComponent produce() {

        HtmlBlock commentsBlock = new HtmlBlock("comments_block");
        {
            commentsBlock.add(new HtmlTitle(session.tr("Comments"), "comments_title"));

            for (Comment comment : comments) {
                commentsBlock.add(new DemandCommentComponent(demandPage, comment));
            }
        }
        return commentsBlock;
    }

    @Override
    protected void extractData() {
        comments = this.demandPage.getDemand().getComments();
    }
}
