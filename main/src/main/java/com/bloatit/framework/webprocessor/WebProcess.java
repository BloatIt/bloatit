package com.bloatit.framework.webprocessor;

import java.math.BigDecimal;

import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;

public abstract class WebProcess extends Action {

    private final String processId;

    public WebProcess(final Url url) {
        super(url);
        processId = Context.getSession().createWebProcess(this);

    }

    public String getId() {
        return processId;
    }

    /**
     * Call after session extraction. Used to reload database objects TODO:
     * verify if this is thread safe
     */
    public abstract void load();

    public void close() {
        Context.getSession().destroyWebProcess(this);
    }

    public void beginSubProcess(@SuppressWarnings("unused") final WebProcess subProcess) {
        // Implement me in subclass if you wish.
    }

    public Url endSubProcess(@SuppressWarnings("unused") final WebProcess subProcess) {
        return null;
    }

    public static abstract class PaymentProcess extends WebProcess {

        public PaymentProcess(final Url url) {
            super(url);
            // TODO Auto-generated constructor stub
        }

        public abstract BigDecimal getAmountToPay();

    }

}
