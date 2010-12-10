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
package test.pages.demand;

import test.Context;
import test.html.HtmlElement;
import test.html.components.basicComponents.HtmlBlock;
import test.html.components.basicComponents.HtmlTitle;
import test.pages.demand.DemandPage.Request;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Comment;

public class DemandCommentListComponent extends HtmlElement {

    private PageIterable<Comment> comments;

    public DemandCommentListComponent(Request request) {
        super();
        extractData(request);
        add(produce(request));
    }

    /**
     * Creates the block that will be displayed in the offer tab.
     */
    protected HtmlElement produce(Request request) {

        HtmlBlock commentsBlock = new HtmlBlock("comments_block");
        {
            commentsBlock.add(new HtmlTitle(Context.tr("Comments"), "comments_title"));

            for (Comment comment : comments) {
                commentsBlock.add(new DemandCommentComponent(request, comment));
            }
        }
        return commentsBlock;
    }

    protected void extractData(Request request) {
        comments = request.demand.getComments();
    }
}
