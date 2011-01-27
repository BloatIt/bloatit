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
package com.bloatit.web.html.pages.demand;

import com.bloatit.common.PageIterable;
import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.Comment;
import com.bloatit.framework.demand.Demand;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;

public final class DemandCommentListComponent extends HtmlPageComponent {

    private PageIterable<Comment> comments;
    private final Demand targetIdea;

    public DemandCommentListComponent(final Demand demand) {
        super();
        this.targetIdea = demand;
        try {
            this.comments = demand.getComments();
            add(produce());
        } catch (final UnauthorizedOperationException e) {
            this.comments = null;
            // No right, no comments
        }

    }

    /**
     * Creates the block that will be displayed in the offer tab.
     */
    protected HtmlElement produce() {

        final HtmlDiv commentsBlock = new HtmlDiv("comments_block");
        {
            commentsBlock.add(new HtmlTitleBlock(Context.tr("Comments"), 2).setCssClass("comments_title"));

            for (final Comment comment : comments) {
                commentsBlock.add(new IdeaCommentComponent(comment, false));
            }
            commentsBlock.add(new IdeaNewCommentComponent(targetIdea));
        }
        return commentsBlock;
    }

}
