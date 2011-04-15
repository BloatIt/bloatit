package com.bloatit.web.components;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.advanced.HtmlClearer;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.UserContentInterface;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.url.MemberPageUrl;

public class UserContentAuthorBlock extends HtmlDiv {

    private HtmlDiv commentInfo;

    public UserContentAuthorBlock(final UserContentInterface<?> content) {
        super("usercontent_infos");
        commentInfo = new HtmlDiv("usercontent_author");
        super.add(commentInfo);
        super.add(new HtmlClearer());

        if (content.getAsTeam() != null) {
            commentInfo.addText(tr("In the name of "));
        } else {
            commentInfo.addText(tr("By "));
        }

        try {
            commentInfo.add(new HtmlAuthorLink(content));
        } catch (final UnauthorizedOperationException e1) {
            throw new ShallNotPassException(e1);
        }

        if (content.getAsTeam() != null) {
            final HtmlSpan userSpan = new HtmlSpan("usercontent_by_user");
            userSpan.addText(tr(" By "));
            try {
                userSpan.add(new MemberPageUrl(content.getMember()).getHtmlLink(content.getMember().getDisplayName()));
            } catch (final UnauthorizedOperationException e1) {
                throw new ShallNotPassException(e1);
            }
            commentInfo.add(userSpan);
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
