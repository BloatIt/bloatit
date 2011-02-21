package com.bloatit.web.components;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlListItem;
import com.bloatit.framework.webserver.components.HtmlRenderer;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.model.Group;
import com.bloatit.web.url.TeamPageUrl;

/**
 * A simple renderer for teams that display only their name on one line, plus a
 * link to their page
 */
public class TeamListRenderer implements HtmlRenderer<Group> {
    @Override
    public XmlNode generate(final Group team) {
        final TeamPageUrl teamUrl = new TeamPageUrl(team);
        try {
            HtmlLink htmlLink;
            htmlLink = teamUrl.getHtmlLink(team.getLogin());

            return new HtmlListItem(htmlLink);
        } catch (final UnauthorizedOperationException e) {
            Log.web().warn("Right error on Team list renderer", e);
        }
        return new PlaceHolderElement();
    }
}
