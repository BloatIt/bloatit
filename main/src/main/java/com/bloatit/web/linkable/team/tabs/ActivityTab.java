package com.bloatit.web.linkable.team.tabs;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock.HtmlTab;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.Session;
import com.bloatit.model.Team;

public class ActivityTab extends HtmlTab{
    private final Team team;
    private final Session session = Context.getSession();

    public ActivityTab(Team team, String title, String tabKey) {
        super(title, tabKey);
        this.team = team;
    }

    @Override
    public XmlNode generateBody() {
        return new HtmlDiv("tab_pane");
    }
}