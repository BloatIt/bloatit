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

package com.bloatit.web.components;

import java.util.Locale;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlImage;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.model.Feature;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Translation;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.FileResourceUrl;

public final class HtmlFeatureSummary extends HtmlDiv {

    private final Feature feature;
    private Locale defaultLocale;

    public enum Compacity {
        NORMAL("normal_feature_summary"), COMPACT("compact_feature_summary"), LINE("line_feature_summary");

        private final String cssClass;

        private Compacity(final String cssClass) {
            this.cssClass = cssClass;

        }

        public String getCssClass() {
            return cssClass;
        }
    }

    // "feature_summary"
    public HtmlFeatureSummary(final Feature feature, final Compacity compacity) {
        super(compacity.getCssClass());
        this.feature = feature;
        if (feature == null) {
            addText("");
            return;
        }
        // Extract locales stuffs
        defaultLocale = Context.getLocalizator().getLocale();

        try {
            switch (compacity) {
                case NORMAL:
                    generateNormalStructure();
                    break;
                case COMPACT:
                    generateCompactStructure();
                    break;
                case LINE:
                    throw new NotImplementedException();
                default:
                    break;
            }

        } catch (final UnauthorizedOperationException e) {
            Context.getSession().notifyError(Context.tr("Unknown right error. Please notify us"));
            throw new ShallNotPassException("Unknown right error", e);
        }
    }

    /**
     * @throws UnauthorizedOperationException
     */
    private void generateCompactStructure() throws UnauthorizedOperationException {
        final HtmlDiv featureSummaryTop = new HtmlDiv("feature_summary_top");
        {
            featureSummaryTop.add(generateTitle());
        }
        add(featureSummaryTop);

        final HtmlDiv featureSummaryBottom = new HtmlDiv("feature_summary_bottom");
        {

            final HtmlDiv featureSummaryLeft = new HtmlDiv("feature_summary_left");
            {
                // Add software image
                featureSummaryLeft.add(generateSoftwareImage());
            }
            featureSummaryBottom.add(featureSummaryLeft);

            final HtmlDiv featureSummaryCenter = new HtmlDiv("feature_summary_center");
            {

                final HtmlDiv featureummaryProgress = FeaturesTools.generateProgress(feature, true);
                featureummaryProgress.add(FeaturesTools.generateDetails(feature, false));
                featureSummaryCenter.add(featureummaryProgress);

            }
            featureSummaryBottom.add(featureSummaryCenter);

        }
        add(featureSummaryBottom);

    }

    /**
     * @throws UnauthorizedOperationException
     */
    private void generateNormalStructure() throws UnauthorizedOperationException {
        final HtmlDiv featureSummaryTop = new HtmlDiv("feature_summary_top");
        {
            final HtmlDiv featureSummaryLeft = new HtmlDiv("feature_summary_left");
            {
                // Add software image
                featureSummaryLeft.add(generateSoftwareImage());
            }
            featureSummaryTop.add(featureSummaryLeft);

            final HtmlDiv featureSummaryCenter = new HtmlDiv("feature_summary_center");
            {
                // Try to display the title
                featureSummaryCenter.add(generateTitle());
            }
            featureSummaryTop.add(featureSummaryCenter);
        }
        add(featureSummaryTop);

        final HtmlDiv featureSummaryBottom = new HtmlDiv("feature_summary_bottom");
        {
            featureSummaryBottom.add(generatePopularityBlock());

            final HtmlDiv featureSummaryProgress = FeaturesTools.generateProgress(feature, true);
            featureSummaryProgress.add(FeaturesTools.generateDetails(feature, true));
            featureSummaryBottom.add(featureSummaryProgress);
        }
        add(featureSummaryBottom);
    }

    /**
     * @return
     */
    private HtmlDiv generatePopularityBlock() {
        final HtmlDiv featureSummaryPopularity = new HtmlDiv("feature_summary_popularity");
        {
            final HtmlParagraph popularityText = new HtmlParagraph(Context.tr("Popularity"), "feature_popularity_text");
            final HtmlParagraph popularityScore = new HtmlParagraph(HtmlTools.compressKarma(feature.getPopularity()), "feature_popularity_score");

            featureSummaryPopularity.add(popularityText);
            featureSummaryPopularity.add(popularityScore);
        }
        return featureSummaryPopularity;
    }

    /**
     * @return
     * @throws UnauthorizedOperationException
     */
    private XmlNode generateSoftwareImage() throws UnauthorizedOperationException {
        // TODO: set a fixed size block to not depend of the image
        // size
        FileMetadata image = feature.getSoftware().getImage();
        if (image != null) {
            final FileResourceUrl imageUrl = new FileResourceUrl(image);
            return new HtmlImage(imageUrl, image.getShortDescription(), "software_image");
        } else {
            // TODO: add a fallback image
            return new PlaceHolderElement();
        }

    }

    /**
     * @return
     * @throws UnauthorizedOperationException
     */
    private XmlNode generateTitle() throws UnauthorizedOperationException {
        final Translation translatedDescription = feature.getDescription().getTranslationOrDefault(defaultLocale);
        final HtmlSpan softwareSpan = new HtmlSpan("feature_software_title");
        softwareSpan.addText(feature.getSoftware().getName());
        final HtmlTitle title = new HtmlTitle(1);
        title.setCssClass("feature_title");
        title.add(softwareSpan);
        title.addText(" – ");
        title.add(new FeaturePageUrl(feature).getHtmlLink(translatedDescription.getTitle()));

        return title;
    }

}
