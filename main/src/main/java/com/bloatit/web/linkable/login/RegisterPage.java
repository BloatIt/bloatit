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
package com.bloatit.web.linkable.login;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.utils.i18n.Country;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FormFieldData;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlPasswordField;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.RegisterActionUrl;
import com.bloatit.web.url.RegisterPageUrl;

/**
 * <p>
 * Page used by users to create their bloatit accounts
 * </p>
 */
@ParamContainer("member/create")
public final class RegisterPage extends MasterPage {

    @SuppressWarnings("unused")
    @RequestParam(name = RegisterAction.COUNTRY_CODE, role = Role.SESSION)
    @Optional("")
    private final String country;

    @SuppressWarnings("unused")
    @RequestParam(name = RegisterAction.LANGUAGE_CODE, role = Role.SESSION)
    @Optional("")
    private final String lang;

    private final RegisterPageUrl url;

    public RegisterPage(final RegisterPageUrl url) {
        super(url);
        this.url = url;
        this.country = url.getCountry();
        this.lang = url.getLang();
    }

    @Override
    protected void doCreate() throws RedirectException {
        final HtmlDiv master = new HtmlDiv("padding_box");
        add(master);

        final HtmlTitleBlock container = new HtmlTitleBlock(Context.tr("Register"), 1);
        final RegisterActionUrl registerActionUrl = new RegisterActionUrl();
        final HtmlForm form = new HtmlForm(registerActionUrl.urlString());
        container.add(form);

        final FormFieldData loginFieldData = registerActionUrl.getLoginParameter().formFieldData();
        final HtmlTextField loginInput = new HtmlTextField(loginFieldData, Context.trc("Login (noun)", "Login"));
        form.add(loginInput);

        final FormFieldData passwordFieldData = registerActionUrl.getPasswordParameter().formFieldData();
        final HtmlPasswordField passwordInput = new HtmlPasswordField(passwordFieldData, Context.tr("Password"));
        form.add(passwordInput);

        final FormFieldData emailFieldData = registerActionUrl.getEmailParameter().formFieldData();
        final HtmlTextField emailInput = new HtmlTextField(emailFieldData, Context.tr("Email"));
        form.add(emailInput);

        final HtmlDropDown countryInput = new HtmlDropDown(RegisterAction.COUNTRY_CODE, Context.tr("Country"));
        for (final Country entry : Country.getAvailableCountries()) {
            countryInput.addDropDownElement(entry.getCode(), entry.getName());
        }
        countryInput.setDefaultValue(url.getCountryParameter().getStringValue());
        form.add(countryInput);

        final LanguageSelector langInput = new LanguageSelector(RegisterAction.LANGUAGE_CODE, Context.tr("Language"));
        langInput.setDefaultValue(url.getLangParameter().getStringValue());
        form.add(langInput);

        final HtmlSubmit button = new HtmlSubmit(Context.tr("Submit"));
        form.add(button);

        master.add(container);
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Make an offer");
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
