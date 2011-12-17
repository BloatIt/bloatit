package com.bloatit.rest.resources;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.data.DaoBankTransaction.DaoBankTransactionSum;
import com.bloatit.framework.restprocessor.RestElement;
import com.bloatit.framework.restprocessor.RestServer.RequestMethod;
import com.bloatit.framework.restprocessor.annotations.REST;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.managers.BankTransactionManager;
import com.bloatit.model.managers.ContributionManager;

@XmlRootElement(name = "moneyresults")
@XmlAccessorType(XmlAccessType.NONE)
public class RestMoneyResults extends RestElement<BankTransaction> {

    @XmlAttribute
    final long nbBankTransaction;
    @XmlAttribute
    final BigDecimal chargedValue;
    @XmlAttribute
    final BigDecimal paidValue;
    @XmlAttribute
    final BigDecimal moneyRaised;

    public RestMoneyResults() {
        this.nbBankTransaction = 0L;
        this.chargedValue = BigDecimal.ZERO;
        this.paidValue = BigDecimal.ZERO;
        this.moneyRaised = BigDecimal.ZERO;
    }

    public RestMoneyResults(final long count, final BigDecimal chargedValue, final BigDecimal paidValue, BigDecimal moneyRaised) {
        this.nbBankTransaction = count;
        this.chargedValue = chargedValue;
        this.paidValue = paidValue;
        this.moneyRaised = moneyRaised;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    /**
     * @param from number of days from now. Represent the begin date of the
     *            aggregated sum.
     * @param to number of days from now. Represent the end date of the
     *            aggregated sum.
     * @return the sum of all {@link BankTransaction} between the <i>from</i>
     *         and <i>to</i> date.
     */
    @REST(name = "moneyquery", method = RequestMethod.GET, params = { "from", "to" })
    public static RestMoneyResults getBankTransactionsSum(final String from, final String to) {
        Date fromDate = DateUtils.nowMinusSomeDays(Integer.parseInt(from));
        Date toDate = DateUtils.nowMinusSomeDays(Integer.parseInt(to));

        final DaoBankTransactionSum sum = BankTransactionManager.getSum(fromDate, toDate);
        BigDecimal moneyRaised = ContributionManager.getMoneyRaised(fromDate, toDate);

        return new RestMoneyResults(sum.count, sum.chargedValueSum, sum.paidValueSum, moneyRaised);
    }
}
