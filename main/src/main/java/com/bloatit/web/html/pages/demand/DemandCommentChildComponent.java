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
package com.bloatit.web.html.pages.demand;


import com.bloatit.framework.Comment;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.HtmlTools;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Session;

public class DemandCommentChildComponent extends HtmlPageComponent {

    // private Offer offer;
    private final Comment comment;
    private HtmlText commentText;

    public DemandCommentChildComponent(final Comment comment) {
        super();
        this.comment = comment;
        extractData();
        add(produce());

    }

    protected HtmlElement produce() {
        final HtmlDiv commentBlock = new HtmlDiv("child_comment_block");
        {

            commentBlock.add(commentText);

            for (final Comment childComment : comment.getChildren()) {
                commentBlock.add(new DemandCommentChildComponent(childComment));
            }

        }
        return commentBlock;
    }

    protected void extractData() {

        final Session session = Context.getSession();
        final String date = "<span class=\"comment_date\">" + HtmlTools.formatDate(session, comment.getCreationDate()) + "</span>";
        // String author = "<span class=\"comment_author\">" +
        // HtmlTools.generateLink(session, comment.getAuthor().getLogin(), new
        // MemberPage(session, comment.getAuthor())) + "</span>";
        final String author = "<span class=\"comment_author\">" + comment.getAuthor().getLogin() + "</span>";

        commentText = new HtmlText(comment.getText() + " – " + author + " " + date);

    }
}
