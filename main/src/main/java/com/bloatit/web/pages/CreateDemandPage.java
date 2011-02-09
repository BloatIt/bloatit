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

import static com.bloatit.framework.webserver.Context.tr;

import java.util.Map.Entry;

import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.utils.i18n.Localizator.LanguageDescriptor;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FormFieldData;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlFormBlock;
import com.bloatit.framework.webserver.components.form.HtmlSimpleDropDown;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.demand.DemandManager;
import com.bloatit.web.actions.CreateDemandAction;
import com.bloatit.web.url.CreateDemandActionUrl;
import com.bloatit.web.url.CreateDemandPageUrl;

/**
 * Page that hosts the form to create a new Idea
 */
@ParamContainer("demand/create")
public final class CreateDemandPage extends LoggedPage {

    private static final int SPECIF_INPUT_NB_LINES = 10;
    private static final int SPECIF_INPUT_NB_COLUMNS = 80;

    public CreateDemandPage(final CreateDemandPageUrl createIdeaPageUrl) {
        super(createIdeaPageUrl);
    }

    @Override
    protected String getPageTitle() {
        return "Create new idea";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent() {
        if (DemandManager.canCreate(session.getAuthToken())) {
            return new HtmlDiv("padding_box").add(generateIdeaCreationForm());
        }
        return generateBadRightError();
    }

    private HtmlElement generateIdeaCreationForm() {
        final HtmlTitleBlock createIdeaTitle = new HtmlTitleBlock(tr("Create a new idea"), 1);
        final CreateDemandActionUrl doCreateUrl = new CreateDemandActionUrl();

        // Create the form stub
        final HtmlForm createIdeaForm = new HtmlForm(doCreateUrl.urlString());
        final HtmlFormBlock specifBlock = new HtmlFormBlock(tr("Specify the new idea"));
        final HtmlFormBlock paramBlock = new HtmlFormBlock(tr("Parameters of the new idea"));

        createIdeaTitle.add(createIdeaForm);
        createIdeaForm.add(specifBlock);
        createIdeaForm.add(paramBlock);
        createIdeaForm.add(new HtmlSubmit(tr("submit")));

        // Create the fields that will describe the description of the idea
        FormFieldData<String> descriptionFieldData = doCreateUrl.getDescriptionParameter().createFormFieldData();
        final HtmlTextField descriptionInput = new HtmlTextField(descriptionFieldData, tr("Title"));
        descriptionInput.setComment(tr("The title of the new idea must be permit to identify clearly the idea's specificity."));

        // Create the fields that will describe the specification of the idea
        FormFieldData<String> specificationFieldData = doCreateUrl.getSpecificationParameter().createFormFieldData();
        final HtmlTextArea specificationInput = new HtmlTextArea(specificationFieldData, tr("Describe the idea"), SPECIF_INPUT_NB_LINES,
                SPECIF_INPUT_NB_COLUMNS);
        specificationInput.setComment(tr("Enter a long description of the idea : list all features, describe them all "
                + "... Try to leave as little room for ambiguity as possible."));
        specifBlock.add(descriptionInput);
        specifBlock.add(specificationInput);

        // Create the fields that will be used to describe the parameters of the
        // idea (project ...)
        final HtmlSimpleDropDown languageInput = new HtmlSimpleDropDown(CreateDemandAction.LANGUAGE_CODE, tr("Language"));
        for (final Entry<String, LanguageDescriptor> langEntry : Localizator.getAvailableLanguages().entrySet()) {
            languageInput.add(langEntry.getValue().name, langEntry.getValue().code);
        }

        FormFieldData<String> categoryFieldData = doCreateUrl.getCategoryParameter().createFormFieldData();
        final HtmlTextField categoryInput = new HtmlTextField(categoryFieldData, tr("Category"));
        FormFieldData<String> projectFieldData = doCreateUrl.getProjectParameter().createFormFieldData();
        final HtmlTextField projectInput = new HtmlTextField(projectFieldData, tr("Project"));
        paramBlock.add(languageInput);
        paramBlock.add(categoryInput);
        paramBlock.add(projectInput);

        final HtmlDiv group = new HtmlDiv();
        group.add(createIdeaTitle);
        return group;
    }

    private HtmlElement generateBadRightError() {
        final HtmlDiv group = new HtmlDiv();

        return group;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to create a new idea.");
    }
}
