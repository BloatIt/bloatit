package com.bloatit.web.pages;

import org.hibernate.CacheMode;

import com.bloatit.common.Log;
import com.bloatit.data.SessionManager;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.model.Payline;
import com.bloatit.model.Payline.TokenNotfoundException;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.PaylinePageUrl;

@ParamContainer("payline/result")
public final class PaylinePage extends MasterPage {

    @RequestParam(name = "token")
    @Optional
    private final String token;

    @RequestParam(name = "ack")
    private final String ack;

    public PaylinePage(final PaylinePageUrl url) {
        super(url);

        SessionManager.getSessionFactory().getCurrentSession().setDefaultReadOnly(false);
        SessionManager.getSessionFactory().getCurrentSession().setCacheMode(CacheMode.NORMAL);

        token = url.getToken();
        ack = url.getAck();

        add(new HtmlParagraph(token));
        add(new HtmlParagraph(ack));

        final Payline payline = new Payline();
        if (ack.equals("ok")) {
            try {
                payline.validatePayment(token);
            } catch (final TokenNotfoundException e) {
                Log.web().fatal("Token not found.", e);
            }
        } else if (ack.equals("cancel")) {
            try {
                payline.cancelPayement(token);
            } catch (final TokenNotfoundException e) {
                Log.web().fatal("Token not found.", e);
            }
        }
    }

    @Override
    protected void doCreate() throws RedirectException {
    }

    @Override
    protected String getPageTitle() {
        return "Transaction result";
    }

    @Override
    public boolean isStable() {
        return false;
    }

}
