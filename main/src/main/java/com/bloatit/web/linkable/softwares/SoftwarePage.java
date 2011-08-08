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
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.SubParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlRenderer;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.components.renderer.HtmlRawTextRenderer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Feature;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Software;
import com.bloatit.model.Translation;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.components.HtmlFeatureSummary;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.components.HtmlFeatureSummary.Compacity;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.ElveosPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.ModifySoftwarePageUrl;
import com.bloatit.web.url.SoftwarePageUrl;

@ParamContainer("softwares/%software%")
public final class SoftwarePage extends ElveosPage {

    @NonOptional(@tr("You have to specify a software number."))
    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the software number: ''%value%''."))
    private final Software software;

    @SuppressWarnings("unused")
    @RequestParam(role = Role.PRETTY, generatedFrom = "software")
    @Optional("john-do")
    private final String name;

    private final SoftwarePageUrl url;
    
    @SubParamContainer
    private HtmlPagedList<Feature> pagedFeatureList;

    public SoftwarePage(final SoftwarePageUrl url) {
        super(url);
        this.url = url;
        this.software = url.getSoftware();
        this.name = url.getName();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        if (AuthToken.isAuthenticated()) {
            // Link to change account settings
            final HtmlDiv modify = new HtmlDiv("float_right");
            layout.addLeft(modify);
            modify.add(new ModifySoftwarePageUrl(software).getHtmlLink(Context.tr("Modify software description")));
        }

        HtmlTitle softwareName;
        softwareName = new HtmlTitle(software.getName(), 1);
        layout.addLeft(softwareName);

        final FileMetadata image = software.getImage();
        if (image != null) {
            layout.addLeft(new HtmlImage(new FileResourceUrl(image), image.getShortDescription(), "float_right"));
        }

        final Locale defaultLocale = Context.getLocalizator().getLocale();
        final Translation translatedDescription = software.getDescription().getTranslationOrDefault(defaultLocale);
        final HtmlParagraph description = new HtmlParagraph(new HtmlRawTextRenderer(translatedDescription.getText()));
        layout.addLeft(description);
        
        
        
        PageIterable<Feature> features = software.getFeatures();
        
        if (features.size() > 0) {
            
            layout.addLeft(new HtmlTitle(Context.tr("Related features"), 1));
            final HtmlRenderer<Feature> featureItemRenderer = new FeaturesListItem();
            final SoftwarePageUrl clonedUrl = url.clone();
            pagedFeatureList = new HtmlPagedList<Feature>(featureItemRenderer, features, clonedUrl, clonedUrl.getPagedFeatureListUrl());
            layout.addLeft(pagedFeatureList);
        } 
        
        
        
        
        
        
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
    protected Breadcrumb createBreadcrumb() {
        return SoftwarePage.generateBreadcrumb(software);
    }

    public static Breadcrumb generateBreadcrumb(final Software software) {
        final Breadcrumb breadcrumb = SoftwareListPage.generateBreadcrumb();
        breadcrumb.pushLink(new SoftwarePageUrl(software).getHtmlLink(software.getName()));

        return breadcrumb;
    }
    
    
    private static class FeaturesListItem implements HtmlRenderer<Feature> {

        public FeaturesListItem() {
            super();
        }

        @Override
        public XmlNode generate(final Feature feature) {
            return new HtmlFeatureSummary(feature, Compacity.NORMAL);
        }
    };
}
