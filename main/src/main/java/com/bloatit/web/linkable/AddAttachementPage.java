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
package com.bloatit.web.linkable;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.PageNotFoundException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.model.Member;
import com.bloatit.model.UserContentInterface;
import com.bloatit.web.components.SideBarUserContentBlock;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.tools.BreadcrumbTools;
import com.bloatit.web.linkable.usercontent.AttachmentField;
import com.bloatit.web.linkable.usercontent.CreateUserContentPage;
import com.bloatit.web.url.AddAttachementActionUrl;
import com.bloatit.web.url.AddAttachementPageUrl;

@ParamContainer("usercontent/attachfile")
public final class AddAttachementPage extends CreateUserContentPage {

    public static final int FILE_MAX_SIZE_MIO = 2;

    @RequestParam(name = "user_content", message = @tr("I cannot find the content number: ''%value%''."))
    @NonOptional(@tr("You have to specify a content on which join the file."))
    private final UserContentInterface userContent;

    private final AddAttachementPageUrl url;

    public AddAttachementPage(final AddAttachementPageUrl url) {
        super(url);
        this.url = url;
        userContent = url.getUserContent();
    }

    @Override
    protected String createPageTitle() {
        return tr("Add an attachment to the release");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws PageNotFoundException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addRight(new SideBarUserContentBlock(userContent));

        layout.addLeft(generateReleaseCreationForm());

        return layout;
    }

    private HtmlElement generateReleaseCreationForm() {
        final HtmlDiv group = new HtmlDiv();
        final HtmlTitleBlock title = new HtmlTitleBlock(tr("Add a new attachment"), 1);
        final AddAttachementActionUrl formUrl = new AddAttachementActionUrl(getSession().getShortKey(), userContent);
        final HtmlForm form = new HtmlForm(formUrl.urlString());

        form.enableFileUpload();

        form.add(new AttachmentField(formUrl, FILE_MAX_SIZE_MIO + " Mio", false, false));
        form.add(new HtmlSubmit(tr("Submit")));

        group.add(title);
        title.add(form);
        return group;
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return AddAttachementPage.generateBreadcrumb(userContent);
    }

    private static Breadcrumb generateBreadcrumb(final UserContentInterface userContent) {
        final Breadcrumb breadcrumb = BreadcrumbTools.generateBreadcrumb(userContent);
        breadcrumb.pushLink(new AddAttachementPageUrl(userContent).getHtmlLink(tr("Add an attachment")));
        return breadcrumb;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to add a new attachment.");
    }
}
