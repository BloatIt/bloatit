package com.bloatit.web.components;

import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.model.UserContentInterface;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.TeamPageUrl;

public class HtmlAuthorLink extends HtmlLink {
    public HtmlAuthorLink(final UserContentInterface content) {
        // @formatter:off
        super(
              content.getAsTeam() != null ? 
                      new TeamPageUrl(content.getAsTeam()).urlString() :
                      new MemberPageUrl(content.getMember()).urlString()
                      ,
              content.getAuthor().getDisplayName());
    }
    // @formatter:on
}
