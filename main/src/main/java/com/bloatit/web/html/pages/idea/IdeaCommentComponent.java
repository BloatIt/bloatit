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

import com.bloatit.framework.Comment;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlTools;
import com.bloatit.web.html.components.custom.renderer.HtmlRawTextRenderer;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlGenericElement;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.CommentReplyPageUrl;

public class IdeaCommentComponent extends HtmlPageComponent {

    private final Comment comment;
    private HtmlParagraph commentText;

    public IdeaCommentComponent(final Comment comment) {
        super();
        this.comment = comment;
        extractData();
        add(produce());
    }

    protected HtmlElement produce() {
        final HtmlDiv commentBlock = new HtmlDiv("main_comment_block");
        {
            commentBlock.add(commentText);

            for (final Comment childComment : comment.getChildren()) {
                commentBlock.add(new IdeaCommentChildComponent(childComment));
            }
        }
        return commentBlock;
    }

    protected void extractData() {
        
        final HtmlGenericElement date = new HtmlGenericElement("span");
//        date.addText(HtmlTools.formatDate(session, comment.getCreationDate()));
        date.addText(HtmlTools.formatDate(Context.getLocalizator().getDate(comment.getCreationDate())));

        date.setCssClass("comment_date");
        
        final HtmlGenericElement author = new HtmlGenericElement("span");
        author.addText(comment.getAuthor().getLogin());
        author.setCssClass("comment_author");
        
        final HtmlGenericElement reply = new HtmlGenericElement("span");
        final HtmlLink replyLink = new HtmlLink(new CommentReplyPageUrl(comment).urlString(), Context.tr("reply"));
        reply.add(replyLink);
        reply.setCssClass("comment_reply");
        
        commentText = new HtmlParagraph();
        commentText.add(new HtmlRawTextRenderer(comment.getText()));
        commentText.add(date);
        commentText.add(author);
        commentText.add(reply);
    }
}
