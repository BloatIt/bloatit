package com.bloatit.web.actions;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Payline;
import com.bloatit.model.Payline.TokenNotfoundException;
import com.bloatit.web.url.PaylineReturnActionUrl;

@ParamContainer("payline/doreturn")
public class PaylineReturnAction extends Action {

    @RequestParam(name = "token", level = Level.INFO)
    private final String token;

    @RequestParam(name = "ack")
    private final String ack;

    public PaylineReturnAction(final PaylineReturnActionUrl url) {
        super(url);
        token = url.getToken();
        ack = url.getAck();
    }

    @Override
    protected Url doProcess() throws RedirectException {
        final Payline payline = new Payline();
        if (ack.equals("ok")) {
            Context.getSession().notifyGood(tr("Your account has been credited."));
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
        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        Context.getSession().notifyError(tr("Error in filling up your account."));
        return Context.getSession().pickPreferredPage();
    }

}
