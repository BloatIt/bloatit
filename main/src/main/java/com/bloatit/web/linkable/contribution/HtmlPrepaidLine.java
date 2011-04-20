package com.bloatit.web.linkable.contribution;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.linkable.members.MembersTools;

public class HtmlPrepaidLine extends HtmlDiv {

    public HtmlPrepaidLine(final Actor<?> actor) throws UnauthorizedOperationException {
        super("quotation_detail_line");

        add(MembersTools.getMemberAvatarSmall(actor));

        add(new HtmlDiv("quotation_detail_line_money").addText(Context.getLocalizator()
                                                                      .getCurrency(actor.getInternalAccount().getAmount())
                                                                      .getDefaultString()));
        add(new HtmlDiv().setCssClass("quotation_detail_line_money_image").add(new HtmlImage(new Image(WebConfiguration.getImgMoneyDownSmall()),
                                                                                             "money up")));
        add(new HtmlDiv("quotation_detail_line_money").addText(Context.getLocalizator().getCurrency(BigDecimal.ZERO).getDefaultString()));

        add(new HtmlDiv("quotation_detail_line_categorie").addText(tr("Prepaid from internal account")));

        final HtmlDiv amountBlock = new HtmlDiv("quotation_detail_line_amount");

        amountBlock.add(new HtmlDiv("quotation_detail_line_amount_money").addText(Context.getLocalizator()
                                                                                         .getCurrency(actor.getInternalAccount()
                                                                                                           .getAmount()
                                                                                                           .negate())
                                                                                         .getDecimalDefaultString()));

        add(amountBlock);
    }
}