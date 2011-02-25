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

package com.bloatit.web.pages;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.model.HighlightDemand;
import com.bloatit.model.managers.HighlightDemandManager;
import com.bloatit.web.components.HtmlDemandSumary;
import com.bloatit.web.components.HtmlDemandSumary.Compacity;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.CreateDemandPageUrl;
import com.bloatit.web.url.IndexPageUrl;

@ParamContainer("index")
public final class IndexPage extends MasterPage {

    public IndexPage(final IndexPageUrl indexPageUrl) {
        super(indexPageUrl);
    }

    @Override
    protected void doCreate() throws RedirectException {

        final HtmlDiv globalDescription = new HtmlDiv("global_description");
        {
            final HtmlParagraph globalConcept = new HtmlParagraph(Context.tr("Linkeos is a platform for free software funding."));
            globalDescription.add(globalConcept);

            final HtmlParagraph needText = new HtmlParagraph();
            needText.addText(Context.tr("If you have a need about a free software, you can "));
            needText.add(new CreateDemandPageUrl().getHtmlLink(Context.tr("create a new demand")));
            needText.addText(Context.tr(" and contribute to it."));
            globalDescription.add(needText);

            final HtmlParagraph devText = new HtmlParagraph();
            devText.addText(Context.tr("If you are a developer, you can make an offer on existing demands to develop it again money."));
            globalDescription.add(devText);

            final HtmlParagraph moreText = new HtmlParagraph();
            moreText.addText(Context.tr("You can find more informations about Linkeos's in the documentation."));
            globalDescription.add(moreText);

        }
        add(globalDescription);

        final HtmlDiv demandList = new HtmlDiv("demand_list");
        {

            final int demandCount = 6;

            final PageIterable<HighlightDemand> hightlightDemandList = HighlightDemandManager.getAll();

            final HighlightDemand[] hightlightDemandArray = new HighlightDemand[demandCount];

            for (final HighlightDemand highlightDemand : hightlightDemandList) {
                final int position = highlightDemand.getPosition() - 1;
                if (position < demandCount) {
                    if (hightlightDemandArray[position] == null) {
                        hightlightDemandArray[position] = highlightDemand;
                    } else {
                        if (hightlightDemandArray[position].getActivationDate().before(highlightDemand.getActivationDate())) {
                            hightlightDemandArray[position] = highlightDemand;
                        }
                    }
                }
            }

            for (int i = 0; i < (demandCount + 1) / 2; i++) {

                final HtmlDiv demandListRow = new HtmlDiv("demand_list_row");
                {
                    final HtmlDiv demandListLeftCase = new HtmlDiv("demand_list_left_case");
                    {
                        if (hightlightDemandArray[i * 2] != null) {
                            demandListLeftCase.add(new HtmlDemandSumary(hightlightDemandArray[i * 2].getDemand(), Compacity.COMPACT));
                        }
                    }
                    demandListRow.add(demandListLeftCase);

                    final HtmlDiv demandListRightCase = new HtmlDiv("demand_list_right_case");
                    {
                        if (hightlightDemandArray[i * 2 + 1] != null) {
                            demandListRightCase.add(new HtmlDemandSumary(hightlightDemandArray[i * 2 + 1].getDemand(), Compacity.COMPACT));
                        }
                    }
                    demandListRow.add(demandListRightCase);

                }
                demandList.add(demandListRow);
            }
        }
        add(demandList);

    }

    @Override
    protected String getPageTitle() {
        return "Finance free software";
    }

    @Override
    protected String getCustomCss() {
        return "index.css";
    }

    @Override
    public boolean isStable() {
        return true;
    }

}
