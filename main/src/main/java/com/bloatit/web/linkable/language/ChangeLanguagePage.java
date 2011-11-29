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

import java.util.Map.Entry;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.UrlParameter;
import com.bloatit.framework.xcgiserver.AvailableLocales;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.ElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ChangeLanguageActionUrl;
import com.bloatit.web.url.ChangeLanguagePageUrl;
import com.bloatit.web.url.IndexPageUrl;

@ParamContainer("language/change")
public final class ChangeLanguagePage extends ElveosPage {
    private final ChangeLanguagePageUrl url;

    public ChangeLanguagePage(final ChangeLanguagePageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateChangeLanguagePageMain());
        layout.addRight(new SideBarDocumentationBlock("change_language"));

        return layout;
    }

    private HtmlElement generateChangeLanguagePageMain() {
        final HtmlTitleBlock master = new HtmlTitleBlock(Context.tr("Change language"), 1);
        final HtmlParagraph help = new HtmlParagraph(Context.tr("The new language will be used during your navigation session on elveos. "
                + "If you close your navigator or disconnect your default language will be used again. "
                + "You can change your default language on your member page."));
        master.add(help);

        final ChangeLanguageActionUrl targetAction = new ChangeLanguageActionUrl();
        final HtmlForm form = new HtmlForm(targetAction.urlString());
        master.add(form);
        final UrlParameter<String, String> languageParameter = targetAction.getLanguageParameter();
        final LanguageSelector language = new LanguageSelector(languageParameter.getName(), Context.tr("Choose your temporary language"));
        if (languageParameter.getDefaultValue() != null && !languageParameter.getDefaultValue().isEmpty()) {
            language.setDefaultValue(languageParameter.getDefaultValue());
        } else {
            language.setDefaultValue(Context.getLocalizator().getLanguageCode());
        }
        form.add(language);

        form.add(new HtmlSubmit(Context.tr("Change language")));

        // Link map
        final HtmlDiv linkMap = new HtmlDiv("language_link_map");
        for (final Entry<String, String> langEntry : AvailableLocales.getAvailableLangs().entrySet()) {
            final ChangeLanguageActionUrl langagueChangeAction = new ChangeLanguageActionUrl();
            langagueChangeAction.setLanguage(langEntry.getKey());
            linkMap.add(langagueChangeAction.getHtmlLink(langEntry.getKey()));
        }

        master.add(linkMap);
        return master;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Change language");
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        final Breadcrumb br = new Breadcrumb();
        br.pushLink(new IndexPageUrl().getHtmlLink(Context.tr("Index")));
        br.pushLink(url.getHtmlLink(Context.tr("Change language")));
        return br;
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
