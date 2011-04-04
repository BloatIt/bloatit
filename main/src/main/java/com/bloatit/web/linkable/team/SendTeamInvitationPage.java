package com.bloatit.web.linkable.team;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlHidden;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.url.SendTeamInvitationActionUrl;
import com.bloatit.web.url.SendTeamInvitationPageUrl;

/**
 * <p>
 * A page to send invitations to teams
 * </p>
 */
@ParamContainer("invitation/send")
public class SendTeamInvitationPage extends LoggedPage {
    @SuppressWarnings("unused")
    private final SendTeamInvitationPageUrl url;

    @RequestParam
    private final Team team;

    public SendTeamInvitationPage(final SendTeamInvitationPageUrl url) {
        super(url);
        this.url = url;
        this.team = url.getTeam();
    }


    @Override
    public void processErrors() throws RedirectException {
    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {
        final HtmlDiv master = new HtmlDiv("padding_box");

        final SendTeamInvitationActionUrl target = new SendTeamInvitationActionUrl();
        final HtmlForm form = new HtmlForm(target.urlString());
        master.add(form);

        final Member me = session.getAuthToken().getMember();
        me.authenticate(session.getAuthToken());

        try {
            if (team == null) {
                final HtmlDropDown teamInput = new HtmlDropDown(SendTeamInvitationAction.TEAM_JOIN_CODE, Context.tr("Select team"));
                form.add(teamInput);
                PageIterable<Team> teams;
                teams = me.getTeams();
                for (final Team g : teams) {
                    try {
                        teamInput.addDropDownElement(g.getId().toString(), g.getLogin());
                    } catch (final UnauthorizedOperationException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                final HtmlHidden hiddenTeam = new HtmlHidden(SendTeamInvitationAction.TEAM_JOIN_CODE, team.getId().toString());
                form.add(hiddenTeam);
            }

            final HtmlDropDown receiverInput = new HtmlDropDown(SendTeamInvitationAction.RECEIVER_CODE, Context.tr("Select team"));
            form.add(receiverInput);
            for (final Member m : MemberManager.getAll()) {
                try {
                    if (!m.equals(me)) {
                        receiverInput.addDropDownElement(m.getId().toString(), m.getLogin());
                    }
                } catch (final UnauthorizedOperationException e) {
                    // TODO
                    throw new FatalErrorException("TODO", e);
                }
            }

            form.add(new HtmlSubmit(Context.tr("Submit")));

            return master;

        } catch (final UnauthorizedOperationException e1) {
            // TODO
            throw new FatalErrorException("TODO", e1);
        }
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("Your must be logged to send team invitations");
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Send team invitations");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return SendTeamInvitationPage.generateBreadcrumb(team);
    }

    public static Breadcrumb generateBreadcrumb(final Team team) {
        final Breadcrumb breadcrumb = TeamPage.generateBreadcrumb(team);

        breadcrumb.pushLink(new SendTeamInvitationPageUrl(team).getHtmlLink(tr("Send team invitation")));

        return breadcrumb;
    }
}
