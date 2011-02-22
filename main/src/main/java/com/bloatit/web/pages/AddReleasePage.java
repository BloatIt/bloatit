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
package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FormFieldData;
import com.bloatit.framework.webserver.components.form.HtmlFileInput;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.web.actions.AddProjectAction;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.url.AddProjectPageUrl;
import com.bloatit.web.url.AddReleaseActionUrl;

/**
 * Page that hosts the form to create a new Idea
 */
@ParamContainer("release/add")
public final class AddReleasePage extends LoggedPage {

    private static final int DESCRIPTION_INPUT_NB_LINES = 5;
    private static final int DESCRIPTION_INPUT_NB_COLUMNS = 80;

    public AddReleasePage(final AddProjectPageUrl addProjectPageUrl) {
        super(addProjectPageUrl);
    }

    @Override
    protected String getPageTitle() {
        return tr("Add a release");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent() {
        return generateReleaseCreationForm();
    }

    private HtmlElement generateReleaseCreationForm() {
        final HtmlTitleBlock createReleaseTitle = new HtmlTitleBlock(tr("Add a new Release"), 1);

        final AddReleaseActionUrl doCreateUrl = new AddReleaseActionUrl();

        // Create the form stub
        final HtmlForm form = new HtmlForm(doCreateUrl.urlString());
        form.enableFileUpload();

        createReleaseTitle.add(form);

        // Create the fields that will describe the descriptions of the project
        final FormFieldData<String> shortDescriptionFormFieldData = doCreateUrl.getShortDescriptionParameter().formFieldData();
        final HtmlTextArea shortDescriptionInput = new HtmlTextArea(shortDescriptionFormFieldData,
                                                                    tr("Comment your release"),
                                                                    DESCRIPTION_INPUT_NB_LINES,
                                                                    DESCRIPTION_INPUT_NB_COLUMNS);
        shortDescriptionInput.setComment(tr("Enter a short comment on your release."));
        form.add(shortDescriptionInput);

        // Language
        final FormFieldData<String> languageFormFieldData = doCreateUrl.getLangParameter().formFieldData();
        final LanguageSelector languageInput = new LanguageSelector(languageFormFieldData, tr("Language"));
        languageInput.setComment(tr("Language of the descriptions."));
        form.add(languageInput);

        final HtmlFileInput attachedFileInput = new HtmlFileInput(AddProjectAction.IMAGE_CODE, tr("Attached file"));
        attachedFileInput.setComment("You must attache a file. This is your release, it can take be a patch, a tar.gz etc.");
        form.add(attachedFileInput);

        form.add(new HtmlSubmit(tr("submit")));

        final HtmlDiv group = new HtmlDiv();
        group.add(createReleaseTitle);
        return group;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to add a new project.");
    }
}
