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
package com.bloatit.web.html.pages;


import com.bloatit.framework.Demand;
import com.bloatit.web.actions.ContributionAction;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.HtmlButton;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlTextArea;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.utils.Message.Level;
import com.bloatit.web.utils.annotations.RequestParam;
import com.bloatit.web.utils.url.Request;
import com.bloatit.web.utils.url.UrlBuilder;

public class ContributePage extends LoggedPage {

    @RequestParam(level = Level.ERROR)
    private Demand targetIdea;

    @RequestParam(defaultValue = "vide")
    private String contributionAmountParam;

    @RequestParam(defaultValue = "vide")
    private String contributionCommentParam;

    public ContributePage(final Request request) throws RedirectException {
        super(request);
    }

    @Override
    public HtmlElement generateRestrictedContent() {

        final HtmlForm contribForm = new HtmlForm(new UrlBuilder(ContributionAction.class).buildUrl());

        // Input field : chose amount
        final HtmlTextField contribField = new HtmlTextField(ContributionAction.AMOUNT_CODE);
        contribField.setLabel(session.tr("Choose amount : "));
        contribField.setDefaultValue(contributionAmountParam);

        // Input field : comment
        final HtmlTextArea commentField = new HtmlTextArea(ContributionAction.COMMENT_CODE);
        commentField.setLabel(session.tr("Comment (optionnal) : "));
        commentField.setDefaultValue(contributionCommentParam);

        final HtmlButton submitButton = new HtmlButton(session.tr("Contribute"));

        // Summary of the idea
        final HtmlTitleBlock summary = new HtmlTitleBlock(targetIdea.getTitle());
        final HtmlText textSummary = new HtmlText(targetIdea.getDescription().toString());
        summary.add(textSummary);

        // Create the form
        contribForm.add(contribField);
        contribForm.add(commentField);
        contribForm.add(submitButton);

        final HtmlTitleBlock contribTitle = new HtmlTitleBlock(session.tr("Contribute"));
        contribTitle.add(summary);
        contribTitle.add(contribForm);

        final HtmlDiv group = new HtmlDiv();
        group.add(contribTitle);

        return group;
    }

    @Override
    protected String getTitle() {
        return session.tr("Contribute to a project");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public String getRefusalReason() {
        return session.tr("You must be logged to contribute");
    }
}
