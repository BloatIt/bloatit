package com.bloatit.web.actions;

import static com.bloatit.framework.webserver.Context.tr;

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.framework.webserver.url.UrlStringBinder;
import com.bloatit.framework.xcgiserver.HttpHeader;
import com.bloatit.model.Payline;
import com.bloatit.model.Payline.Reponse;
import com.bloatit.web.url.PaylineActionUrl;
import com.bloatit.web.url.PaylineNotifyActionUrl;
import com.bloatit.web.url.PaylinePageUrl;

@ParamContainer("paylinedopayment")
public final class PaylineAction extends LoggedAction {

    public static final String CHARGE_AMOUNT_CODE = "amount";

    @RequestParam(level = Level.ERROR, name = CHARGE_AMOUNT_CODE, role = RequestParam.Role.POST)
    private final BigDecimal amount;

    public PaylineAction(final PaylineActionUrl url) {
        super(url);
        amount = url.getAmount();
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        // Constructing the urls.
        final HttpHeader header = Context.getHeader();
        final String returnUrl = "http://" + header.getHttpHost() + new PaylinePageUrl("ok").urlString();
        final String cancelUrl = "http://" + header.getHttpHost() + new PaylinePageUrl("cancel").urlString();
        final String notificationUrl = "http://" + header.getHttpHost() + new PaylineNotifyActionUrl().urlString();

        // Make the payment request.
        final Payline payline = new Payline();
        payline.authenticate(Context.getSession().getAuthToken());
        if (payline.canMakePayment()) {
            Reponse reponse;
            try {
                reponse = payline.doPayment(amount, cancelUrl, returnUrl, notificationUrl);
                if (reponse.isAccepted()) {
                    return new UrlStringBinder(reponse.getRedirectUrl());
                }
                session.notifyBad(reponse.getMessage());
            } catch (final UnauthorizedOperationException e) {
                session.notifyBad(tr("Unauthorized !"));
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
