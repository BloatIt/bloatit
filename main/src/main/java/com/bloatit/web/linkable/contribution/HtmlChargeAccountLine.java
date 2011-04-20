package com.bloatit.web.linkable.contribution;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;

import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlForm.Method;
import com.bloatit.framework.webprocessor.components.form.HtmlMoneyField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.linkable.members.MembersTools;

public class HtmlChargeAccountLine extends HtmlDiv {

    public HtmlChargeAccountLine(final BigDecimal amountToCharge, final Actor<?> actor, final Url recalculateTargetForm) {
        super("quotation_detail_line");
        final Localizator localizator = Context.getLocalizator();

        add(MembersTools.getMemberAvatarSmall(actor));
        add(new HtmlDiv("quotation_detail_line_money").addText(localizator.getCurrency(BigDecimal.ZERO).getSimpleEuroString()));
        add(new HtmlDiv("quotation_detail_line_money_image").add(new HtmlImage(new Image(WebConfiguration.getImgMoneyUpSmall()), "money up")));
        add(new HtmlDiv("quotation_detail_line_money").addText(localizator.getCurrency(amountToCharge).getSimpleEuroString()));

        add(new HtmlDiv("quotation_detail_line_categorie").addText(tr("Internal account")));
        add(new HtmlDiv("quotation_detail_line_description").addText(tr("Load money in your internal account for future contributions.")));

        final HtmlDiv amountBlock;
        if (recalculateTargetForm == null) {
            amountBlock = new HtmlDiv("quotation_detail_line_amount");
            amountBlock.add(new HtmlDiv("quotation_detail_line_amount_money").addText(localizator.getCurrency(amountToCharge)
                                                                                                 .getTwoDecimalEuroString()));
        } else {
            amountBlock = new HtmlDiv("quotation_detail_line_field");
            final HtmlForm form = new HtmlForm(recalculateTargetForm.urlString(), Method.GET);

            final HtmlMoneyField moneyField = new HtmlMoneyField("preload");
            if (amountToCharge == null) {
                moneyField.setDefaultValue("0");
            } else {
                moneyField.setDefaultValue(amountToCharge.toPlainString());
            }

            final HtmlSubmit recalculate = new HtmlSubmit(tr("recalculate"));

            form.add(moneyField);
            form.add(recalculate);
            amountBlock.add(form);
        }
        add(amountBlock);
    }
}
