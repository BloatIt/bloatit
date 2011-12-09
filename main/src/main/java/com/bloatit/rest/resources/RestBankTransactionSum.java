package com.bloatit.rest.resources;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.restprocessor.RestElement;
import com.bloatit.model.BankTransaction;

@XmlRootElement(name = "banktransaction")
@XmlAccessorType(XmlAccessType.NONE)
public class RestBankTransactionSum extends RestElement<BankTransaction> {
    private final Long count;
    private final BigDecimal chargedValue;
    private final BigDecimal paidValue;

    public RestBankTransactionSum() {
        this.count = 0L;
        this.chargedValue = BigDecimal.ZERO;
        this.paidValue = BigDecimal.ZERO;
    }

    public RestBankTransactionSum(final Long count, final BigDecimal chargedValue, final BigDecimal paidValue) {
        this.count = count;
        this.chargedValue = chargedValue;
        this.paidValue = paidValue;
    }

    @XmlAttribute
    public BigDecimal getChargedValue() {
        return chargedValue;
    }

    @XmlAttribute
    public BigDecimal getPaidValue() {
        return paidValue;
    }

    @XmlAttribute
    public Long getCount() {
        return count;
    }

    @Override
    public boolean isNull() {
        return false;
    }
}
