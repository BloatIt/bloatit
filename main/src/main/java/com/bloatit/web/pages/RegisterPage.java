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

import java.util.Map.Entry;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.utils.i18n.Country;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.utils.i18n.Localizator.LanguageDescriptor;
import com.bloatit.framework.webserver.Context;
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
import com.bloatit.web.actions.RegisterAction;
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
    @RequestParam(name = RegisterAction.COUNTRY_CODE, defaultValue = "", role = Role.SESSION)
    private final String country;

    @SuppressWarnings("unused")
    @RequestParam(name = RegisterAction.LANGUAGE_CODE, defaultValue = "", role = Role.SESSION)
    private final String lang;

    public RegisterPage(final RegisterPageUrl url) {
        super(url);
        this.country = url.getCountry();
        this.lang = url.getLang();
    }

    @Override
    protected void doCreate() throws RedirectException {
        final HtmlDiv master = new HtmlDiv("padding_box");
        add(master);

        final HtmlTitleBlock container = new HtmlTitleBlock(Context.tr("Register"), 1);
        RegisterActionUrl registerActionUrl = new RegisterActionUrl();
        final HtmlForm form = new HtmlForm(registerActionUrl.urlString());
        container.add(form);

        FormFieldData<String> loginFieldData = registerActionUrl.getLoginParameter().createFormFieldData();
        final HtmlTextField loginInput = new HtmlTextField(loginFieldData, Context.trc("Login (noun)", "Login"));
        form.add(loginInput);

        FormFieldData<String> passwordFieldData = registerActionUrl.getPasswordParameter().createFormFieldData();
        final HtmlPasswordField passwordInput = new HtmlPasswordField(passwordFieldData, Context.tr("Password"));
        form.add(passwordInput);

        FormFieldData<String> emailFieldData = registerActionUrl.getEmailParameter().createFormFieldData();
        final HtmlTextField emailInput = new HtmlTextField(emailFieldData, Context.tr("Email"));
        form.add(emailInput);

        final HtmlDropDown<Country> countryInput = new HtmlDropDown<Country>(RegisterAction.COUNTRY_CODE, Context.tr("Country"));
        for (final Country entry : Country.getAvailableCountries()) {
            countryInput.add(entry);
        }
        form.add(countryInput);

        final HtmlDropDown<LanguageDescriptor> langInput = new HtmlDropDown<LanguageDescriptor>(RegisterAction.LANGUAGE_CODE, Context.tr("Language"));
        for (final Entry<String, LanguageDescriptor> entry : Localizator.getAvailableLanguages().entrySet()) {
            langInput.add(entry.getValue());
        }
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
