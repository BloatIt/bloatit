package com.bloatit.web.linkable.money;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.model.Actor;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.SideBarButton;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.pages.master.sidebar.TitleSideBarElementLayout;
import com.bloatit.web.url.WithdrawMoneyPageUrl;

public class SideBarWithdrawMoneyBlock extends TitleSideBarElementLayout {

    public SideBarWithdrawMoneyBlock(final Actor<?> me) {
        setTitle(tr("Withdraw money"));

        add(new HtmlParagraph(tr("You can withdraw money from you elveos account and get a wire transfer to your personal bank account using the following link:")));
        add(new SideBarButton(tr("Withdraw money"), new WithdrawMoneyPageUrl(me), WebConfiguration.getImgAccountWithdraw()).asElement());
        add(new HtmlDefineParagraph(tr("Note: "), tr("Do not withdraw money if you are planning to contribute to a project in the future, "
                + "this will prevent you from paying our commission again later.\n"
                + "Remember: You will have to pay the wire transfer fees (There is no fee in the euro zone).")));

    }
}
