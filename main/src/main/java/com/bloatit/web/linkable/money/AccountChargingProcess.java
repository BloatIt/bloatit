package com.bloatit.web.linkable.money;

import java.math.BigDecimal;

import com.bloatit.framework.webprocessor.WebProcess;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.url.AccountChargingProcessUrl;
import com.bloatit.web.url.LoginPageUrl;

@ParamContainer("account/charging/process")
public class AccountChargingProcess extends WebProcess {

    public static final String CHARGE_AMOUNT_CODE = "amount";

    @Optional
    @RequestParam(name = CHARGE_AMOUNT_CODE, role = RequestParam.Role.GET)
    private BigDecimal amount;

    private final AccountChargingProcessUrl url;

    public AccountChargingProcess(final AccountChargingProcessUrl url) {
        super(url);
        this.url = url;
        amount = url.getAmount();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    protected Url doProcess() {
        return new LoginPageUrl();
    }

    @Override
    protected Url doProcessErrors() {
        return session.getLastVisitedPage();
    }

    @Override
    public void doLoad() {
        // TODO Do nothing ?
    }
}
