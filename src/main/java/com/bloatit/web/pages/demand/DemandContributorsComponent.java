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
package com.bloatit.web.pages.demand;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Contribution;
import com.bloatit.web.htmlrenderer.HtmlResult;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlListItem;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlPagedList;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlRenderer;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlText;
import com.bloatit.web.pages.components.PageComponent;
import com.bloatit.web.pages.demand.DemandPage;

public class DemandContributorsComponent extends PageComponent {

    private final DemandPage demandPage;
    private int contributionCount;
    private HtmlText contributionMin;
    private HtmlText contributionMax;
    private HtmlText contributionMean;
    private PageIterable<Contribution> contributions;

    public DemandContributorsComponent(DemandPage demandPage) {
        super(demandPage.getSession());
        this.demandPage = demandPage;

    }

    @Override
    protected HtmlComponent produce() {
        HtmlBlock contributorsBlock = new HtmlBlock("contributors_block");
        {

            //Display contribution count
            contributorsBlock.add(new HtmlText("" + contributionCount + session.tr("&nbsp;contributions")));


            //Display contribution stats
            if (contributionCount > 0) {
                contributorsBlock.add(contributionMin);
                contributorsBlock.add(contributionMax);
                contributorsBlock.add(contributionMean);
            }

            //Create contribution renderer
            HtmlRenderer<Contribution> contributionRenderer = generateContributionRenderer();


            //Create paged list
            HtmlPagedList<Contribution> participationsList = new HtmlPagedList<Contribution>("contribution_list", contributionRenderer, contributions, demandPage, session);
            contributorsBlock.add(participationsList);

        }
        return contributorsBlock;
    }

    @Override
    protected void extractData() {

        contributionCount = demandPage.getDemand().getContributions().size();

        float contributionMeanValue = demandPage.getDemand().getContribution().floatValue() / contributionCount;
        String contributionMinValue = demandPage.getDemand().getContributionMin().toPlainString();
        String contributionMaxValue = demandPage.getDemand().getContributionMax().toPlainString();

        contributionMin = new HtmlText(session.tr("Min:&nbsp;") + contributionMinValue);
        contributionMax = new HtmlText(session.tr("Max:&nbsp;") + contributionMaxValue);
        contributionMean = new HtmlText(session.tr("Mean:&nbsp;") + contributionMeanValue);

        contributions = demandPage.getDemand().getContributions();
    }

    private HtmlRenderer<Contribution> generateContributionRenderer() {
        return new HtmlRenderer<Contribution>() {

            @Override
            public void generate(HtmlResult htmlResult, Contribution item) {

                String itemString = item.getAuthor().getLogin() + " " + item.getAmount().toPlainString() + " " + item.getCreationDate().toString();

                HtmlListItem htmlItem = new HtmlListItem(itemString);

                htmlItem.generate(htmlResult);
            }
        };
    }
}
