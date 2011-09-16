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

import java.util.Locale;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Description;
import com.bloatit.model.Member;
import com.bloatit.model.Translation;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.TranslateActionUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("translation/%description%/dotranslate")
public final class TranslateAction extends LoggedElveosAction {

    @RequestParam(role = Role.PAGENAME)
    @NonOptional(@tr("You have to specify a description to translate."))
    private final Description description;

    @RequestParam
    @NonOptional(@tr("You have to specify a source language."))
    private final Locale sourceLanguage;

    @RequestParam
    @NonOptional(@tr("You have to specify a target language."))
    private final Language targetLanguage;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You forgot to write a title"))
    @MinConstraint(min = 10, message = @tr("The title must have at least %constraint% chars."))
    @MaxConstraint(max = 80, message = @tr("The title must be %constraint% chars length max."))
    private final String title;

    @NonOptional(@tr("You forgot to write a description"))
    @MinConstraint(min = 10, message = @tr("The description must have at least %constraint% chars."))
    @MaxConstraint(max = 800000, message = @tr("The description must be %constraint% chars length max."))
    @RequestParam(role = Role.POST)
    private String content;

    private final TranslateActionUrl url;

    public TranslateAction(final TranslateActionUrl url) {
        super(url);
        this.url = url;
        this.description = url.getDescription();
        sourceLanguage = url.getSourceLanguage();
        targetLanguage = url.getTargetLanguage();
        title = url.getTitle();
        content = url.getContent();
    }

    @Override
    public Url doProcessRestricted(final Member me) {

        Translation translation = description.getTranslation(targetLanguage);
        try {
            if (translation == null) {
                // The translation don't exist
                description.addTranslation(me, null, targetLanguage, title, content);
            } else {

                translation.setTitle(title);
                translation.setText(content, me);

            }
        } catch (UnauthorizedOperationException e) {
            throw new BadProgrammerException("Fail to update translation");
        }

        return session.pickPreferredPage();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        if (targetLanguage.equals(description.getDefaultLanguage())) {
            session.notifyError(Context.tr("You cannot translate into the reference language"));
            return session.getLastStablePage();
        }

        return NO_ERROR;
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to write a translation.");
    }

    @Override
    protected Url doProcessErrors() {
        return session.getLastVisitedPage();
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getTitleParameter());
        session.addParameter(url.getContentParameter());
        session.addParameter(url.getSourceLanguageParameter());
        session.addParameter(url.getTargetLanguageParameter());        
    }

}
