package com.bloatit.web.linkable.money;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import org.hibernate.CacheMode;

import com.bloatit.common.Log;
import com.bloatit.data.SessionManager;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.model.Payline;
import com.bloatit.model.Payline.TokenNotfoundException;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
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
    protected HtmlElement createBodyContent() throws RedirectException {
        // TODO maybe we should create the page here.
        return new PlaceHolderElement();
    }

    @Override
    protected String createPageTitle() {
        return "Transaction result";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return PaylinePage.generateBreadcrumb(ack);
    }

    public static Breadcrumb generateBreadcrumb(final String ack) {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new PaylinePageUrl(ack).getHtmlLink(tr("Transaction result")));

        return breadcrumb;
    }

}
