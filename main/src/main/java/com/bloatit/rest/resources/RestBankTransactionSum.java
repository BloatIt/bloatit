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
public class RestBankTransactionSum extends RestElement<BankTransaction>{
    private final BigDecimal sum;
    private final Long count;

    public RestBankTransactionSum() {
        this.sum = BigDecimal.ZERO;
        this.count = 0L;
    }
    
    public RestBankTransactionSum(BigDecimal value, Long count) {
        this.sum = value;
        this.count = count;
    }

    @XmlAttribute
    public BigDecimal getSum() {
        return sum;
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