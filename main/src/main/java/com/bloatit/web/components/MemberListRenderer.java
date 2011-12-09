//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.components;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlRenderer;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.components.HtmlFollowButton.HtmlFollowActorButton;
import com.bloatit.web.linkable.members.MembersTools;
import com.bloatit.web.url.MemberPageUrl;

/**
 * A simple renderer for teams that display only their name on one line, plus a
 * link to their page
 */
public class MemberListRenderer implements HtmlRenderer<Member> {
    @Override
    public HtmlNode generate(final Member member) {
        final MemberPageUrl memberUrl = new MemberPageUrl(member);
        final HtmlDiv box = new HtmlDiv("actor-box");

        box.add(new HtmlDiv("actor-box-avatar").add(MembersTools.getMemberAvatar(member)));

        HtmlDiv content = new HtmlDiv("actor-box-content");
        box.add(content);

        // Name
        final HtmlDiv nameBox = new HtmlDiv("actor-box-actor-name");
        HtmlLink htmlLink;
        htmlLink = memberUrl.getHtmlLink(member.getDisplayName());
        htmlLink.setCssClass("member-link");
        final HtmlSpan karma = new HtmlSpan("karma");
        karma.addAttribute("title", Context.tr("{0} karma's ", member.getDisplayName()));
        karma.addText(HtmlTools.compressKarma(member.getKarma()));
        nameBox.add(htmlLink);
        nameBox.add(karma);
        content.add(nameBox);

        // Subtitle
        HtmlDiv subtitle = new HtmlDiv("actor-box-subtitle");
        content.add(subtitle);

        boolean isContributor = false;
        boolean isDeveloper = false;

        if (member.getOffers(false).size() > 0) {
            isDeveloper = true;

        }
        if (member.getContributions().size() > 0) {
            isContributor = true;
        }

        if (isContributor && isDeveloper) {
            subtitle.addText(Context.tr("Developer and contributor"));
        } else if (isContributor) {
            subtitle.addText(Context.tr("Contributor"));
        } else if (isDeveloper) {
            subtitle.addText(Context.tr("Developer"));
        }

        // Follow
        //content.add(new HtmlFollowActorButton(member));

        return box;
    }
}
