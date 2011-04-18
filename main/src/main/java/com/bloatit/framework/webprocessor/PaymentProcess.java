package com.bloatit.framework.webprocessor;

import java.math.BigDecimal;

import com.bloatit.framework.webprocessor.url.Url;

public abstract class PaymentProcess extends WebProcess {
    public PaymentProcess(final Url url) {
        super(url);
    }

    public abstract BigDecimal getAmountToPay();
}