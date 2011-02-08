/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import java.math.BigDecimal;

import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FormFieldData;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlMoneyField;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.web.url.AccountChargingPageUrl;
import com.bloatit.web.url.PaylineActionUrl;

/**
 * A page used to put money onto the internal bloatit account
 */
@ParamContainer("charging")
public final class AccountChargingPage extends LoggedPage {

    public AccountChargingPage(final AccountChargingPageUrl url) {
        super(url);
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
    public HtmlElement createRestrictedContent() {

        final HtmlTitleBlock title = new HtmlTitleBlock(tr("Charge your account"), 1);

        title.add(new HtmlParagraph("utiliser la carte de test n° 4970100000325734 avec une date d’expiration valide et le cryptogramme visuel 123."));

        final PaylineActionUrl chargeActionUrl = new PaylineActionUrl();
        final HtmlForm form = new HtmlForm(chargeActionUrl.urlString());
        {
            FormFieldData<BigDecimal> loginData = chargeActionUrl.getAmountParameter().createFormFieldData();
            final HtmlMoneyField amount = new HtmlMoneyField(loginData, "Amount");
            final HtmlSubmit submit = new HtmlSubmit(tr("Submit"));

            form.add(amount);
            form.add(submit);
        }
        title.add(form);

        return title;
    }

    @Override
    public String getRefusalReason() {
        return "You need to login before you can charge your account";
    }
}
