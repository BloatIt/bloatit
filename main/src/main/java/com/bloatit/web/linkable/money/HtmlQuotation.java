package com.bloatit.web.linkable.money;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.web.linkable.money.Quotation.QuotationAmountEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationDifferenceEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationPercentEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationProxyEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationTotalEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationVisitor;

public class HtmlQuotation extends HtmlDiv {

    private final Quotation quotation;

    public HtmlQuotation(Quotation quotation) {
        super("quotation_block");
        this.quotation = quotation;

        HtmlParagraph quotationBlock = new HtmlParagraph();

        quotationBlock.add(new QuotationRenderer(quotation, 0));

        add(quotationBlock);

    }

    public class QuotationRenderer extends HtmlDiv implements QuotationVisitor {

        private final int level;

        QuotationRenderer(QuotationEntry entry, int level) {
            this.level = level;
            entry.accept(this);
        }

        @Override
        public void visit(QuotationTotalEntry entry) {

            setCssClass("quotation_total_" + level);

            HtmlDiv line = new HtmlDiv("quotation_line_" + level);
            line.add(new HtmlDiv("quotation_label").addText(entry.getLabel()));
            line.add(new HtmlDiv("quotation_money").addText(Context.getLocalizator().getCurrency(entry.getValue()).getDecimalDefaultString()));
            add(line);

            if (!entry.isClosed()) {
                for (QuotationEntry childEntry : entry.getChildren()) {
                    add(new QuotationRenderer(childEntry, level + 1));
                }
            }

        }

        @Override
        public void visit(QuotationAmountEntry entry) {
            setCssClass("quotation_amount_" + level);

            HtmlDiv line = new HtmlDiv("quotation_line_" + level);
            line.add(new HtmlDiv("quotation_label").addText(entry.getLabel()));
            line.add(new HtmlDiv("quotation_money").addText(Context.getLocalizator().getCurrency(entry.getValue()).getDecimalDefaultString()));
            add(line);

            if (!entry.isClosed()) {
                for (QuotationEntry childEntry : entry.getChildren()) {
                    add(new QuotationRenderer(childEntry, level + 1));
                }
            }

        }

        @Override
        public void visit(QuotationProxyEntry entry) {
            setCssClass("quotation_proxy_" + level);

            HtmlDiv line = new HtmlDiv("quotation_line_" + level);
            line.add(new HtmlDiv("quotation_label").addText(entry.getLabel()));
            line.add(new HtmlDiv("quotation_money").addText(Context.getLocalizator().getCurrency(entry.getValue()).getDecimalDefaultString()));
            add(line);

            if (!entry.isClosed()) {
                for (QuotationEntry childEntry : entry.getChildren()) {
                    add(new QuotationRenderer(childEntry, level + 1));
                }
            }

        }

        @Override
        public void visit(QuotationPercentEntry entry) {
            setCssClass("quotation_percent_" + level);

            HtmlDiv line = new HtmlDiv("quotation_line_" + level);
            line.add(new HtmlDiv("quotation_label").addText(entry.getLabel()));
            line.add(new HtmlDiv("quotation_money").addText(Context.getLocalizator().getCurrency(entry.getValue()).getDecimalDefaultString()));
            add(line);

            if (!entry.isClosed()) {
                for (QuotationEntry childEntry : entry.getChildren()) {
                    add(new QuotationRenderer(childEntry, level + 1));
                }
            }
        }

        @Override
        public void visit(QuotationDifferenceEntry entry) {
            setCssClass("quotation_difference_" + level);

            HtmlDiv line = new HtmlDiv("quotation_line_" + level);
            line.add(new HtmlDiv("quotation_label").addText(entry.getLabel()));
            line.add(new HtmlDiv("quotation_money").addText(Context.getLocalizator().getCurrency(entry.getValue()).getDecimalDefaultString()));
            add(line);

            if (!entry.isClosed()) {
                for (QuotationEntry childEntry : entry.getChildren()) {
                    add(new QuotationRenderer(childEntry, level + 1));
                }
            }

        }

        @Override
        public void visit(Quotation quotation) {
            setCssClass("quotation_total_" + level);

            HtmlDiv line = new HtmlDiv("quotation_title_line_" + level);
            add(line);

            for (QuotationEntry childEntry : quotation.getChildren()) {
                add(new QuotationRenderer(childEntry, level + 1));
            }

            HtmlDiv totalLine = new HtmlDiv("quotation_line_" + level);
            totalLine.add(new HtmlDiv("quotation_total_label").addText(tr("Total to pay")));
            totalLine.add(new HtmlDiv("quotation_money").addText(Context.getLocalizator().getCurrency(quotation.getValue()).getDecimalDefaultString()));
            add(totalLine);
        }
    }
}
