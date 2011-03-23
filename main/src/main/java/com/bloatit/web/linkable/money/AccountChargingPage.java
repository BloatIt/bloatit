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

import static com.bloatit.framework.webserver.Context.tr;

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FieldData;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlMoneyField;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.url.AccountChargingPageUrl;
import com.bloatit.web.url.PaylineActionUrl;

/**
 * A page used to put money onto the internal bloatit account
 */
@ParamContainer("charging")
public final class AccountChargingPage extends LoggedPage {

    public static final String CHARGE_AMOUNT_CODE = "amount";

    @Optional
    @RequestParam(name = CHARGE_AMOUNT_CODE, role = RequestParam.Role.GET)
    private final BigDecimal amount;

    public AccountChargingPage(final AccountChargingPageUrl url) {
        super(url);
        this.amount = url.getAmount();
    }

    @Override
    protected String getPageTitle() {
        return tr("Charge your account");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public void processErrors() throws RedirectException {
    }

    @Override
    public HtmlElement createRestrictedContent() {

        final HtmlTitleBlock title = new HtmlTitleBlock(tr("Charge your account"), 1);

        title.add(new HtmlParagraph("utiliser la carte de test n° 4970100000325734 avec une date d’expiration valide et le cryptogramme visuel 123."));

        final PaylineActionUrl chargeActionUrl = new PaylineActionUrl();
        final HtmlForm form = new HtmlForm(chargeActionUrl.urlString());
        {
            final FieldData amountData = chargeActionUrl.getAmountParameter().pickFieldData();
            final HtmlMoneyField amountInput = new HtmlMoneyField(amountData.getName(), "Amount");
            if(amount == null) {
                amountInput.setDefaultValue(amountData.getSuggestedValue());
            } else {
                amountInput.setDefaultValue(amount.toPlainString());

            }
            amountInput.addErrorMessages(amountData.getErrorMessages());
            final HtmlSubmit submit = new HtmlSubmit(tr("Submit"));

            form.add(amountInput);
            form.add(submit);
        }
        title.add(form);

        return title;
    }

    @Override
    public String getRefusalReason() {
        return "You need to login before you can charge your account";
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return AccountChargingPage.generateBreadcrumb(session.getAuthToken().getMember());
    }

    public static Breadcrumb generateBreadcrumb(Member member) {
        Breadcrumb breadcrumb = MemberPage.generateBreadcrumb(member);

        breadcrumb.pushLink(new AccountChargingPageUrl().getHtmlLink(tr("Charge account")));

        return breadcrumb;
    }
}
