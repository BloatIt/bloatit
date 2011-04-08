package com.bloatit.web.linkable.money;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.common.Log;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.model.Payline;
import com.bloatit.model.Payline.TokenNotfoundException;
import com.bloatit.web.url.PaylineReturnActionUrl;

@ParamContainer("payline/doreturn")
public class PaylineReturnAction extends Action {

    @RequestParam(name = "token")
    @Optional
    private final String token;

    @RequestParam(name = "ack")
    private final String ack;

    @RequestParam(name = "process")
    @Optional
    private final PaylineProcess process;


    public PaylineReturnAction(final PaylineReturnActionUrl url) {
        super(url);
        token = url.getToken();
        ack = url.getAck();
        process = url.getProcess();
    }

    @Override
    protected Url doProcess() {
        final Payline payline = new Payline();
        if (ack.equals("ok")) {
            try {
                payline.validatePayment(token);
            } catch (final TokenNotfoundException e) {
                Log.web().fatal("Token not found.", e);
            }
        } else if (ack.equals("cancel")) {
            Context.getSession().notifyError(tr("Error in filling up your account."));
            try {
                payline.cancelPayement(token);
            } catch (final TokenNotfoundException e) {
                Log.web().fatal("Token not found.", e);
            }
        }

        if(process != null) {
            Url target = process.getParentProcess().endSubProcess(process);
            process.close();
            if(target != null) {
                return target;
            }
        }

        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected Url doProcessErrors() {
        Context.getSession().notifyError(tr("Error in filling up your account."));
        return Context.getSession().pickPreferredPage();
    }

}
