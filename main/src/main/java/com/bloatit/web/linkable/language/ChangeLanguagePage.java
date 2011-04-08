/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.language;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.UrlParameter;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.meta.bugreport.SideBarBugReportBlock;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ChangeLanguageActionUrl;
import com.bloatit.web.url.ChangeLanguagePageUrl;
import com.bloatit.web.url.IndexPageUrl;

@ParamContainer("language/change")
public class ChangeLanguagePage extends MasterPage {
    private ChangeLanguagePageUrl url;

    public ChangeLanguagePage(ChangeLanguagePageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected void doCreate() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateChangeLanguagePageMain());
        layout.addRight(new SideBarDocumentationBlock("change_language"));
        
        add(layout);
    }

    private HtmlElement generateChangeLanguagePageMain() {
        HtmlTitleBlock master = new HtmlTitleBlock("Change language", 1);
        ChangeLanguageActionUrl targetAction = new ChangeLanguageActionUrl();
        HtmlForm form = new HtmlForm(targetAction.urlString());
        master.add(form);
        UrlParameter<String, String> languageParameter = targetAction.getLanguageParameter();
        LanguageSelector language = new LanguageSelector(languageParameter.getName(), Context.tr("Choose your temporary language"));
        if (languageParameter.getDefaultValue() != null && !languageParameter.getDefaultValue().isEmpty()) {
            language.setDefaultValue(languageParameter.getDefaultValue());
        } else {
            language.setDefaultValue(Context.getLocalizator().getLanguageCode());
        }
        form.add(language);

        form.add(new HtmlSubmit("Change language"));
        return master;
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Change language");
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        Breadcrumb br = new Breadcrumb();
        br.pushLink(new IndexPageUrl().getHtmlLink(Context.tr("Index")));
        br.pushLink(url.getHtmlLink(Context.tr("Change language")));
        return br;
    }

    @Override
    public boolean isStable() {
        return false;
    }

}
