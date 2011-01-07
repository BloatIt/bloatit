package com.bloatit.web.actions;

import com.bloatit.framework.Payline;
import com.bloatit.framework.Payline.Reponse;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.HttpHeader;
import com.bloatit.web.utils.url.PaylineActionUrl;
import com.bloatit.web.utils.url.PaylineNotifyActionUrl;
import com.bloatit.web.utils.url.PaylinePageUrl;
import com.bloatit.web.utils.url.Url;
import com.bloatit.web.utils.url.UrlStringBinder;

@ParamContainer("payline/dopayment")
public class PaylineAction extends Action {

    public static final String CHARGE_AMOUNT_CODE = "amount";

    @RequestParam(level = Level.ERROR, name = CHARGE_AMOUNT_CODE, role = RequestParam.Role.POST)
    Integer amount;

    public PaylineAction(PaylineActionUrl url) {
        super(url);
        amount = url.getAmount();
    }

    @Override
    protected Url doProcess() throws RedirectException {

        HttpHeader header = Context.getHeader();

        StringBuilder sb = new StringBuilder();
        sb.append("http://");
        sb.append(header.getHttpHost());
        sb.append("/payline/result");

        String returnUrl = "http://" + header.getHttpHost() + new PaylinePageUrl("ok").urlString();
        String cancelUrl = "http://" + header.getHttpHost() + new PaylinePageUrl("cancel").urlString();
        String notificationUrl = "http://" + header.getHttpHost() + new PaylineNotifyActionUrl().urlString();

        System.err.println(notificationUrl);

        Payline payline = new Payline(cancelUrl, returnUrl, notificationUrl);

        payline.authenticate(Context.getSession().getAuthToken());

        if (payline.canMakePayment()){
            Reponse beginPayment = payline.beginPayment(amount);
            return new UrlStringBinder(beginPayment.getRedirect());
        }
        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        return Context.getSession().pickPreferredPage();
    }

}
