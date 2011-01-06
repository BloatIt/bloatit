package com.bloatit.web.html.pages;

import java.util.Locale;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.PaylinePageUrl;
import com.experian.payline.ws.impl.DoWebPaymentRequest;
import com.experian.payline.ws.impl.DoWebPaymentResponse;
import com.experian.payline.ws.impl.GetWebPaymentDetailsRequest;
import com.experian.payline.ws.impl.WebPaymentAPI_Service;
import com.experian.payline.ws.obj.Order;
import com.experian.payline.ws.obj.Payment;
import com.experian.payline.ws.obj.Result;

@ParamContainer("payline")
public class PaylinePage extends Page {

    @RequestParam(level = Level.INFO)
    private final String token;

    @RequestParam(level = Level.INFO)
    private final String result;

    public PaylinePage(PaylinePageUrl url) throws RedirectException {
        super(url);

        token = url.getToken();
        result = url.getResult();

        if (result != null && !result.isEmpty()) {
            System.out.println(result);
            add(new HtmlParagraph().addText(result));
        } else if (token != null && !token.isEmpty()) {
            System.out.println(token);
            add(new HtmlParagraph().addText("Hello this is the token: " + token));

            WebPaymentAPI_Service paylineApi = new WebPaymentAPI_Service();
            GetWebPaymentDetailsRequest parameters = new GetWebPaymentDetailsRequest();
            parameters.setToken(token);
            Result result = paylineApi.getWebPaymentAPI().getWebPaymentDetails(parameters).getResult();
            add(new HtmlParagraph().addText("Code result is : " + result.getCode()));
            add(new HtmlParagraph().addText("Long message is : " + result.getLongMessage()));
        } else {

            DoWebPaymentRequest paymentRequest = new DoWebPaymentRequest();

            // for virtual wallets
            // Buyer buyer = new Buyer();
            // paymentRequest.setBuyer(buyer);

            paymentRequest.setCancelURL("http://f2.b219.org:8081/fr/payline/result-cancel");
            paymentRequest.setReturnURL("http://f2.b219.org:8081/fr/payline/result-ok");
            // notify when the payment is accepted.
            // In post or get ... the token=...
            paymentRequest.setNotificationURL("http://f2.b219.org:8081/fr/payline");

            Payment payement = new Payment();
            // payment immediate Action = 101; mode "CPT"
            payement.setAction("101");
            payement.setMode("CPT");
            payement.setAmount("200");
            // Contract vad
            payement.setContractNumber("1234567");
            // 978 : euros
            // 840 : dollars US
            payement.setCurrency("978");
            // payement.setDifferedActionDate(value);
            paymentRequest.setPayment(payement);

            // Permet de donner des détails sur la command
            // et les produis achetés.
            Order order = new Order();
            order.setRef("uniquevalue !");
            // Pour savoir d'où vient la commende.
            // Tracing interne
            order.setOrigin("");
            order.setCountry("");
            order.setDate("26/01/2011 13:37"); // "dd/mm/yyyy HH24:mi"
            // Payement ...
            order.setCurrency("978");
            order.setAmount("200"); // entier * 100
            order.setTaxes("20"); // entier * 100
            paymentRequest.setOrder(order);

            // Used if we hava a custom payment page
            // paymentRequest.setCustomPaymentPageCode("");

            // iso code of the language of the payline pages.
            paymentRequest.setLanguageCode(Locale.FRENCH.getISO3Language());
            paymentRequest.setSecurityMode("ssl");
            // paymentRequest.setOwner();

            // Pour les payement en plusieur fois.
            // paymentRequest.setRecurring(buyer);
            // Some personal infos
            // paymentRequest.setPrivateDataList(buyer);
            // Liste des moyens de payment rien pour tous.
            // paymentRequest.setSelectedContractList();

            WebPaymentAPI_Service paylineApi = new WebPaymentAPI_Service();

            DoWebPaymentResponse doWebPayment = paylineApi.getWebPaymentAPI().doWebPayment(paymentRequest);

            String redirectURL = doWebPayment.getRedirectURL();
            System.out.println(doWebPayment.getToken());
            System.out.println(doWebPayment.getResult().getLongMessage());

            throw new RedirectException(redirectURL);
        }
    }

    @Override
    protected String getTitle() {
        return "Test payline";
    }

    @Override
    public boolean isStable() {
        return false;
    }

}
