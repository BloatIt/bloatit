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
package com.bloatit.web.linkable.translation;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.util.Locale;

import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownEditor;
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlHidden;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Description;
import com.bloatit.model.Member;
import com.bloatit.model.Translation;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.linkable.IndexPage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.url.TranslateActionUrl;
import com.bloatit.web.url.TranslatePageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("translation/%description%/translate")
public final class TranslatePage extends LoggedElveosPage {

    @RequestParam(role = Role.PAGENAME)
    @NonOptional(@tr("You have to specify a description to translate."))
    private final Description description;

    @RequestParam
    @NonOptional(@tr("You have to specify a source language."))
    private final Locale sourceLanguage;

    @RequestParam(role = Role.GET)
    @NonOptional(@tr("You have to specify a type of description."))
    private DescriptionType type;

    public enum DescriptionType {
        SOFTWARE, FEATURE
    }

    @SuppressWarnings("unused")
    private final TranslatePageUrl url;

    public TranslatePage(final TranslatePageUrl url) {
        super(url);
        this.url = url;
        description = url.getDescription();
        sourceLanguage = url.getSourceLanguage();
        type = url.getType();
    }

    @Override
    protected String createPageTitle() {
        return tr("Translate");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) {

        // Temporary language chooser
        Language sourceLanguage = description.getDefaultLanguage();
        Language targetLanguage;

        if (sourceLanguage.equals(Language.FR)) {
            targetLanguage = Language.EN;
        } else {
            targetLanguage = Language.FR;
        }

        Translation targetTranslation = description.getTranslation(targetLanguage);
        Translation sourceTranslation = description.getTranslation(sourceLanguage);

        final HtmlDiv layout = new HtmlDiv("translate_page");

        final HtmlBranch sourceColumn = new HtmlDiv("source_column");
        final HtmlDiv targetColumn = new HtmlDiv("target_column");

        layout.add(new HtmlTitle(Context.tr("Translate"), 1));
        layout.add(sourceColumn);
        layout.add(targetColumn);

        // Source
        sourceColumn.add(new HtmlTitle(Context.tr("Source language: {0}", sourceLanguage.getLocale().getDisplayLanguage()), 2));
        HtmlElveosForm sourceCol = new HtmlElveosForm("", false);
        sourceColumn.add(sourceCol);

        if (type == DescriptionType.FEATURE) {
            final HtmlTextField sourceTitleInput = new HtmlTextField("", tr("Title"));
            sourceTitleInput.addAttribute("readonly", "readonly");
            sourceTitleInput.setDefaultValue(sourceTranslation.getTitle());
            sourceTitleInput.setLong();
            sourceCol.add(sourceTitleInput);
        }

        final HtmlTextArea sourceSpecificationInput = new HtmlTextArea("", tr("Description"), 30, 80);

        sourceSpecificationInput.addAttribute("readonly", "readonly");
        sourceSpecificationInput.setDefaultValue(sourceTranslation.getText());
        sourceSpecificationInput.setLong();
        sourceCol.add(sourceSpecificationInput);

        // Target
        targetColumn.add(new HtmlTitle(Context.tr("Target language: {0}", targetLanguage.getLocale().getDisplayLanguage()), 2));

        TranslateActionUrl translateUrl = new TranslateActionUrl(Context.getSession().getShortKey(),
                                                                 description,
                                                                 sourceLanguage.getLocale(),
                                                                 targetLanguage);
        HtmlElveosForm form = new HtmlElveosForm(translateUrl.urlString(), false);
        targetColumn.add(form);
        FormBuilder ftool = new FormBuilder(TranslateAction.class, translateUrl);

        // Title of the description
        if (type == DescriptionType.FEATURE) {
            HtmlTextField title = new HtmlTextField(translateUrl.getTitleParameter().getName());
            ftool.add(form, title);
            if (targetTranslation != null) {
                ftool.setDefaultValueIfNeeded(title, targetTranslation.getTitle());
            }
        } else {
            HtmlHidden hidden = new HtmlHidden(translateUrl.getTitleParameter().getName(), "John-Doe-software");
            form.add(hidden);
        }

        // Content of the description
        final MarkdownEditor specificationInput = new MarkdownEditor(translateUrl.getContentParameter().getName(), 30, 80);
        ftool.add(form, specificationInput);
        if (targetTranslation != null) {
            ftool.setDefaultValueIfNeeded(specificationInput, targetTranslation.getText());
        }

        form.addSubmit(new HtmlSubmit(Context.tr("Save translation")));
        return layout;
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return TranslatePage.generateBreadcrumb(description, sourceLanguage, type);
    }

    private static Breadcrumb generateBreadcrumb(Description description, Locale sourceLanguage, DescriptionType type) {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new TranslatePageUrl(description, sourceLanguage, type).getHtmlLink(tr("Translation")));

        return breadcrumb;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to add a translation.");
    }
}
