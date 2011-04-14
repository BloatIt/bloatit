package com.bloatit.web.linkable.money;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.SessionManager;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlString;
import com.bloatit.framework.xcgiserver.HttpHeader;
import com.bloatit.model.Member;
import com.bloatit.model.Payline;
import com.bloatit.model.Payline.Reponse;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.PaylineActionUrl;
import com.bloatit.web.url.PaylineNotifyActionUrl;
import com.bloatit.web.url.PaylineReturnActionUrl;

@ParamContainer("paylinedopayment")
public final class PaylineAction extends LoggedAction {

    @RequestParam
    private final PaylineProcess process;

    public PaylineAction(final PaylineActionUrl url) {
        super(url);
        process = url.getProcess();
    }

    @Override
    public Url doProcessRestricted(final Member authenticatedMember) {
        // Constructing the urls.
        final HttpHeader header = Context.getHeader().getHttpHeader();
        final PaylineReturnActionUrl paylineReturnActionUrl = new PaylineReturnActionUrl("ok", process);
        paylineReturnActionUrl.setProcess(process);
        final String returnUrl = paylineReturnActionUrl.externalUrlString(header);
        final PaylineReturnActionUrl paylineReturnActionUrlCancel = new PaylineReturnActionUrl("cancel", process);
        paylineReturnActionUrlCancel.setProcess(process);
        final String cancelUrl = paylineReturnActionUrlCancel.externalUrlString(header);
        final PaylineNotifyActionUrl paylineNotifyActionUrl = new PaylineNotifyActionUrl(process);
        paylineNotifyActionUrl.setProcess(process);
        final String notificationUrl = paylineNotifyActionUrl.externalUrlString(header);

        // Make the payment request.
        final Payline payline = new Payline();
        if (payline.canMakePayment()) {
            Reponse reponse;
            try {

                reponse = payline.doPayment(process.getAmount(), cancelUrl, returnUrl, notificationUrl);
                SessionManager.storeTemporarySession(reponse.getToken(), session);

                if (reponse.isAccepted()) {
                    return new UrlString(reponse.getRedirectUrl());
                }
                session.notifyBad(reponse.getMessage());
            } catch (final UnauthorizedOperationException e) {
                throw new ShallNotPassException("Not authorized", e);
            }
            return Context.getSession().pickPreferredPage();
        }
        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected Url doCheckRightsAndEverything(final Member authenticatedMember) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
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
