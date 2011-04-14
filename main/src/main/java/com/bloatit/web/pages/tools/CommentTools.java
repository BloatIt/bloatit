package com.bloatit.web.pages.tools;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlClearer;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.components.renderer.HtmlRawTextRenderer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Comment;
import com.bloatit.model.FileMetadata;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.linkable.members.MembersTools;
import com.bloatit.web.url.CommentReplyPageUrl;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.PopularityVoteActionUrl;

public class CommentTools {

    private static final int NB_COLUMNS = 80;
    private static final int NB_ROWS = 10;

    public static XmlNode generateCommentList(final PageIterable<Comment> comments) {
        final PlaceHolderElement ph = new PlaceHolderElement();
        for (final Comment comment : comments) {
            ph.add(generateComment(comment, false));
        }
        return ph;
    }

    private static HtmlElement generateComment(final Comment comment, final boolean child) {
        final HtmlDiv commentBlock = (child) ? new HtmlDiv("child_comment_block") : new HtmlDiv("main_comment_block");
        {

            commentBlock.add(new HtmlDiv("float_right").add(MembersTools.getMemberAvatar(comment.getMember())));

            final HtmlParagraph commentText = new HtmlParagraph();
            commentText.add(new HtmlRawTextRenderer(comment.getText()));
            commentBlock.add(commentText);

            // Attachements
            for (final FileMetadata attachment : comment.getFiles()) {
                final HtmlParagraph attachmentPara = new HtmlParagraph();
                attachmentPara.add(new FileResourceUrl(attachment).getHtmlLink(attachment.getFileName()));
                attachmentPara.addText(tr(": ") + attachment.getShortDescription());
                commentBlock.add(attachmentPara);
            }

            final HtmlDiv commentInfo = new HtmlDiv("comment_info");
            commentBlock.add(commentInfo);
            commentBlock.add(new HtmlClearer());

            commentInfo.addText(Context.tr("Created by "));

            try {
                final MemberPageUrl memberUrl = new MemberPageUrl(comment.getMember());
                commentInfo.add(memberUrl.getHtmlLink(comment.getMember().getDisplayName()));
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
}
