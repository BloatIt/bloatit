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

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FormFieldData;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlMoneyField;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Demand;
import com.bloatit.web.components.HtmlDemandSumary;
import com.bloatit.web.components.HtmlDemandSumary.Compacity;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;

/**
 * A page that hosts the form used to contribute on a Demand
 */
@ParamContainer("contribute")
public final class ContributePage extends LoggedPage {

    @RequestParam(level = Level.ERROR)
    private final Demand targetIdea;

    private final ContributePageUrl url;

    public ContributePage(final ContributePageUrl url) {
        super(url);
        this.url = url;
        targetIdea = url.getTargetIdea();
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
        FormFieldData<BigDecimal> amountFieldData = formActionUrl.getAmountParameter().formFieldData();
        final HtmlMoneyField contribField = new HtmlMoneyField(amountFieldData, tr("Choose amount: "));
        contribField.setComment(Context.tr("The minimun is 1€. Don't use cents."));

        // Input field : comment
        FormFieldData<String> commentFieldData = formActionUrl.getCommentParameter().formFieldData();
        final HtmlTextArea commentField = new HtmlTextArea(commentFieldData, tr("Comment: "), 10, 60);
        commentField.setComment(Context.tr("Optional. The comment will be publicly visible in the contribution list."));

        final HtmlSubmit submitButton = new HtmlSubmit(tr("Contribute"));

        // Create the form
        contribForm.add(contribField);
        contribForm.add(commentField);
        contribForm.add(submitButton);

        final HtmlTitleBlock contribTitle = new HtmlTitleBlock(tr("Contribute"), 1);
        contribTitle.add(new HtmlDemandSumary(targetIdea, Compacity.NORMAL));
        contribTitle.add(contribForm);

        final HtmlDiv group = new HtmlDiv();
        group.add(contribTitle);

        return group;
    }

    @Override
    protected String getPageTitle() {
        return tr("Contribute to a project");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to contribute");
    }
}
