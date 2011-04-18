/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.money;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.url.AccountChargingPageUrl;

/**
 * A page used to put money onto the internal bloatit account
 */
@ParamContainer("charging")
public final class AccountChargingPage extends LoggedPage {

    @RequestParam
    AccountChargingProcess process;

    public AccountChargingPage(final AccountChargingPageUrl url) {
        super(url);
        this.process = url.getProcess();
    }

    @Override
    protected String createPageTitle() {
        return tr("Charge your account");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) {

        final HtmlTitleBlock title = new HtmlTitleBlock(tr("Charge your account"), 1);

        title.add(new HtmlParagraph("utiliser la carte de test n° 4970100000325734 avec une date d’expiration valide et le cryptogramme visuel 123."));

        /*
         * final PaylineActionUrl chargeActionUrl = new PaylineActionUrl();
         * final HtmlForm form = new HtmlForm(chargeActionUrl.urlString()); {
         * final FieldData amountData =
         * chargeActionUrl.getAmountParameter().pickFieldData(); final
         * HtmlMoneyField amountInput = new HtmlMoneyField(amountData.getName(),
         * "Amount"); if(process.getAmount() == null) {
         * amountInput.setDefaultValue(amountData.getSuggestedValue()); } else {
         * amountInput.setDefaultValue(process.getAmount().toPlainString()); }
         * amountInput.addErrorMessages(amountData.getErrorMessages()); final
         * HtmlSubmit submit = new HtmlSubmit(tr("Submit"));
         * form.add(amountInput); form.add(submit); } title.add(form);
         */

        return title;
    }

    @Override
    public String getRefusalReason() {
        return "You need to login before you can charge your account";
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return AccountChargingPage.generateBreadcrumb(session.getAuthToken().getMember(), process);
    }

    public static Breadcrumb generateBreadcrumb(final Member member, final AccountChargingProcess process) {
        final Breadcrumb breadcrumb = MemberPage.generateBreadcrumb(member);

        breadcrumb.pushLink(new AccountChargingPageUrl(process).getHtmlLink(tr("Charge account")));

        return breadcrumb;
    }
}
