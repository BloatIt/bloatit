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
import test.HtmlElement;
import test.pages.components.HtmlBlock;
import test.pages.components.HtmlText;
import test.pages.demand.DemandPage.Request;

import com.bloatit.framework.Comment;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.server.Session;

public class DemandCommentChildComponent extends HtmlElement {

    //private Offer offer;
    private Comment comment;
    private HtmlText commentText;

    public DemandCommentChildComponent(Request request, Comment comment) {
        super();
        this.comment = comment;
        extractData(request);
        add(produce(request));

    }

    protected HtmlElement produce(Request request) {
        HtmlBlock commentBlock = new HtmlBlock("child_comment_block");
        {

            commentBlock.add(commentText);

            for (Comment childComment : comment.getChildren()) {
                commentBlock.add( new DemandCommentChildComponent(request, childComment));
            }
            
        }
        return commentBlock;
    }

    protected void extractData(Request request) {

        Session session = Context.getSession();
        String date = "<span class=\"comment_date\">" + HtmlTools.formatDate(session , comment.getCreationDate()) + "</span>";
//      String author = "<span class=\"comment_author\">" + HtmlTools.generateLink(session, comment.getAuthor().getLogin(), new MemberPage(session, comment.getAuthor())) + "</span>";
        String author = "<span class=\"comment_author\">" + comment.getAuthor().getLogin() + "</span>";

        commentText = new HtmlText(comment.getText() + " – " + author + " " + date);

    }

     

}
