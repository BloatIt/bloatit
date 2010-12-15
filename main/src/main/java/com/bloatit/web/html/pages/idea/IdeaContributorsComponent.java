/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.html.pages.idea;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Contribution;
import com.bloatit.framework.Demand;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.custom.HtmlPagedList;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.components.standard.HtmlRenderer;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.url.Request;
import com.bloatit.web.utils.url.UrlBuilder;

public class IdeaContributorsComponent extends HtmlDiv {

    private int contributionCount;
    private HtmlParagraph contributionMin;
    private HtmlParagraph contributionMax;
    private HtmlParagraph contributionMean;
    private PageIterable<Contribution> contributions;
    private final Demand demand;

    public IdeaContributorsComponent(final Request request, final Demand demand) {
        super();
        this.demand = demand;
        extractData(request);
        add(produce(request));


    }

    protected HtmlElement produce(final Request request) {
        final Session session = Context.getSession();
        final HtmlDiv contributorsBlock = new HtmlDiv("contributors_block");
        {

            // Display contribution count
            contributorsBlock.add(new HtmlParagraph("" + contributionCount + session.tr("&nbsp;contributions")));

            // Display contribution stats
            if (contributionCount > 0) {
                contributorsBlock.add(contributionMin);
                contributorsBlock.add(contributionMax);
                contributorsBlock.add(contributionMean);
            }

            // Create contribution renderer
            final HtmlRenderer<Contribution> contributionRenderer = generateContributionRenderer();

            // Create paged list
            final HtmlPagedList<Contribution> participationsList = new HtmlPagedList<Contribution>("contribution_list", contributionRenderer,
                    contributions, new UrlBuilder(IdeaPage.class, request.getParameters()), request);
            contributorsBlock.add(participationsList);

        }
        return contributorsBlock;
    }

    protected void extractData(final Request request) {

        final Session session = Context.getSession();
        contributionCount = demand.getContributions().size();

        if (contributionCount > 0) {

            final float contributionMeanValue = demand.getContribution().floatValue() / contributionCount;
            final String contributionMinValue = demand.getContributionMin().toPlainString();
            final String contributionMaxValue = demand.getContributionMax().toPlainString();

            contributionMin = new HtmlParagraph(session.tr("Min:&nbsp;") + contributionMinValue);
            contributionMax = new HtmlParagraph(session.tr("Max:&nbsp;") + contributionMaxValue);
            contributionMean = new HtmlParagraph(session.tr("Mean:&nbsp;") + contributionMeanValue);
        }

        contributions = demand.getContributions();
    }

    private HtmlRenderer<Contribution> generateContributionRenderer() {
        return new HtmlRenderer<Contribution>() {

            @Override
            public HtmlNode generate(final Contribution item) {
                final String itemString = item.getAuthor().getLogin() + " " + item.getAmount().toPlainString() + " "
                        + item.getCreationDate().toString();
                return new HtmlText(itemString);
            }
        };
    }
}
