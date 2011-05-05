package com.bloatit.web.linkable.money;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.model.Team;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.SideBarButton;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.pages.master.sidebar.TitleSideBarElementLayout;
import com.bloatit.web.url.AccountChargingProcessUrl;

public class SideBarLoadAccountBlock extends TitleSideBarElementLayout {

    public SideBarLoadAccountBlock(final Team asTeam) {
        setTitle(tr("Load account"));

        add(new HtmlParagraph(tr("You can charge your account with a credit card using the following link: ")));
        // TODO good URL
        final AccountChargingProcessUrl chargingAccountUrl = new AccountChargingProcessUrl();
        chargingAccountUrl.setTeam(asTeam);
        add(new SideBarButton(tr("Charge your account"), chargingAccountUrl, WebConfiguration.getImgAccountCharge()).asElement());
        add(new HtmlDefineParagraph(tr("Note: "),
                                    tr("We have charge to pay every time you charge your account, hence we will perceive our 10% commission, even if you withdraw the money as soon as you have loaded it.")));
    }
}