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
package com.bloatit.web.pages.demand;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.renderer.HtmlRawTextRenderer;
import com.bloatit.model.Comment;
import com.bloatit.model.Demand;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.actions.IdeaCommentAction;
import com.bloatit.web.pages.master.HtmlPageComponent;
import com.bloatit.web.url.CommentReplyPageUrl;
import com.bloatit.web.url.IdeaCommentActionUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.PopularityVoteActionUrl;

public final class DemandCommentListComponent extends HtmlPageComponent {

    private PageIterable<Comment> comments;
    private final Demand targetDemand;
    private static final int NB_COLUMNS = 80;
    private static final int NB_ROWS = 10;

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

        final HtmlDiv commentsBlock = new HtmlDiv("comments_block", "comments_block");
        {
            commentsBlock.add(new HtmlTitleBlock(Context.tr("Comments ({0})", comments.size()), 2).setCssClass("comments_title"));

            for (final Comment comment : comments) {
                commentsBlock.add(generateComment(comment, false));
            }
            commentsBlock.add(generateNewCommentComponent(targetDemand));
        }
        return commentsBlock;
    }

    private HtmlElement generateComment(final Comment comment, final boolean child) {
        final HtmlDiv commentBlock = (child) ? new HtmlDiv("child_comment_block") : new HtmlDiv("main_comment_block");
        {

            final HtmlParagraph commentText = new HtmlParagraph();
            commentText.add(new HtmlRawTextRenderer(comment.getText()));
            commentBlock.add(commentText);

            final HtmlDiv commentInfo = new HtmlDiv("comment_info");
            commentBlock.add(commentInfo);

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

            // ////////////////////
            // Popularity
            final HtmlSpan commentPopularity = new HtmlSpan("comment_populatity");
            {

                commentPopularity.addText(Context.tr("Popularity: {0}", HtmlTools.compressKarma(comment.getPopularity())));

                if (!comment.isOwnedByMe()) {
                    final int vote = comment.getUserVoteValue();
                    if (vote == 0) {
                        commentPopularity.addText(" (");

                        // Usefull
                        final PopularityVoteActionUrl usefullUrl = new PopularityVoteActionUrl(comment, true);
                        final HtmlLink usefullLink = usefullUrl.getHtmlLink(Context.tr("Usefull"));
                        usefullLink.setCssClass("usefull");

                        // Useless
                        final PopularityVoteActionUrl uselessUrl = new PopularityVoteActionUrl(comment, false);
                        final HtmlLink uselessLink = uselessUrl.getHtmlLink(Context.tr("Useless"));
                        uselessLink.setCssClass("useless");

                        commentPopularity.add(usefullLink);
                        commentPopularity.addText(" – ");
                        commentPopularity.add(uselessLink);

                        commentPopularity.addText(")");
                    } else {
                        // Already voted
                        final HtmlSpan voted = new HtmlSpan("comment_voted");
                        {
                            if (vote > 0) {
                                voted.addText("+" + vote);
                                voted.setCssClass("comment_voted usefull");
                            } else {
                                voted.addText("−" + Math.abs(vote));
                                voted.setCssClass("comment_voted useless");
                            }
                        }
                        commentPopularity.add(voted);
                    }
                }

            }
            commentInfo.add(commentPopularity);

            // Display child elements
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

    private HtmlElement generateNewCommentComponent(final Demand demand) {
        final IdeaCommentActionUrl url = new IdeaCommentActionUrl(demand);
        final HtmlDiv commentBlock = new HtmlDiv("new_comment_block");

        final HtmlForm form = new HtmlForm(url.urlString());
        commentBlock.add(form);

        final HtmlTextArea commentInput = new HtmlTextArea(IdeaCommentAction.COMMENT_CONTENT_CODE, Context.tr("New comment : "), NB_ROWS, NB_COLUMNS);
        form.add(commentInput);
        commentInput.setComment(Context.tr("Use this field to comment the demand. If you want to reply to a previous comment, use the reply link."));

        form.add(new HtmlSubmit(Context.tr("Submit comment")));

        return commentBlock;
    }

}
