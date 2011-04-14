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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlTableModel;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Contribution;
import com.bloatit.model.Feature;
import com.bloatit.web.url.ContributionProcessUrl;
import com.bloatit.web.url.FeatureContributorsComponentUrl;

@ParamContainer(value = "FeatureContributorsComponent", isComponent = true)
public final class FeatureContributorsComponent extends HtmlDiv {

    private final Feature feature;

    public FeatureContributorsComponent(final FeatureContributorsComponentUrl url, final Feature feature) {
        super();
        this.feature = feature;
        add(produce());
    }

    @SuppressWarnings("synthetic-access")
    protected HtmlElement produce() {
        final HtmlDiv contributorsBlock = new HtmlDiv("contribution_block");
        {

            try {
                final int contributionCount = feature.getContributions().size();

                // Display contribution count
                contributorsBlock.add(new HtmlTitle(Context.trn("{0} contribution", "{0} contributions", contributionCount, contributionCount), 1));

                // TODO: generate contribution graph

                // Display contribution stats
                if (contributionCount > 0) {
                    final String contributionMeanValue = Context.getLocalizator()
                                                                .getCurrency(feature.getContribution().divide(new BigDecimal(contributionCount),
                                                                                                              RoundingMode.HALF_EVEN))
                                                                .getDefaultString();
                    final String contributionMinValue = Context.getLocalizator().getCurrency(feature.getContributionMin()).getDefaultString();
                    final String contributionMaxValue = Context.getLocalizator().getCurrency(feature.getContributionMax()).getDefaultString();
                    final String contributionMedianValue = Context.getLocalizator()
                                                                  .getCurrency(computeMedian(feature.getContributions()))
                                                                  .getDefaultString();

                    final HtmlTable statTable = new HtmlTable(new ContributionStatTableModel(contributionMinValue,
                                                                                             contributionMaxValue,
                                                                                             contributionMeanValue,
                                                                                             contributionMedianValue));
                    contributorsBlock.add(statTable);
                }

                final HtmlTable table = new HtmlTable(new ContributionTableModel(feature.getContributions()));
                contributorsBlock.add(table);

                contributorsBlock.add(new ContributionProcessUrl(feature).getHtmlLink(Context.tr("Contribute")));

            } catch (final UnauthorizedOperationException e) {
                throw new ShallNotPassException(e);
            }

        }
        return contributorsBlock;
    }

    private BigDecimal computeMedian(final PageIterable<Contribution> contributions) {

        try {
            final Iterator<Contribution> it = contributions.iterator();
            final List<BigDecimal> list = new ArrayList<BigDecimal>();

            while (it.hasNext()) {
                list.add(it.next().getAmount());
            }
            Collections.sort(list);

            final int middle = list.size() / 2;
            if (list.size() % 2 == 1) {
                return list.get(middle);
            }
            return list.get(middle - 1).add(list.get(middle)).divide(new BigDecimal(2), RoundingMode.HALF_EVEN);

        } catch (final UnauthorizedOperationException e) {
            return new BigDecimal(-1);
        }

    }

    public static class ContributionTableModel extends HtmlTableModel {
        private Iterator<Contribution> it;
        private Contribution contribution;
        private final PageIterable<Contribution> contributions;

        public ContributionTableModel(final PageIterable<Contribution> contributions) {
            this.contributions = contributions;

        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public XmlNode getHeader(final int column) {
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
            return new HtmlText(value);
        }

        @Override
        public boolean next() {
            if (it == null) {
                it = contributions.iterator();
            }

            if (it.hasNext()) {
                contribution = it.next();
                return true;
            }
            return false;
        }

        @Override
        public String getColumnCss(final int column) {
            if (column == 1) {
                return "money_cell";
            }
            return null;
        }

        @Override
        public XmlNode getBody(final int column) {
            String value = "";
            try {
                switch (column) {
                    case 0:
                        value = contribution.getMember().getDisplayName();
                        break;
                    case 1:
                        // TODO: align money at right in CSS
                        value = Context.getLocalizator().getCurrency(contribution.getAmount()).getDefaultString();
                        break;
                    case 2:
                        value = Context.getLocalizator().getDate(contribution.getCreationDate()).toString(FormatStyle.MEDIUM);
                        break;
                    case 3:
                        value = contribution.getComment();
                        break;
                }

            } catch (final UnauthorizedOperationException e) {
                // Display nothing
            }

            if (value == null) {
                value = "";
            }
            return new HtmlText(value);
        }
    }

    public static final class ContributionStatTableModel extends HtmlTableModel {

        private final String contributionMinValue;
        private final String contributionMaxValue;
        private final String contributionMeanValue;
        private int line = -1;
        private final int lineCount = 1;
        private final String contributionMedianValue;

        private ContributionStatTableModel(final String contributionMinValue,
                                           final String contributionMaxValue,
                                           final String contributionMeanValue,
                                           final String contributionMedianValue) {

            this.contributionMinValue = contributionMinValue;
            this.contributionMaxValue = contributionMaxValue;
            this.contributionMeanValue = contributionMeanValue;
            this.contributionMedianValue = contributionMedianValue;

        }

        @Override
        public boolean hasHeader() {
            return true;
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnCss(final int column) {
            return "money_cell";
        }

        @Override
        public XmlNode getHeader(final int column) {
            String value = "";

            switch (column) {
                case 0:
                    value = Context.tr("Minimun");
                    break;
                case 1:
                    value = Context.tr("Maximum");
                    break;
                case 2:
                    value = Context.tr("Mean");
                    break;
                case 3:
                    value = Context.tr("Median");
                    break;
            }
            return new HtmlText(value);
        }

        @Override
        public boolean next() {
            line++;
            if (line < lineCount) {
                return true;
            }

            return false;
        }

        @Override
        public XmlNode getBody(final int column) {
            String value = "";

            switch (column) {
                case 0:
                    value = contributionMinValue;
                    break;
                case 1:
                    value = contributionMaxValue;
                    break;
                case 2:
                    value = contributionMeanValue;
                    break;
                case 3:
                    value = contributionMedianValue;
                    break;
            }
            return new HtmlText(value);
        }
    }

}
