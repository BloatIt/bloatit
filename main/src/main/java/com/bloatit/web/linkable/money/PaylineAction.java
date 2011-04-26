package com.bloatit.web.linkable.money;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.PaylineActionUrl;

@ParamContainer(value="paylinedopayment", protocol=Protocol.HTTPS)
public final class PaylineAction extends LoggedAction {

    @RequestParam
    private final PaylineProcess process;

    public PaylineAction(final PaylineActionUrl url) {
        super(url);
        process = url.getProcess();
    }

    @Override
    public Url doProcessRestricted(final Member me) {
        return process.initiatePayment();
    }

    @Override
    protected Url doCheckRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to charge your account.");
    }

    @Override
    protected void transmitParameters() {
        // Nothing to transmit
    }
}
