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
package com.bloatit.web.linkable.money;

import java.math.BigDecimal;

public class Quotation extends QuotationEntry {

    private final BigDecimal total;

    public Quotation(final BigDecimal total) {
        super(null, null);
        this.total = total;

    }

    public static class QuotationTotalEntry extends QuotationEntry {

        private final String totalLabel;

        public QuotationTotalEntry(final String label, final String comment, final String totalLabel) {
            super(label, comment);
            this.totalLabel = totalLabel;
        }

        @Override
        public BigDecimal getValue() {
            BigDecimal value = BigDecimal.ZERO;
            for (final QuotationEntry entry : entries) {
                value = value.add(entry.getValue());
            }

            return value;
        }

        @Override
        public void accept(final QuotationVisitor visitor) {
            visitor.visit(this);
        }

        public String getTotalLabel() {
            return totalLabel;
        }

    }

    public static class QuotationAmountEntry extends QuotationEntry {

        private final BigDecimal amount;

        public QuotationAmountEntry(final String label, final String comment, final BigDecimal amount) {
            super(label, comment);
            this.amount = amount;
        }

        @Override
        public BigDecimal getValue() {
            return amount;
        }

        @Override
        public void accept(final QuotationVisitor visitor) {
            visitor.visit(this);
        }

    }

    public static class QuotationProxyEntry extends QuotationEntry { // NO_UCD

        private final QuotationEntry reference;

        public QuotationProxyEntry(final String label, final String comment, final QuotationEntry reference) {
            super(label, comment);
            this.reference = reference;
        }

        @Override
        public BigDecimal getValue() {
            return reference.getValue();
        }

        @Override
        public void accept(final QuotationVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class QuotationPercentEntry extends QuotationEntry {

        private final BigDecimal percent;
        private final QuotationEntry reference;

        public QuotationPercentEntry(final String label, final String comment, final QuotationEntry reference, final BigDecimal percent) {
            super(label, comment);
            this.reference = reference;
            this.percent = percent;
        }

        @Override
        public BigDecimal getValue() {
            return reference.getValue().multiply(percent).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }

        @Override
        public void accept(final QuotationVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class QuotationDifferenceEntry extends QuotationEntry {

        private final QuotationEntry reference1;
        private final QuotationEntry reference2;

        public QuotationDifferenceEntry(final String label, final String comment, final QuotationEntry reference1, final QuotationEntry reference2) {
            super(label, comment);
            this.reference1 = reference1;
            this.reference2 = reference2;

        }

        @Override
        public BigDecimal getValue() {
            return reference1.getValue().subtract(reference2.getValue());
        }

        @Override
        public void accept(final QuotationVisitor visitor) {
            visitor.visit(this);
        }

    }

    public static String padding(String output) {
        StringBuilder o = new StringBuilder();
        o.append(output);
        while (output.length() < 40) {
            o.append(' ');
        }
        return o.toString();
    }

    public interface QuotationVisitor {
        void visit(QuotationTotalEntry entry);

        void visit(QuotationAmountEntry entry);

        void visit(QuotationProxyEntry entry);

        void visit(QuotationPercentEntry entry);

        void visit(QuotationDifferenceEntry entry);

        void visit(Quotation quotation);
    }

    @Override
    public BigDecimal getValue() {
        return total;
    }

    @Override
    public void accept(final QuotationVisitor visitor) {
        visitor.visit(this);

    }

}
