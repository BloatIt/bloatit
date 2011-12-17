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
package com.bloatit.web.linkable.meta.feedback;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.meta.MetaFeedback;
import com.bloatit.framework.meta.MetaFeedbackManager;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.linkable.IndexPage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.ElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.MetaEditFeedbackActionUrl;
import com.bloatit.web.url.MetaFeedbackEditPageUrl;
import com.bloatit.web.url.MetaFeedbackListPageUrl;

@ParamContainer("meta/feedback/edit")
public final class MetaFeedbackEditPage extends ElveosPage {

    private final MetaFeedbackEditPageUrl url;

    @RequestParam
    private final String feedbackId;

    public MetaFeedbackEditPage(final MetaFeedbackEditPageUrl url) {
        super(url);
        this.url = url;
        this.feedbackId = url.getFeedbackId();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock("Edit Feedback", 1);

        final MetaEditFeedbackActionUrl editBugActionUrl = new MetaEditFeedbackActionUrl(getSession().getShortKey(), feedbackId);
        final HtmlElveosForm form = new HtmlElveosForm(editBugActionUrl.urlString());

        final FieldData descriptionFieldData = editBugActionUrl.getDescriptionParameter().pickFieldData();
        final HtmlTextArea bugDescription = new HtmlTextArea(descriptionFieldData.getName(), 20, 100);

        final String suggestedValue = descriptionFieldData.getSuggestedValue();
        if (suggestedValue != null) {
            bugDescription.setDefaultValue(suggestedValue);
        } else {
            final MetaFeedback byId = MetaFeedbackManager.getById(feedbackId);
            if (byId == null) {
                Context.getSession().notifyWarning("The feedback you selected doesn't exist");
                throw new RedirectException(new MetaFeedbackListPageUrl());
            }
            bugDescription.setDefaultValue(byId.getDescription());
        }

        bugDescription.setComment(tr("You can use markdown syntax in this field."));

        form.add(bugDescription);
        form.addSubmit(new HtmlSubmit(tr("Update the feedback")));
        pageTitle.add(form);

        layout.addLeft(pageTitle);

        return layout;
    }

    @Override
    protected String createPageTitle() {
        return "Members list";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return MetaFeedbackEditPage.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new MembersListPageUrl().getHtmlLink(tr("Members")));

        return breadcrumb;
    }
}
