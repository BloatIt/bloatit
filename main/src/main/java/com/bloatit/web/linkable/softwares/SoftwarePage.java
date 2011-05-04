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
package com.bloatit.web.linkable.softwares;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.util.Locale;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.renderer.HtmlRawTextRenderer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Software;
import com.bloatit.model.Translation;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.ElveosPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.SoftwarePageUrl;

@ParamContainer("software")
public final class SoftwarePage extends ElveosPage {

    @ParamConstraint(optionalErrorMsg = @tr("You have to specify a software number."))
    @RequestParam(name = "id", conversionErrorMsg = @tr("I cannot find the software number: ''%value%''."))
    private final Software software;
    
    @SuppressWarnings("unused")
    @RequestParam(role = Role.PRETTY, generatedFrom = "software")
    @Optional("john-do")
    private final String name;

    private final SoftwarePageUrl url;

    public SoftwarePage(final SoftwarePageUrl url) {
        super(url);
        this.url = url;
        this.software = url.getSoftware();
        this.name = url.getName();
    }

    @Override
    protected HtmlElement createBodyContent(AuthToken authToken) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        HtmlTitle softwareName;
        softwareName = new HtmlTitle(software.getName(), 1);
        layout.addLeft(softwareName);

        final FileMetadata image = software.getImage();
        if (image != null) {
            layout.addLeft(new HtmlImage(new FileResourceUrl(image), image.getShortDescription(), "float_right"));
        }

        final Locale defaultLocale = Context.getLocalizator().getLocale();
        final Translation translatedDescription = software.getDescription().getTranslationOrDefault(defaultLocale);

        final HtmlParagraph shortDescription = new HtmlParagraph(new HtmlRawTextRenderer(translatedDescription.getTitle()));
        final HtmlParagraph description = new HtmlParagraph(new HtmlRawTextRenderer(translatedDescription.getText()));

        layout.addLeft(shortDescription);
        layout.addLeft(description);

        return layout;
    }

    @Override
    protected String createPageTitle() {
        return tr("Software - ") + software.getName();
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Breadcrumb createBreadcrumb(AuthToken authToken) {
        return SoftwarePage.generateBreadcrumb(software);
    }

    public static Breadcrumb generateBreadcrumb(final Software software) {
        final Breadcrumb breadcrumb = SoftwareListPage.generateBreadcrumb();
        breadcrumb.pushLink(new SoftwarePageUrl(software).getHtmlLink(software.getName()));

        return breadcrumb;
    }
}
