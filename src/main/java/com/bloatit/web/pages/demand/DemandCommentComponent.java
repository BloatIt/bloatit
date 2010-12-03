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

import com.bloatit.framework.Comment;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlText;
import com.bloatit.web.pages.MemberPage;
import com.bloatit.web.pages.components.PageComponent;
import com.bloatit.web.pages.demand.DemandPage;

public class DemandCommentComponent extends PageComponent {

    //private Offer offer;
    private final DemandPage demandPage;
    private Comment comment;
    private HtmlText commentText;

    public DemandCommentComponent(DemandPage demandPage, Comment comment) {
        super(demandPage.getSession());
        this.demandPage = demandPage;
        this.comment = comment;

    }

    @Override
    protected HtmlComponent produce() {
        HtmlBlock commentBlock = new HtmlBlock("main_comment_block");
        {

            commentBlock.add(commentText);

            for (Comment childComment : comment.getChildren()) {
                commentBlock.add(new DemandCommentChildComponent(demandPage, childComment));
            }


        }
        return commentBlock;
    }

    @Override
    protected void extractData() {

        String date = "<span class=\"comment_date\">" + HtmlTools.formatDate(session, comment.getCreationDate()) + "</span>";
        String author = "<span class=\"comment_author\">" + HtmlTools.generateLink(session, comment.getAuthor().getLogin(), new MemberPage(session, comment.getAuthor())) + "</span>";

        commentText = new HtmlText(comment.getText() + " – " + author + " " + date);

    }
}
