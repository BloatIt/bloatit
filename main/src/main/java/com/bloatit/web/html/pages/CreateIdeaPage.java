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
package com.bloatit.web.html.pages;

import java.util.Map.Entry;

import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.actions.CreateIdeaAction;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.HtmlDropDown;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlFormBlock;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextArea;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.i18n.Localizator;
import com.bloatit.web.utils.i18n.Localizator.LanguageDescriptor;
import com.bloatit.web.utils.url.CreateIdeaActionUrl;
import com.bloatit.web.utils.url.CreateIdeaPageUrl;

/**
 * Page that hosts the form to create a new Idea
 */
@ParamContainer("idea/create")
public final class CreateIdeaPage extends LoggedPage {

	@RequestParam(name = CreateIdeaAction.DESCRIPTION_CODE, defaultValue = "", role = Role.SESSION)
	private final String description;

	@RequestParam(name = CreateIdeaAction.SPECIFICATION_CODE, defaultValue = "", role = Role.SESSION)
	private final String specification;

	@RequestParam(name = CreateIdeaAction.PROJECT_CODE, defaultValue = "", role = Role.SESSION)
	private final String project;

	@RequestParam(name = CreateIdeaAction.CATEGORY_CODE, defaultValue = "", role = Role.SESSION)
	private final String category;

	@SuppressWarnings("unused") // Will be used when language can be changed on Idea creation
    @RequestParam(name = CreateIdeaAction.LANGUAGE_CODE, defaultValue = "", role = Role.SESSION)
	private final String lang;

	public CreateIdeaPage(final CreateIdeaPageUrl createIdeaPageUrl) throws RedirectException {
		super(createIdeaPageUrl);
		this.description = createIdeaPageUrl.getDescription();
		this.specification = createIdeaPageUrl.getSpecification();
		this.project = createIdeaPageUrl.getProject();
		this.category = createIdeaPageUrl.getCategory();
		this.lang = createIdeaPageUrl.getLang();
	}

	@Override
	protected final String getTitle() {
		return "Create new idea";
	}

	@Override
	public final boolean isStable() {
		return false;
	}

	@Override
	public HtmlElement createRestrictedContent() {
		if (DemandManager.canCreate(session.getAuthToken())) {
			return generateIdeaCreationForm();
		} else {
			return generateBadRightError();
		}
	}

	private HtmlElement generateIdeaCreationForm() {
		HtmlTitleBlock createIdeaTitle = new HtmlTitleBlock(Context.tr("Create a new idea"), 1);
		final CreateIdeaActionUrl doCreateUrl = new CreateIdeaActionUrl();

		// Create the form stub
		HtmlForm createIdeaForm = new HtmlForm(doCreateUrl.urlString());
		HtmlFormBlock specifBlock = new HtmlFormBlock(Context.tr("Specify the new idea"));
		HtmlFormBlock paramBlock = new HtmlFormBlock(Context.tr("Parameters of the new idea"));

		createIdeaTitle.add(createIdeaForm);
		createIdeaForm.add(specifBlock);
		createIdeaForm.add(paramBlock);
		createIdeaForm.add(new HtmlSubmit(Context.tr("submit")));

		// Create the fields that will describe the description of the idea
		HtmlTextField descriptionInput = new HtmlTextField(CreateIdeaAction.DESCRIPTION_CODE, Context.tr("Title"));
		descriptionInput.setDefaultValue(description);
		descriptionInput.setComment(Context.tr("The title of the new idea must be permit to identify clearly the idea's specificity."));

		// Create the fields that will describe the specification of the idea
		HtmlTextArea specificationInput = new HtmlTextArea(CreateIdeaAction.SPECIFICATION_CODE, Context.tr("Describe the idea"), 10, 80);
		specificationInput.setDefaultValue(specification);
		specificationInput.setComment(Context.tr("Enter a long description of the idea : list all features, describe them all "
				+ "... Try to leave as little room for ambiguity as possible."));
		specifBlock.add(descriptionInput);
		specifBlock.add(specificationInput);

		// Create the fields that will be used to describe the parameters of the
		// idea (project ...)
		HtmlDropDown languageInput = new HtmlDropDown(CreateIdeaAction.LANGUAGE_CODE, Context.tr("Language"));
		for (Entry<String, LanguageDescriptor> langEntry : Localizator.getAvailableLanguages().entrySet()) {
			languageInput.add(langEntry.getValue().name, langEntry.getValue().code);
		}

		HtmlTextField categoryInput = new HtmlTextField(CreateIdeaAction.CATEGORY_CODE, Context.tr("Category"));
		categoryInput.setDefaultValue(category);
		HtmlTextField projectInput = new HtmlTextField(CreateIdeaAction.PROJECT_CODE, Context.tr("Project"));
		projectInput.setDefaultValue(project);
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
		return Context.tr("You must be logged to create a new idea.");
	}
}
