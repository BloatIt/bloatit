package com.bloatit.framework;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.bloatit.common.PageIterable;
import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.model.data.DaoBankTransaction;
import com.experian.payline.ws.impl.DoWebPaymentRequest;
import com.experian.payline.ws.impl.DoWebPaymentResponse;
import com.experian.payline.ws.impl.WebPaymentAPI_Service;
import com.experian.payline.ws.obj.Order;
import com.experian.payline.ws.obj.Payment;

public class Payline extends Unlockable {

    private static final String ORDER_ORIGINE = "payline";
    /**
     * 978 : euros 840 : dollars US
     */
    private static final String CURRENCY = "978";
    // Numero de contrat de vente à distance (VAD) found on payline administration page.
    private static final String CONTRACT_NUMBER = "1234567";
    // immediate payment: Action = 101; mode "CPT"
    private static final String MODE = "CPT";
    private static final String ACTION = "101";

    public static class Reponse {
        private final String redirect;
        private final String token;
        private final String message;

        public Reponse(String redirect, String token, String message) {
            super();
            this.redirect = redirect;
            this.token = token;
            this.message = message;
        }

        public final String getRedirect() {
            return redirect;
        }

        public final String getToken() {
            return token;
        }

        public final String getMessage() {
            return message;
        }

    }

    private final DoWebPaymentRequest paymentRequest;

    public Payline(String cancelUrl, String returnUrl, String notificationUrl) {
        paymentRequest = new DoWebPaymentRequest();
        paymentRequest.setCancelURL(cancelUrl);
        paymentRequest.setReturnURL(returnUrl);
        paymentRequest.setNotificationURL(notificationUrl);
        // identifiant commerçant : 54652391742591
        // Votre clé d’accès au service Payline : ar6NsH8gOFdAxFXnm568
        // Les certificats serveur Payline* : homologation et production
        // Votre ou vos contrats de vente à distance : 1234567
    }

    public boolean canMakePayment(){
        return getToken() != null;
    }

    public Reponse beginPayment(int amount) {

        if (getToken() == null) {
            throw new UnauthorizedOperationException();
        }

        // Payment details
        Payment payement = new Payment();
        payement.setAction(ACTION);
        payement.setMode(MODE);
        payement.setAmount(String.valueOf(amount * 100));
        payement.setContractNumber(CONTRACT_NUMBER);
        payement.setCurrency(CURRENCY);
        paymentRequest.setPayment(payement);

        // Order details
        Order order = new Order();
        order.setRef(createOrderRef(getToken().getMember()));
        order.setOrigin(ORDER_ORIGINE);
        order.setCountry(Locale.FRANCE.getCountry());
        order.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        order.setCurrency(CURRENCY);
        order.setAmount(String.valueOf(amount * 100)); // entier * 100
        order.setTaxes(String.valueOf(amount * 20)); // TODO Taxes amount
        paymentRequest.setOrder(order);

        // paymentRequest.setCustomPaymentPageCode("");
        paymentRequest.setLanguageCode(Locale.FRENCH.getISO3Language());
        paymentRequest.setSecurityMode("ssl");

        WebPaymentAPI_Service paylineService = new WebPaymentAPI_Service();
        DoWebPaymentResponse reponse = paylineService.getWebPaymentAPI().doWebPayment(paymentRequest);

        System.err.println(reponse.getResult().getCode());
        System.err.println(reponse.getResult().getLongMessage());

        if (reponse.getResult().getCode().equals("00000")) {// Accepted

            return new Reponse(reponse.getRedirectURL(), reponse.getToken(), reponse.getResult().getLongMessage());
            // reponse.getResult().getShortMessage();
        }
        return new Reponse(reponse.getRedirectURL(), reponse.getToken(), reponse.getResult().getLongMessage());
    }

    /**
     * Return a unique ref.
     *
     * @param member
     * @return
     */
    private String createOrderRef(Member member) {
        StringBuilder ref = new StringBuilder();
        ref.append("PAYLINE");
        ref.append(member.getId());
        PageIterable<DaoBankTransaction> bankTransaction = DaoBankTransaction.getbankTransaction(member.getDao());
        if (bankTransaction.size() == 0) {
            ref.append("0");
        } else {
            ref.append(bankTransaction.iterator().next().getId() + 1);
        }
        return ref.toString();
    }


}


