package com.bloatit.web.actions;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.IndexPageUrl;
import com.bloatit.web.utils.url.PaylineNotifyActionUrl;
import com.bloatit.web.utils.url.Url;
import com.experian.payline.ws.impl.GetWebPaymentDetailsRequest;
import com.experian.payline.ws.impl.WebPaymentAPI_Service;
import com.experian.payline.ws.obj.Result;

@ParamContainer("payline/donotify")
public class PaylineNotifyAction extends LoggedAction {

    @RequestParam(name = "token", level = Level.INFO)
    private final String token;

    public PaylineNotifyAction(PaylineNotifyActionUrl url) {
        super(url);
        token = url.getToken();
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {

        System.out.println(token);
        System.out.println("notification !! ");

        WebPaymentAPI_Service paylineApi = new WebPaymentAPI_Service();
        GetWebPaymentDetailsRequest parameters = new GetWebPaymentDetailsRequest();
        parameters.setToken(token);
        Result result = paylineApi.getWebPaymentAPI().getWebPaymentDetails(parameters).getResult();

        System.out.println(result.getCode());

        return new IndexPageUrl();
    }

    @Override
    public Url doProcessErrors() throws RedirectException {
        System.out.println("ERROR notify ");
        return new IndexPageUrl();
    }

    @Override
    protected String getRefusalReason() {
        // TODO : set a valid error message
        return Context.tr("I don't know what happens in here");
    }

    @Override
    protected void transmitParameters() {
        // Nothing to transmit
    }
}
