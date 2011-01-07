package com.bloatit.web.html.pages;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.PaylinePageUrl;
import com.bloatit.web.utils.url.UrlStringBinder;
import com.experian.payline.ws.impl.DoWebPaymentRequest;
import com.experian.payline.ws.impl.DoWebPaymentResponse;
import com.experian.payline.ws.impl.WebPaymentAPI_Service;
import com.experian.payline.ws.obj.Order;
import com.experian.payline.ws.obj.Payment;

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
            System.out.println(token);

            if (result.startsWith("notify")) {
                add(new HtmlParagraph().addText("Hello this is the token: " + token));

//                WebPaymentAPI_Service paylineApi = new WebPaymentAPI_Service();
//                GetWebPaymentDetailsRequest parameters = new GetWebPaymentDetailsRequest();
//                parameters.setToken(token);
//                Result result = paylineApi.getWebPaymentAPI().getWebPaymentDetails(parameters).getResult();
//                add(new HtmlParagraph().addText("Code result is : " + result.getCode()));
//                add(new HtmlParagraph().addText("Long message is : " + result.getLongMessage()));
            }
        } else {

            DoWebPaymentRequest paymentRequest = new DoWebPaymentRequest();

            // for virtual wallets
            // Buyer buyer = new Buyer();
            // paymentRequest.setBuyer(buyer);

            paymentRequest.setCancelURL("http://f2.b219.org:8081/fr/payline/result-cancel");
            paymentRequest.setReturnURL("http://f2.b219.org:8081/fr/payline/result-ok");
            // notify when the payment is accepted.
            // In post or get ... the token=...
            paymentRequest.setNotificationURL("http://f2.b219.org:8081/fr/payline/result-notify");

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
            order.setRef("OtherValue2");
            // Pour savoir d'où vient la commende.
            // Tracing interne
            order.setOrigin("osef");
            order.setCountry("FR");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm");
            order.setDate(simpleDateFormat.format(new Date())); // "dd/mm/yyyy HH24:mi"

            // Payement ...
            order.setCurrency("978");
            order.setAmount("2000"); // entier * 100
            order.setTaxes("200"); // entier * 100
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

            // URL wsdlURL =
            // WebPaymentAPI_Service.class.getClassLoader().getResource("http://www.payline.com/wsdl/v4_0/homologation/WebPaymentAPI.wsdl");
            // QName serviceName = new QName("https://homologation.payline.com",
            // "WebPaymentAPI");

            WebPaymentAPI_Service paylineService = new WebPaymentAPI_Service();
            // An alternate way to get the SOAP service interface; includes logging
            // interceptors.
            // JaxWsProxyFactoryBean factory = new
            // org.apache.cxf.jaxws.JaxWsProxyFactoryBean();
            // factory.setServiceClass(WebPaymentAPI.class);
            // // factory.setAddress("https://homologation.payline.com");
            // factory.setAddress("http://impl.ws.payline.experian.com");
            //
            // try {
            // EndpointInfo ei = new EndpointInfo();
            // ei.setName(new QName("http://impl.ws.payline.experian.com",
            // "WebPaymentAPI"));
            //
            // HTTPConduit c = new HTTPConduit(CXFBusFactory.getDefaultBus(), ei);
            // factory.setBus(CXFBusFactory.getDefaultBus());
            //
            // AuthorizationPolicy authorization = new AuthorizationPolicy();
            // authorization.setAuthorization("NTQ2NTIzOTE3NDI1OTE6YXI2TnNIOGdPRmRBeEZYbm01Njg=");
            // authorization.setAuthorizationType("BASIC");
            // c.setAuthorization(authorization);
            // TLSClientParameters params = new TLSClientParameters();
            // params.setSecureSocketProtocol("SSL");
            // c.setTlsClientParameters(params);
            // } catch (IOException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }

            // factory.setWsdlURL("http://www.payline.com/wsdl/v4_0/homologation/WebPaymentAPI.wsdl");
            // factory.getInInterceptors().add(new
            // org.apache.cxf.interceptor.LoggingInInterceptor());
            // factory.getOutInterceptors().add(new
            // org.apache.cxf.interceptor.LoggingOutInterceptor());
            // WebPaymentAPI paylineService = (WebPaymentAPI) factory.create();

            // identifiant commerçant : 54652391742591
            // Votre clé d’accès au service Payline : ar6NsH8gOFdAxFXnm568
            // Les certificats serveur Payline* : homologation et production
            // Votre ou vos contrats de vente à distance : 1234567

            DoWebPaymentResponse reponse = paylineService.getWebPaymentAPI().doWebPayment(paymentRequest);

            System.out.println(reponse.getToken());
            System.out.println(reponse.getResult().getLongMessage());

            throw new RedirectException(new UrlStringBinder(reponse.getRedirectURL()));
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
