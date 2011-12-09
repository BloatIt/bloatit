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
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Team;
import com.bloatit.web.linkable.team.TeamTools;
import com.bloatit.web.url.TeamPageUrl;

/**
 * A simple renderer for teams that display only their name on one line, plus a
 * link to their page
 */
public class TeamListRenderer implements HtmlRenderer<Team> {
    @Override
    public HtmlNode generate(final Team team) {
        final TeamPageUrl teamUrl = new TeamPageUrl(team);
        final HtmlDiv box = new HtmlDiv("actor-box");

        box.add(new HtmlDiv("actor-box-avatar").add(TeamTools.getTeamAvatar(team)));

        final HtmlDiv content = new HtmlDiv("actor-box-content");
        box.add(content);

        // Name
        final HtmlDiv nameBox = new HtmlDiv("actor-box-actor-name");
        HtmlLink htmlLink;
        htmlLink = teamUrl.getHtmlLink(team.getDisplayName());
        htmlLink.setCssClass("team-link");
        nameBox.add(htmlLink);
        content.add(nameBox);

        // Subtitle
        final HtmlDiv subtitle = new HtmlDiv("actor-box-subtitle");
        content.add(subtitle);
        subtitle.addText(Context.trn("{0} member", "{0} members", team.getMembers().size(), team.getMembers().size()));

        // Follow
        // content.add(new HtmlFollowActorButton(team));

        return box;
    }
}
