//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
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

    final protected QuotationEntry subTotalTTCEntry;
    final protected QuotationEntry feesHT;
    final protected QuotationEntry feesTTC;
    final protected QuotationEntry totalHT;
    final protected QuotationEntry totalTTC;
    final protected QuotationEntry bank;
    final protected QuotationEntry commission;

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
                                                                                "" + Float.valueOf(variableBank).floatValue() * 100 + "%",
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
