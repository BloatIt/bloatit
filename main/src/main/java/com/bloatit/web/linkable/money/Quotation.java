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

    public final BigDecimal total;

    public Quotation(final BigDecimal total) {
        super();
        this.total = total;
    }

    public static class QuotationTotalEntry extends QuotationEntry {

        public QuotationTotalEntry() {
            super();
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
    }

    public static class QuotationAmountEntry extends QuotationEntry {

        public final BigDecimal amount;

        public QuotationAmountEntry(final BigDecimal amount) {
            super();
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

        public final QuotationEntry reference;

        public QuotationProxyEntry(final QuotationEntry reference) {
            super();
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

        public final BigDecimal percent;
        public final QuotationEntry reference;

        public QuotationPercentEntry(final QuotationEntry reference, final BigDecimal percent) {
            super();
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
    
    public static class QuotationMultiplyEntry extends QuotationEntry {
        
        public final BigDecimal multiplyBy;
        public final QuotationEntry reference;
        
        public QuotationMultiplyEntry(final QuotationEntry reference, final BigDecimal multiplyBy) {
            super();
            this.reference = reference;
            this.multiplyBy = multiplyBy;
        }
        
        @Override
        public BigDecimal getValue() {
            return reference.getValue().multiply(multiplyBy);
        }
        
        @Override
        public void accept(final QuotationVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class QuotationDifferenceEntry extends QuotationEntry {

        public final QuotationEntry reference1;
        public final QuotationEntry reference2;

        public QuotationDifferenceEntry(final QuotationEntry reference1, final QuotationEntry reference2) {
            super();
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

    public interface QuotationVisitor {
        void visit(QuotationTotalEntry entry);

        void visit(QuotationAmountEntry entry);

        void visit(QuotationProxyEntry entry);

        void visit(QuotationPercentEntry entry);
        
        void visit(QuotationMultiplyEntry entry);

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
