package com.bloatit.web.pages.tools;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlClearer;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.renderer.HtmlRawTextRenderer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Comment;
import com.bloatit.model.FileMetadata;
import com.bloatit.web.components.KudosableAuthorBlock;
import com.bloatit.web.linkable.members.MembersTools;
import com.bloatit.web.url.CommentReplyPageUrl;
import com.bloatit.web.url.FileResourceUrl;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

public class CommentTools {

    public static HtmlElement generateCommentList(final PageIterable<Comment> comments) {
        final PlaceHolderElement ph = new PlaceHolderElement();
        for (final Comment comment : comments) {
            ph.add(generateComment(comment, false, null));
        }
        return ph;
    }

    public static HtmlElement generateCommentList(final PageIterable<Comment> comments, Map<String, String> formatMap) {
        final PlaceHolderElement ph = new PlaceHolderElement();
        for (final Comment comment : comments) {
            ph.add(generateComment(comment, false, formatMap));
        }
        return ph;
    }

    private static HtmlElement generateComment(final Comment comment, final boolean child, Map<String, String> formatMap) {
        final HtmlDiv commentBlock = (child) ? new HtmlDiv("child_comment_block") : new HtmlDiv("main_comment_block");
        {

            commentBlock.add(new HtmlDiv("float_right").add(MembersTools.getMemberAvatar(comment.getMember())));

            final HtmlParagraph commentText = new HtmlParagraph();
            commentText.add(new HtmlRawTextRenderer(formatComment(comment.getText(), formatMap)));
            commentBlock.add(commentText);

            // Attachements
            for (final FileMetadata attachment : comment.getFiles()) {
                final HtmlParagraph attachmentPara = new HtmlParagraph();
                attachmentPara.add(new FileResourceUrl(attachment).getHtmlLink(attachment.getFileName()));
                attachmentPara.addText(tr(": ") + attachment.getShortDescription());
                commentBlock.add(attachmentPara);
            }

            commentBlock.add(new KudosableAuthorBlock(comment));
            commentBlock.add(new HtmlClearer());

            // Display child elements
            for (final Comment childComment : comment.getChildren()) {
                commentBlock.add(generateComment(childComment, true, formatMap));
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

    private static String formatComment(final String inputComment, Map<String, String> formatMap) {
        String comment = inputComment;
        if (formatMap != null) {
            for (Entry<String, String> formatter : formatMap.entrySet()) {
                if (!formatter.getValue().isEmpty()) {
                    comment = comment.replaceAll(formatter.getKey(), Matcher.quoteReplacement(formatter.getValue()));
                } else {
                    comment = comment.replaceAll(formatter.getKey(), "null");
                }
            }
        }

        return comment;
    }
}
