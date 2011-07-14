/*
 * 
 */
package com.bloatit.web.components;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.advanced.HtmlClearer;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.UserContentInterface;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.url.MemberPageUrl;

public class UserContentAuthorBlock extends HtmlDiv {

    private final HtmlDiv commentInfo;

    public UserContentAuthorBlock(final UserContentInterface content) {
        super("usercontent_infos");
        commentInfo = new HtmlDiv("usercontent_author");
        super.add(commentInfo);
        super.add(new HtmlClearer());

        final HtmlAuthorLink authorLink = new HtmlAuthorLink(content);
        if (content.getAsTeam() != null) {
            final HtmlSpan userSpan = new HtmlSpan("usercontent_by_user");
            userSpan.add(new MemberPageUrl(content.getMember()).getHtmlLink(content.getMember().getDisplayName()));
            final HtmlMixedText authorMixed = new HtmlMixedText(tr("In the name of <0::> by <1::>"), authorLink, userSpan);
            commentInfo.add(authorMixed);
        } else {
            commentInfo.addText(tr("By "));
            commentInfo.add(authorLink);
        }

        commentInfo.addText(" – ");

        final HtmlSpan dateSpan = new HtmlSpan("usercontent_date");
        dateSpan.addText(HtmlTools.formatDate(Context.getLocalizator().getDate(content.getCreationDate())));
        commentInfo.add(dateSpan);
    }

    @Override
    public HtmlBranch add(final XmlNode html) {
        return commentInfo.add(html);
    }

}
