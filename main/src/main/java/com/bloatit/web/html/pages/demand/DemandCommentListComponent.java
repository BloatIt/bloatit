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
import com.bloatit.web.html.HtmlTools;
import com.bloatit.web.html.components.custom.renderer.HtmlRawTextRenderer;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.components.standard.HtmlSpan;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.CommentReplyPageUrl;
import com.bloatit.web.utils.url.KudoActionUrl;
import com.bloatit.web.utils.url.MemberPageUrl;

public final class DemandCommentListComponent extends HtmlPageComponent {

    private PageIterable<Comment> comments;
    private final Demand targetDemand;

    public DemandCommentListComponent(final Demand demand) {
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

        final HtmlDiv commentsBlock = new HtmlDiv("comments_block");
        {
            commentsBlock.add(new HtmlTitleBlock(Context.tr("Comments ({0})", comments.size()), 2).setCssClass("comments_title"));

            for (final Comment comment : comments) {
                commentsBlock.add(generateComment(comment, false));
            }
            commentsBlock.add(new IdeaNewCommentComponent(targetDemand));
        }
        return commentsBlock;
    }

    private HtmlElement generateComment(Comment comment, boolean child) {
        final HtmlDiv commentBlock = (child) ? new HtmlDiv("child_comment_block") : new HtmlDiv("main_comment_block");
        {

            final HtmlParagraph commentText = new HtmlParagraph();
            commentText.add(new HtmlRawTextRenderer(comment.getText()));
            commentBlock.add(commentText);



            final HtmlDiv commentInfo = new HtmlDiv("comment_info");
            commentBlock.add(commentInfo);

            commentInfo.addText(Context.tr("Created by "));


            commentInfo.addText(Context.tr("Created by "));

            try {
                final MemberPageUrl memberUrl = new MemberPageUrl(comment.getAuthor());
                commentInfo.add(memberUrl.getHtmlLink(comment.getAuthor().getDisplayName()));
            } catch (final UnauthorizedOperationException e1) {
                // Nothing.
            }

            commentInfo.addText(" – ");

            final HtmlSpan dateSpan = new HtmlSpan("comment_date");
            dateSpan.addText(HtmlTools.formatDate(Context.getLocalizator().getDate(comment.getCreationDate())));
            commentInfo.add(dateSpan);

            commentInfo.addText(" – ");

            //////////////////////
            // Popularity
            final HtmlSpan commentPopularity = new HtmlSpan("comment_populatity");
            {

                commentPopularity.addText(Context.tr("Popularity: {0}", HtmlTools.compressKarma(comment.getPopularity())));

                commentPopularity.addText(" (");

                //Usefull
                KudoActionUrl usefullUrl = new KudoActionUrl(comment);
                final HtmlLink usefullLink = usefullUrl.getHtmlLink(Context.tr("Usefull"));
                usefullLink.setCssClass("usefull");

                //Useless
                KudoActionUrl uselessUrl = new KudoActionUrl(comment);
                final HtmlLink uselessLink = uselessUrl.getHtmlLink(Context.tr("Useless"));
                uselessLink.setCssClass("useless");

                commentPopularity.add(usefullLink);
                commentPopularity.addText(" – ");
                commentPopularity.add(uselessLink);


                commentPopularity.addText(")");


            }
            commentInfo.add(commentPopularity);

            //Display child elements
            for (final Comment childComment : comment.getChildren()) {
                commentBlock.add(generateComment(childComment, true));

            }

            if (!child) {
                final HtmlDiv reply = new HtmlDiv("comment_reply");
                final HtmlLink replyLink = new HtmlLink(new CommentReplyPageUrl(comment).urlString(), Context.tr("Reply"));
                reply.add(replyLink);
                commentBlock.add(reply);
            }
        }
        return commentBlock;
    }

}
