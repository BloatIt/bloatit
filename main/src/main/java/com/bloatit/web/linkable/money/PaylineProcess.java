package com.bloatit.web.linkable.money;

import java.math.BigDecimal;

import com.bloatit.framework.webprocessor.WebProcess;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.url.PaylineActionUrl;
import com.bloatit.web.url.PaylineProcessUrl;

@ParamContainer("payline/process")
public class PaylineProcess extends WebProcess {
    @RequestParam
    private final PaymentProcess parentProcess;

    private boolean success = false;

    private final PaylineProcessUrl url;

    public PaylineProcess(final PaylineProcessUrl url) {
        super(url);
        this.url = url;
        parentProcess = url.getParentProcess();
    }

    public BigDecimal getAmount() {
        return parentProcess.getAmountToPay();
    }

    public WebProcess getParentProcess() {
        return parentProcess;
    }

    @Override
    protected Url doProcess() {
        parentProcess.beginSubProcess(this);
        return new PaylineActionUrl(this);
    }

    @Override
    protected Url doProcessErrors() {
        return session.getLastVisitedPage();
    }

    @Override
    public void load() {
        parentProcess.load();
    }

    /**
     * Indicates that the payment process is successful
     */
    public void setSuccessful() {
        this.success = true;
    }

    /**
     * Indicates if the process was successful or not.
     * <p>
     * Note: To be successful, a call to {@link #setSuccessful()} must be done
     * before. The default value is not successful.
     * </p>
     * 
     * @return <i>true</i> when the process is successful, <i>false</i>
     *         otherwise
     */
    public boolean isSuccessful() {
        return success;
    }
}
