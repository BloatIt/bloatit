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
package com.bloatit.web.linkable.bugs;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.util.EnumSet;

import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Bug;
import com.bloatit.model.Member;
import com.bloatit.web.pages.LoggedElveosPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ModifyBugActionUrl;
import com.bloatit.web.url.ModifyBugPageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("bugs/%bug%/modify")
public final class ModifyBugPage extends LoggedElveosPage {
    private static final int BUG_CHANGE_COMMENT_INPUT_NB_LINES = 5;
    private static final int BUG_CHANGE_COMMENT_INPUT_NB_COLUMNS = 80;

    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the bug number: ''%value%''."))
    @NonOptional(@tr("You have to specify a bug number."))
    private final Bug bug;
    private final ModifyBugPageUrl url;

    public ModifyBugPage(final ModifyBugPageUrl modifyBugPageUrl) {
        super(modifyBugPageUrl);
        this.url = modifyBugPageUrl;
        bug = modifyBugPageUrl.getBug();
    }

    @Override
    protected String createPageTitle() {
        return "Modify a bug";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateModifyBugForm());
        return layout;
    }

    private HtmlElement generateModifyBugForm() {
        final HtmlTitleBlock formTitle = new HtmlTitleBlock(Context.tr("Modify a bug"), 1);
        final ModifyBugActionUrl doModifyUrl = new ModifyBugActionUrl(bug);

        // Create the form stub
        final HtmlForm modifyBugForm = new HtmlForm(doModifyUrl.urlString());
        formTitle.add(modifyBugForm);

        // Level
        final FieldData levelFieldData = doModifyUrl.getLevelParameter().pickFieldData();
        final HtmlDropDown levelInput = new HtmlDropDown(levelFieldData.getName(), Context.tr("New Level"));
        levelInput.addErrorMessages(levelFieldData.getErrorMessages());
        levelInput.addDropDownElements(EnumSet.allOf(BindedLevel.class));
        final String suggestedLevel = levelFieldData.getSuggestedValue();
        if (suggestedLevel != null) {
            levelInput.setDefaultValue(suggestedLevel);
        } else {
            levelInput.setDefaultValue(BindedLevel.getBindedLevel(bug.getErrorLevel()).getLevel().toString());
        }
        levelInput.setComment(Context.tr("New level of the bug. Current level is ''{0}''.", BindedLevel.getBindedLevel(bug.getErrorLevel())
                                                                                                       .getDisplayName()));
        modifyBugForm.add(levelInput);

        // State
        final FieldData stateFieldData = doModifyUrl.getStateParameter().pickFieldData();
        final HtmlDropDown stateInput = new HtmlDropDown(stateFieldData.getName(), Context.tr("New state"));
        stateInput.addDropDownElements(EnumSet.allOf(BindedState.class));
        stateInput.addErrorMessages(stateFieldData.getErrorMessages());
        final String suggestedState = stateFieldData.getSuggestedValue();
        if (suggestedState != null) {
            stateInput.setDefaultValue(suggestedState);
        } else {
            stateInput.setDefaultValue(BindedState.getBindedState(bug.getState()).getState().toString());
        }

        stateInput.setComment(Context.tr("New state of the bug. Current state is ''{0}''.", BindedState.getBindedState(bug.getState())
                                                                                                       .getDisplayName()));
        modifyBugForm.add(stateInput);

        // Create the fields that will describe the reason of bug change
        final FieldData descriptionFieldData = doModifyUrl.getReasonParameter().pickFieldData();
        final HtmlTextArea descriptionInput = new HtmlTextArea(descriptionFieldData.getName(),
                                                               Context.tr("Reason"),
                                                               BUG_CHANGE_COMMENT_INPUT_NB_LINES,
                                                               BUG_CHANGE_COMMENT_INPUT_NB_COLUMNS);
        descriptionInput.addErrorMessages(descriptionFieldData.getErrorMessages());
        descriptionInput.setDefaultValue(descriptionFieldData.getSuggestedValue());
        descriptionInput.setComment(Context.tr("Optional. Enter the reason of the bug modification."));
        modifyBugForm.add(descriptionInput);

        modifyBugForm.add(new HtmlSubmit(Context.tr("Modify the bug")));

        final HtmlDiv group = new HtmlDiv();
        group.add(formTitle);
        return group;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to modify a bug.");
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return ModifyBugPage.generateBreadcrumb(bug);
    }

    private static Breadcrumb generateBreadcrumb(final Bug bug) {
        final Breadcrumb breadcrumb = BugPage.generateBreadcrumb(bug);
        breadcrumb.pushLink(new ModifyBugPageUrl(bug).getHtmlLink(tr("Modify")));
        return breadcrumb;
    }
}
