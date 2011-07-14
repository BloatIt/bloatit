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
package com.bloatit.web.linkable.meta.bugreport;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.meta.MetaBug;
import com.bloatit.framework.meta.MetaBugManager;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.ElveosPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.MetaBugEditPageUrl;
import com.bloatit.web.url.MetaBugsListPageUrl;
import com.bloatit.web.url.MetaEditBugActionUrl;

@ParamContainer("meta/bug/edit")
public final class MetaBugEditPage extends ElveosPage {

    private final MetaBugEditPageUrl url;

    @RequestParam
    private final String bugId;

    public MetaBugEditPage(final MetaBugEditPageUrl url) {
        super(url);
        this.url = url;
        this.bugId = url.getBugId();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock("Edit Bug", 1);

        final MetaEditBugActionUrl editBugActionUrl = new MetaEditBugActionUrl(getSession().getShortKey(), bugId);
        final HtmlForm form = new HtmlForm(editBugActionUrl.urlString());

        final FieldData descriptionFieldData = editBugActionUrl.getDescriptionParameter().pickFieldData();
        final HtmlTextArea bugDescription = new HtmlTextArea(descriptionFieldData.getName(), 20, 100);

        final String suggestedValue = descriptionFieldData.getSuggestedValue();
        if (suggestedValue != null) {
            bugDescription.setDefaultValue(suggestedValue);
        } else {
            final MetaBug byId = MetaBugManager.getById(bugId);
            if (byId == null) {
                Context.getSession().notifyWarning("The bug you selected doesn't exist");
                throw new RedirectException(new MetaBugsListPageUrl());
            }
            bugDescription.setDefaultValue(byId.getDescription());
        }

        bugDescription.setComment(tr("You can use markdown syntax in this field."));

        final HtmlSubmit submit = new HtmlSubmit(tr("Update the bug"));

        form.add(bugDescription);
        form.add(submit);
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
        return MetaBugEditPage.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new MembersListPageUrl().getHtmlLink(tr("Members")));

        return breadcrumb;
    }
}
