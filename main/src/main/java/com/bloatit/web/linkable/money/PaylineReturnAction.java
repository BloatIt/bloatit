package com.bloatit.web.linkable.money;

import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.url.PaylineReturnActionUrl;

@ParamContainer("payline/doreturn")
public final class PaylineReturnAction extends Action {

    @RequestParam(name = "token")
    @Optional
    private final String token;

    @RequestParam(name = "ack")
    private final String ack;

    @RequestParam(name = "process")
    private final PaylineProcess process;

    public PaylineReturnAction(final PaylineReturnActionUrl url) {
        super(url);
        token = url.getToken();
        ack = url.getAck();
        process = url.getProcess();
    }

    @Override
    protected Url doProcess() {
        if (ack.equals("ok")) {
            process.validatePayment(token);
        } else if (ack.equals("cancel")) {
            process.refusePayment(token);
        }
        final Url target = process.close();
        if (target != null) {
            return target;
        }
        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected Url doProcessErrors() {
        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected Url checkRightsAndEverything() {
        return NO_ERROR; // Nothing else to check
    }

    @Override
    protected void transmitParameters() {
        // No post parameters.
    }

}
