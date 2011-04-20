package com.bloatit.web.linkable.contribution;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.javascript.JsShowHide;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Payline;
import com.bloatit.web.url.QuotationPageUrl;

public class HtmlTotalSummary extends HtmlDiv {
    public HtmlTotalSummary(final StandardQuotation quotation, final boolean showFeesDetails, final QuotationPageUrl myUrl) {
        super("quotation_totals_lines");

        final HtmlDiv subtotal = new HtmlDiv("quotation_total_line");
        {
            subtotal.add(new HtmlDiv("label").addText(tr("Subtotal TTC")));
            subtotal.add(new HtmlDiv("money").addText(Context.getLocalizator()
                                                             .getCurrency(quotation.subTotalTTCEntry.getValue())
                                                             .getDecimalDefaultString()));
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
            feesHT.add(new HtmlDiv("money").addText(Context.getLocalizator().getCurrency(quotation.feesHT.getValue()).getDecimalDefaultString()));

        }
        add(feesHT);
        // Fee details
        final HtmlDiv feesDetail = new HtmlDiv("quotation_total_line_details_block");
        final HtmlDiv feesBank = new HtmlDiv("quotation_total_line_details");
        {
            feesBank.add(new HtmlDiv("label").addText(tr("Bank fees")));
            feesBank.add(new HtmlDiv("money").addText(Context.getLocalizator().getCurrency(quotation.bank.getValue()).getDecimalDefaultString()));
        }
        feesDetail.add(feesBank);

        final HtmlDiv elveosCommission = new HtmlDiv("quotation_total_line_details");
        {
            elveosCommission.add(new HtmlDiv("label").addText(tr("Elveos's commission")));
            elveosCommission.add(new HtmlDiv("money").addText(Context.getLocalizator()
                                                                     .getCurrency(quotation.commission.getValue())
                                                                     .getDecimalDefaultString()));

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
                                  Payline.COMMISSION_VARIABLE_RATE.multiply(new BigDecimal("100")),
                                  Context.getLocalizator().getCurrency(Payline.COMMISSION_FIX_RATE).getDecimalDefaultString()));

            feesTTC.add(new HtmlDiv("label").add(new HtmlMixedText(tr("Fees TTC <0::>"), detailSpan)));
            feesTTC.add(new HtmlDiv("money").addText(Context.getLocalizator().getCurrency(quotation.feesTTC.getValue()).getDecimalDefaultString()));
        }
        add(feesTTC);

        final HtmlDiv totalHT = new HtmlDiv("quotation_total_line_ht");
        {
            totalHT.add(new HtmlDiv("label").addText(tr("Total HT")));
            totalHT.add(new HtmlDiv("money").addText(Context.getLocalizator().getCurrency(quotation.totalHT.getValue()).getDecimalDefaultString()));
        }
        add(totalHT);

        final HtmlDiv totalTTC = new HtmlDiv("quotation_total_line_total");
        {
            totalTTC.add(new HtmlDiv("label").addText(tr("Total TTC")));
            totalTTC.add(new HtmlDiv("money").addText(Context.getLocalizator()
                                                             .getCurrency(quotation.totalTTC.getValue())
                                                             .getDecimalDefaultString()));
        }
        add(totalTTC);

    }

}