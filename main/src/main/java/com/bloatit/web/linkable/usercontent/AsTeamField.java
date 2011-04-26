package com.bloatit.web.linkable.usercontent;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.Action;
import com.bloatit.web.url.UserContentActionUrl;

public class AsTeamField extends PlaceHolderElement {

    private HtmlDropDown teamInput;

    public AsTeamField(final UserContentActionUrl targetUrl, final Member me, final UserTeamRight right, final String label, final String comment) {
        super();
        if (me != null && me.canAccessTeams(Action.READ)) {
            try {
                final PageIterable<Team> teams = me.getTeams();
                final FieldData teamData = targetUrl.getTeamParameter().pickFieldData();
                teamInput = new HtmlDropDown(teamData.getName(), label);
                getTeamInput().addErrorMessages(teamData.getErrorMessages());
                getTeamInput().setComment(comment);
                getTeamInput().addDropDownElement("", Context.tr("Myself"));
                int nbTeam = 0;
                for (final Team team : teams) {
                    if (team.getUserTeamRight(me).contains(right)) {
                        getTeamInput().addDropDownElement(team.getId().toString(), team.getDisplayName());
                        nbTeam++;
                    }
                }
                getTeamInput().setDefaultValue(teamData.getSuggestedValue());
                if (nbTeam > 0) {
                    add(getTeamInput());
                }
            } catch (final UnauthorizedOperationException e) {
                Context.getSession().notifyError(Context.tr("An error prevented us from displaying you some information. Please notify us."));
                throw new ShallNotPassException("Can't access current user teams (I checked before tho)", e);
            }
        }
    }

    public HtmlDropDown getTeamInput() {
        return teamInput;
    }

}
