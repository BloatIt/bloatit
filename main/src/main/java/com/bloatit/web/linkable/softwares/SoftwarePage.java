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
import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.SubParamContainer;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlRenderer;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.renderer.HtmlCachedMarkdownRenderer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Feature;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Software;
import com.bloatit.model.Translation;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.HtmlFeatureSummary;
import com.bloatit.web.components.HtmlFeatureSummary.Compacity;
import com.bloatit.web.components.HtmlFollowButton.HtmlFollowSoftwareButton;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.components.SideBarButton;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.ElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.translation.TranslatePage.DescriptionType;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.ModifySoftwarePageUrl;
import com.bloatit.web.url.SoftwareAtomFeedUrl;
import com.bloatit.web.url.SoftwarePageUrl;
import com.bloatit.web.url.TranslatePageUrl;

@ParamContainer("softwares/%software%")
public final class SoftwarePage extends ElveosPage {

    @NonOptional(@tr("You have to specify a software number."))
    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the software number: ''%value%''."))
    private final Software software;

    @SuppressWarnings("unused")
    @RequestParam(role = Role.PRETTY, generatedFrom = "software")
    @Optional("john-doe")
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

        layout.addRight(new SideBarButton(Context.tr("Follow {0}", software.getName()),
                                          new SoftwareAtomFeedUrl(software),
                                          WebConfiguration.getAtomImg(),
                                          false));

        final HtmlDiv softwarePage = new HtmlDiv("software_page");

        if (AuthToken.isAuthenticated()) {
            final HtmlDiv languageButton = new HtmlDiv("language_button");
            final TranslatePageUrl translatePageUrl = new TranslatePageUrl(software.getDescription(),
                                                                           new Locale(Context.getLocalizator().getLocale().getLanguage()),
                                                                           DescriptionType.SOFTWARE);
            final HtmlLink link = translatePageUrl.getHtmlLink(Context.tr("translate"));
            languageButton.add(link);

            softwarePage.add(languageButton);
        }

        HtmlTitle softwareName;
        softwareName = new HtmlTitle(software.getName(), 1);
        softwarePage.add(softwareName);

        final FileMetadata image = software.getImage();
        if (image != null) {
            softwarePage.add(new HtmlImage(new FileResourceUrl(image), image.getShortDescription(), "float_right"));
        }

        final Locale defaultLocale = Context.getLocalizator().getLocale();
        final Translation translatedDescription = software.getDescription().getTranslationOrDefault(Language.fromLocale(defaultLocale));
        final HtmlCachedMarkdownRenderer description = new HtmlCachedMarkdownRenderer(translatedDescription.getText());
        softwarePage.add(description);

        if (AuthToken.isAuthenticated()) {
            // Link to change account settings
            final HtmlDiv modify = new HtmlDiv("float_right");
            softwarePage.add(modify);
            modify.add(new ModifySoftwarePageUrl(software).getHtmlLink(Context.tr("Modify software description")));
            modify.add(new HtmlFollowSoftwareButton(software));
        }

        final PageIterable<Feature> features = software.getFeatures();

        if (features.size() > 0) {
            softwarePage.add(new HtmlTitle(Context.tr("Related features"), 1));
            final HtmlRenderer<Feature> featureItemRenderer = new FeaturesListItem();
            final SoftwarePageUrl clonedUrl = url.clone();
            pagedFeatureList = new HtmlPagedList<Feature>(featureItemRenderer, features, clonedUrl, clonedUrl.getPagedFeatureListUrl());
            softwarePage.add(pagedFeatureList);
        }

        layout.addLeft(softwarePage);

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
        public HtmlNode generate(final Feature feature) {
            return new HtmlFeatureSummary(feature, Compacity.NORMAL, FeaturesTools.FeatureContext.OTHER);
        }
    };
}
