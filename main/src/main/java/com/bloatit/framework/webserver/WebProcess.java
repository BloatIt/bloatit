package com.bloatit.framework.webserver;

import java.math.BigDecimal;

import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;

public abstract class WebProcess extends Action {

    private final String processId;

    public WebProcess(Url url) {
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

    public void beginSubProcess(WebProcess subProcess) {

    }

    public Url endSubProcess(WebProcess subProcess) {
        return null;
    }

    public static abstract class PaymentProcess extends WebProcess {

        public PaymentProcess(Url url) {
            super(url);
            // TODO Auto-generated constructor stub
        }

        public abstract BigDecimal getAmountToPay();

    }

}
