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

import com.bloatit.framework.Demand;
import com.bloatit.web.actions.ContributionAction;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.custom.HtmlIdeaSumary;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextArea;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.ContributePageUrl;
import com.bloatit.web.utils.url.ContributionActionUrl;

@ParamContainer("contribute")
public class ContributePage extends LoggedPage {

    @RequestParam(level = Level.ERROR)
    private final Demand targetIdea;

    @RequestParam(name = ContributionAction.AMOUNT_CODE, defaultValue = "", role = Role.SESSION)
    private final String contributionAmountParam;

    @RequestParam(name = ContributionAction.COMMENT_CODE, defaultValue = "", role = Role.SESSION)
    private final String contributionCommentParam;

    private final ContributePageUrl url;

    public ContributePage(final ContributePageUrl url) throws RedirectException {
        super(url);
        this.url = url;
        targetIdea = url.getTargetIdea();
        contributionAmountParam = url.getContributionAmountParam();
        contributionCommentParam = url.getContributionCommentParam();
    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {
        addNotifications(url.getMessages());
        if (url.getMessages().hasMessage(Level.ERROR)) {
            session.notifyList(url.getMessages());
            throw new RedirectException(Context.getSession().getLastStablePage());
        }

        final ContributionActionUrl formActionUrl = new ContributionActionUrl(targetIdea);

        final HtmlForm contribForm = new HtmlForm(formActionUrl.urlString());
        contribForm.setCssClass("padding_box");
        
        // Input field : choose amount
        final HtmlTextField contribField = new HtmlTextField(ContributionAction.AMOUNT_CODE);
        contribField.setLabel(Context.tr("Choose amount: "));
        contribField.setDefaultValue(contributionAmountParam);

        // Input field : comment
        final HtmlTextArea commentField = new HtmlTextArea(ContributionAction.COMMENT_CODE, 10, 60);
        commentField.setLabel(Context.tr("Comment (optional) : "));
        commentField.setDefaultValue(contributionCommentParam);

        final HtmlSubmit submitButton = new HtmlSubmit(Context.tr("Contribute"));

        
        
        // Create the form
        contribForm.add(contribField);
        contribForm.add(commentField);
        contribForm.add(submitButton);

        final HtmlTitleBlock contribTitle = new HtmlTitleBlock(Context.tr("Contribute"), 1);
        contribTitle.add(new HtmlIdeaSumary(targetIdea));
        contribTitle.add(contribForm);

        final HtmlDiv group = new HtmlDiv();
        group.add(contribTitle);

        return group;
    }

    @Override
    protected String getTitle() {
        return Context.tr("Contribute to a project");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to contribute");
    }
}
