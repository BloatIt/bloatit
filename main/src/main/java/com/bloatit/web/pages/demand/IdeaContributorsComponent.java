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
package com.bloatit.web.pages.demand;

import static com.bloatit.framework.webserver.Context.tr;

import java.math.BigDecimal;
import java.util.Iterator;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.advanced.HtmlTable;
import com.bloatit.framework.webserver.components.advanced.HtmlTable.HtmlTableModel;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Contribution;
import com.bloatit.model.Demand;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.url.IdeaContributorsComponentUrl;

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
        } catch (final UnauthorizedOperationException e) {
            // No right, no display
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

            HtmlTable table = new HtmlTable(new  ContributionTableModel(contributions));

            contributorsBlock.add(table);

        }
        return contributorsBlock;
    }

    protected void extractData() throws UnauthorizedOperationException {

        contributionCount = demand.getContributions().size();

        if (contributionCount > 0) {

            final String contributionMeanValue = Context.getLocalizator().getCurrency(demand.getContribution().divide(new BigDecimal(contributionCount))).getDefaultString();
            final String contributionMinValue = Context.getLocalizator().getCurrency(demand.getContributionMin()).getDefaultString();
            final String contributionMaxValue = Context.getLocalizator().getCurrency(demand.getContributionMax()).getDefaultString();

            contributionMin = new HtmlParagraph(tr("Min: ") + contributionMinValue);
            contributionMax = new HtmlParagraph(tr("Max: ") + contributionMaxValue);
            contributionMean = new HtmlParagraph(tr("Mean: ") + contributionMeanValue);
        }

        contributions = demand.getContributions();

    }


    private static final class ContributionTableModel implements HtmlTableModel {
        private Iterator<Contribution> it;
        private Contribution contribution;
        private final PageIterable<Contribution> contributions;

        private ContributionTableModel(PageIterable<Contribution> contributions) {
            this.contributions = contributions;

        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getHeader(int column) {
            String value;
            switch (column) {
            case 0:
                value = Context.tr("Author");
                break;
            case 1:
                value = Context.tr("Amount");
                break;
            case 2:
                value = Context.tr("Date");
                break;
            case 3:
                value = Context.tr("Comment");
                break;
            default:
                value = "";
                break;
            }
            return value;
        }

        @Override
        public boolean next() {
            if(it == null) {
                it = contributions.iterator();
            }

            if (it.hasNext()) {
                contribution = it.next();
                return true;
            }
            return false;
        }

        @Override
        public String getBody(int column) {
            String value = "";
            try {
                switch (column) {
                case 0:
                    value = contribution.getAuthor().getDisplayName();
                    break;
                case 1:
                    //TODO: align money at right in CSS
                    value = Context.getLocalizator().getCurrency(contribution.getAmount()).getDefaultString();
                    break;
                case 2:
                    value = contribution.getCreationDate().toString();
                    break;
                case 3:
                    value = contribution.getComment();
                    break;
                }

            } catch (UnauthorizedOperationException e) {
                //Display nothing
            }

            if(value == null) {
                value = "";
            }
            return value;
        }
    }

}
