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

import static com.bloatit.framework.webprocessor.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.i18n.Country;
import com.bloatit.framework.webprocessor.Context;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlPasswordField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.linkable.meta.bugreport.SideBarBugReportBlock;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.SignUpActionUrl;
import com.bloatit.web.url.SignUpPageUrl;

/**
 * <p>
 * Page used by users to create their bloatit accounts
 * </p>
 */
@ParamContainer("member/signup")
public final class SignUpPage extends MasterPage {

    @SuppressWarnings("unused")
    @RequestParam(name = SignUpAction.COUNTRY_CODE, role = Role.SESSION)
    @Optional("")
    private final String country;

    @SuppressWarnings("unused")
    @RequestParam(name = SignUpAction.LANGUAGE_CODE, role = Role.SESSION)
    @Optional("")
    private final String lang;

    private final SignUpPageUrl url;

    public SignUpPage(final SignUpPageUrl url) {
        super(url);
        this.url = url;
        this.country = url.getCountry();
        this.lang = url.getLang();
    }

    @Override
    protected void doCreate() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateSignUpPageMain());
        

        add(layout);
    }

    /**
     * @return
     */
    private HtmlElement generateSignUpPageMain() {
        final HtmlDiv master = new HtmlDiv();

        final HtmlTitleBlock container = new HtmlTitleBlock(Context.tr("Register"), 1);
        final SignUpActionUrl signUpActionUrl = new SignUpActionUrl();
        final HtmlForm form = new HtmlForm(signUpActionUrl.urlString());
        container.add(form);

        final FieldData loginFieldData = signUpActionUrl.getLoginParameter().pickFieldData();
        final HtmlTextField loginInput = new HtmlTextField(loginFieldData.getName(), Context.trc("Login (noun)", "Login"));
        loginInput.setDefaultValue(loginFieldData.getSuggestedValue());
        loginInput.setComment(Context.tr("When you login, case of login field be will be ignored"));
        loginInput.addErrorMessages(loginFieldData.getErrorMessages());
        form.add(loginInput);

        final FieldData passwordFieldData = signUpActionUrl.getPasswordParameter().pickFieldData();
        final HtmlPasswordField passwordInput = new HtmlPasswordField(passwordFieldData.getName(), Context.tr("Password"));
        passwordInput.addErrorMessages(passwordFieldData.getErrorMessages());
        form.add(passwordInput);

        final FieldData emailFieldData = signUpActionUrl.getEmailParameter().pickFieldData();
        final HtmlTextField emailInput = new HtmlTextField(emailFieldData.getName(), Context.tr("Email"));
        emailInput.setDefaultValue(emailFieldData.getSuggestedValue());
        emailInput.addErrorMessages(emailFieldData.getErrorMessages());
        form.add(emailInput);

        final HtmlDropDown countryInput = new HtmlDropDown(SignUpAction.COUNTRY_CODE, Context.tr("Country"));
        for (final Country entry : Country.getAvailableCountries()) {
            countryInput.addDropDownElement(entry.getCode(), entry.getName());
        }
        if (url.getCountryParameter().getStringValue() != null && !url.getCountryParameter().getStringValue().isEmpty()) {
            countryInput.setDefaultValue(url.getCountryParameter().getStringValue());
        } else {
            countryInput.setDefaultValue(Context.getLocalizator().getCountryCode());
        }
        form.add(countryInput);

        final LanguageSelector langInput = new LanguageSelector(SignUpAction.LANGUAGE_CODE, Context.tr("Language"));
        if (url.getLangParameter().getStringValue() != null && !url.getLangParameter().getStringValue().isEmpty()) {
            langInput.setDefaultValue(url.getLangParameter().getStringValue());
        } else {
            langInput.setDefaultValue(Context.getLocalizator().getLanguageCode());
        }
        form.add(langInput);

        final HtmlSubmit button = new HtmlSubmit(Context.tr("Submit"));
        form.add(button);

        master.add(container);
        return master;
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Sign-in");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return SignUpPage.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new SignUpPageUrl().getHtmlLink(tr("Sign-in")));

        return breadcrumb;
    }
}
