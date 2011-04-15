package com.bloatit.web.linkable.team;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.PageNotFoundException;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.url.JoinTeamActionUrl;
import com.bloatit.web.url.JoinTeamPageUrl;

@ParamContainer("team/join")
public final class JoinTeamPage extends LoggedPage {
    private JoinTeamPageUrl url;

    @RequestParam(name = "target", conversionErrorMsg = @tr("I cannot find the team number: ''%value''."))
    @ParamConstraint(optionalErrorMsg = @tr("You have to specify a team number."))
    private Team targetTeam;

    public JoinTeamPage(final JoinTeamPageUrl url) {
        super(url);
    }

    @Override
    public void processErrors() throws RedirectException {
        if (!url.getMessages().isEmpty()) {
            throw new PageNotFoundException();
        }
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        final HtmlDiv master = new HtmlDiv("padding_box");

        final HtmlForm form = new HtmlForm(new JoinTeamActionUrl(targetTeam).urlString());
        master.add(form);

        form.add(new HtmlSubmit(Context.tr("send")));
        // HtmlTextArea justification = new HtmlTextArea("", rows, cols)

        return master;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged before you try to join a team.");
    }

    @Override
    protected String createPageTitle() {
        if (targetTeam.isPublic()) {
            return Context.tr("Join a team");
        } else {
            return Context.tr("Request to join a team");
        }
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return JoinTeamPage.generateBreadcrumb(targetTeam);
    }

    public static Breadcrumb generateBreadcrumb(final Team team) {
        final Breadcrumb breadcrumb = TeamPage.generateBreadcrumb(team);

        breadcrumb.pushLink(new JoinTeamPageUrl(team).getHtmlLink(tr("Join team")));

        return breadcrumb;
    }

}
