package com.bloatit.web.linkable.money;

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.form.FormField;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.model.Member;
import com.bloatit.model.MoneyWithdrawal;
import com.bloatit.model.Team;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.linkable.team.TeamPage;
import com.bloatit.web.url.WithdrawMoneyActionUrl;
import com.bloatit.web.url.WithdrawMoneyPageUrl;

@ParamContainer("account/withdraw/docreate")
public class WithdrawMoneyAction extends LoggedElveosAction {
    private final WithdrawMoneyActionUrl url;

    @RequestParam(role = Role.GET)
    private final Actor<?> actor;

    @RequestParam(role = Role.POST)
    @MaxConstraint(max = 100000, message = @tr("Amount to withdraw must be smaller or equal than %constraint%."))
    @MinConstraint(min = 1, message = @tr("Amount to withdraw must be greater or equal than %constraint%."))
    @NonOptional(@tr("The amount is needed."))
    @FormField(label = @tr("Amount to withdraw"))
    private final BigDecimal amount;

    @RequestParam(role = Role.POST)
    @MinConstraint(min = 14, message = @tr("IBAN must be between 14 and 34 characters."))
    @MaxConstraint(max = 34, message = @tr("IBAN must be between 14 and 34 characters."))
    @NonOptional(@tr("Please specify your IBAN."))
    @FormField(label = @tr("Your IBAN"))
    private final String IBAN;

    public WithdrawMoneyAction(final WithdrawMoneyActionUrl url) {
        super(url);
        this.url = url;
        this.amount = url.getAmount();
        this.actor = url.getActor();
        this.IBAN = url.getIBAN();
    }

    @Override
    protected Url doProcessRestricted(final Member me) {
        new MoneyWithdrawal(actor, IBAN, amount);
        final String amountStr = Context.getLocalizator().getCurrency(amount).getSimpleEuroString();
        if (actor instanceof Member) {
            session.notifyGood(Context.tr("Requested to withdraw {0} from your account.", amountStr));
            return MemberPage.myAccountUrl(me);
        }
        session.notifyGood(Context.tr("Requested to withdraw {0} from team {1} account.", amountStr, ((Team) actor).getDisplayName()));
        return TeamPage.AccountUrl(((Team) actor));
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        if (actor instanceof Member) {
            if (!me.equals(actor)) {
                session.notifyWarning(Context.tr("You cannot withdraw money on someone else account."));
                return new WithdrawMoneyPageUrl(actor);
            }
            try {
                if (me.getInternalAccount().getAmount().compareTo(amount) < 0) {
                    session.notifyWarning(Context.tr("You cannot withdraw more money than you currently have on your account."));
                    return new WithdrawMoneyPageUrl(actor);
                }
            } catch (final UnauthorizedOperationException e) {
                throw new ShallNotPassException("Logged user cannot access his own internal account.");
            }
        } else {
            final Team t = (Team) actor;
            if (!me.hasBankTeamRight(t)) {
                session.notifyWarning(Context.tr("You cannot withdraw money on team {0} account.", t.getDisplayName()));
                return new WithdrawMoneyPageUrl(actor);
            }
            try {
                if (t.getInternalAccount().getAmount().compareTo(amount) < 0) {
                    session.notifyWarning(Context.tr("You cannot withdraw more money than the team currently have on her account."));
                    return new WithdrawMoneyPageUrl(actor);
                }
            } catch (final UnauthorizedOperationException e) {
                throw new ShallNotPassException("User cannot access team internal account balance while he has bank rights.");
            }
        }

        if (!MoneyWithdrawal.checkIban(IBAN)) {
            session.notifyWarning(Context.tr("Invalid IBAN."));
            return new WithdrawMoneyPageUrl(actor);
        }

        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        if (actor != null) {
            return new WithdrawMoneyPageUrl(actor);
        }
        return new PageNotFoundUrl();
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
