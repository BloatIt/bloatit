package com.bloatit.web.pages;

import com.bloatit.common.Log;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.model.Payline;
import com.bloatit.model.Payline.TokenNotfoundException;
import com.bloatit.web.pages.master.Page;
import com.bloatit.web.url.PaylinePageUrl;

@ParamContainer("payline/result")
public final class PaylinePage extends Page {

    @RequestParam(name = "token", level = Level.INFO)
    private final String token;

    @RequestParam(name = "ack")
    private final String ack;

    public PaylinePage(final PaylinePageUrl url) {
        super(url);
        token = url.getToken();
        ack = url.getAck();

        add(new HtmlParagraph(token));
        add(new HtmlParagraph(ack));

        if (ack.equals("ok")) {
            final Payline payline = new Payline();
            try {
                payline.validatePayment(token);
            } catch (final TokenNotfoundException e) {
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
