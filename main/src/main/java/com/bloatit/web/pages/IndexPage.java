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

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.Image.ImageType;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlImage;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.model.HighlightDemand;
import com.bloatit.model.managers.HighlightDemandManager;
import com.bloatit.web.components.IndexDemandBlock;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.SideBarElementLayout;
import com.bloatit.web.pages.master.TwoColumnLayout;
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
//            final HtmlParagraph globalConcept = new HtmlParagraph(Context.tr("Linkeos is a platform for free software funding."));
//            globalDescription.add(globalConcept);
//
//            final HtmlParagraph needText = new HtmlParagraph();
//            needText.addText(Context.tr("If you have a need about a free software, you can "));
//            needText.add(new CreateDemandPageUrl().getHtmlLink(Context.tr("create a new demand")));
//            needText.addText(Context.tr(" and contribute to it."));
//            globalDescription.add(needText);
//
//            final HtmlParagraph devText = new HtmlParagraph();
//            devText.addText(Context.tr("If you are a developer, you can make an offer on existing demands to develop it again money."));
//            globalDescription.add(devText);
//
//            final HtmlParagraph moreText = new HtmlParagraph();
//            moreText.addText(Context.tr("You can find more informations about Linkeos's in the documentation."));
//            globalDescription.add(moreText);

            HtmlTitle title = new HtmlTitle("Get paid to create free software", 1);

            globalDescription.add(title);

            HtmlImage image = new HtmlImage(new Image("presentation.png", ImageType.LOCAL), tr("Elveos's presentation"));
            globalDescription.add(image);



        }
        add(globalDescription);


        TwoColumnLayout twoColumnLayout = new TwoColumnLayout(true);
        twoColumnLayout.addLeft(new HtmlTitle(tr("Hightlighted feature"),1));

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
                            demandListLeftCase.add(new IndexDemandBlock(hightlightDemandArray[i * 2].getDemand(),hightlightDemandArray[i * 2].getReason() ));
                        }
                    }
                    demandListRow.add(demandListLeftCase);

                    final HtmlDiv demandListRightCase = new HtmlDiv("demand_list_right_case");
                    {
                        if (hightlightDemandArray[i * 2 + 1] != null) {
                            demandListRightCase.add(new IndexDemandBlock(hightlightDemandArray[i * 2 + 1].getDemand(), hightlightDemandArray[i * 2 + 1].getReason()));
                        }
                    }
                    demandListRow.add(demandListRightCase);

                }
                demandList.add(demandListRow);
            }
        }
        twoColumnLayout.addLeft(demandList);

        twoColumnLayout.addRight(new SideBarElementLayout() {
            {
                add(new HtmlParagraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec scelerisque, neque pellentesque sollicitudin ullamcorper, lorem nibh turpis duis."));
                add(new HtmlParagraph("Nullam non quam dui, non ullamcorper libero. Duis justo nibh, tristique at turpis duis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer libero elit, facilisis in vehicula eu, porta quis quam. Aliquam cursus posuere."));
                add(new HtmlParagraph("Fusce adipiscing nunc nisi. Nulla justo nibh, laoreet at adipiscing vel, lobortis ut ligula. In a purus nec elit ornare gravida. Fusce varius metus eu libero posuere pretium. Nam et feugiat tortor. Curabitur adipiscing tincidunt nibh ac lobortis. Aenean sed nulla ut lacus fringilla semper non ac nunc. Etiam iaculis vestibulum quam. Pellentesque id ipsum ac tortor porttitor cursus ac a lorem. Pellentesque imperdiet rutrum nibh, vel posuere."));
            }
        });

        add(twoColumnLayout);

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
