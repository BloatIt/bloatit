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
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlHidden;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Description;
import com.bloatit.model.Member;
import com.bloatit.model.Translation;
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

        final HtmlDiv sourceColumn = new HtmlDiv("source_column");
        final HtmlDiv targetColumn = new HtmlDiv("target_column");

        layout.add(new HtmlTitle(Context.tr("Translate"), 1));
        layout.add(sourceColumn);
        layout.add(targetColumn);

        // Source
        sourceColumn.add(new HtmlTitle(Context.tr("Source language: {0}", sourceLanguage.getLocale().getDisplayLanguage()), 2));

        final HtmlTextField sourceTitleInput = new HtmlTextField("", tr("Title"));
        sourceTitleInput.setCssClass("input_long_400px");
        sourceTitleInput.addAttribute("readonly", "readonly");
        sourceTitleInput.setDefaultValue(sourceTranslation.getTitle());
        sourceColumn.add(sourceTitleInput);

        final HtmlTextArea sourceSpecificationInput = new HtmlTextArea("", tr("Description"), 30, 80);

        sourceSpecificationInput.addAttribute("readonly", "readonly");
        sourceSpecificationInput.setDefaultValue(sourceTranslation.getText());
        sourceColumn.add(sourceSpecificationInput);

        // Target
        targetColumn.add(new HtmlTitle(Context.tr("Target language: {0}", targetLanguage.getLocale().getDisplayLanguage()), 2));

        TranslateActionUrl translateUrl = new TranslateActionUrl(Context.getSession().getShortKey(),
                                                                 description,
                                                                 sourceLanguage.getLocale(),
                                                                 targetLanguage);
        HtmlForm translateForm = new HtmlForm(translateUrl.urlString());
        targetColumn.add(translateForm);

        final FieldData titleFieldData = translateUrl.getTitleParameter().pickFieldData();
        // Title of the description
        if (type == DescriptionType.FEATURE) {
            final HtmlTextField titleInput = new HtmlTextField(titleFieldData.getName(), tr("Title"));
            titleInput.addErrorMessages(titleFieldData.getErrorMessages());
            titleInput.setCssClass("input_long_400px");

            if (titleFieldData.getSuggestedValue() == null) {
                if (targetTranslation != null) {
                    titleInput.setDefaultValue(targetTranslation.getTitle());
                }
            } else {
                titleInput.setDefaultValue(titleFieldData.getSuggestedValue());
            }

            translateForm.add(titleInput);
        } else {
            HtmlHidden hidden = new HtmlHidden(titleFieldData.getName(), "John-Doe");
            translateForm.add(hidden);
        }

        // Content of the description
        final FieldData contentFieldData = translateUrl.getContentParameter().pickFieldData();
        final MarkdownEditor specificationInput = new MarkdownEditor(contentFieldData.getName(), tr("Description"), 30, 80);

        if (contentFieldData.getSuggestedValue() == null || contentFieldData.getSuggestedValue().isEmpty()) {
            if (targetTranslation != null) {
                specificationInput.setDefaultValue(targetTranslation.getText());
            }
        } else {
            specificationInput.setDefaultValue(contentFieldData.getSuggestedValue());
        }
        specificationInput.addErrorMessages(contentFieldData.getErrorMessages());
        translateForm.add(specificationInput);

        translateForm.add(new HtmlSubmit(Context.tr("Save translation")));

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
