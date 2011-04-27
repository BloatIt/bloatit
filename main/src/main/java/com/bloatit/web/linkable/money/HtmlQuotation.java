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

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.linkable.money.Quotation.QuotationAmountEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationDifferenceEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationPercentEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationProxyEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationTotalEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationVisitor;

public class HtmlQuotation extends HtmlDiv {

    public HtmlQuotation(final Quotation quotation) {
        super("quotation_block");
        final HtmlParagraph quotationBlock = new HtmlParagraph();
        quotationBlock.add(new QuotationRenderer(quotation, 0));
        add(quotationBlock);
    }

    public class QuotationRenderer extends HtmlDiv implements QuotationVisitor {

        private final int level;

        private QuotationRenderer(final QuotationEntry entry, final int level) {
            this.level = level;
            entry.accept(this);
        }

        @Override
        public void visit(final QuotationTotalEntry entry) {
            setCssClass("quotation_total_" + level);

            final HtmlDiv line = new HtmlDiv("quotation_line_" + level);
            line.add(new HtmlDiv("quotation_label").addText(entry.getLabel()));
            line.add(new HtmlDiv("quotation_money").addText(Context.getLocalizator().getCurrency(entry.getValue()).getTwoDecimalEuroString()));
            add(line);

            if (!entry.isClosed()) {
                for (final QuotationEntry childEntry : entry.getChildren()) {
                    add(new QuotationRenderer(childEntry, level + 1));
                }
            }

        }

        @Override
        public void visit(final QuotationAmountEntry entry) {
            setCssClass("quotation_amount_" + level);

            final HtmlDiv line = new HtmlDiv("quotation_line_" + level);
            line.add(new HtmlDiv("quotation_label").addText(entry.getLabel()));
            line.add(new HtmlDiv("quotation_money").addText(Context.getLocalizator().getCurrency(entry.getValue()).getTwoDecimalEuroString()));
            add(line);

            if (!entry.isClosed()) {
                for (final QuotationEntry childEntry : entry.getChildren()) {
                    add(new QuotationRenderer(childEntry, level + 1));
                }
            }

        }

        @Override
        public void visit(final QuotationProxyEntry entry) {
            setCssClass("quotation_proxy_" + level);

            final HtmlDiv line = new HtmlDiv("quotation_line_" + level);
            line.add(new HtmlDiv("quotation_label").addText(entry.getLabel()));
            line.add(new HtmlDiv("quotation_money").addText(Context.getLocalizator().getCurrency(entry.getValue()).getTwoDecimalEuroString()));
            add(line);

            if (!entry.isClosed()) {
                for (final QuotationEntry childEntry : entry.getChildren()) {
                    add(new QuotationRenderer(childEntry, level + 1));
                }
            }
        }

        @Override
        public void visit(final QuotationPercentEntry entry) {
            setCssClass("quotation_percent_" + level);

            final HtmlDiv line = new HtmlDiv("quotation_line_" + level);
            line.add(new HtmlDiv("quotation_label").addText(entry.getLabel()));
            line.add(new HtmlDiv("quotation_money").addText(Context.getLocalizator().getCurrency(entry.getValue()).getTwoDecimalEuroString()));
            add(line);

            if (!entry.isClosed()) {
                for (final QuotationEntry childEntry : entry.getChildren()) {
                    add(new QuotationRenderer(childEntry, level + 1));
                }
            }
        }

        @Override
        public void visit(final QuotationDifferenceEntry entry) {
            setCssClass("quotation_difference_" + level);

            final HtmlDiv line = new HtmlDiv("quotation_line_" + level);
            line.add(new HtmlDiv("quotation_label").addText(entry.getLabel()));
            line.add(new HtmlDiv("quotation_money").addText(Context.getLocalizator().getCurrency(entry.getValue()).getTwoDecimalEuroString()));
            add(line);

            if (!entry.isClosed()) {
                for (final QuotationEntry childEntry : entry.getChildren()) {
                    add(new QuotationRenderer(childEntry, level + 1));
                }
            }
        }

        @Override
        public void visit(final Quotation quotation) {
            setCssClass("quotation_total_" + level);

            final HtmlDiv line = new HtmlDiv("quotation_title_line_" + level);
            add(line);

            for (final QuotationEntry childEntry : quotation.getChildren()) {
                add(new QuotationRenderer(childEntry, level + 1));
            }

            final HtmlDiv totalLine = new HtmlDiv("quotation_line_" + level);
            totalLine.add(new HtmlDiv("quotation_total_label").addText(tr("Total to pay")));
            totalLine.add(new HtmlDiv("quotation_money").addText(Context.getLocalizator().getCurrency(quotation.getValue()).getTwoDecimalEuroString()));
            add(totalLine);
        }
    }
}
