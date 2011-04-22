package com.bloatit.web.linkable.team;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.SendTeamInvitationActionUrl;
import com.bloatit.web.url.SendTeamInvitationPageUrl;

/**
 * <p>
 * A page to send invitations to teams
 * </p>
 */
@ParamContainer("invitation/send")
public class SendTeamInvitationPage extends LoggedPage {
    private final SendTeamInvitationPageUrl url;

    @RequestParam(conversionErrorMsg = @tr("I cannot find the team number: ''%value%''."))
    @ParamConstraint(optionalErrorMsg = @tr("You have to specify a team number."))
    private final Team team;

    public SendTeamInvitationPage(final SendTeamInvitationPageUrl url) {
        super(url);
        this.url = url;
        this.team = url.getTeam();
    }

    @Override
    public HtmlElement createRestrictedContent(final Member me) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        final HtmlDiv left = new HtmlDiv();
        layout.addLeft(left);

        final SendTeamInvitationActionUrl target = new SendTeamInvitationActionUrl(team);
        final HtmlForm form = new HtmlForm(target.urlString());
        left.add(form);
        final FieldData fieldData = target.getReceiverParameter().pickFieldData();
        final HtmlDropDown receiverInput = new HtmlDropDown(fieldData.getName(), Context.tr("Select a member"));
        form.add(receiverInput);
        for (final Member m : MemberManager.getAll()) {
            if (!m.equals(me)) {
                receiverInput.addDropDownElement(m.getId().toString(), m.getDisplayName());
            }
        }
        form.add(new HtmlSubmit(Context.tr("Submit")));
        return layout ;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("Your must be logged to send team invitations");
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Send team invitations");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return SendTeamInvitationPage.generateBreadcrumb(team);
    }

    public static Breadcrumb generateBreadcrumb(final Team team) {
        final Breadcrumb breadcrumb = TeamPage.generateBreadcrumb(team);
        breadcrumb.pushLink(new SendTeamInvitationPageUrl(team).getHtmlLink(tr("Send team invitation")));
        return breadcrumb;
    }
}
