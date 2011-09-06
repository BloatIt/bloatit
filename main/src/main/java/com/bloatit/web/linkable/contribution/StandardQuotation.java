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

import com.bloatit.model.BankTransaction;
import com.bloatit.web.linkable.money.Quotation.QuotationAmountEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationDifferenceEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationMultiplyEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationPercentEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationTotalEntry;
import com.bloatit.web.linkable.money.QuotationEntry;

public class StandardQuotation {

    protected final QuotationEntry subTotal;

    protected final QuotationEntry fees;
    protected final QuotationEntry bankFees;
    protected final QuotationEntry taxesFees;
    protected final QuotationEntry elveosFees;

    protected final QuotationEntry totalTTC;
    protected final QuotationEntry totalHT;

    public StandardQuotation(final BigDecimal amount) {
        subTotal = new QuotationAmountEntry(amount);

        fees = new QuotationTotalEntry();
        fees.addEntry(new QuotationPercentEntry(subTotal, BankTransaction.COMMISSION_VARIABLE_RATE));
        fees.addEntry(new QuotationAmountEntry(BankTransaction.COMMISSION_FIX_RATE));

        totalTTC = new QuotationTotalEntry();
        totalTTC.addEntry(subTotal);
        totalTTC.addEntry(fees);

        bankFees = new QuotationTotalEntry();
        bankFees.addEntry(new QuotationMultiplyEntry(totalTTC, new BigDecimal("0.0110")));
        bankFees.addEntry(new QuotationAmountEntry(new BigDecimal("0.30")));

        taxesFees = new QuotationPercentEntry(fees, new BigDecimal("0.163879599"));

        QuotationEntry taxesAndBank = new QuotationTotalEntry();
        taxesAndBank.addEntry(taxesFees);
        taxesAndBank.addEntry(bankFees);

        elveosFees = new QuotationDifferenceEntry(fees, taxesAndBank);

        totalHT = new QuotationTotalEntry();
        totalHT.addEntry(subTotal);
        totalHT.addEntry(bankFees);
        totalHT.addEntry(elveosFees);

    }
}
