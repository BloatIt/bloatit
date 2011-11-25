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
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.advanced.HtmlClearer;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.javascript.JsShowHide;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.renderer.HtmlCachedMarkdownRenderer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Bug;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.right.Action;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.features.FeatureTabPane;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.ElveosPage;
import com.bloatit.web.linkable.master.HtmlDefineParagraph;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.tools.CommentTools;
import com.bloatit.web.linkable.usercontent.AttachmentField;
import com.bloatit.web.linkable.usercontent.CommentForm;
import com.bloatit.web.url.AddAttachementActionUrl;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.CreateCommentActionUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.ModifyBugPageUrl;

@ParamContainer("bugs/%bug%")
public final class BugPage extends ElveosPage {

    private static final int FILE_MAX_SIZE_MIO = 2;

    @NonOptional(@tr("You have to specify a bug number."))
    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the bug number: ''%value%''."))
    private final Bug bug;

    @SuppressWarnings("unused")
    @RequestParam(role = Role.PRETTY, generatedFrom = "bug")
    @Optional("Title")
    private final String title;

    private final BugPageUrl url;

    public BugPage(final BugPageUrl url) {
        super(url);
        this.url = url;
        this.bug = url.getBug();
        title = url.getTitle();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        layout.addRight(new SideBarFeatureBlock(bug.getFeature()));

        final HtmlDiv bugListDiv = new HtmlDiv("bug_list");
        layout.addLeft(bugListDiv);
        final FeaturePageUrl featurePageUrl = new FeaturePageUrl(bug.getFeature(), FeatureTabKey.bugs);
        featurePageUrl.setAnchor(FeatureTabPane.FEATURE_TAB_PANE);
        bugListDiv.add(new HtmlParagraph(featurePageUrl.getHtmlLink(tr("Return to bugs list"))));

        if (bug.canAccessErrorLevel(Action.WRITE) && bug.canAccessStatus(Action.WRITE)) {
            bugListDiv.add(new HtmlParagraph(new ModifyBugPageUrl(bug).getHtmlLink(tr("Modify the bug's properties"))));
        }

        HtmlTitle bugTitle;
        bugTitle = new HtmlTitle(bug.getTitle(), 1);
        layout.addLeft(bugTitle);

        // Details
        final HtmlDiv bugDetails = new HtmlDiv("bug_details");
        layout.addLeft(bugDetails);
        bugDetails.add(new HtmlDefineParagraph(Context.tr("Reported by: "), bug.getAuthor().getDisplayName()));
        bugDetails.add(new HtmlDefineParagraph(Context.tr("State: "), tr(BindedState.getBindedState(bug.getState()).getDisplayName())));
        bugDetails.add(new HtmlDefineParagraph(Context.tr("Level: "), tr(BindedLevel.getBindedLevel(bug.getErrorLevel()).getDisplayName())));
        bugDetails.add(new HtmlDefineParagraph(Context.tr("Creation date: "), Context.getLocalizator()
                                                                                     .getDate(bug.getCreationDate())
                                                                                     .toString(FormatStyle.MEDIUM)));

        final HtmlDiv description = new HtmlDiv("bug_description");
        description.add(new HtmlCachedMarkdownRenderer(bug.getDescription()));
        layout.addLeft(description);

        // Attachments
        final PageIterable<FileMetadata> files = bug.getFiles();
        if (files.size() > 0 || bug.getRights().isOwner()) {

            final HtmlDiv attachmentDiv = new HtmlDiv();
            layout.addLeft(attachmentDiv);
            attachmentDiv.setCssClass("bug_attachements");
            attachmentDiv.add(new HtmlTitle("Attachements", 3));

            for (final FileMetadata attachment : files) {
                final HtmlParagraph attachmentPara = new HtmlParagraph();
                attachmentPara.add(new FileResourceUrl(attachment).getHtmlLink(attachment.getFileName()));
                attachmentPara.addText(tr(": ") + attachment.getShortDescription());
                attachmentDiv.add(attachmentPara);
            }

            if (bug.getRights().isOwner()) {

                final HtmlParagraph newAttachementLink = new HtmlParagraph(Context.tr("add new attachement"), "fake_link");
                final HtmlBranch generateNewAttachementForm = generateNewAttachementForm();

                attachmentDiv.add(newAttachementLink);
                attachmentDiv.add(generateNewAttachementForm);

                final JsShowHide showHide = new JsShowHide(generateNewAttachementForm, false);
                showHide.setHasFallback(false);

                showHide.addActuator(newAttachementLink);
                showHide.addListener(generateNewAttachementForm);
                showHide.apply();
            }
        }

        layout.addLeft(new HtmlClearer());

        // Comments
        layout.addLeft(CommentTools.generateCommentList(bug.getComments(), generateBugFormatMap()));
        layout.addLeft(new CommentForm(new CreateCommentActionUrl(getSession().getShortKey(), bug)));

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

    private HtmlBranch generateNewAttachementForm() {
        final AddAttachementActionUrl targetUrl = new AddAttachementActionUrl(getSession().getShortKey(), bug);
        final HtmlForm addAttachementForm = new HtmlForm(targetUrl.urlString());
        addAttachementForm.enableFileUpload();
        addAttachementForm.add(new AttachmentField(targetUrl, FILE_MAX_SIZE_MIO + " Mio", false, false));
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
