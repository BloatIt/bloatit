package com.bloatit.web.linkable.money;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.common.Log;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Payline;
import com.bloatit.model.Payline.Reponse;
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
                process.setSuccessful();
            } catch (final TokenNotfoundException e) {
                Log.web().fatal("Token not found.", e);
            }
        } else if (ack.equals("cancel")) {
            try {
                final Reponse paymentDetails = payline.getPaymentDetails(token);
                String message = paymentDetails.getMessage().replace("\n", ". ");
                Log.framework().info("Payline transaction failure. (Reason: " + message + ")");
                session.notifyBad("Payment canceled. Reason : " + message + ".");
                payline.cancelPayement(token);
            } catch (final TokenNotfoundException e) {
                Log.web().fatal("Token not found.", e);
            }
        }

        final Url target = process.getParentProcess().endSubProcess(process);
        process.close();
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
