package com.bloatit.web.linkable.money;

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.MoneyWithdrawal;
import com.bloatit.model.Team;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.linkable.team.TeamPage;
import com.bloatit.web.url.AccountPageUrl;
import com.bloatit.web.url.TeamPageUrl;
import com.bloatit.web.url.WithdrawMoneyActionUrl;
import com.bloatit.web.url.WithdrawMoneyPageUrl;

@ParamContainer("money/dowithdraw")
public class WithdrawMoneyAction extends LoggedAction {
    private final WithdrawMoneyActionUrl url;

    @RequestParam(role = Role.GET)
    private final Actor<?> actor;

    @RequestParam(role = Role.POST)
    @ParamConstraint(min = "1", minErrorMsg = @tr("Amount to withdraw must be greater or equal to %constraint%."), //
    max = "100000", maxErrorMsg = @tr("Amount to withdraw and must be lesser or equal than %constraint%."))
    private final BigDecimal amount;

    @RequestParam(role = Role.POST)
    @ParamConstraint(min = "14", minErrorMsg = @tr("IBAN must be between 14 and 34 characters."), //
    max = "34", maxErrorMsg = @tr("IBAN must be between 14 and 34 characters."))
    private final String IBAN;

    public WithdrawMoneyAction(WithdrawMoneyActionUrl url) {
        super(url);
        this.url = url;
        this.amount = url.getAmount();
        this.actor = url.getActor();
        this.IBAN = url.getIBAN();
    }

    @Override
    protected Url doProcessRestricted(Member me) {
        new MoneyWithdrawal(actor, IBAN, amount);
        String amountStr = Context.getLocalizator().getCurrency(amount).getSimpleEuroString();
        if (actor instanceof Member) {
            session.notifyGood(Context.tr("Requested to withdraw {0} from your account.", amountStr));
            return new AccountPageUrl();
        } else {
            session.notifyGood(Context.tr("Requested to withdraw {0} from team {1} account.", amountStr, ((Team) actor).getDisplayName()));
            TeamPageUrl teamPageUrl = new TeamPageUrl((Team) actor);
            teamPageUrl.setActiveTabKey(TeamPage.ACCOUNT_TAB);
            return teamPageUrl;
        }
    }

    @Override
    protected Url checkRightsAndEverything(Member me) {
        if (actor instanceof Member) {
            if (!me.equals(actor)) {
                session.notifyBad(Context.tr("You cannot withdraw money on someone else account."));
                return new WithdrawMoneyPageUrl(actor);
            }
            try {
                if (me.getInternalAccount().getAmount().compareTo(amount) < 0) {
                    session.notifyBad(Context.tr("You cannot withdraw more money than you currently have on your account."));
                    return new WithdrawMoneyPageUrl(actor);
                }
            } catch (UnauthorizedOperationException e) {
                throw new ShallNotPassException("Logged user cannot access his own internal account.");
            }
        } else {
            Team t = (Team) actor;
            if (!me.hasBankTeamRight(t)) {
                session.notifyBad(Context.tr("You cannot withdraw money on team {0} account.", t.getDisplayName()));
                return new WithdrawMoneyPageUrl(actor);
            }
            try {
                if (t.getInternalAccount().getAmount().compareTo(amount) < 0) {
                    session.notifyBad(Context.tr("You cannot withdraw more money than the team currently have on her account."));
                    return new WithdrawMoneyPageUrl(actor);
                }
            } catch (UnauthorizedOperationException e) {
                throw new ShallNotPassException("User cannot access team internal account balance while he has bank rights.");
            }
        }

        if (!MoneyWithdrawal.checkIban(IBAN)) {
            session.notifyBad(Context.tr("Invalid IBAN."));
            return new WithdrawMoneyPageUrl(actor);
        }

        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors(ElveosUserToken userToken) {
        if (actor != null) {
            return new WithdrawMoneyPageUrl(actor);
        } else {
            return new PageNotFoundUrl();
        }
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to withdraw money.");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getAmountParameter());
        session.addParameter(url.getIBANParameter());
    }
}
