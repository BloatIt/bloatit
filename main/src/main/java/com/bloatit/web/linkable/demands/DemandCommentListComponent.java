/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.demands;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Comment;
import com.bloatit.model.Feature;
import com.bloatit.web.pages.master.HtmlPageComponent;
import com.bloatit.web.pages.tools.CommentTools;

public final class DemandCommentListComponent extends HtmlPageComponent {

    private PageIterable<Comment> comments;
    private final Feature targetDemand;

    public DemandCommentListComponent(final Feature demand) {
        super();
        this.targetDemand = demand;
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

        final HtmlDiv commentsBlock = new HtmlDiv("comments_block", "comments_block");
        {
            commentsBlock.add(new HtmlTitleBlock(Context.tr("Comments ({0})", comments.size()), 1).setCssClass("comments_title"));

            commentsBlock.add(CommentTools.generateCommentList(comments));
            commentsBlock.add(CommentTools.generateNewCommentComponent(targetDemand));
        }
        return commentsBlock;
    }

}
