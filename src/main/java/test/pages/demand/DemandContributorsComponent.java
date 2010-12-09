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
package test.pages.demand;

import test.Context;
import test.HtmlElement;
import test.HtmlNode;
import test.Request;
import test.pages.components.HtmlBlock;
import test.pages.components.HtmlListItem;
import test.pages.components.HtmlPagedList;
import test.pages.components.HtmlRenderer;
import test.pages.components.HtmlText;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Contribution;
import com.bloatit.framework.Demand;
import com.bloatit.web.server.Session;

public class DemandContributorsComponent extends HtmlBlock {

    private int contributionCount;
    private HtmlText contributionMin;
    private HtmlText contributionMax;
    private HtmlText contributionMean;
    private PageIterable<Contribution> contributions;
    private Demand demand;

    public DemandContributorsComponent(Request request, Demand demand) {
        super();
        this.demand = demand;
        extractData(request);
        produce(request);

    }

    protected HtmlElement produce(Request request) {
        Session session = Context.getSession();
        HtmlBlock contributorsBlock = new HtmlBlock("contributors_block");
        {

            //Display contribution count
            contributorsBlock.add(new HtmlText("" + contributionCount + session .tr("&nbsp;contributions")));


            //Display contribution stats
            if (contributionCount > 0) {
                contributorsBlock.add(contributionMin);
                contributorsBlock.add(contributionMax);
                contributorsBlock.add(contributionMean);
            }

            //Create contribution renderer
            HtmlRenderer<Contribution> contributionRenderer = generateContributionRenderer();


            //Create paged list
            HtmlPagedList<Contribution> participationsList = new HtmlPagedList<Contribution>("contribution_list", contributionRenderer, contributions, request, session);
            contributorsBlock.add(participationsList);

        }
        return contributorsBlock;
    }

    protected void extractData(Request request) {

        Session session = Context.getSession();
        contributionCount = demand.getContributions().size();

        float contributionMeanValue = demand.getContribution().floatValue() / contributionCount;
        String contributionMinValue = demand.getContributionMin().toPlainString();
        String contributionMaxValue = demand.getContributionMax().toPlainString();

        contributionMin = new HtmlText(session.tr("Min:&nbsp;") + contributionMinValue);
        contributionMax = new HtmlText(session.tr("Max:&nbsp;") + contributionMaxValue);
        contributionMean = new HtmlText(session.tr("Mean:&nbsp;") + contributionMeanValue);

        contributions = demand.getContributions();
    }

    private HtmlRenderer<Contribution> generateContributionRenderer() {
        return new HtmlRenderer<Contribution>() {

            @Override
            public HtmlNode generate(Contribution item) {
                String itemString = item.getAuthor().getLogin() + " " + item.getAmount().toPlainString() + " " + item.getCreationDate().toString();
                return new HtmlListItem(itemString);
            }
        };
    }
}
