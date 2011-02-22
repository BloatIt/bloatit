package com.bloatit.web.pages.tools;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.framework.webserver.components.renderer.HtmlRawTextRenderer;
import com.bloatit.model.Comment;
import com.bloatit.model.Commentable;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.actions.CreateCommentAction;
import com.bloatit.web.url.CommentReplyPageUrl;
import com.bloatit.web.url.CreateCommentActionUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.PopularityVoteActionUrl;

public class CommentTools {

    private static final int NB_COLUMNS = 80;
    private static final int NB_ROWS = 10;

    public static XmlNode generateCommentList(PageIterable<Comment> comments) {
        PlaceHolderElement ph = new PlaceHolderElement();
            for (final Comment comment : comments) {
                ph.add(generateComment(comment, false));
            }
        return ph;
    }

    private static HtmlElement generateComment(final Comment comment, final boolean child) {
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

                if (!comment.isOwner()) {
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


    public static HtmlElement generateNewCommentComponent(final Commentable commentable) {
        final CreateCommentActionUrl url = new CreateCommentActionUrl(commentable);
        final HtmlDiv commentBlock = new HtmlDiv("new_comment_block");

        final HtmlForm form = new HtmlForm(url.urlString());
        commentBlock.add(form);

        final HtmlTextArea commentInput = new HtmlTextArea(CreateCommentAction.COMMENT_CONTENT_CODE, Context.tr("New comment : "), NB_ROWS, NB_COLUMNS);
        form.add(commentInput);
        commentInput.setComment(Context.tr("Use this field to comment the demand. If you want to reply to a previous comment, use the reply link."));

        form.add(new HtmlSubmit(Context.tr("Submit comment")));

        return commentBlock;
    }
}
