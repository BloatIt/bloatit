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
package com.bloatit.web.linkable.features;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.util.Locale;

import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock.HtmlTab;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.renderer.HtmlCachedMarkdownRenderer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Description;
import com.bloatit.model.Feature;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Translation;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.components.UserContentAuthorBlock;
import com.bloatit.web.pages.master.HtmlPageComponent;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.FeatureTabPaneUrlComponent;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.ModifyFeaturePageUrl;
import com.bloatit.web.url.TranslatePageUrl;

@ParamContainer(value = "featureTabPane", isComponent = true)
public final class FeatureTabPane extends HtmlPageComponent {
    public static final String FEATURE_TAB_PANE = "activeTabKey";

    public static enum FeatureTabKey {
        bugs, details, offers, contributions, description
    }

    @RequestParam(role = Role.PAGENAME)
    @NonOptional(@tr("The tab is not optional."))
    private FeatureTabKey activeTabKey;
 
    // Useful for Url generation Do not delete
    @SuppressWarnings("unused")
    private FeatureContributorsComponent contribution;


    private final Feature feature;

    protected FeatureTabPane(final FeatureTabPaneUrlComponent url, final Feature feature) {
        super();
        this.feature = feature;
        activeTabKey = url.getActiveTabKey();

        final FeaturePageUrl featureUrl = new FeaturePageUrl(feature, activeTabKey);

        // Create tab pane
        final HtmlTabBlock tabPane = new HtmlTabBlock(FEATURE_TAB_PANE, activeTabKey.name(), featureUrl);

        // Create description tab
        tabPane.addTab(new HtmlTab(Context.tr("Description"), FeatureTabKey.description.name()) {
            @SuppressWarnings("synthetic-access")
            @Override
            public HtmlNode generateBody() {
                return generateDescriptionTabContent(feature);
            }
        });

        tabPane.addTab(new HtmlTab(Context.tr("Contributions ({0})", feature.getContributions(false).size()), FeatureTabKey.contributions.name()) {
            @Override
            public HtmlNode generateBody() {
                return new FeatureContributorsComponent(feature);
            }
        });

        tabPane.addTab(new HtmlTab(Context.tr("Offers ({0})", feature.getOffers().size()), FeatureTabKey.offers.name()) {
            @Override
            public HtmlNode generateBody() {
                return new FeatureOfferListComponent(feature);
            }
        });

        // Create Details tab
        // tabPane.addTab(new HtmlTab(Context.tr("Details"), DETAILS_TAB) {
        // @Override
        // public XmlNode generateBody() {
        // return new FeatureOfferListComponent(feature);
        // }
        // });

        // Create Bugtracker tab only after preparation
        if (feature.getFeatureState() == FeatureState.DEVELOPPING || feature.getFeatureState() == FeatureState.FINISHED) {

            tabPane.addTab(new HtmlTab(Context.tr("Bugs ({0})", feature.countOpenBugs()), FeatureTabKey.bugs.name()) {
                @Override
                public HtmlNode generateBody() {
                    return new FeatureBugListComponent(feature);
                }
            });
        }

        add(tabPane);
    }

    private HtmlNode generateDescriptionTabContent(final Feature feature) {

        final HtmlDiv descriptionBlock = new HtmlDiv("description_block");
        {
            final Locale defaultLocale = new Locale(Context.getLocalizator().getLocale().getLanguage());
            
            Description featureDescription = feature.getDescription();
            final Translation translatedDescription = featureDescription.getTranslationOrDefault(Language.fromLocale(defaultLocale));
            
            if(AuthToken.isAuthenticated()) {
            
                final HtmlDiv languageButton = new HtmlDiv("language_button");
                {
                    TranslatePageUrl translatePageUrl = new TranslatePageUrl(featureDescription, defaultLocale);
                    HtmlLink link = translatePageUrl.getHtmlLink(Context.tr("translate"));
                    languageButton.add(link);
                }
                descriptionBlock.add(languageButton);
            }
            
            final HtmlDiv descriptionText = new HtmlDiv("description_text");
            {
                final HtmlElement description = new HtmlCachedMarkdownRenderer(translatedDescription.getText());
                descriptionText.add(description);

            }
            descriptionBlock.add(descriptionText);

            // Attachements
            for (final FileMetadata attachment : feature.getFiles()) {
                final HtmlParagraph attachmentPara = new HtmlParagraph();
                attachmentPara.add(new FileResourceUrl(attachment).getHtmlLink(attachment.getFileName()));
                attachmentPara.addText(tr(": ") + attachment.getShortDescription());
                descriptionBlock.add(attachmentPara);
            }

            final HtmlDiv descriptionSeparator = new HtmlDiv("description_separator");
            descriptionBlock.add(descriptionSeparator);

            final HtmlDiv descriptionFooter = new HtmlDiv("description_footer");
            {
                if (feature.canModify()) {
                    HtmlDiv modifyFeature = new HtmlDiv("float_right");
                    modifyFeature.add(new ModifyFeaturePageUrl(feature).getHtmlLink(Context.tr("Modify the feature")));
                    descriptionFooter.add(modifyFeature);
                }

                descriptionFooter.add(new UserContentAuthorBlock(feature));

                // KEEP THIS CODE, will be restored someday
                // final HtmlDiv descriptionTranslate = new
                // HtmlDiv("description_translate");
                // {
                // descriptionTranslate.add(new
                // PageNotFoundUrl().getHtmlLink(Context.tr("Translate")));
                // }
                // descriptionFooter.add(descriptionTranslate);

            }
            descriptionBlock.add(descriptionFooter);

        }
        return descriptionBlock;
    }
}
