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
package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.PageNotFoundException;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.renderer.HtmlRawTextRenderer;
import com.bloatit.model.Bug;
import com.bloatit.model.FileMetadata;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.FileResourceUrl;

@ParamContainer("demand/bug")
public final class BugPage extends MasterPage {

    public static final String BUG_FIELD_NAME = "id";

    @ParamConstraint(optionalErrorMsg = @tr("The id of the project is incorrect or missing"))
    @RequestParam(name = BUG_FIELD_NAME, level = Level.ERROR)
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
        if (url.getMessages().hasMessage(Level.ERROR)) {
            throw new PageNotFoundException();
        }

        final HtmlDiv box = new HtmlDiv("padding_box");

        HtmlTitle bugTitle;
        bugTitle = new HtmlTitle(bug.getTitle(), 1);
        box.add(bugTitle);

        final HtmlParagraph description = new HtmlParagraph(new HtmlRawTextRenderer(bug.getDescription()));
        box.add(description);


        //Attachements
        for(FileMetadata attachement: bug.getFiles()) {
            final HtmlParagraph attachementPara = new HtmlParagraph();
            attachementPara.add(new FileResourceUrl(attachement).getHtmlLink(attachement.getFileName()));
            attachementPara.addText(tr(": ")+attachement.getShortDescription());
            box.add(attachementPara);
        }

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
}
