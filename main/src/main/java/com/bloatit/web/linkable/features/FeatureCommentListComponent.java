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
package com.bloatit.web.linkable.features;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Comment;
import com.bloatit.model.Feature;
import com.bloatit.web.linkable.usercontent.CreateCommentForm;
import com.bloatit.web.pages.master.HtmlPageComponent;
import com.bloatit.web.pages.tools.CommentTools;
import com.bloatit.web.url.CreateCommentActionUrl;

public final class FeatureCommentListComponent extends HtmlPageComponent {

    private PageIterable<Comment> comments;
    private final Feature targetFeature;

    protected FeatureCommentListComponent(final Feature feature) {
        super();
        this.targetFeature = feature;
        this.comments = feature.getComments();
        add(produce());

    }

    /**
     * Creates the block that will be displayed in the offer tab.
     */
    private HtmlElement produce() {

        final HtmlDiv commentsBlock = new HtmlDiv("comments_block", "comments_block");
        {
            commentsBlock.add(new HtmlTitleBlock(Context.tr("Comments ({0})", targetFeature.getCommentsCount()), 1).setCssClass("comments_title"));

            commentsBlock.add(CommentTools.generateCommentList(comments));
            commentsBlock.add(new CreateCommentForm(new CreateCommentActionUrl(targetFeature)));
        }
        return commentsBlock;
    }

}
