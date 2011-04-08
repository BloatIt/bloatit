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
import com.bloatit.framework.meta.MetaBugManager;
import com.bloatit.framework.webprocessor.PageNotFoundException;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.MetaBugEditPageUrl;
import com.bloatit.web.url.MetaEditBugActionUrl;

@ParamContainer("meta/bug/edit")
public final class MetaBugEditPage extends MasterPage {

    private final MetaBugEditPageUrl url;

    @RequestParam
    private final String bugId;

    public MetaBugEditPage(final MetaBugEditPageUrl url) {
        super(url);
        this.url = url;
        this.bugId = url.getBugId();
    }

    @Override
    protected void doCreate() throws RedirectException {

        //TODO: why not add this message on all pages ?
        addNotifications(url.getMessages());
        if (!url.getMessages().isEmpty()) {
            throw new PageNotFoundException();
        }

        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock("Edit Bug", 1);


        final MetaEditBugActionUrl editBugActionUrl = new MetaEditBugActionUrl(bugId);
        final HtmlForm form = new HtmlForm(editBugActionUrl.urlString());



        final FieldData descriptionFieldData = editBugActionUrl.getDescriptionParameter().pickFieldData();
        final HtmlTextArea bugDescription = new HtmlTextArea(descriptionFieldData.getName(), 20, 100);

        final String suggestedValue = descriptionFieldData.getSuggestedValue();
        if(suggestedValue != null) {
            bugDescription.setDefaultValue(suggestedValue);
        } else {
            bugDescription.setDefaultValue(MetaBugManager.getById(bugId).getDescription());
        }

        bugDescription.setComment(tr("You can use markdown syntax in this field."));

        final HtmlSubmit submit = new HtmlSubmit(tr("Update the bug"));

        form.add(bugDescription);
        form.add(submit);
        pageTitle.add(form);


        layout.addLeft(pageTitle);

        

        add(layout);
    }

    @Override
    protected String getPageTitle() {
        return "Members list";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return MetaBugEditPage.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new MembersListPageUrl().getHtmlLink(tr("Members")));

        return breadcrumb;
    }
}
