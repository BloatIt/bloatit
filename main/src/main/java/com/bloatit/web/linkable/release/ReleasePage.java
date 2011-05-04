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
package com.bloatit.web.linkable.release;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Release;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AddAttachementPageUrl;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.ReleasePageUrl;

@ParamContainer("release")
public final class ReleasePage extends MasterPage {

    @ParamConstraint(optionalErrorMsg = @tr("You have to specify a release number."))
    @RequestParam(name = "id", conversionErrorMsg = @tr("I cannot find the release number: ''%value%''."))
    private final Release release;

    private final ReleasePageUrl url;

    public ReleasePage(final ReleasePageUrl url) {
        super(url);
        this.url = url;
        this.release = url.getRelease();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addRight(new SideBarFeatureBlock(release.getFeature(), getToken().getMember()));

        layout.addLeft(new HtmlTitleBlock(Context.tr("Release"), 1));
        layout.addLeft(new HtmlDiv().add(new HtmlParagraph(tr("date: "
                + Context.getLocalizator().getDate(release.getCreationDate()).toString(FormatStyle.MEDIUM)))));
        layout.addLeft(new HtmlDiv().add(new HtmlParagraph(tr("version: " + release.getVersion()))));
        layout.addLeft(new HtmlDiv().add(new HtmlParagraph(tr("description: ")).add(new HtmlParagraph(release.getDescription()))));

        final HtmlDiv fileBloc = new HtmlDiv();
        layout.addLeft(fileBloc);
        for (final FileMetadata files : release.getFiles()) {
            final HtmlParagraph attachmentPara = new HtmlParagraph();
            attachmentPara.add(new FileResourceUrl(files).getHtmlLink(files.getFileName()));
            attachmentPara.addText(tr(": ") + files.getShortDescription());
            fileBloc.add(attachmentPara);
        }

        if (release.canAddFile()) {
            fileBloc.add(new AddAttachementPageUrl(release).getHtmlLink(tr("Add an attachment")));
        }
        return layout;
    }

    @Override
    protected String createPageTitle() {
        return tr("Release");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return ReleasePage.generateBreadcrumb(release);
    }

    public static Breadcrumb generateBreadcrumb(final Release release) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbOffers(release.getFeature());

        breadcrumb.pushLink(new ReleasePageUrl(release).getHtmlLink(tr("Release ") + release.getVersion()));

        return breadcrumb;
    }
}
