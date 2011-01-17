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
package com.bloatit.web.html.pages;

import com.bloatit.web.actions.PaylineAction;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.AccountChargingPageUrl;
import com.bloatit.web.utils.url.PaylineActionUrl;

/**
 * A page used to put money onto the internal bloatit account
 */
@ParamContainer("charging")
public final class AccountChargingPage extends LoggedPage {

    public AccountChargingPage(final AccountChargingPageUrl url) {
        super(url);
    }

    @Override
    protected String getTitle() {
        return Context.tr("Charge your bloatit account");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent() {
        final HtmlTitleBlock title = new HtmlTitleBlock(Context.tr("Charge your account"), 1);

        title.add(new HtmlParagraph("utiliser la carte de test n° 4970100000325734 avec une date d’expiration valide et le cryptogramme visuel 123."));

        final PaylineActionUrl chargeActionUrl = new PaylineActionUrl();
        final HtmlForm form = new HtmlForm(chargeActionUrl.urlString());
        {
            final HtmlTextField amount = new HtmlTextField(PaylineAction.CHARGE_AMOUNT_CODE, "Amount");
            final HtmlSubmit submit = new HtmlSubmit(Context.tr("Submit"));

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
