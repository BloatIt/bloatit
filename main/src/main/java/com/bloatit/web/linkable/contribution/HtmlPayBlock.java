package com.bloatit.web.linkable.contribution;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Team;

public class HtmlPayBlock extends HtmlDiv {
    public HtmlPayBlock(final StandardQuotation quotation, final Team team, final Url paymentUrl, final Url returnUrl) {
        super("pay_actions");
        final HtmlLink payContributionLink = paymentUrl.getHtmlLink(tr("Pay {0}", Context.getLocalizator()
                                                                                         .getCurrency(quotation.totalTTC.getValue())
                                                                                         .getTwoDecimalEuroString()));
        payContributionLink.setCssClass("button");

        add(new HtmlParagraph(Context.tr("You are using a beta version. Payment with real money is not activated."), "debug")).add(new HtmlParagraph(Context.tr("You can simulate it using this card number: 4970100000325734, and the security number: 123."),
                                                                                                                                                     "debug"));
        if (team != null) {
            add(new HtmlParagraph(Context.tr("You are using the account of ''{0}'' team.", team.getLogin()), "use_account"));
        }
        add(returnUrl.getHtmlLink(Context.tr("edit")));
        add(payContributionLink);
    }
}
