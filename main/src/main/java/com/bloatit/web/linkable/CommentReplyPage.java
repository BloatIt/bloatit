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
package com.bloatit.web.linkable;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Comment;
import com.bloatit.model.Member;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.linkable.bugs.BugPage;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.tools.CommentTools;
import com.bloatit.web.linkable.usercontent.AsTeamField;
import com.bloatit.web.linkable.usercontent.CreateUserContentPage;
import com.bloatit.web.url.CommentReplyPageUrl;
import com.bloatit.web.url.CreateCommentActionUrl;

/**
 * Page that hosts the form used to reply to an existing comment
 */
@ParamContainer("comments/%comment%/reply")
public final class CommentReplyPage extends CreateUserContentPage {

    private static final int NB_LINES = 10;
    private static final int NB_COLUMNS = 80;

    private final CommentReplyPageUrl url;

    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the comment number: ''%value%''."))
    @NonOptional(@tr("You have to specify a comment number."))
    private final Comment comment;

    public CommentReplyPage(final CommentReplyPageUrl url) {
        super(url);
        this.url = url;
        this.comment = url.getComment();
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        final HtmlDiv box = new HtmlDiv();

        // ///////////
        // RIGHT BAR
        layout.addRight(new SideBarDocumentationBlock("cc_by"));

        // //////////
        // PAGE CORE
        layout.addLeft(box);

        final HtmlTitle title = new HtmlTitle(Context.tr("Reply to a comment"), 1);
        final CreateCommentActionUrl commentCommentActionUrl = new CreateCommentActionUrl(getSession().getShortKey(), comment);
        final HtmlElveosForm form = new HtmlElveosForm(commentCommentActionUrl.urlString());
        final FormBuilder ftool = new FormBuilder(CreateCommentAction.class, commentCommentActionUrl);

        // as team
        form.addAsTeamField(new AsTeamField(commentCommentActionUrl,
                                            loggedUser,
                                            UserTeamRight.TALK,
                                            Context.tr("In the name of"),
                                            Context.tr("Write this comment in the name of this group.")));

        // Comment text
        ftool.add(form, new HtmlTextArea(commentCommentActionUrl.getCommentParameter().getName(), NB_LINES, NB_COLUMNS));

        // submit
        form.addSubmit(new HtmlSubmit(Context.tr("Comment")));

        box.add(title);
        box.add(form);

        layout.addLeft(new HtmlTitle(Context.tr("Previous comments"), 1));
        layout.addLeft(CommentTools.generateCommentList(comment));

        return layout;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("you must be logged in to comment.");
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Reply to a comment");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return CommentReplyPage.generateBreadcrumb(comment);
    }

    private static Breadcrumb generateBreadcrumb(final Comment comment) {

        Breadcrumb breadcrumb;

        switch (comment.getRootParentType()) {
            case BUG:
                breadcrumb = BugPage.generateBreadcrumb(comment.getRootComment().getParentBug());
                break;
            case FEATURE:
                breadcrumb = FeaturePage.generateBreadcrumb(comment.getRootComment().getParentFeature());
                break;
            default:
                breadcrumb = new Breadcrumb();
        }

        breadcrumb.pushLink(new CommentReplyPageUrl(comment).getHtmlLink(tr("Reply to {0}''s comment", comment.getMember().getDisplayName())));

        return breadcrumb;
    }

}
