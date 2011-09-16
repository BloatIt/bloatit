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
import com.bloatit.data.DaoBankTransaction.State;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.bank.MercanetAPI;
import com.bloatit.framework.bank.MercanetAPI.PaymentMethod;
import com.bloatit.framework.bank.MercanetResponse;
import com.bloatit.framework.bank.MercanetTransaction;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.highlevel.MeanUserException;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.utils.RandomString;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.SessionManager;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlString;
import com.bloatit.model.Actor;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.Configuration;
import com.bloatit.model.Member;
import com.bloatit.model.Payment;
import com.bloatit.model.Team;
import com.bloatit.model.managers.BankTransactionManager;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.actions.AccountProcess;
import com.bloatit.web.actions.WebProcess;
import com.bloatit.web.url.PaymentActionUrl;
import com.bloatit.web.url.PaymentAutoresponseActionUrl;
import com.bloatit.web.url.PaymentProcessUrl;
import com.bloatit.web.url.PaymentResponseActionUrl;

@ParamContainer(value = "payment/process", protocol = Protocol.HTTPS)
public class PaymentProcess extends WebProcess {

    @RequestParam
    private Actor<?> actor;

    @RequestParam
    @NonOptional(@tr("The process is closed, expired, missing or invalid."))
    private final AccountProcess parentProcess;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You must choose a payment method."))
    private final PaymentMethod paymentMethod;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You must accept the terms of sales to continue."))
    private final Boolean tos;

    private boolean success = false;
    private boolean badParams = false;

    private final PaymentProcessUrl url;
    private BankTransaction bankTransaction;
    private int mercanetTransactionId;

    public PaymentProcess(final PaymentProcessUrl url) {
        super(url);
        this.url = url;
        actor = url.getActor();
        parentProcess = url.getParentProcess();
        paymentMethod = url.getPaymentMethod();
        tos = url.getTos();
    }

    public synchronized boolean isSuccessful() {
        return success;
    }

    @Override
    protected synchronized Url doProcess() {

        if(!FrameworkConfiguration.isMercanetEnabled()) {
            session.notifyError(Context.tr("The payment system is temporarily disabled. Please retry later."));
            return session.getLastStablePage();
        }
        
        url.getParentProcess().addChildProcess(this);

        if (tos == null || !tos.booleanValue()) {
            session.notifyWarning(Context.tr("You must accept the terms of sales to continue."));
            transmitParameters();
            badParams = true;
            return close();

        }

        return new PaymentActionUrl(Context.getSession().getShortKey(), this);
    }

    @Override
    protected synchronized Url doProcessErrors() {
        if (parentProcess != null) {
            url.getParentProcess().addChildProcess(this);
            badParams = true;
            return close();
        }
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
    }

    synchronized Url initiatePayment() {
        // Constructing the urls.
        final PaymentResponseActionUrl normalReturnActionUrl = new PaymentResponseActionUrl(this);
        final PaymentResponseActionUrl cancelReturnActionUrl = new PaymentResponseActionUrl(this);

        String token = new RandomString(10).nextString();
        final PaymentAutoresponseActionUrl autoResponseActionUrl = new PaymentAutoresponseActionUrl(token);
        autoResponseActionUrl.setProcess(this);

        SessionManager.storeTemporarySession(token, session);
        MercanetTransaction mercanetTransaction;
        try {
            bankTransaction = Payment.doPayment(actor, getAmount());

            String contact = "team:" + bankTransaction.getAuthor().getLogin();

            if (!bankTransaction.getAuthor().isTeam()) {

                contact = ((Member) bankTransaction.getAuthor()).getEmail();
            }

            mercanetTransactionId = Configuration.getInstance().getNextMercanetTransactionId();
            mercanetTransaction = MercanetAPI.createTransaction(mercanetTransactionId,
                                                                bankTransaction.getValuePaid(),
                                                                "" + bankTransaction.getId(),
                                                                "" + bankTransaction.getAuthor().getId(),
                                                                contact,
                                                                normalReturnActionUrl,
                                                                cancelReturnActionUrl,
                                                                autoResponseActionUrl);

        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException("Not authorized", e);
        }

        StringBuilder url = new StringBuilder(mercanetTransaction.getUrl());

        boolean firstParam = true;

        for (Entry<String, String> param : mercanetTransaction.getHiddenParameters(paymentMethod).entrySet()) {
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
        return ((AccountProcess) getFather()).getAmountToPayBeforeComission();
    }

    synchronized void handlePayment(String data) throws UnauthorizedOperationException {

        MercanetResponse response = MercanetAPI.parseResponse(data);

        if (response.hasError()) {
            throw new BadProgrammerException("Failure during payment response procession. Transaction id: " + mercanetTransactionId
                    + ". Banktransaction: " + bankTransaction.getId() + ".Member:" + bankTransaction.getAuthor().getId());
        }

        if (!response.check(bankTransaction.getId().toString(), bankTransaction.getAuthor().getId().toString(), mercanetTransactionId)) {
            throw new MeanUserException("Data received from transaction do not match the value sent when creating transaction");
        }

        Payment.handlePayment(bankTransaction, response);

        // Flush doesn't reload entities stored into the processes. Hence we
        // need to reload the process to make sure we don't have references to
        // old database objects
        load();

        if (bankTransaction.getState() == State.VALIDATED) {

            success = true;
            // Notify the user:
            final String paidValueStr = Context.getLocalizator().getCurrency(bankTransaction.getValuePaid()).getTwoDecimalEuroString();
            session.notifyGood(Context.tr("Payment of {0} accepted.", paidValueStr));
        } else {
            Log.payment().info("Payment refused. [" + response.getResponseCode().code + ": " + response.getResponseCode().label + "]");
            session.notifyWarning(Context.tr("Payment canceled. Reason: {0}.", response.getResponseCode().getDisplayName()));
        }
    }

    synchronized void refusePayment(String data) {
        // Log.framework().info("Payline transaction failure. (Reason: " +
        // message + ")");
        // session.notifyWarning("Payment canceled. Reason: " + message + ".");
        Payment.cancelPayment(bankTransaction);
    }

    public synchronized String getPaymentReference() {
        try {
            return bankTransaction.getReference();
        } catch (final UnauthorizedOperationException e) {
            Log.payment().fatal("Cannot find a reference.", e);
            return "Reference-not-Found";
        }
    }

    @Override
    public synchronized void update(final WebProcess subProcess) {
        if (bankTransaction.getStateUnprotected() == State.VALIDATED) {
            success = true;
        }
        super.update(this);

    }

    @Override
    protected synchronized void transmitParameters() {
        session.addParameter(url.getPaymentMethodParameter());
        session.addParameter(url.getTosParameter());
    }

    public boolean hasBadParams() {
        return badParams;
    }

}
