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
import com.bloatit.framework.webprocessor.components.javascript.JsShowHide;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.BankTransaction;
import com.bloatit.web.linkable.money.Quotation;
import com.bloatit.web.linkable.money.Quotation.QuotationAmountEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationDifferenceEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationPercentEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationProxyEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationTotalEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationVisitor;
import com.bloatit.web.linkable.money.QuotationEntry;
import com.bloatit.web.url.QuotationPageUrl;

public class HtmlTotalSummary extends HtmlDiv {

    private final LinkedHashMap<QuotationEntry, String> idQuotationMap = new LinkedHashMap<QuotationEntry, String>();
    private final LinkedHashMap<QuotationEntry, String> jsQuotationEntryReference = new LinkedHashMap<QuotationEntry, String>();
    private final RandomString rng = new RandomString(8);

    private int jsquotationEntryIndex = 0;
    private final StringBuilder quotationEntryString = new StringBuilder();


    public HtmlTotalSummary(final StandardQuotation quotation, final boolean showFeesDetails, final QuotationPageUrl myUrl) {
        this(quotation, showFeesDetails, myUrl, null, null);
    }

    public HtmlTotalSummary(final StandardQuotation quotation,
                            final boolean showFeesDetails,
                            final QuotationPageUrl myUrl,
                            final BigDecimal staticAmount,
                            final HtmlElement variableField) {
        super("quotation_totals_lines");


        final HtmlDiv subtotal = new HtmlDiv("quotation_total_line");
        {
            subtotal.add(new HtmlDiv("label").addText(tr("Subtotal TTC")));


            final HtmlDiv quotationSubTotalTTC = new HtmlDiv("money");
            bind(quotationSubTotalTTC,quotation.subTotalTTCEntry);
            subtotal.add(quotationSubTotalTTC.addText(Context.getLocalizator()
                                                             .getCurrency(quotation.subTotalTTCEntry.getValue())
                                                             .getTwoDecimalEuroString()));
        }
        add(subtotal);

        final QuotationPageUrl showDetailUrl = myUrl.clone();
        showDetailUrl.setShowFeesDetails(!showFeesDetails);
        final HtmlLink showDetailLink = showDetailUrl.getHtmlLink(tr("fees details"));

        final HtmlDiv feesHT = new HtmlDiv("quotation_total_line_ht");
        {

            final HtmlSpan detailSpan = new HtmlSpan("details");
            detailSpan.add(showDetailLink);

            feesHT.add(new HtmlDiv("label").add(new HtmlMixedText(tr("Fees HT <0::>"), detailSpan)));
            final HtmlDiv htmlDiv = new HtmlDiv("money");
            bind(htmlDiv, quotation.feesHT);
            feesHT.add(htmlDiv.addText(Context.getLocalizator().getCurrency(quotation.feesHT.getValue()).getTwoDecimalEuroString()));

        }
        add(feesHT);
        // Fee details
        final HtmlDiv feesDetail = new HtmlDiv("quotation_total_line_details_block");
        final HtmlDiv feesBank = new HtmlDiv("quotation_total_line_details");
        {
            feesBank.add(new HtmlDiv("label").addText(tr("Bank fees")));
            final HtmlDiv htmlDiv = new HtmlDiv("money");
            bind(htmlDiv, quotation.bank);
            feesBank.add(htmlDiv.addText(Context.getLocalizator().getCurrency(quotation.bank.getValue()).getTwoDecimalEuroString()));
        }
        feesDetail.add(feesBank);

        final HtmlDiv elveosCommission = new HtmlDiv("quotation_total_line_details");
        {
            elveosCommission.add(new HtmlDiv("label").addText(tr("Elveos's commission")));
            final HtmlDiv htmlDiv = new HtmlDiv("money");
            bind(htmlDiv, quotation.commission);
            elveosCommission.add(htmlDiv.addText(Context.getLocalizator()
                                                                     .getCurrency(quotation.commission.getValue())
                                                                     .getTwoDecimalEuroString()));

        }
        feesDetail.add(elveosCommission);
        add(feesDetail);

        // Add show/hide template
        final JsShowHide showHideFees = new JsShowHide(showFeesDetails);
        showHideFees.addActuator(showDetailLink);
        showHideFees.addListener(feesDetail);
        showHideFees.apply();

        final HtmlDiv feesTTC = new HtmlDiv("quotation_total_line");
        {

            final HtmlSpan detailSpan = new HtmlSpan("details");
            detailSpan.addText(tr("({0}% + {1})",
                                  BankTransaction.COMMISSION_VARIABLE_RATE.multiply(new BigDecimal("100")),
                                  Context.getLocalizator().getCurrency(BankTransaction.COMMISSION_FIX_RATE).getTwoDecimalEuroString()));

            feesTTC.add(new HtmlDiv("label").add(new HtmlMixedText(tr("Fees TTC <0::>"), detailSpan)));
            final HtmlDiv htmlDiv = new HtmlDiv("money");
            bind(htmlDiv, quotation.feesTTC);
            feesTTC.add(htmlDiv.addText(Context.getLocalizator().getCurrency(quotation.feesTTC.getValue()).getTwoDecimalEuroString()));
        }
        add(feesTTC);

        final HtmlDiv totalHT = new HtmlDiv("quotation_total_line_ht");
        {
            totalHT.add(new HtmlDiv("label").addText(tr("Total HT")));
            final HtmlDiv htmlDiv = new HtmlDiv("money");
            bind(htmlDiv, quotation.totalHT);
            totalHT.add(htmlDiv.addText(Context.getLocalizator().getCurrency(quotation.totalHT.getValue()).getTwoDecimalEuroString()));
        }
        add(totalHT);

        final HtmlDiv totalTTC = new HtmlDiv("quotation_total_line_total");
        {
            totalTTC.add(new HtmlDiv("label").addText(tr("Total TTC")));
            final HtmlDiv htmlDiv = new HtmlDiv("money");
            bind(htmlDiv, quotation.totalTTC);
            totalTTC.add(htmlDiv.addText(Context.getLocalizator().getCurrency(quotation.totalTTC.getValue()).getTwoDecimalEuroString()));
        }
        add(totalTTC);

        if (staticAmount != null) {

            variableField.setId(rng.nextString());

            final HtmlScript quotationUpdateScript = new HtmlScript();

            final TemplateFile quotationUpdateScriptTemplate = new TemplateFile("quotation.js");
            quotationUpdateScriptTemplate.addNamedParameter("pre_total", staticAmount.toPlainString());
            quotationUpdateScriptTemplate.addNamedParameter("charge_field_id", variableField.getId());
            quotationUpdateScriptTemplate.addNamedParameter("fallback_url", myUrl.urlString());
            quotationUpdateScriptTemplate.addNamedParameter("commission_variable_rate", String.valueOf(BankTransaction.COMMISSION_VARIABLE_RATE));
            quotationUpdateScriptTemplate.addNamedParameter("commission_fix_rate", String.valueOf(BankTransaction.COMMISSION_FIX_RATE));
            quotationUpdateScriptTemplate.addNamedParameter("input_offset", "0");
            quotationUpdateScriptTemplate.addNamedParameter("output_offset", "5");



            for (final Entry<QuotationEntry, String> entryQuotation : idQuotationMap.entrySet()) {

                export(entryQuotation.getKey(), entryQuotation.getValue());



            }



            quotationUpdateScriptTemplate.addNamedParameter("entry_list", quotationEntryString.toString());

            try {
                quotationUpdateScript.append(quotationUpdateScriptTemplate.getContent(null));
            } catch (final IOException e) {
                Log.web().error("Fail to generate quotation update script", e);
            }

            add(quotationUpdateScript);



        }

    }

    private void export(final QuotationEntry key, final String value) {

        if(jsQuotationEntryReference.containsKey(key)) {
            return;
        }

        final String quotationEntryStringId = "entry"+jsquotationEntryIndex++;


        key.accept(new QuotationVisitor() {

            @Override
            public void visit(final Quotation quotation) {
                quotationEntryString.append(quotationEntryStringId + " = new Quotation('"+value+"', "+quotation.total+");\n");
            }

            @Override
            public void visit(final QuotationDifferenceEntry entry) {
                quotationEntryString.append(quotationEntryStringId + " = new QuotationDifferenceEntry('"+value+"', "+getJsQuotationEntryReference(entry.reference1)+", "+getJsQuotationEntryReference(entry.reference2)+");\n");
            }

            @Override
            public void visit(final QuotationPercentEntry entry) {
                quotationEntryString.append(quotationEntryStringId + " = new QuotationPercentEntry('"+value+"', "+getJsQuotationEntryReference(entry.reference)+", "+entry.percent+");\n");
            }

            @Override
            public void visit(final QuotationProxyEntry entry) {
                quotationEntryString.append(quotationEntryStringId + " = new QuotationProxyEntry('"+value+"', "+getJsQuotationEntryReference(entry.reference)+");\n");
            }

            @Override
            public void visit(final QuotationAmountEntry entry) {
                quotationEntryString.append(quotationEntryStringId + " = new QuotationAmountEntry('"+value+"', "+entry.amount+");\n");
            }

            @Override
            public void visit(final QuotationTotalEntry entry) {
                quotationEntryString.append(quotationEntryStringId + " = new QuotationTotalEntry('"+value+"');\n");

                for(final QuotationEntry childEntry : entry.getChildren()) {
                    quotationEntryString.append(quotationEntryStringId + ".addEntry("+getJsQuotationEntryReference(childEntry)+");\n");
                }
            }
        });

        quotationEntryString.append("quotationEntries.push("+quotationEntryStringId+");\n");
        jsQuotationEntryReference.put(key, quotationEntryStringId);

    }

    private String getJsQuotationEntryReference(final QuotationEntry reference) {
        if(!jsQuotationEntryReference.containsKey(reference)) {
            export(reference, idQuotationMap.get(reference));
        }


        return jsQuotationEntryReference.get(reference);

    }

    private void bind(final HtmlDiv div, final QuotationEntry entry) {
        final String id = rng.nextString();
        div.setId(id);
        idQuotationMap.put(entry, id);

    }

}
