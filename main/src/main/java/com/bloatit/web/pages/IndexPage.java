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

package com.bloatit.web.pages;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.web.components.HtmlDemandSumary;
import com.bloatit.web.pages.master.Page;
import com.bloatit.web.url.CreateDemandPageUrl;
import com.bloatit.web.url.IndexPageUrl;

@ParamContainer("index")
public final class IndexPage extends Page {

    public IndexPage(final IndexPageUrl indexPageUrl) {
        super(indexPageUrl);
    }

    @Override
    protected void doCreate() throws RedirectException {

        final HtmlDiv globalDescription = new HtmlDiv("global_description");
        {
            HtmlParagraph globalConcept = new HtmlParagraph(Context.tr("Linkeos is a platform for free software funding."));
            globalDescription.add(globalConcept);

            HtmlParagraph needText = new HtmlParagraph();
            needText.addText(Context.tr("If you have a need about a free software, you can "));
            needText.add(new CreateDemandPageUrl().getHtmlLink(Context.tr("create a new demand")));
            needText.addText(Context.tr(" and contribute to it."));
            globalDescription.add(needText);

            HtmlParagraph devText = new HtmlParagraph();
            devText.addText(Context.tr("If you are a developer, you can make an offer on existing demands to develop it again money."));
            globalDescription.add(devText);


            HtmlParagraph moreText = new HtmlParagraph();
            moreText.addText(Context.tr("You can find more informations about Linkeos's in the documentation."));
            globalDescription.add(moreText);

        }
        add(globalDescription);


        final HtmlDiv demandList = new HtmlDiv("demand_list");
        {
            int demandCount = 6;

            for(int i = 0; i < (demandCount+1)/2 ; i++) {

                final HtmlDiv demandListRow = new HtmlDiv("demand_list_row");
                {
                    final HtmlDiv demandListLeftCase = new HtmlDiv("demand_list_left_case");
                    {
                        demandListLeftCase.add(new HtmlDemandSumary(null, true));
                    }
                    demandListRow.add(demandListLeftCase);

                    final HtmlDiv demandListRightCase= new HtmlDiv("demand_list_right_case");
                    {
                        demandListRightCase.add(new HtmlDemandSumary(null, true));
                    }
                    demandListRow.add(demandListRightCase);

                }
                demandList.add(demandListRow);
            }
        }
        add(demandList);


    }

    @Override
    protected String getTitle() {
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
