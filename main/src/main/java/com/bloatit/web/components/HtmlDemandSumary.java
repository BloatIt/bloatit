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

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlImage;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.model.Demand;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Translation;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.linkable.demands.DemandsTools;
import com.bloatit.web.url.DemandPageUrl;
import com.bloatit.web.url.FileResourceUrl;

public final class HtmlDemandSumary extends HtmlDiv {

    private final Demand demand;
    private Locale defaultLocale;

    public enum Compacity {
        NORMAL("demand_summary"), COMPACT("compact_demand_summary"), LINE("line_demand_summary");

        private final String cssClass;

        private Compacity(final String cssClass) {
            this.cssClass = cssClass;

        }

        public String getCssClass() {
            return cssClass;
        }

    }

    // "demand_summary"
    public HtmlDemandSumary(final Demand demand, final Compacity compacity) {
        super(compacity.getCssClass());
        this.demand = demand;
        if (demand == null) {
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
            addText("");
            return;
        }
    }

    /**
     * @throws UnauthorizedOperationException
     */
    private void generateCompactStructure() throws UnauthorizedOperationException {
        final HtmlDiv demandSummaryTop = new HtmlDiv("demand_summary_top");
        {
            demandSummaryTop.add(generateTitle());
        }
        add(demandSummaryTop);

        final HtmlDiv demandSummaryBottom = new HtmlDiv("demand_sumary_bottom");
        {

            final HtmlDiv demandSummaryLeft = new HtmlDiv("demand_summary_left");
            {
                // Add project image
                demandSummaryLeft.add(generateProjectImage());
            }
            demandSummaryBottom.add(demandSummaryLeft);

            final HtmlDiv demandSummaryCenter = new HtmlDiv("demand_summary_center");
            {

                final HtmlDiv demandSummaryProgress = DemandsTools.generateProgress(demand);
                demandSummaryProgress.add(DemandsTools.generateDetails(demand));
                demandSummaryCenter.add(demandSummaryProgress);

            }
            demandSummaryBottom.add(demandSummaryCenter);

        }
        add(demandSummaryBottom);

    }

    /**
     * @throws UnauthorizedOperationException
     */
    private void generateNormalStructure() throws UnauthorizedOperationException {
        final HtmlDiv demandSummaryTop = new HtmlDiv("demand_summary_top");
        {
            final HtmlDiv demandSummaryLeft = new HtmlDiv("demand_summary_left");
            {
                // Add project image
                demandSummaryLeft.add(generateProjectImage());
            }
            demandSummaryTop.add(demandSummaryLeft);

            final HtmlDiv demandSummaryCenter = new HtmlDiv("demand_summary_center");
            {
                // Try to display the title
                demandSummaryCenter.add(generateTitle());
            }
            demandSummaryTop.add(demandSummaryCenter);
        }
        add(demandSummaryTop);

        final HtmlDiv demandSummaryBottom = new HtmlDiv("demand_sumary_bottom");
        {
            demandSummaryBottom.add(generatePopularityBlock());

            final HtmlDiv demandSummaryProgress = DemandsTools.generateProgress(demand);
            demandSummaryProgress.add(DemandsTools.generateDetails(demand));
            demandSummaryBottom.add(demandSummaryProgress);
        }
        add(demandSummaryBottom);
    }


    /**
     * @return
     */
    private HtmlDiv generatePopularityBlock() {
        final HtmlDiv demandSummaryPopularity = new HtmlDiv("demand_summary_popularity");
        {
            final HtmlParagraph popularityText = new HtmlParagraph(Context.tr("Popularity"), "demand_popularity_text");
            final HtmlParagraph popularityScore = new HtmlParagraph(HtmlTools.compressKarma(demand.getPopularity()), "demand_popularity_score");

            demandSummaryPopularity.add(popularityText);
            demandSummaryPopularity.add(popularityScore);
        }
        return demandSummaryPopularity;
    }

    /**
     * @return
     * @throws UnauthorizedOperationException
     */
    private XmlNode generateProjectImage() throws UnauthorizedOperationException {
        // TODO: set a fixed size block to not depend of the image
        // size
        FileMetadata image = demand.getProject().getImage();
        if (image != null) {
            final FileResourceUrl imageUrl = new FileResourceUrl(image);
            return new HtmlImage(imageUrl, image.getShortDescription(), "project_image");
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
        final Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);
        final HtmlSpan projectSpan = new HtmlSpan("demand_project_title");
        projectSpan.addText(demand.getProject().getName());
        final HtmlTitle title = new HtmlTitle(1);
        title.setCssClass("demand_title");
        title.add(projectSpan);
        title.addText(" – ");
        title.add(new DemandPageUrl(demand).getHtmlLink(translatedDescription.getTitle()));

        return title;
    }


}
