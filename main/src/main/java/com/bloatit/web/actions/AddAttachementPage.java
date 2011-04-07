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
package com.bloatit.web.actions;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.PageNotFoundException;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FieldData;
import com.bloatit.framework.webserver.components.form.HtmlFileInput;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.UserContentInterface;
import com.bloatit.web.components.SideBarUserContentBlock;
import com.bloatit.web.linkable.meta.bugreport.SideBarBugReportBlock;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.pages.tools.BreadcrumbTools;
import com.bloatit.web.url.AddAttachementActionUrl;
import com.bloatit.web.url.AddAttachementPageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("usercontent/attachfile")
public final class AddAttachementPage extends LoggedPage {


    @RequestParam(name="user_content")
    UserContentInterface userContent;

    private final AddAttachementPageUrl url;

    public AddAttachementPage(final AddAttachementPageUrl url) {
        super(url);
        this.url = url;
        userContent = url.getUserContent();
    }

    @Override
    protected String getPageTitle() {
        return tr("Add an attachement to the release");
    }

    @Override
    public boolean isStable() {
        return false;
    }
    @Override
    public void processErrors() throws RedirectException {
    }
    @Override
    public HtmlElement createRestrictedContent() throws PageNotFoundException {
        addNotifications(url.getMessages());
        if (!url.getMessages().isEmpty()) {
            throw new PageNotFoundException();
        }
        final TwoColumnLayout layout = new TwoColumnLayout(true);
        layout.addRight(new SideBarUserContentBlock(userContent));
        layout.addRight(new SideBarBugReportBlock(url));
        layout.addLeft(generateReleaseCreationForm());

        return layout;
    }

    private HtmlElement generateReleaseCreationForm() {
        final HtmlTitleBlock title = new HtmlTitleBlock(tr("Add a new attachement"), 1);

        final AddAttachementActionUrl formUrl = new AddAttachementActionUrl(userContent);

        // Create the form stub
        final HtmlForm form = new HtmlForm(formUrl.urlString());
        form.enableFileUpload();

        title.add(form);


        // attachement

        final FieldData attachementDescriptiondData = formUrl.getAttachementDescriptionParameter().pickFieldData();
        final HtmlTextField attachementDescriptionInput = new HtmlTextField(attachementDescriptiondData.getName(),
                                                                            Context.tr("Attachment description"));
        attachementDescriptionInput.setDefaultValue(attachementDescriptiondData.getSuggestedValue());
        attachementDescriptionInput.addErrorMessages(attachementDescriptiondData.getErrorMessages());
        attachementDescriptionInput.setComment(tr("Mandatory"));
        form.add(attachementDescriptionInput);

        final FieldData attachedFileData = formUrl.getAttachementParameter().pickFieldData();
        final HtmlFileInput attachedFileInput = new HtmlFileInput(attachedFileData.getName(), tr("Attached file"));
        attachedFileInput.setDefaultValue(attachedFileData.getSuggestedValue());
        attachedFileInput.addErrorMessages(attachedFileData.getErrorMessages());
        attachedFileInput.setComment("You must attache a file. This is your release, it can take be a patch, a tar.gz etc.");
        form.add(attachedFileInput);

        form.add(new HtmlSubmit(tr("Submit")));

        final HtmlDiv group = new HtmlDiv();
        group.add(title);
        return group;
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return AddAttachementPage.generateBreadcrumb(userContent);
    }

    public static Breadcrumb generateBreadcrumb(UserContentInterface userContent) {
        Breadcrumb breadcrumb = BreadcrumbTools.generateBreadcrumb(userContent);

        breadcrumb.pushLink(new AddAttachementPageUrl(userContent).getHtmlLink(tr("Add an attachement")));

        return breadcrumb;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to add a new attachement.");
    }
}
