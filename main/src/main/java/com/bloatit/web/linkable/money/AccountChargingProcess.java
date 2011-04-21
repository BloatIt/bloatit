package com.bloatit.web.linkable.money;

import com.bloatit.framework.webprocessor.PaymentProcess;
import com.bloatit.framework.webprocessor.WebProcess;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.url.AccountChargingPageUrl;
import com.bloatit.web.url.AccountChargingProcessUrl;
import com.bloatit.web.url.AccountPageUrl;

@ParamContainer("account/charging/process")
public class AccountChargingProcess extends PaymentProcess {

    @SuppressWarnings("unused")
    private final AccountChargingProcessUrl url;

    public AccountChargingProcess(final AccountChargingProcessUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected Url doProcess() {
        return new AccountChargingPageUrl(this);
    }

    @Override
    public Url notifyChildClosed(final WebProcess subProcess) {
        if (subProcess.getClass().equals(PaylineProcess.class)) {
            final PaylineProcess subPro = (PaylineProcess) subProcess;
            if (subPro.isSuccessful()) {
                // Redirects to the contribution action which will perform the
                // actual contribution
                return new AccountPageUrl();
            }
            unlock();
            return new AccountPageUrl();
        }
        return null;
    }

    @Override
    public void doDoLoad() {
        // Nothing to do.
    }

}
