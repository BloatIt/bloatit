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

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.i18n.Country;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlPasswordField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.UrlParameter;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.linkable.IndexPage;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.ElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.SignUpActionUrl;
import com.bloatit.web.url.SignUpPageUrl;

/**
 * <p>
 * Page used by users to create their bloatit accounts
 * </p>
 */
@ParamContainer(value = "member/signup", protocol = Protocol.HTTPS)
public final class SignUpPage extends ElveosPage {
    private final SignUpPageUrl url;

    @RequestParam
    @Optional
    private final Boolean invoice;

    public SignUpPage(final SignUpPageUrl url) {
        super(url);
        this.url = url;
        this.invoice = url.getInvoice();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateSignUpPageMain());
        layout.addRight(new SideBarDocumentationBlock("privacy"));

        return layout;
    }

    private HtmlElement generateSignUpPageMain() {
        final HtmlDiv master = new HtmlDiv();

        final HtmlTitleBlock container = new HtmlTitleBlock(Context.tr("Sign up"), 1);
        final SignUpActionUrl targetUrl = new SignUpActionUrl();
        targetUrl.setInvoice(invoice);
        final HtmlForm form = new HtmlForm(targetUrl.urlString());
        container.add(form);

        // Login
        final FieldData loginFieldData = targetUrl.getLoginParameter().pickFieldData();
        final HtmlTextField loginInput = new HtmlTextField(loginFieldData.getName(), Context.trc("Login (noun)", "Login"));
        loginInput.setDefaultValue(loginFieldData.getSuggestedValue());
        loginInput.setComment(Context.tr("When you login, case of login field be will be ignored."));
        loginInput.addErrorMessages(loginFieldData.getErrorMessages());
        form.add(loginInput);

        // Password
        final FieldData passwordFieldData = targetUrl.getPasswordParameter().pickFieldData();
        final HtmlPasswordField passwordInput = new HtmlPasswordField(passwordFieldData.getName(), tr("Password"));
        passwordInput.addErrorMessages(passwordFieldData.getErrorMessages());
        passwordInput.addAttribute("autocomplete", "off");
        passwordInput.setComment(Context.tr("7 characters minimum."));
        form.add(passwordInput);

        // Password check
        final FieldData passwordCheckFieldData = targetUrl.getPasswordCheckParameter().pickFieldData();
        final HtmlPasswordField passwordCheckInput = new HtmlPasswordField(passwordCheckFieldData.getName(), tr("Reenter your password"));
        passwordCheckInput.addErrorMessages(passwordCheckFieldData.getErrorMessages());
        passwordCheckInput.addAttribute("autocomplete", "off");
        form.add(passwordCheckInput);

        // Email
        final FieldData emailFieldData = targetUrl.getEmailParameter().pickFieldData();
        final HtmlTextField emailInput = new HtmlTextField(emailFieldData.getName(), tr("Email"));
        emailInput.setDefaultValue(emailFieldData.getSuggestedValue());
        emailInput.addErrorMessages(emailFieldData.getErrorMessages());
        form.add(emailInput);

        // Country
        final HtmlDropDown countryInput = new HtmlDropDown(targetUrl.getCountryParameter().getName(), tr("Country"));
        for (final Country entry : Country.getAvailableCountries()) {
            countryInput.addDropDownElement(entry.getCode(), entry.getName());
        }
        if (targetUrl.getCountryParameter().getStringValue() != null && !targetUrl.getCountryParameter().getStringValue().isEmpty()) {
            countryInput.setDefaultValue(targetUrl.getCountryParameter().getStringValue());
        } else {
            countryInput.setDefaultValue(Context.getLocalizator().getCountryCode());
        }
        form.add(countryInput);

        // Language
        final LanguageSelector langInput = new LanguageSelector(targetUrl.getLangParameter().getName(), tr("Language"));
        langInput.setDefaultValue(targetUrl.getLangParameter().getStringValue(), Context.getLocalizator().getLanguageCode());
        form.add(langInput);

        

        if (invoice != null && invoice) {
            
         // Invoice

            final HtmlTitle invoicingTitle = new HtmlTitle(Context.tr("Invoicing informations"), 1);
            form.add(invoicingTitle);

            // Name
            final FieldData nameData = targetUrl.getNameParameter().pickFieldData();
            
            final HtmlTextField nameInput = new HtmlTextField(nameData.getName(), Context.tr("Name"));
            nameInput.setDefaultValue(nameData.getSuggestedValue());
            nameInput.addErrorMessages(nameData.getErrorMessages());
            nameInput.setComment(Context.tr("Your full name"));
            form.add(nameInput);

            // Street
            form.add(generateTextField(targetUrl.getStreetParameter(),//
                                       Context.tr("Street")//
            ));

            // Extras
            form.add(generateTextField(targetUrl.getExtrasParameter(),//
                                       Context.tr("Extras"),//

                                       Context.tr("Optional.")));

            // City
            form.add(generateTextField(targetUrl.getCityParameter(),//
                                       Context.tr("City")//
            ));

            // Postal code
            form.add(generateTextField(targetUrl.getPostalCodeParameter(),//
                                       Context.tr("Postcode")//
            ));

        }
        // Submit
        final HtmlSubmit button = new HtmlSubmit(tr("Signup"));
        form.add(button);

        master.add(container);

        return master;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Sign-in");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return SignUpPage.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new SignUpPageUrl().getHtmlLink(tr("Sign-in")));

        return breadcrumb;
    }

    private HtmlTextField generateTextField(final UrlParameter<?, ?> parameter, final String name) {
        return generateTextField(parameter, name, null);
    }

    private HtmlTextField generateTextField(final UrlParameter<?, ?> parameter, final String name, final String comment) {
        final FieldData fieldData = parameter.pickFieldData();
        final HtmlTextField input = new HtmlTextField(fieldData.getName(), name);
        input.setDefaultValue(fieldData.getSuggestedValue());
        if (comment != null) {
            input.setComment(comment);
        }
        input.addErrorMessages(fieldData.getErrorMessages());
        return input;
    }
}
