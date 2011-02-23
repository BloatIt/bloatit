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
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Release;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.ReleasePageUrl;

@ParamContainer("release")
public final class ReleasePage extends MasterPage {
    @ParamConstraint(optionalErrorMsg = @tr("The id of the release is incorrect or missing"))
    @RequestParam
    private final Release release;

    public ReleasePage(final ReleasePageUrl url) {
        super(url);
        this.release = url.getRelease();
    }

    @Override
    protected void doCreate() throws RedirectException {
        final HtmlDiv master = new HtmlDiv("padding_box");
        add(master);

        master.add(new HtmlTitleBlock(Context.tr("Release"), 1));
        master.add(new HtmlDiv().add(new HtmlParagraph(tr("date: "
                + Context.getLocalizator().getDate(release.getCreationDate()).toString(FormatStyle.MEDIUM)))));
        master.add(new HtmlDiv().add(new HtmlParagraph(tr("version: " + release.getVersion()))));
        master.add(new HtmlDiv().add(new HtmlParagraph(tr("description: ")).add(new HtmlParagraph(release.getDescription()))));

        HtmlDiv fileBloc = new HtmlDiv();
        master.add(fileBloc);
        for (FileMetadata files : release.getFiles()) {
            final HtmlParagraph attachementPara = new HtmlParagraph();
            attachementPara.add(new FileResourceUrl(files).getHtmlLink(files.getFileName()));
            attachementPara.addText(tr(": ") + files.getShortDescription());
            fileBloc.add(attachementPara);
        }
    }

    @Override
    protected String getPageTitle() {
        return tr("Release");
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
