package com.bloatit.web.actions;

import com.bloatit.common.Log;
import com.bloatit.framework.Payline;
import com.bloatit.framework.Payline.Reponse;
import com.bloatit.framework.Payline.TokenNotfoundException;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.utils.url.IndexPageUrl;
import com.bloatit.web.utils.url.PaylineNotifyActionUrl;
import com.bloatit.web.utils.url.Url;

@ParamContainer("payline/donotify")
public class PaylineNotifyAction extends Action {

    @RequestParam(name = "token", level = Level.INFO)
    private final String token;

    public PaylineNotifyAction(PaylineNotifyActionUrl url) {
        super(url);
        token = url.getToken();
    }

    @Override
    public Url doProcess() throws RedirectException {
        Log.web().info("Get a payline notification: " + token);
        Payline payline = new Payline();
        try {
            Reponse paymentDetails = payline.getPaymentDetails(token);
            if (paymentDetails.isAccepted()) {
                payline.validatePayment(token);
            } else {
                Log.web().error("Payment is not accepted: " + token);
            }
        } catch (TokenNotfoundException e) {
            Log.web().error("Token not found ! ", e);
        }
        return new IndexPageUrl();
    }

    @Override
    public Url doProcessErrors() throws RedirectException {
        Log.web().error("Payline notification with parameter errors ! ");
        return new IndexPageUrl();
    }
}
