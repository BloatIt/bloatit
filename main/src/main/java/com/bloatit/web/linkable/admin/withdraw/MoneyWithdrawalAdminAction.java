package com.bloatit.web.linkable.admin.withdraw;

import com.bloatit.data.DaoMoneyWithdrawal.State;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.MoneyWithdrawal;
import com.bloatit.web.linkable.admin.AdminAction;
import com.bloatit.web.url.MoneyWithdrawalAdminActionUrl;
import com.bloatit.web.url.MoneyWithdrawalAdminPageUrl;

@ParamContainer("admin/dowithdraw")
public class MoneyWithdrawalAdminAction extends AdminAction {
    private final MoneyWithdrawalAdminActionUrl url;

    @RequestParam(name = "newState", role = Role.POST)
    private final State newState;

    @RequestParam(role = Role.GET)
    private final MoneyWithdrawal target;

    public MoneyWithdrawalAdminAction(MoneyWithdrawalAdminActionUrl url) {
        super(url);
        this.url = url;
        this.newState = url.getNewState();
        this.target = url.getTarget();
    }

    @Override
    protected Url doProcessAdmin() {
        State old = target.getState();
        target.setState(newState);
        session.notifyGood("Successfuly modified withdraw request from " + target.getActor().getDisplayName() + " of "
                + target.getAmountWithdrawn().toPlainString() + "€ from State " + old + " to state " + newState + ".");
        return new MoneyWithdrawalAdminPageUrl();
    }

    @Override
    protected Url doCheckRightsAndEverything(Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        session.notifyBad("Plop");
        if (target == null) {
            return new PageNotFoundUrl();
        }
        return new MoneyWithdrawalAdminPageUrl();
    }

    @Override
    protected void transmitParameters() {
    }
}
