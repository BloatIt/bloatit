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
package com.bloatit.web.html.pages.idea;

import static com.bloatit.web.server.Context.tr;

import com.bloatit.common.PageIterable;
import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.Contribution;
import com.bloatit.framework.Demand;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.custom.HtmlPagedList;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.components.standard.HtmlRenderer;
import com.bloatit.web.utils.url.IdeaContributorsComponentUrl;

@ParamContainer(value = "DemandContributorsComponent", isComponent = true)
public final class IdeaContributorsComponent extends HtmlDiv {

    private int contributionCount;
    private HtmlParagraph contributionMin;
    private HtmlParagraph contributionMax;
    private HtmlParagraph contributionMean;
    private PageIterable<Contribution> contributions;
    private final Demand demand;
    private HtmlPagedList<Contribution> participationsList;

    public IdeaContributorsComponent(final IdeaContributorsComponentUrl url, final Demand demand) {
        super();
        this.demand = demand;
        try {
            extractData();
            add(produce(url));
        } catch (UnauthorizedOperationException e) {
            //No right, no display
        }

    }

    protected HtmlElement produce(IdeaContributorsComponentUrl url) {
        final HtmlDiv contributorsBlock = new HtmlDiv("contributors_block");
        {

            // Display contribution count
            contributorsBlock.add(new HtmlParagraph(contributionCount + tr(" contributions")));

            // Display contribution stats
            if (contributionCount > 0) {
                contributorsBlock.add(contributionMin);
                contributorsBlock.add(contributionMax);
                contributorsBlock.add(contributionMean);
            }

            // Create contribution renderer
            final HtmlRenderer<Contribution> contributionRenderer = generateContributionRenderer();

            // Create paged list
            url = url.clone();
            participationsList = new HtmlPagedList<Contribution>(contributionRenderer, contributions, url, url.getParticipationsListUrl());
            participationsList.setCssClass("contribution_list");
            contributorsBlock.add(participationsList);

        }
        return contributorsBlock;
    }

    protected void extractData() throws UnauthorizedOperationException {

            contributionCount = demand.getContributions().size();

            if (contributionCount > 0) {

                final float contributionMeanValue = demand.getContribution().floatValue() / contributionCount;
                final String contributionMinValue = demand.getContributionMin().toPlainString();
                final String contributionMaxValue = demand.getContributionMax().toPlainString();

                contributionMin = new HtmlParagraph(tr("Min: ") + contributionMinValue);
                contributionMax = new HtmlParagraph(tr("Max: ") + contributionMaxValue);
                contributionMean = new HtmlParagraph(tr("Mean: ") + contributionMeanValue);
            }

            contributions = demand.getContributions();

    }

    private HtmlRenderer<Contribution> generateContributionRenderer() {
        return new HtmlRenderer<Contribution>() {

            @Override
            public HtmlNode generate(final Contribution item) {
                String itemString = tr("You are not authorized to see this.");
                try {
                    itemString = item.getAuthor().getLogin() + " " + item.getAmount().toPlainString() + " "
                            + item.getCreationDate().toString();
                } catch (UnauthorizedOperationException e) {
                    // do nothing
                }
                return new HtmlText(itemString);
            }
        };
    }
}
