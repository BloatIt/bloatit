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
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.components.standard.HtmlSpan;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.CommentReplyPageUrl;

/**
 * A component designed to be inserted into an idea page and that will
 * be used to display a comment (either main comment or child comment).
 */
public final class IdeaCommentComponent extends HtmlPageComponent {

    private final Comment comment;
    private final boolean child;

    public IdeaCommentComponent(final Comment comment, boolean child) {
        super();
        this.comment = comment;
        this.child = child;
        add(produce());
    }

    protected HtmlElement produce() {
        final HtmlDiv commentBlock = (child)?new HtmlDiv("child_comment_block"):new HtmlDiv("main_comment_block");
        {
            CommentKudoComponent commentKudo = new CommentKudoComponent(comment);
            commentBlock.add(commentKudo);

            HtmlParagraph commentText = new HtmlParagraph();
            commentText.add(new HtmlRawTextRenderer(comment.getText()));
            commentBlock.add(commentText);

            HtmlDiv commentInfo = new HtmlDiv("comment_info");
            commentText.add(commentInfo);

            final HtmlSpan date = new HtmlSpan();
            date.addText(HtmlTools.formatDate(Context.getLocalizator().getDate(comment.getCreationDate())));
            date.setCssClass("comment_date");
            commentInfo.add(date);

            final HtmlSpan author = new HtmlSpan();
            author.addText(comment.getAuthor().getLogin());
            author.setCssClass("comment_author");
            commentInfo.add(author);

            for (final Comment childComment : comment.getChildren()) {
                commentBlock.add(new IdeaCommentComponent(childComment, true));

            }

            if(!child){
                final HtmlDiv reply = new HtmlDiv("comment_reply");
                final HtmlLink replyLink = new HtmlLink(new CommentReplyPageUrl(comment).urlString(), Context.tr("reply"));
                replyLink.setCssClass("button");
                reply.add(replyLink);
                commentBlock.add(reply);
            }
        }
        return commentBlock;
    }
}
