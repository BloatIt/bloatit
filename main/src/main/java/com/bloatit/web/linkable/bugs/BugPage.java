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

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.PageNotFoundException;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.form.FieldData;
import com.bloatit.framework.webserver.components.form.HtmlFileInput;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.framework.webserver.components.renderer.HtmlRawTextRenderer;
import com.bloatit.model.Bug;
import com.bloatit.model.FileMetadata;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.tools.CommentTools;
import com.bloatit.web.url.AddAttachementActionUrl;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.ModifyBugPageUrl;

@ParamContainer("feature/bug")
public final class BugPage extends MasterPage {

    public static final String BUG_FIELD_NAME = "id";

    @ParamConstraint(optionalErrorMsg = @tr("The id of the project is incorrect or missing"))
    @RequestParam(name = BUG_FIELD_NAME)
    private final Bug bug;

    private final BugPageUrl url;

    public BugPage(final BugPageUrl url) {
        super(url);
        this.url = url;
        this.bug = url.getBug();
    }

    @Override
    protected void doCreate() throws RedirectException {
        session.notifyList(url.getMessages());
        if (url.getMessages().hasMessage()) {
            throw new PageNotFoundException();
        }

        final HtmlDiv box = new HtmlDiv("padding_box");

        HtmlTitle bugTitle;
        bugTitle = new HtmlTitle(bug.getTitle(), 1);
        box.add(bugTitle);

        box.add(new HtmlParagraph(tr("State: {0}", BindedState.getBindedState(bug.getState()))));
        box.add(new HtmlParagraph(tr("Level: {0}", BindedLevel.getBindedLevel(bug.getErrorLevel()))));

        box.add(new ModifyBugPageUrl(bug).getHtmlLink(tr("Modify the bug's properties")));

        final HtmlParagraph description = new HtmlParagraph(new HtmlRawTextRenderer(bug.getDescription()));
        box.add(description);

        // Attachements

        for (final FileMetadata attachement : bug.getFiles()) {
            final HtmlParagraph attachementPara = new HtmlParagraph();
            attachementPara.add(new FileResourceUrl(attachement).getHtmlLink(attachement.getFileName()));
            attachementPara.addText(tr(": ") + attachement.getShortDescription());
            box.add(attachementPara);
        }

        if (bug.isOwner()) {
            box.add(generateNewAttachementForm());
        }

        box.add(CommentTools.generateCommentList(bug.getComments()));
        box.add(CommentTools.generateNewCommentComponent(bug));

        add(box);
    }

    @Override
    protected String getPageTitle() {
        if (bug != null) {
            return tr("Bug - ") + bug.getTitle();
        }
        return tr("Bug - No bug");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    private XmlNode generateNewAttachementForm() {
        final AddAttachementActionUrl addAttachementActionUrl = new AddAttachementActionUrl(bug);
        final HtmlForm addAttachementForm = new HtmlForm(addAttachementActionUrl.urlString());
        addAttachementForm.enableFileUpload();

        // File
        final HtmlFileInput attachementInput = new HtmlFileInput(ReportBugAction.ATTACHEMENT_CODE, Context.tr("Attachement file"));
        attachementInput.setComment("Optional. If attach a file, you must add an attachement description. Max 2go.");
        addAttachementForm.add(attachementInput);

        final FieldData attachementDescriptionFieldData = addAttachementActionUrl.getAttachementDescriptionParameter().pickFieldData();
        final HtmlTextField attachementDescriptionInput = new HtmlTextField(attachementDescriptionFieldData.getName(),
                                                                            Context.tr("Attachment description"));
        attachementDescriptionInput.setDefaultValue(attachementDescriptionFieldData.getSuggestedValue());
        attachementDescriptionInput.addErrorMessages(attachementDescriptionFieldData.getErrorMessages());
        attachementDescriptionInput.setComment(Context.tr("Need only if you add an attachement."));
        addAttachementForm.add(attachementDescriptionInput);
        addAttachementForm.add(new HtmlSubmit(Context.tr("Add attachement")));

        return addAttachementForm;
    }

}
