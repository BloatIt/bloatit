package com.bloatit.web.components;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlRenderer;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.advanced.HtmlClearer;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.model.Team;
import com.bloatit.web.linkable.team.TeamTools;
import com.bloatit.web.url.TeamPageUrl;

/**
 * A simple renderer for teams that display only their name on one line, plus a
 * link to their page
 */
public class TeamListRenderer implements HtmlRenderer<Team> {
    @Override
    public XmlNode generate(final Team team) {
        final TeamPageUrl teamUrl = new TeamPageUrl(team);
        try {
            HtmlDiv box = new HtmlDiv("team_box");

            box.add(new HtmlDiv("float_right").add(TeamTools.getTeamAvatar(team)));

            HtmlDiv textBox = new HtmlDiv("team_text");
            HtmlLink htmlLink;
            htmlLink = teamUrl.getHtmlLink(team.getLogin());

            textBox.add(htmlLink);
            box.add(textBox);
            box.add(new HtmlClearer());

            return box;
        } catch (final UnauthorizedOperationException e) {
            Log.web().warn("Right error on Team list renderer", e);
        }
        return new PlaceHolderElement();
    }
}
