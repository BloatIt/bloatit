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
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Feature;
import com.bloatit.model.Translation;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.features.FeaturesTools.FeatureContext;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.url.FeaturePageUrl;

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
    public HtmlFeatureSummary(final Feature feature, final Compacity compacity, FeaturesTools.FeatureContext context) {
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
                    generateNormalStructure(context);
                    break;
                case COMPACT:
                    generateCompactStructure(context);
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
     * @param me
     * @param userToken
     * @throws UnauthorizedOperationException
     */
    private void generateCompactStructure(FeaturesTools.FeatureContext context) throws UnauthorizedOperationException {
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
                featureSummaryLeft.add(new SoftwaresTools.Logo(feature.getSoftware()));
            }
            featureSummaryBottom.add(featureSummaryLeft);

            final HtmlDiv featureSummaryCenter = new HtmlDiv("feature_summary_center");
            {
                final HtmlDiv featureummaryProgress = FeaturesTools.generateProgress(feature, context);
                
                featureummaryProgress.add(FeaturesTools.generateDetails(feature, false));
                featureSummaryCenter.add(featureummaryProgress);
            }
            featureSummaryBottom.add(featureSummaryCenter);

        }
        add(featureSummaryBottom);

    }

    /**
     * @param context 
     * @param me
     * @throws UnauthorizedOperationException
     */
    private void generateNormalStructure(FeatureContext context) throws UnauthorizedOperationException {
        final HtmlDiv featureSummaryTop = new HtmlDiv("feature_summary_top");
        {
            final HtmlDiv featureSummaryLeft = new HtmlDiv("feature_summary_left");
            {
                // Add software image
                featureSummaryLeft.add(new SoftwaresTools.Logo(feature.getSoftware()));
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

            final HtmlDiv featureSummaryBottomCenter = new HtmlDiv("feature_summary_bottom_center");
            {

                featureSummaryBottomCenter.add(FeaturesTools.generateProgress(feature, context));
                
                featureSummaryBottomCenter.add(FeaturesTools.generateDetails(feature, false));
                
                featureSummaryBottomCenter.add(FeaturesTools.generateState(feature));
            }
            featureSummaryBottom.add(featureSummaryBottomCenter);
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
    private XmlNode generateTitle() throws UnauthorizedOperationException {
        final Translation translatedDescription = feature.getDescription().getTranslationOrDefault(defaultLocale);

        final HtmlTitle title = new HtmlTitle(1);
        title.setCssClass("feature_title");

        if (feature.getSoftware() != null) {
            final HtmlSpan softwareSpan = new HtmlSpan("feature_software_title");
            softwareSpan.addText(feature.getSoftware().getName());
            title.add(softwareSpan);
            title.addText(" – ");
        }

        title.add(new FeaturePageUrl(feature, FeatureTabKey.description).getHtmlLink(translatedDescription.getTitle()));

        return title;
    }

}
