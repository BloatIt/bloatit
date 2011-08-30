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
package com.bloatit.model;

import java.math.BigDecimal;

import org.apache.commons.lang.RandomStringUtils;

import com.bloatit.common.Log;
import com.bloatit.data.DaoBankTransaction.State;
import com.bloatit.framework.bank.MercanetResponse;
import com.bloatit.framework.bank.MercanetResponse.ResponseCode;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.highlevel.MeanUserException;
import com.bloatit.framework.mails.ElveosMail;
import com.bloatit.framework.model.ModelAccessor;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedOperationException.SpecialCode;

public final class Payment {

    public Payment() {
    }

    public static void validatePayment(final BankTransaction transaction) {
        if (!transaction.setValidated()) {
            throw new MeanUserException("Cannot validate the BankTransaction.");
        }
    }

    public static BankTransaction doPayment(final Actor<?> targetActor, final BigDecimal amount) throws UnauthorizedOperationException {
        if (!AuthToken.isAuthenticated()) {
            throw new UnauthorizedOperationException(SpecialCode.AUTHENTICATION_NEEDED);
        }
        if (!targetActor.equals(AuthToken.getMember()) && !targetActor.equals(AuthToken.getAsTeam())) {
            throw new UnauthorizedOperationException(SpecialCode.AUTHENTICATION_NEEDED);
        }

        final BigDecimal amountToPay = BankTransaction.computateAmountToPay(amount);

        if (amountToPay.scale() > 2) {
            throw new BadProgrammerException("The amount to pay cannot have more than 2 digit after the '.'.");
        }
        if (amount.scale() > 2) {
            throw new BadProgrammerException("The amount cannot have more than 2 digit after the '.'.");
        }
        if (amount.compareTo(amountToPay) > 0) {
            throw new BadProgrammerException("The amount to pay must be superior to the amount '.'.");
        }

        final String orderReference = createOrderRef(targetActor);

        final BankTransaction bankTransaction = new BankTransaction(targetActor,//
                                                                    amount, //
                                                                    amountToPay, //
                                                                    orderReference);
        bankTransaction.setAuthorized();

        return bankTransaction;
    }

    /**
     * Return a unique ref.
     * 
     * @param actor
     * @return
     */
    private static String createOrderRef(final Actor<?> actor) {
        final StringBuilder ref = new StringBuilder();
        // It is a payline action
        ref.append("MERCANET-");

        // Add the member id
        ref.append(actor.getId());
        ref.append('-');

        PageIterable<BankTransaction> bankTransaction;
        try {
            // Add the last bankTransaction + 1
            bankTransaction = actor.getBankTransactions();
            if (bankTransaction.size() == 0) {
                ref.append('0');
            } else {
                ref.append(bankTransaction.iterator().next().getId() + 1);
            }

            // Add a random string to ensure uniqueness.
            ref.append('-').append(RandomStringUtils.randomAlphabetic(5));
        } catch (final UnauthorizedOperationException e) {
            Log.model().fatal("Unauthorized exception should never append ! ", e);
            ref.append("ERROR");
            return ref.toString();
        }
        return ref.toString();
    }

    public static void cancelPayment(final BankTransaction transaction) {
        transaction.setRefused();
    }

    public synchronized static void handlePayment(BankTransaction transaction, MercanetResponse response) {
        if (transaction.getStateUnprotected() == State.VALIDATED) {
            // Auto response can validate it before response happens
            return;
        }

        if (response.getResponseCode() == ResponseCode.AUTHORISATION_ACCEPTED) {
            Payment.validatePayment(transaction);

            // Payment process is critical. We must be sure the DB is updated
            ModelAccessor.flush();

            // Notify the user:
            // TODO Store member id in bank transaction
            Member member = MemberManager.getById(Integer.valueOf(response.getCustomerId()));
            Localizator localizator = new Localizator(member.getLocaleUnprotected());
            final String valueStr = localizator.getCurrency(transaction.getValueUnprotected()).getSimpleEuroString();
            final String paidValueStr = localizator.getCurrency(transaction.getValuePaidUnprotected()).getTwoDecimalEuroString();
            ElveosMail mail = new ElveosMail.ChargingAccountSuccess(transaction.getReferenceUnprotected(), paidValueStr, valueStr);
            mail.sendMail(member, "account-charging");
        } else {
            Payment.cancelPayment(transaction);
            Log.payment().info("Payment refused. [" + response.getResponseCode().code + ": " + response.getResponseCode().label + "]");
        }
    }

    public static class BankTransactionException extends Exception {
        private static final long serialVersionUID = 4891304798361361776L;

        public BankTransactionException() {
            super();
        }

        public BankTransactionException(final String message) {
            super(message);
        }
    }

}
