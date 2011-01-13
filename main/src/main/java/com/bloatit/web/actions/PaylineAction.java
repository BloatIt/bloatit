package com.bloatit.web.actions;

import java.math.BigDecimal;

import com.bloatit.common.UnauthorizedOperationException;
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
public final class PaylineAction extends LoggedAction {

    public static final String CHARGE_AMOUNT_CODE = "amount";

    @RequestParam(level = Level.ERROR, name = CHARGE_AMOUNT_CODE, role = RequestParam.Role.POST)
    private final BigDecimal amount;

    public PaylineAction(PaylineActionUrl url) {
        super(url);
        amount = url.getAmount();
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        // Constructing the urls.
        HttpHeader header = Context.getHeader();
        String returnUrl = "http://" + header.getHttpHost() + new PaylinePageUrl("ok").urlString();
        String cancelUrl = "http://" + header.getHttpHost() + new PaylinePageUrl("cancel").urlString();
        String notificationUrl = "http://" + header.getHttpHost() + new PaylineNotifyActionUrl().urlString();

        // Make the payment request.
        Payline payline = new Payline();
        payline.authenticate(Context.getSession().getAuthToken());
        if (payline.canMakePayment()) {
            Reponse reponse;
            try {
                reponse = payline.doPayment(amount, cancelUrl, returnUrl, notificationUrl);
                if (reponse.isAccepted()) {
                    return new UrlStringBinder(reponse.getRedirectUrl());
                }
                session.notifyBad(reponse.getMessage());
            } catch (UnauthorizedOperationException e) {
                session.notifyBad("Unauthorized !");
            }
            return Context.getSession().pickPreferredPage();
        }
        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
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
