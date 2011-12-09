/*
 *
 */
package com.bloatit.web.linkable.contribution;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.bloatit.common.Log;
import com.bloatit.common.TemplateFile;
import com.bloatit.framework.utils.RandomString;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.advanced.HtmlScript;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable;
import com.bloatit.framework.webprocessor.components.javascript.JsShowHide;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.BankTransaction;
import com.bloatit.web.linkable.money.Quotation;
import com.bloatit.web.linkable.money.Quotation.QuotationAmountEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationDifferenceEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationMultiplyEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationPercentEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationProxyEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationTotalEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationVisitor;
import com.bloatit.web.linkable.money.QuotationEntry;
import com.bloatit.web.linkable.process.AccountProcess;
import com.bloatit.web.url.ChangePrepaidAmountActionUrl;
import com.bloatit.web.url.QuotationPageUrl;

public class HtmlTotalSummary extends HtmlTable {

    public HtmlTotalSummary(final StandardQuotation quotation,
                            final boolean showFeesDetails,
                            final QuotationPageUrl myUrl,
                            final AccountProcess process) {
        this(quotation, showFeesDetails, myUrl, null, null, null, process);
    }

    public HtmlTotalSummary(final StandardQuotation quotation,
                            final boolean showFeesDetails,
                            final QuotationPageUrl myUrl,
                            final BigDecimal staticAmount,
                            final HtmlElement variableField,
                            final HtmlBranch scriptContainer,
                            final AccountProcess process) {
        super(new HtmlTotalSummaryModel(quotation, showFeesDetails, myUrl, staticAmount, variableField, scriptContainer, process));
        this.setCssClass("quotation_totals_lines");

    }

    private static class HtmlTotalSummaryModel extends HtmlLineTableModel {

        private final LinkedHashMap<QuotationEntry, String> idQuotationMap = new LinkedHashMap<QuotationEntry, String>();
        private final LinkedHashMap<QuotationEntry, String> jsQuotationEntryReference = new LinkedHashMap<QuotationEntry, String>();
        private final RandomString rng = new RandomString(8);

        private int jsquotationEntryIndex = 0;
        private final StringBuilder quotationEntryString = new StringBuilder();

        public HtmlTotalSummaryModel(final StandardQuotation quotation,
                                     final boolean showFeesDetails,
                                     final QuotationPageUrl myUrl,
                                     final BigDecimal staticAmount,
                                     final HtmlElement variableField,
                                     final HtmlBranch scriptContainer,
                                     final AccountProcess process) {
            // Subtotal
            final HtmlTableLine subtotalLine = new HtmlTableLine();
            addLine(subtotalLine);
            subtotalLine.setCssClass("quotation_total_line");

            subtotalLine.addCell(new HtmlTableCell("label") {
                @Override
                public HtmlNode getBody() {
                    return new HtmlText(tr("Subtotal"));
                }
            });

            final HtmlTableCell quotationSubTotal = new HtmlTableCell("money") {
                @Override
                public HtmlNode getBody() {
                    return new HtmlText(Context.getLocalizator().getCurrency(quotation.subTotal.getValue()).getTwoDecimalEuroString());
                }
            };
            bind(quotationSubTotal, quotation.subTotal);
            subtotalLine.addCell(quotationSubTotal);

            // Fees
            final QuotationPageUrl showDetailUrl = myUrl.clone();
            showDetailUrl.setShowFeesDetails(!showFeesDetails);
            final HtmlLink showDetailLink = showDetailUrl.getHtmlLink(tr("details"));

            final HtmlTableLine feesHTLine = new HtmlTableLine();
            addLine(feesHTLine);
            feesHTLine.setCssClass("quotation_total_line");
            {
                final HtmlSpan detailSpan = new HtmlSpan("details");
                detailSpan.add(showDetailLink);

                feesHTLine.addCell(new HtmlTableCell("label") {
                    @Override
                    public HtmlNode getBody() {
                        return new HtmlMixedText(tr("Fees (10&nbsp;% + 0.30&nbsp;€) <0::>"), detailSpan);
                    }
                });

                final HtmlTableCell quotationFee = new HtmlTableCell("money") {
                    @Override
                    public HtmlNode getBody() {
                        return new HtmlText(Context.getLocalizator().getCurrency(quotation.fees.getValue()).getTwoDecimalEuroString());
                    }
                };
                bind(quotationFee, quotation.fees);
                feesHTLine.addCell(quotationFee);
            }

            // Fee details
            final HtmlTableLine feesDetailLine = new HtmlTableLine();
            addLine(feesDetailLine);
            feesDetailLine.setCssClass("quotation_total_line_details_block");

            final HtmlDiv feesDetailLabel = new HtmlDiv("quotation_total_line_details");
            {
                feesDetailLabel.add(new HtmlDiv().addText(tr("Bank fees (guidance only)")));
                feesDetailLabel.add(new HtmlDiv().addText(tr("Elveos's commission (guidance only)")));
                feesDetailLabel.add(new HtmlDiv().addText(tr("Taxes")));
            }

            final HtmlTableCell cellDetailsLabel = new HtmlTableCell("label") {
                @Override
                public HtmlNode getBody() {
                    return feesDetailLabel;
                }
            };
            feesDetailLine.addCell(cellDetailsLabel);

            final HtmlDiv feesDetailMoney = new HtmlDiv("quotation_total_line_details");
            {

                final HtmlDiv htmlDiv = new HtmlDiv("money");
                bind(htmlDiv, quotation.bankFees);
                feesDetailMoney.add(htmlDiv.addText(Context.getLocalizator().getCurrency(quotation.bankFees.getValue()).getTwoDecimalEuroString()));

                final HtmlDiv htmlDiv1 = new HtmlDiv("money");
                bind(htmlDiv1, quotation.elveosFees);
                feesDetailMoney.add(htmlDiv1.addText(Context.getLocalizator().getCurrency(quotation.elveosFees.getValue()).getTwoDecimalEuroString()));

                final HtmlDiv htmlDiv2 = new HtmlDiv("money");
                bind(htmlDiv2, quotation.taxesFees);
                feesDetailMoney.add(htmlDiv2.addText(Context.getLocalizator().getCurrency(quotation.taxesFees.getValue()).getTwoDecimalEuroString()));
            }

            final HtmlTableCell cellDetailsMoney = new HtmlTableCell("money") {
                @Override
                public HtmlNode getBody() {
                    return feesDetailMoney;
                }
            };
            feesDetailLine.addCell(cellDetailsMoney);

            // Add show/hide template
            final JsShowHide showHideFees = new JsShowHide(showDetailLink, showFeesDetails);
            showHideFees.addActuator(showDetailLink);
            showHideFees.addListener(feesDetailLine);
            showHideFees.apply();

            // Total TTC
            final HtmlTableLine totalTTCLine = new HtmlTableLine();
            addLine(totalTTCLine);
            totalTTCLine.setCssClass("quotation_total_line_total");

            totalTTCLine.addCell(new HtmlTableCell("label") {
                @Override
                public HtmlNode getBody() {
                    return new HtmlText(tr("Total TTC"));
                }
            });

            final HtmlTableCell quotationTotalTTC = new HtmlTableCell("money") {
                @Override
                public HtmlNode getBody() {
                    return new HtmlText(Context.getLocalizator().getCurrency(quotation.totalTTC.getValue()).getTwoDecimalEuroString());
                }
            };
            bind(quotationTotalTTC, quotation.totalTTC);
            totalTTCLine.addCell(quotationTotalTTC);

            // Total HT
            final HtmlTableLine totalHTLine = new HtmlTableLine();
            addLine(totalHTLine);
            totalHTLine.setCssClass("quotation_total_line_ht");

            totalHTLine.addCell(new HtmlTableCell("label") {
                @Override
                public HtmlNode getBody() {
                    return new HtmlText(tr("Total HT"));
                }
            });

            final HtmlTableCell quotationTotalHT = new HtmlTableCell("money") {
                @Override
                public HtmlNode getBody() {
                    return new HtmlText(Context.getLocalizator().getCurrency(quotation.totalHT.getValue()).getTwoDecimalEuroString());
                }
            };
            bind(quotationTotalHT, quotation.totalHT);
            totalHTLine.addCell(quotationTotalHT);

            if (staticAmount != null) {

                variableField.setId(rng.nextString());

                final HtmlScript quotationUpdateScript = new HtmlScript();

                final TemplateFile quotationUpdateScriptTemplate = new TemplateFile("quotation.js");
                quotationUpdateScriptTemplate.addNamedParameter("pre_total", staticAmount.toPlainString());
                quotationUpdateScriptTemplate.addNamedParameter("charge_field_id", variableField.getId());
                final ChangePrepaidAmountActionUrl changePrepaidAmountActionUrl = new ChangePrepaidAmountActionUrl(Context.getSession().getShortKey(),
                                                                                                                   process);
                changePrepaidAmountActionUrl.setSilent(true);
                quotationUpdateScriptTemplate.addNamedParameter("callback_url", changePrepaidAmountActionUrl.urlString());
                quotationUpdateScriptTemplate.addNamedParameter("commission_variable_rate", String.valueOf(BankTransaction.COMMISSION_VARIABLE_RATE));
                quotationUpdateScriptTemplate.addNamedParameter("commission_fix_rate", String.valueOf(BankTransaction.COMMISSION_FIX_RATE));
                quotationUpdateScriptTemplate.addNamedParameter("input_offset", "0");
                quotationUpdateScriptTemplate.addNamedParameter("output_offset", "5");
                quotationUpdateScriptTemplate.addNamedParameter("locale", "\"" + Context.getLocalizator().getLanguageCode() + "\"");

                for (final Entry<QuotationEntry, String> entryQuotation : idQuotationMap.entrySet()) {
                    export(entryQuotation.getKey(), entryQuotation.getValue());
                }

                quotationUpdateScriptTemplate.addNamedParameter("entry_list", quotationEntryString.toString());

                try {
                    quotationUpdateScript.append(quotationUpdateScriptTemplate.getContent(null));
                } catch (final IOException e) {
                    Log.web().error("Fail to generate quotation update script", e);
                }

                scriptContainer.add(quotationUpdateScript);
            }
        }

        private void bind(final HtmlElement element, final QuotationEntry entry) {
            final String id = rng.nextString();
            element.setId(id);
            idQuotationMap.put(entry, id);
        }

        private String getJsQuotationEntryReference(final QuotationEntry reference) {
            if (!jsQuotationEntryReference.containsKey(reference)) {
                export(reference, idQuotationMap.get(reference));
            }

            return jsQuotationEntryReference.get(reference);
        }

        private void export(final QuotationEntry key, final String value) {
            if (jsQuotationEntryReference.containsKey(key)) {
                return;
            }

            final String quotationEntryStringId = "entry" + jsquotationEntryIndex++;

            key.accept(new QuotationVisitor() {

                @Override
                public void visit(final Quotation quotation) {
                    quotationEntryString.append(quotationEntryStringId + " = new Quotation('" + value + "', " + quotation.total + ");\n");
                }

                @Override
                public void visit(final QuotationDifferenceEntry entry) {
                    quotationEntryString.append(quotationEntryStringId + " = new QuotationDifferenceEntry('" + value + "', "
                            + getJsQuotationEntryReference(entry.reference1) + ", " + getJsQuotationEntryReference(entry.reference2) + ");\n");
                }

                @Override
                public void visit(final QuotationPercentEntry entry) {
                    quotationEntryString.append(quotationEntryStringId + " = new QuotationPercentEntry('" + value + "', "
                            + getJsQuotationEntryReference(entry.reference) + ", " + entry.percent + ");\n");
                }

                @Override
                public void visit(final QuotationMultiplyEntry entry) {
                    quotationEntryString.append(quotationEntryStringId + " = new QuotationMultiplyEntry('" + value + "', "
                            + getJsQuotationEntryReference(entry.reference) + ", " + entry.multiplyBy + ");\n");
                }

                @Override
                public void visit(final QuotationProxyEntry entry) {
                    quotationEntryString.append(quotationEntryStringId + " = new QuotationProxyEntry('" + value + "', "
                            + getJsQuotationEntryReference(entry.reference) + ");\n");
                }

                @Override
                public void visit(final QuotationAmountEntry entry) {
                    quotationEntryString.append(quotationEntryStringId + " = new QuotationAmountEntry('" + value + "', " + entry.amount + ");\n");
                }

                @Override
                public void visit(final QuotationTotalEntry entry) {
                    quotationEntryString.append(quotationEntryStringId + " = new QuotationTotalEntry('" + value + "');\n");

                    for (final QuotationEntry childEntry : entry.getChildren()) {
                        quotationEntryString.append(quotationEntryStringId + ".addEntry(" + getJsQuotationEntryReference(childEntry) + ");\n");
                    }
                }

            });

            quotationEntryString.append("quotationEntries.push(" + quotationEntryStringId + ");\n");
            jsQuotationEntryReference.put(key, quotationEntryStringId);
        }
    }
}
