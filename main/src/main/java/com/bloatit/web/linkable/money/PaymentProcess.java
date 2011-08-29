//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.money;

import java.math.BigDecimal;
import java.util.Map.Entry;

import com.bloatit.common.Log;
import com.bloatit.framework.bank.MercanetAPI;
import com.bloatit.framework.bank.MercanetResponse;
import com.bloatit.framework.bank.MercanetAPI.PaymentMethod;
import com.bloatit.framework.bank.MercanetResponse.ResponseCode;
import com.bloatit.framework.bank.MercanetTransaction;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.mails.ElveosMail;
import com.bloatit.framework.model.ModelAccessor;
import com.bloatit.framework.utils.RandomString;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.SessionManager;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlString;
import com.bloatit.model.Actor;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.Configuration;
import com.bloatit.model.Member;
import com.bloatit.model.Payline;
import com.bloatit.model.Payline.Reponse;
import com.bloatit.model.Payline.TokenNotfoundException;
import com.bloatit.model.Payment;
import com.bloatit.model.Team;
import com.bloatit.model.managers.BankTransactionManager;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.actions.AccountProcess;
import com.bloatit.web.actions.WebProcess;
import com.bloatit.web.url.PaymentResponseActionUrl;
import com.bloatit.web.url.PaymentActionUrl;
import com.bloatit.web.url.PaymentAutoresponseActionUrl;
import com.bloatit.web.url.PaymentProcessUrl;

@ParamContainer(value = "payment/process", protocol = Protocol.HTTPS)
public class PaymentProcess extends WebProcess {

    @RequestParam
    private Actor<?> actor;

    @SuppressWarnings("unused")
    @RequestParam
    private final AccountProcess parentProcess;

    private boolean success = false;

    // Make the payment request.
    private final Payment payment = new Payment();

    private final PaymentProcessUrl url;
    private BankTransaction bankTransaction;
    private int mercanetTransactionId;

    public PaymentProcess(final PaymentProcessUrl url) {
        super(url);
        this.url = url;
        actor = url.getActor();
        parentProcess = url.getParentProcess();
    }

    public synchronized boolean isSuccessful() {
        return success;
    }

    @Override
    protected synchronized Url doProcess() {
        url.getParentProcess().addChildProcess(this);
        return new PaymentActionUrl(Context.getSession().getShortKey(), this);
    }

    @Override
    protected synchronized Url doProcessErrors() {
        return session.getLastVisitedPage();
    }

    @Override
    public synchronized void doLoad() {
        if (actor instanceof Member) {
            actor = MemberManager.getById(actor.getId());
        } else if (actor instanceof Team) {
            actor = TeamManager.getById(actor.getId());
        }

        if (bankTransaction != null) {
            bankTransaction = BankTransactionManager.getById(bankTransaction.getId());
        }
        // actor = (Actor<?>) DBRequests.getById(DaoActor.class,
        // actor.getId()).accept(new DataVisitorConstructor());
    }

    synchronized Url initiatePayment() {
        // Constructing the urls.
        final PaymentResponseActionUrl normalReturnActionUrl = new PaymentResponseActionUrl("ok", this);
        final PaymentResponseActionUrl cancelReturnActionUrl = new PaymentResponseActionUrl("cancel", this);
        final PaymentAutoresponseActionUrl autoResponseActionUrl = new PaymentAutoresponseActionUrl(this);

        String token = new RandomString(10).nextString();
        autoResponseActionUrl.setToken(token);
        SessionManager.storeTemporarySession(token, session);

        try {
            bankTransaction = payment.doPayment(actor, getAmount());
        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException("Not authorized", e);
        }

        mercanetTransactionId = Configuration.getInstance().getNextMercanetTransactionId();
        MercanetTransaction mercanetTransaction = MercanetAPI.createTransaction(mercanetTransactionId, parentProcess.getAmountToPay(), ""
                + bankTransaction.getId(), normalReturnActionUrl, cancelReturnActionUrl, autoResponseActionUrl);

        StringBuilder url = new StringBuilder(mercanetTransaction.getUrl());

        boolean firstParam = true;

        for (Entry<String, String> param : mercanetTransaction.getHiddenParameters(PaymentMethod.VISA).entrySet()) {
            if (firstParam) {
                url.append("?");
                firstParam = false;
            } else {
                url.append("&");
            }
            url.append(param.getKey());
            url.append("=");
            url.append(param.getValue());
        }

        return new UrlString(url.toString());
    }

    private BigDecimal getAmount() {
        return ((AccountProcess) getFather()).getAmountToPay();
    }

    synchronized void validatePayment(String data) throws UnauthorizedOperationException {

        MercanetResponse response = MercanetAPI.parseResponse(data);

        if (response.hasError()) {
            throw new BadProgrammerException("Failure during payment response procession. Transaction id: " + mercanetTransactionId
                    + ". Banktransaction: " + bankTransaction.getId() + ".Member:" + bankTransaction.getAuthor().getId());
        }

        if (response.getResponseCode() == ResponseCode.AUTHORISATION_ACCEPTED ) {

            payment.validatePayment(bankTransaction);

            // The payline process is critical. We must be sure the DB is
            // updated right NOW!
            ModelAccessor.flush();

            success = true;
            // Notify the user:
            final String valueStr = Context.getLocalizator().getCurrency(bankTransaction.getValue()).getSimpleEuroString();
            final String paidValueStr = Context.getLocalizator().getCurrency(bankTransaction.getValuePaid()).getTwoDecimalEuroString();
            session.notifyGood(Context.tr("Payment of {0} accepted.", paidValueStr));
            new ElveosMail.ChargingAccountSuccess(bankTransaction.getReference(), paidValueStr, valueStr);

        } else {
            /*payment.cancelPayement(bankTransaction);
            Log.payment().info("Mercanet transaction failure. (Reason: " + message + ")");
            session.notifyWarning("Payment canceled. Reason: " + message + ".");*/
        }
    }

    synchronized void refusePayment(String data) {
        // Log.framework().info("Payline transaction failure. (Reason: " +
        // message + ")");
        // session.notifyWarning("Payment canceled. Reason: " + message + ".");
        payment.cancelPayment(bankTransaction);
    }

    public synchronized String getPaymentReference() {
        try {
            return bankTransaction.getReference();
        } catch (final UnauthorizedOperationException e) {
            Log.web().fatal("Cannot find a reference.", e);
            return "Reference-not-Found";
        }
    }
}
