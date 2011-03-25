package com.bloatit.web.linkable.money;

import static com.bloatit.framework.webserver.Context.tr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.SessionManager;
import com.bloatit.framework.webserver.WebHeader;
import com.bloatit.framework.xcgiserver.HttpHeader;

public class Quotation {

    private final QuotationTotalEntry rootEntry;

    public Quotation() {
        rootEntry = new QuotationTotalEntry(tr("Quotation"), null, tr("Total to pay"));
        rootEntry.setClosed(false);
    }

    public QuotationTotalEntry getRootEntry() {
        return rootEntry;
    }

    public static abstract class QuotationEntry {
        private final String label;
        private final String comment;
        private boolean closed;
        protected final List<QuotationEntry> entries = new ArrayList<QuotationEntry>();

        public QuotationEntry(String label, String comment) {
            super();
            this.label = label;
            this.comment = comment;
            this.closed = true;
        }

        public boolean isClosed() {
            return closed;
        }

        public void setClosed(boolean closed) {
            this.closed = closed;
        }

        public abstract BigDecimal getValue();

        public String getLabel() {
            return label;
        }

        public String getComment() {
            return comment;
        }

        public void addEntry(QuotationEntry entry) {
            entries.add(entry);
        }

        public List<QuotationEntry> getChildren() {
            return entries;
        }

        public abstract void accept(QuotationVisitor visitor);

        public abstract void print(int indent);

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
        public void print(int indent) {
            String indentStr = "";
            for (int i = 0; i < indent * 4; i++) {
                indentStr += " ";
            }

            System.out.println(indentStr + (getLabel() != null ? getLabel() : "") + "    " + (getComment() != null ? getComment() : ""));

            for (QuotationEntry entry : entries) {
                entry.print(indent + 1);
            }

            String output = indentStr + totalLabel;
            while (output.length() < 40) {
                output += " ";
            }

            output += Context.getLocalizator().getCurrency(getValue()).getDecimalDefaultString();

            System.out.println(output);

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
        public void print(int indent) {
            String indentStr = "";
            for (int i = 0; i < indent * 4; i++) {
                indentStr += " ";
            }

            String output = indentStr + (getLabel() != null ? getLabel() : "") + "    " + (getComment() != null ? getComment() : "");
            output = padding(output);

            output += Context.getLocalizator().getCurrency(getValue()).getDecimalDefaultString();

            System.out.println(output);

            for (QuotationEntry entry : entries) {
                entry.print(indent + 1);
            }

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
        public void print(int indent) {
            String indentStr = "";
            for (int i = 0; i < indent * 4; i++) {
                indentStr += " ";
            }

            String output = indentStr + (getLabel() != null ? getLabel() : "") + "    " + (getComment() != null ? getComment() : "");
            output = padding(output);

            output += Context.getLocalizator().getCurrency(getValue()).getDecimalDefaultString();

            System.out.println(output);

            for (QuotationEntry entry : entries) {
                entry.print(indent + 1);
            }
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
        public void print(int indent) {
            String indentStr = "";
            for (int i = 0; i < indent * 4; i++) {
                indentStr += " ";
            }

            String output = indentStr + (getLabel() != null ? getLabel() : "") + "    " + (getComment() != null ? getComment() : "");
            output = padding(output);

            output += Context.getLocalizator().getCurrency(getValue()).getDecimalDefaultString();

            System.out.println(output);

            for (QuotationEntry entry : entries) {
                entry.print(indent + 1);
            }
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
        public void print(int indent) {
            String indentStr = "";
            for (int i = 0; i < indent * 4; i++) {
                indentStr += " ";
            }

            String output = indentStr + (getLabel() != null ? getLabel() : "") + "    " + (getComment() != null ? getComment() : "");
            output = padding(output);

            output += Context.getLocalizator().getCurrency(getValue()).getDecimalDefaultString();

            System.out.println(output);

            for (QuotationEntry entry : entries) {
                entry.print(indent + 1);
            }
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

    public static void main(String[] args) {

        int amount = 10;
        double partVariable = 0.1;
        double partFixe = 0.3;

        double fixBank = 0.30;
        double variableBank = 0.03;

        Context.reInitializeContext(new WebHeader(new HttpHeader(new HashMap<String, String>())), SessionManager.createSession());

        Quotation quotation = new Quotation();

        QuotationTotalEntry contributionTotal = new QuotationTotalEntry("Contributions", null, "Total before fees");
        QuotationAmountEntry missingAmount = new QuotationAmountEntry("missing amount", null, new BigDecimal(0));

        QuotationAmountEntry prepaid = new QuotationAmountEntry("prepaid", null, new BigDecimal(amount));
        contributionTotal.addEntry(missingAmount);
        contributionTotal.addEntry(prepaid);
        quotation.getRootEntry().addEntry(contributionTotal);

        QuotationTotalEntry feesTotal = new QuotationTotalEntry(null, null, null);

        QuotationPercentEntry percentFeesTotal = new QuotationPercentEntry("Fees", null, contributionTotal, new BigDecimal(partVariable));

        QuotationAmountEntry fixfeesTotal = new QuotationAmountEntry("Fees", null, new BigDecimal(partFixe));
        feesTotal.addEntry(percentFeesTotal);
        feesTotal.addEntry(fixfeesTotal);

        QuotationProxyEntry feesProxy = new QuotationProxyEntry("Fees", ""+partVariable*100+"% + "+partFixe+"€", feesTotal);

        // Fees details
        // Bank fees
        QuotationTotalEntry bankFeesTotal = new QuotationTotalEntry("Bank fees", null, "Total bank fees");

        QuotationAmountEntry fixBankFee = new QuotationAmountEntry("fix fee", null, new BigDecimal(fixBank));

        QuotationPercentEntry variableBankFee = new QuotationPercentEntry("variable fee", ""+variableBank*100+"%", quotation.getRootEntry(), new BigDecimal(variableBank));
        bankFeesTotal.addEntry(variableBankFee);
        bankFeesTotal.addEntry(fixBankFee);
        feesProxy.addEntry(bankFeesTotal);

        // Our fees
        QuotationDifferenceEntry commissionTTC = new QuotationDifferenceEntry("Elveos's commission TTC", null, feesTotal, bankFeesTotal);


        QuotationPercentEntry commissionHT = new QuotationPercentEntry("commission HT", null, commissionTTC, new BigDecimal(1/1.196));
        QuotationDifferenceEntry ourFeesTVA = new QuotationDifferenceEntry("TVA for commission", "19.6%", commissionTTC, commissionHT);
        commissionTTC.addEntry(commissionHT);
        commissionTTC.addEntry(ourFeesTVA);
        feesProxy.addEntry(commissionTTC);

        quotation.getRootEntry().addEntry(feesProxy);

        quotation.getRootEntry().print(0);

        System.out.println("Rendement: "
                + commissionHT.getValue()
                              .divide(quotation.getRootEntry().getValue(), BigDecimal.ROUND_HALF_EVEN)
                              .multiply(new BigDecimal(100))
                              .setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }


    public interface QuotationVisitor {
        void visit(QuotationTotalEntry entry);
        void visit(QuotationAmountEntry entry);
        void visit(QuotationProxyEntry entry);
        void visit(QuotationPercentEntry entry);
        void visit(QuotationDifferenceEntry entry);
    }

    public void check(BigDecimal rightAmount) {
        if(!rightAmount.equals(getRootEntry().getValue())) {
            throw new FatalErrorException("Erroned quotation. Expected: "+rightAmount+". Computed: "+getRootEntry().getValue());
        }
    }
}
