package com.bloatit.web.html.pages;

import com.bloatit.common.Log;
import com.bloatit.framework.Payline;
import com.bloatit.framework.Payline.TokenNotfoundException;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.PaylinePageUrl;

@ParamContainer("payline/result")
public final class PaylinePage extends Page {

    @RequestParam(name="token", level=Level.INFO)
    private final String token;

    @RequestParam(name="ack")
    private final String ack;

    public PaylinePage(PaylinePageUrl url){
        super(url);
        token = url.getToken();
        ack = url.getAck();

        add(new HtmlParagraph(token));
        add(new HtmlParagraph(ack));

        if (ack.equals("ok")){
            Payline payline = new Payline();
            try {
                payline.validatePayment(token);
            } catch (TokenNotfoundException e) {
                Log.web().fatal("Token not found." + e);
            }
        }
    }

    @Override
    protected String getTitle() {
        return "Transaction result";
    }

    @Override
    public boolean isStable() {
        return false;
    }

}
