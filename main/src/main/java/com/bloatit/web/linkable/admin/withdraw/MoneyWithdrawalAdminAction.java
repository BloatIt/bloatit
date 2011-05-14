package com.bloatit.web.linkable.admin.withdraw;

import com.bloatit.data.DaoMoneyWithdrawal.State;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.MoneyWithdrawal;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.admin.AdminAction;
import com.bloatit.web.url.MoneyWithdrawalAdminActionUrl;
import com.bloatit.web.url.MoneyWithdrawalAdminPageUrl;

@ParamContainer("admin/dowithdraw")
public class MoneyWithdrawalAdminAction extends AdminAction {
    @SuppressWarnings("unused")
    private final MoneyWithdrawalAdminActionUrl url;

    @RequestParam(name = "newState", role = Role.POST)
    private final State newState;

    @RequestParam(role = Role.GET)
    private final String backTo;

    @RequestParam(role = Role.GET)
    private final MoneyWithdrawal target;

    public MoneyWithdrawalAdminAction(final MoneyWithdrawalAdminActionUrl url) {
        super(url);
        this.url = url;
        this.newState = url.getNewState();
        this.target = url.getTarget();
        this.backTo = url.getBackTo();
    }

    @Override
    protected Url doProcessAdmin() throws UnauthorizedOperationException {
        State old;
        old = target.getState();

        target.setState(newState);
        session.notifyGood("Successfuly modified withdraw request from " + target.getActor().getDisplayName() + " of "
                + target.getAmountWithdrawn().toPlainString() + "€ from State " + old + " to state " + newState + ".");
        final MoneyWithdrawalAdminPageUrl back = new MoneyWithdrawalAdminPageUrl();
        back.setFilter(backTo);
        return back;
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors(final ElveosUserToken userToken) {
        if (target == null) {
            return new PageNotFoundUrl();
        }
        return new MoneyWithdrawalAdminPageUrl();
    }

    @Override
    protected void transmitParameters() {
    }
}
