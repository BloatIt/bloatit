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

import java.util.HashMap;
import java.util.Map;

import com.bloatit.data.DaoBug.BugState;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.renderer.HtmlRawTextRenderer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Bug;
import com.bloatit.model.FileMetadata;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.usercontent.AttachmentField;
import com.bloatit.web.linkable.usercontent.CreateCommentForm;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.pages.tools.CommentTools;
import com.bloatit.web.url.AddAttachementActionUrl;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.CreateCommentActionUrl;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.ModifyBugPageUrl;

@ParamContainer("feature/bug")
public final class BugPage extends MasterPage {

    @ParamConstraint(optionalErrorMsg = @tr("You have to specify a bug number."))
    @RequestParam(name = "id", conversionErrorMsg = @tr("I cannot find the bug number: ''%value%''."))
    private final Bug bug;

    private final BugPageUrl url;

    public BugPage(final BugPageUrl url) {
        super(url);
        this.url = url;
        this.bug = url.getBug();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        HtmlTitle bugTitle;
        bugTitle = new HtmlTitle(bug.getTitle(), 1);
        layout.addLeft(bugTitle);

        layout.addLeft(new HtmlParagraph(tr("State: {0}", tr(BindedState.getBindedState(bug.getState()).getDisplayName()))));
        layout.addLeft(new HtmlParagraph(tr("Level: {0}", tr(BindedLevel.getBindedLevel(bug.getErrorLevel()).getDisplayName()))));

        layout.addLeft(new ModifyBugPageUrl(bug).getHtmlLink(tr("Modify the bug's properties")));

        final HtmlParagraph description = new HtmlParagraph(new HtmlRawTextRenderer(bug.getDescription()));
        layout.addLeft(description);

        // Attachments
        for (final FileMetadata attachment : bug.getFiles()) {
            final HtmlParagraph attachmentPara = new HtmlParagraph();
            attachmentPara.add(new FileResourceUrl(attachment).getHtmlLink(attachment.getFileName()));
            attachmentPara.addText(tr(": ") + attachment.getShortDescription());
            layout.addLeft(attachmentPara);
        }

        if (bug.isOwner()) {
            layout.addLeft(generateNewAttachementForm());
        }
        layout.addLeft(CommentTools.generateCommentList(bug.getComments(), generateBugFormatMap()));
        layout.addLeft(new CreateCommentForm(new CreateCommentActionUrl(bug)));

        return layout;
    }

    private Map<String, String> generateBugFormatMap() {
        final Map<String, String> formatMap = new HashMap<String, String>();

        formatMap.put("%REASON%", tr("Reason: "));
        formatMap.put("%LEVEL%", tr("Level: "));
        formatMap.put("%STATE%", tr("State: "));

        formatMap.put("%FATAL%", tr(BindedLevel.getBindedLevel(Level.FATAL).getDisplayName()));
        formatMap.put("%MAJOR%", tr(BindedLevel.getBindedLevel(Level.MAJOR).getDisplayName()));
        formatMap.put("%MINOR%", tr(BindedLevel.getBindedLevel(Level.MINOR).getDisplayName()));

        formatMap.put("%PENDING%", tr(BindedState.getBindedState(BugState.PENDING).getDisplayName()));
        formatMap.put("%DEVELOPING%", tr(BindedState.getBindedState(BugState.DEVELOPING).getDisplayName()));
        formatMap.put("%RESOLVED%", tr(BindedState.getBindedState(BugState.RESOLVED).getDisplayName()));

        return formatMap;
    }

    @Override
    protected String createPageTitle() {
        if (bug != null) {
            return tr("Bug - ") + bug.getTitle();
        }
        return tr("Bug - No bug");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    private HtmlElement generateNewAttachementForm() {
        final AddAttachementActionUrl targetUrl = new AddAttachementActionUrl(bug);
        final HtmlForm addAttachementForm = new HtmlForm(targetUrl.urlString());

        addAttachementForm.enableFileUpload();
        addAttachementForm.add(new AttachmentField(targetUrl, "2 Gio"));
        addAttachementForm.add(new HtmlSubmit(Context.tr("Add attachment")));
        return addAttachementForm;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return BugPage.generateBreadcrumb(bug);
    }

    public static Breadcrumb generateBreadcrumb(final Bug bug) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbBugs(bug.getFeature());
        breadcrumb.pushLink(new BugPageUrl(bug).getHtmlLink(tr("Bug #") + bug.getId()));
        return breadcrumb;
    }
}
