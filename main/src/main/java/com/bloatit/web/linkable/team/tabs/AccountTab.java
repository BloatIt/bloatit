package com.bloatit.web.linkable.team.tabs;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock.HtmlTab;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.model.Team;
import com.bloatit.web.components.AccountComponent;

public class AccountTab extends HtmlTab {
    private final Team team;

    public AccountTab(Team team, String title, String tabKey) {
        super(title, tabKey);
        this.team = team;
    }

    @Override
    public XmlNode generateBody() {
        if (!team.hasTeamPrivilege(UserTeamRight.BANK)) {
            throw new ShallNotPassException("You cannot access team bank information.");
        }

        HtmlDiv master = new HtmlDiv("tab_pane");

        try {
            master.add(new AccountComponent(team));
        } catch (UnauthorizedOperationException e) {
            e.printStackTrace();
        }

        return master;
    }
}
