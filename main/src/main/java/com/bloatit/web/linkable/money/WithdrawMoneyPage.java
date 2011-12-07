package com.bloatit.web.linkable.money;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlMoneyField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.model.Actor;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.linkable.team.TeamPage;
import com.bloatit.web.url.WithdrawMoneyActionUrl;
import com.bloatit.web.url.WithdrawMoneyPageUrl;

/**
 * Page used by teams and or members to withdraw money from their internal
 * account back to their physical bank account
 */
@ParamContainer("account/withdraw/create")
public class WithdrawMoneyPage extends LoggedElveosPage {
    private final WithdrawMoneyPageUrl url;

    @RequestParam(role = Role.GET)
    private final Actor<?> actor;

    public WithdrawMoneyPage(final WithdrawMoneyPageUrl url) {
        super(url);
        this.url = url;
        this.actor = url.getActor();
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        final TwoColumnLayout master = new TwoColumnLayout(true, url);
        master.addLeft(generateCore());

        return master;
    }

    /**
     * Creates the form used to input the amount to withdraw
     */
    private HtmlElement generateCore() {
        final HtmlDiv master = new HtmlDiv();
        master.add(new HtmlTitle(1).addText(tr("Withdraw money")));

        final WithdrawMoneyActionUrl targetUrl = new WithdrawMoneyActionUrl(getSession().getShortKey(), actor);
        final HtmlElveosForm form = new HtmlElveosForm(targetUrl.urlString());
        master.add(form);
        FormBuilder ftool = new FormBuilder(WithdrawMoneyAction.class, targetUrl);

        // Amount
        HtmlMoneyField money = new HtmlMoneyField(targetUrl.getAmountParameter().getName());
        ftool.add(form, money);
        try {
            final BigDecimal available = actor.getInternalAccount().getAmount();
            ftool.setDefaultValueIfNeeded(money, String.valueOf(available.setScale(0).intValue()));
        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException("Cannot account amount.");
        }

        ftool.add(form, new HtmlTextField(targetUrl.getIBANParameter().getName()));
        form.addSubmit(new HtmlSubmit(tr("Submit")));
        return master;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to withdraw money.");
    }

    @Override
    protected String createPageTitle() {
        return tr("Withdraw money");
    }

    private boolean isTeamAccount() {
        return (actor instanceof Team);
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        if (isTeamAccount()) {
            return generateBreadcrumb((Team) actor);
        }
        return generateBreadcrumb(member);

    }

    protected static Breadcrumb generateBreadcrumb(final Member member) {
        final Breadcrumb breadcrumb = MemberPage.generateAccountBreadcrumb(member);
        breadcrumb.pushLink(new WithdrawMoneyPageUrl(member).getHtmlLink(tr("Withdraw money")));
        return breadcrumb;
    }

    protected static Breadcrumb generateBreadcrumb(final Team team) {
        final Breadcrumb breadcrumb = TeamPage.generateAccountBreadcrumb(team);
        breadcrumb.pushLink(new WithdrawMoneyPageUrl(team).getHtmlLink(tr("Withdraw money")));
        return breadcrumb;
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
