package com.bloatit.web.linkable.contribution;

import java.math.BigDecimal;

import com.bloatit.model.Payline;
import com.bloatit.web.linkable.money.Quotation;
import com.bloatit.web.linkable.money.Quotation.QuotationAmountEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationDifferenceEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationPercentEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationTotalEntry;
import com.bloatit.web.linkable.money.QuotationEntry;

public class StandardQuotation {

    final public QuotationEntry subTotalTTCEntry;
    final public QuotationEntry feesHT;
    final public QuotationEntry feesTTC;
    final public QuotationEntry totalHT;
    final public QuotationEntry totalTTC;
    final public QuotationEntry bank;
    final public QuotationEntry commission;

    public StandardQuotation(final BigDecimal amount) {

        final String fixBank = "0.30";
        final String variableBank = "0.03";
        final String TVAInvertedRate = "0.836120401";

        final Quotation quotation = new Quotation(Payline.computateAmountToPay(amount));

        subTotalTTCEntry = new QuotationAmountEntry("Subtotal TTC", null, amount);

        // Fees TTC
        final QuotationTotalEntry feesTotal = new QuotationTotalEntry(null, null, null);
        final QuotationPercentEntry feesVariable = new QuotationPercentEntry("Fees", null, subTotalTTCEntry, Payline.COMMISSION_VARIABLE_RATE);
        final QuotationAmountEntry feesFix = new QuotationAmountEntry("Fees", null, Payline.COMMISSION_FIX_RATE);
        feesTotal.addEntry(feesVariable);
        feesTotal.addEntry(feesFix);

        feesTTC = feesTotal;

        // Fees HT

        feesHT = new QuotationPercentEntry("Fees HT", null, feesTotal, new BigDecimal(TVAInvertedRate));

        // Total TTC
        totalTTC = quotation;

        // Total HT
        totalHT = new QuotationTotalEntry("Fees HT", null, null).addEntry(feesHT).addEntry(subTotalTTCEntry);

        // Fees details
        // Bank fees
        bank = new QuotationTotalEntry("Bank fees", null, "Total bank fees");

        final QuotationAmountEntry fixBankFee = new QuotationAmountEntry("Fix fee", null, new BigDecimal(fixBank));

        final QuotationPercentEntry variableBankFee = new QuotationPercentEntry("Variable fee",
                                                                                "" + Float.valueOf(variableBank) * 100 + "%",
                                                                                quotation,
                                                                                new BigDecimal(variableBank));
        bank.addEntry(variableBankFee);
        bank.addEntry(fixBankFee);

        // Our fees
        commission = new QuotationDifferenceEntry("Elveos's commission TTC", null, feesHT, bank);

        quotation.addEntry(subTotalTTCEntry);
        quotation.addEntry(feesTTC);
    }
}
