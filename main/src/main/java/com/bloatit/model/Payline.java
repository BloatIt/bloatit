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
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.RestrictedObject;
import com.experian.payline.ws.impl.DoWebPaymentRequest;
import com.experian.payline.ws.impl.DoWebPaymentResponse;
import com.experian.payline.ws.impl.GetWebPaymentDetailsRequest;
import com.experian.payline.ws.impl.WebPaymentAPI_Service;
import com.experian.payline.ws.obj.Order;
import com.experian.payline.ws.obj.Payment;
import com.experian.payline.ws.obj.Result;

public final class Payline extends RestrictedObject {

    private static final String ACCEPTED_CODE = "00000";
    private static final String ORDER_ORIGINE = "payline";
    /**
     * 978 : euros 840 : dollars US
     */
    private static final String CURRENCY = "978";
    // Numero de contrat de vente à distance (VAD) found on payline
    // administration page.
    private static final String CONTRACT_NUMBER = "42";
    // immediate payment: Action = 101; mode "CPT"
    private static final String MODE = "CPT";
    private static final String ACTION = "101";

    public static final class Reponse {
        private final String code;
        private final String token;
        private final String message;
        private final String redirectUrl;

        Reponse(final DoWebPaymentResponse reponse) {
            code = reponse.getResult().getCode();
            token = reponse.getToken();
            message = reponse.getResult().getShortMessage() + "\n" + reponse.getResult().getLongMessage();
            this.redirectUrl = reponse.getRedirectURL();
        }

        Reponse(final Result result, final String token) {
            code = result.getCode();
            this.token = token;
            message = result.getShortMessage() + "\n" + result.getLongMessage();
            this.redirectUrl = "";
        }

        public String getToken() {
            return token;
        }

        public String getMessage() {
            return message;
        }

        public String getCode() {
            return code;
        }

        public boolean isAccepted() {
            return code.equals(ACCEPTED_CODE);
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }
    }

    public static class TokenNotfoundException extends Exception {
        private static final long serialVersionUID = 4891304798361361776L;

        public TokenNotfoundException() {
            super();
        }

        public TokenNotfoundException(final String message) {
            super(message);
        }
    }

    public Payline() {
        // identifiant commerçant : 54652391742591
        // Votre clé d’accès au service Payline : ar6NsH8gOFdAxFXnm568
        // Les certificats serveur Payline* : homologation et production
        // Votre ou vos contrats de vente à distance : 1234567
        // Votre ou vos contrats de vente à distance : 987654
        // Votre ou vos contrats de vente à distance : 42
    }

    public boolean canMakePayment() {
        return getAuthTokenUnprotected() != null;
    }

    public void validatePayment(final String token) throws TokenNotfoundException {
        final BankTransaction transaction = BankTransaction.getByToken(token);
        if (transaction != null) {
            if (!transaction.setValidated()) {
                throw new TokenNotfoundException("Cannot validate the BankTransaction.");
            }
        } else {
            throw new TokenNotfoundException("Token is not found in DB: " + token);
        }
    }

    // public void getTransactionDetails(final String token) throws
    // TokenNotfoundException
    // {
    // final WebPaymentAPI_Service paylineApi = new WebPaymentAPI_Service();
    // final GetWebPaymentDetailsRequest request =
    // createWebPaymementRequest(token);
    // Transaction transaction =
    // paylineApi.getWebPaymentAPI().getWebPaymentDetails(request).getTransaction();
    //
    // System.err.println(transaction.getExplanation());
    // System.err.println(transaction.getFraudResult());
    // System.err.println(transaction.getIsPossibleFraud());
    // System.err.println(transaction.getScore());
    // }

    public Reponse getPaymentDetails(final String token) throws TokenNotfoundException {
        final WebPaymentAPI_Service paylineApi = new WebPaymentAPI_Service();
        final GetWebPaymentDetailsRequest parameters = createWebPaymementRequest(token);
        final Result result = paylineApi.getWebPaymentAPI().getWebPaymentDetails(parameters).getResult();
        return new Reponse(result, token);
    }

    private GetWebPaymentDetailsRequest createWebPaymementRequest(final String token) throws TokenNotfoundException {
        final BankTransaction transaction = BankTransaction.getByToken(token);
        if (transaction == null) {
            throw new TokenNotfoundException("Token is not found in DB: " + token);
        }

        final GetWebPaymentDetailsRequest parameters = new GetWebPaymentDetailsRequest();
        parameters.setToken(token);
        return parameters;
    }

    public Reponse doPayment(final BigDecimal amount, final String cancelUrl, final String returnUrl, final String notificationUrl)
            throws UnauthorizedOperationException {
        final DoWebPaymentRequest paymentRequest = new DoWebPaymentRequest();
        paymentRequest.setCancelURL(cancelUrl);
        paymentRequest.setReturnURL(returnUrl);
        paymentRequest.setNotificationURL(notificationUrl);

        if (getAuthToken() == null) {
            throw new UnauthorizedOperationException(Action.READ);
        }
        if (amount.scale() > 2) {
            throw new FatalErrorException("The amount cannot have more than 2 digit after the '.'.");
        }

        final BigDecimal amountX100 = amount.scaleByPowerOfTen(2);

        addPaymentDetails(amountX100, paymentRequest);
        final String orderReference = addOrderDetails(amountX100, paymentRequest);

        // paymentRequest.setCustomPaymentPageCode("");
        paymentRequest.setLanguageCode(Locale.FRENCH.getISO3Language());
        paymentRequest.setSecurityMode("ssl");

        final WebPaymentAPI_Service paylineService = new WebPaymentAPI_Service();

        // TODO catch exceptions !!
        final DoWebPaymentResponse apiReponse = paylineService.getWebPaymentAPI().doWebPayment(paymentRequest);

        final Reponse reponse = new Reponse(apiReponse);
        createBankTransaction(amount, orderReference, reponse);
        return reponse;
    }

    private void createBankTransaction(final BigDecimal amount, final String orderReference, final Reponse reponse) {
        if (reponse.getToken() != null && !reponse.getToken().isEmpty()) {
            final BankTransaction bankTransaction = new BankTransaction(reponse.getMessage(),//
                                                                        reponse.getToken(),//
                                                                        getAuthTokenUnprotected().getMember(),//
                                                                        amount, //
                                                                        orderReference);
            bankTransaction.setProcessInformations(reponse.getCode());
            if (reponse.isAccepted()) {
                bankTransaction.setAuthorized();
            } else {
                bankTransaction.setRefused();
            }
        }
    }

    private String addOrderDetails(final BigDecimal amountX100, final DoWebPaymentRequest paymentRequest) {
        // Order details
        final Order order = new Order();
        final Member member = getAuthTokenUnprotected().getMember();
        final String orderReference = createOrderRef(member);
        order.setRef(orderReference);
        order.setOrigin(ORDER_ORIGINE);
        order.setCountry(Locale.FRANCE.getCountry());
        order.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        order.setCurrency(CURRENCY);
        order.setAmount(amountX100.toPlainString()); // entier * 100
        order.setTaxes(amountX100.divide(new BigDecimal("0.21"), 0, RoundingMode.HALF_EVEN).toPlainString());
        paymentRequest.setOrder(order);
        return orderReference;
    }

    private void addPaymentDetails(final BigDecimal amountX100, final DoWebPaymentRequest paymentRequest) {
        // Payment details
        final Payment payement = new Payment();
        payement.setAction(ACTION);
        payement.setMode(MODE);
        payement.setAmount(amountX100.toPlainString());
        payement.setContractNumber(CONTRACT_NUMBER);
        payement.setCurrency(CURRENCY);
        paymentRequest.setPayment(payement);
    }

    /**
     * Return a unique ref.
     *
     * @param member
     * @return
     */
    private String createOrderRef(final Member member) {
        final StringBuilder ref = new StringBuilder();
        ref.append("PAYLINE-");
        ref.append(member.getId());
        ref.append("-");
        PageIterable<BankTransaction> bankTransaction;
        try {
            bankTransaction = member.getBankTransactions();
            if (bankTransaction.size() == 0) {
                ref.append("0");
            } else {
                ref.append(bankTransaction.iterator().next().getId() + 1);
            }
        } catch (final UnauthorizedOperationException e) {
            Log.model().fatal("Unauthorized exception should never append ! ", e);
            assert false;
            ref.append("ERROR");
            return ref.toString();
        }
        return ref.toString();
    }

    public void cancelPayement(final String token) throws TokenNotfoundException {
        final BankTransaction transaction = BankTransaction.getByToken(token);
        if (transaction != null) {
            transaction.setRefused();
        } else {
            throw new TokenNotfoundException("Token is not found in DB: " + token);
        }
    }

    @Override
    protected boolean isMine(final Member member) {
        return false;
    }

}
