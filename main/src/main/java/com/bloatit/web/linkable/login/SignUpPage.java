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
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlCheckbox;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlEmailField;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webprocessor.components.form.HtmlPasswordField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.javascript.HtmlHiddenableDiv;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.components.HtmlElveosForm;
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
        final HtmlElveosForm form = new HtmlElveosForm(targetUrl.urlString());
        container.add(form);

        FormBuilder ftool = new FormBuilder(SignUpAction.class, targetUrl);

        ftool.add(form, new HtmlTextField(targetUrl.getLoginParameter().getName()));
        ftool.add(form, new HtmlPasswordField(targetUrl.getPasswordParameter().getName()));
        ftool.add(form, new HtmlEmailField(targetUrl.getEmailParameter().getName()));

        // Country
        final HtmlDropDown countryInput = new HtmlDropDown(targetUrl.getCountryParameter().getName());
        for (final Country entry : Country.getAvailableCountries()) {
            countryInput.addDropDownElement(entry.getCode(), entry.getName());
        }
        if (!ftool.suggestedValueChanged(countryInput)) {
            countryInput.setDefaultValue(Context.getLocalizator().getCountryCode());
        }
        ftool.add(form, countryInput);

        // Language
        FieldData langData = targetUrl.getLangParameter().pickFieldData();
        final LanguageSelector langInput = new LanguageSelector(targetUrl.getLangParameter().getName());
        langInput.setDefaultValue(langData.getSuggestedValue(), Context.getLocalizator().getLanguageCode());
        ftool.add(form, langInput);

        // Newsletter
        ftool.add(form, new HtmlCheckbox(targetUrl.getNewsletterParameter().getName(), LabelPosition.BEFORE));

        if (invoice != null && invoice) {
            final HtmlTitle invoicingTitle = new HtmlTitle(Context.tr("Invoicing informations"), 1);
            form.add(invoicingTitle);

            ftool.add(form, new HtmlTextField(targetUrl.getNameParameter().getName()));
            HtmlCheckbox isCompanyCheckbox = new HtmlCheckbox(targetUrl.getIsCompanyParameter().getName(), LabelPosition.BEFORE);
            ftool.add(form, isCompanyCheckbox);
            ftool.add(form, new HtmlTextField(targetUrl.getStreetParameter().getName()));
            ftool.add(form, new HtmlTextField(targetUrl.getExtrasParameter().getName()));
            ftool.add(form, new HtmlTextField(targetUrl.getCityParameter().getName()));
            ftool.add(form, new HtmlTextField(targetUrl.getPostalCodeParameter().getName()));

            HtmlHiddenableDiv hiddenableDiv = new HtmlHiddenableDiv(isCompanyCheckbox, false);
            form.add(hiddenableDiv);
            ftool.add(hiddenableDiv, new HtmlTextField(targetUrl.getTaxIdentificationParameter().getName()));
        }
        // Submit
        form.addSubmit(new HtmlSubmit(tr("Signup")));

        master.add(container);
        return master;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Signup");
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
}
