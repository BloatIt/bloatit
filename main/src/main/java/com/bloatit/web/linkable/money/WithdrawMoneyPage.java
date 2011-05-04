package com.bloatit.web.linkable.money;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlMoneyField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.AuthenticatedUserToken;
import com.bloatit.web.linkable.team.TeamPage;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.WithdrawMoneyActionUrl;
import com.bloatit.web.url.WithdrawMoneyPageUrl;

/**
 * Page used by teams and or members to withdraw money from their internal
 * account back to their physical bank account
 */
@ParamContainer("money/withdraw")
public class WithdrawMoneyPage extends LoggedPage {
    private final WithdrawMoneyPageUrl url;

    @RequestParam(role = Role.GET)
    private final Actor<?> actor;

    public WithdrawMoneyPage(WithdrawMoneyPageUrl url) {
        super(url);
        this.url = url;
        this.actor = url.getActor();
    }

    @Override
    public HtmlElement createRestrictedContent(Member loggedUser) throws RedirectException {
        TwoColumnLayout master = new TwoColumnLayout(true, url);
        master.addLeft(generateCore());

        return master;
    }

    /**
     * Creates the form used to input the amount to withdraw
     */
    private HtmlElement generateCore() {
        HtmlDiv master = new HtmlDiv();
        master.add(new HtmlTitle(1).addText(tr("Withdraw money")));

        WithdrawMoneyActionUrl targetUrl = new WithdrawMoneyActionUrl(actor);
        HtmlForm form = new HtmlForm(targetUrl.urlString());
        master.add(form);

        // Amount
        FieldData moneyData = targetUrl.getAmountParameter().pickFieldData();
        HtmlMoneyField moneyInput = new HtmlMoneyField(moneyData.getName(), tr("Amount to withdraw: "));
        if (moneyData.getSuggestedValue() != null && !moneyData.getSuggestedValue().isEmpty()) {
            moneyInput.setDefaultValue(moneyData.getSuggestedValue());
        } else {
            try {
                BigDecimal available = actor.getInternalAccount().getAmount();
                available.setScale(0);
                moneyInput.setDefaultValue("" + available.intValue());
            } catch (UnauthorizedOperationException e) {
                throw new ShallNotPassException("Cannot account amount.");
            }
        }
        form.add(moneyInput);

        // IBAN
        FieldData IBANData = targetUrl.getIBANParameter().pickFieldData();
        HtmlTextField ibanInput = new HtmlTextField(IBANData.getName(), tr("IBAN: "));
        ibanInput.setDefaultStringValue(IBANData.getSuggestedValue());
        ibanInput.turnOffAutoComplete();
        form.add(ibanInput);

        form.add(new HtmlSubmit(tr("Submit")));

        // Plop
        HtmlDiv testIban = new HtmlDiv();
        testIban.addText(Context.tr("You can test the page with the following IBAN: GB87 BARC 2065 8244 9716 55"));
        master.add(testIban);

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
    protected Breadcrumb createBreadcrumb(Member member) {
        if (isTeamAccount()) {
            return generateBreadcrumb((Team) actor);
        }
        return generateBreadcrumb(member);

    }

    protected static Breadcrumb generateBreadcrumb(final Member member) {
        final Breadcrumb breadcrumb = AccountPage.generateBreadcrumb(member);
        breadcrumb.pushLink(new WithdrawMoneyPageUrl(member).getHtmlLink(tr("Withdraw money")));
        return breadcrumb;
    }

    protected static Breadcrumb generateBreadcrumb(final Team team) {
        final Breadcrumb breadcrumb = TeamPage.generateBreadcrumb(team);
        breadcrumb.pushLink(new WithdrawMoneyPageUrl(team).getHtmlLink(tr("Account informations")));
        return breadcrumb;
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
