package com.bloatit.web.linkable.money;

import java.math.BigDecimal;

public class Quotation extends QuotationEntry {

    private final BigDecimal total;

    public Quotation(BigDecimal total) {
        super(null, null);
        this.total = total;

    }

    public static class QuotationTotalEntry extends QuotationEntry {

        private final String totalLabel;

        public QuotationTotalEntry(String label, String comment, String totalLabel) {
            super(label, comment);
            this.totalLabel = totalLabel;
        }

        @Override
        public BigDecimal getValue() {
            BigDecimal value = BigDecimal.ZERO;
            for (QuotationEntry entry : entries) {
                value = value.add(entry.getValue());
            }

            return value;
        }

        @Override
        public void accept(QuotationVisitor visitor) {
            visitor.visit(this);
        }

        public String getTotalLabel() {
            return totalLabel;
        }

    }

    public static class QuotationAmountEntry extends QuotationEntry {

        private final BigDecimal amount;

        public QuotationAmountEntry(String label, String comment, BigDecimal amount) {
            super(label, comment);
            this.amount = amount;
        }

        @Override
        public BigDecimal getValue() {
            return amount;
        }

        @Override
        public void accept(QuotationVisitor visitor) {
            visitor.visit(this);
        }

    }

    public static class QuotationProxyEntry extends QuotationEntry {

        private final QuotationEntry reference;

        public QuotationProxyEntry(String label, String comment, QuotationEntry reference) {
            super(label, comment);
            this.reference = reference;
        }

        @Override
        public BigDecimal getValue() {
            return reference.getValue();
        }

        @Override
        public void accept(QuotationVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class QuotationPercentEntry extends QuotationEntry {

        private final BigDecimal percent;
        private final QuotationEntry reference;

        public QuotationPercentEntry(String label, String comment, QuotationEntry reference, BigDecimal percent) {
            super(label, comment);
            this.reference = reference;
            this.percent = percent;
        }

        @Override
        public BigDecimal getValue() {
            return reference.getValue().multiply(percent).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }

        @Override
        public void accept(QuotationVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class QuotationDifferenceEntry extends QuotationEntry {

        private final QuotationEntry reference1;
        private final QuotationEntry reference2;

        public QuotationDifferenceEntry(String label, String comment, QuotationEntry reference1, QuotationEntry reference2) {
            super(label, comment);
            this.reference1 = reference1;
            this.reference2 = reference2;

        }

        @Override
        public BigDecimal getValue() {
            return reference1.getValue().subtract(reference2.getValue());
        }

        @Override
        public void accept(QuotationVisitor visitor) {
            visitor.visit(this);
        }

    }

    public static String padding(String output) {
        while (output.length() < 40) {
            output += " ";
        }
        return output;
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
    public void accept(QuotationVisitor visitor) {
        visitor.visit(this);

    }

}
