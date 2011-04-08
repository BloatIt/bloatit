package com.bloatit.web.linkable.money;

import com.bloatit.common.Log;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Payline;
import com.bloatit.model.Payline.Reponse;
import com.bloatit.model.Payline.TokenNotfoundException;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.PaylineNotifyActionUrl;

@ParamContainer("payline/donotify")
public final class PaylineNotifyAction extends Action {

    public static final String TOKEN_CODE = "token";

    @RequestParam(name = TOKEN_CODE)
    @Optional
    private final String token;

    @RequestParam(name = "process")
    private final PaylineProcess process;

    public PaylineNotifyAction(final PaylineNotifyActionUrl url) {
        super(url);
        token = url.getToken();
        process = url.getProcess();
    }

    @Override
    public Url doProcess() {
        Log.web().info("Get a payline notification: " + token);
        final Payline payline = new Payline();
        try {
            final Reponse paymentDetails = payline.getPaymentDetails(token);
            if (paymentDetails.isAccepted()) {
                payline.validatePayment(token);
            } else {
                // TODO send mail
                payline.cancelPayement(token);
                session.notifyBad("Your payment is refused.");
                Log.web().error("Payment is not accepted: " + token);
            }
        } catch (final TokenNotfoundException e) {
            Log.web().error("Token not found ! ", e);
        }

        final Url target = process.getParentProcess().endSubProcess(process);
        process.close();
        if (target != null) {
            return target;
        }

        return new IndexPageUrl();
    }

    @Override
    public Url doProcessErrors() {
        Log.web().error("Payline notification with parameter errors ! ");
        return new IndexPageUrl();
    }

    @Override
    protected Url checkRightsAndEverything() {
        return NO_ERROR; // Nothing else to check
    }

    @Override
    protected void transmitParameters() {
        // Nothing
    }
}
