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

import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTabBlock;
import com.bloatit.web.pages.components.PageComponent;

public class DemandTabPane extends PageComponent {

    private final DemandPage demandPage;
    
    public DemandTabPane(DemandPage demandPage) {
        super(demandPage.getSession());
        this.demandPage = demandPage;
    }


    /**
     * Creates the block that will be displayed in the offer tab.
     */
    @Override
    protected HtmlComponent produce() {
        

        //Create description tab
        HtmlTabBlock.HtmlTab descriptionTab = new HtmlTabBlock.HtmlTab("description_tab", session.tr("Description"), demandPage, new DemandDescriptionComponent(demandPage));

        //Create participations tab
        HtmlTabBlock.HtmlTab participationsTab = new HtmlTabBlock.HtmlTab("participations_tab", session.tr("Participations"), demandPage, new DemandContributorsComponent(demandPage));

        //Create Comments tab
        HtmlTabBlock.HtmlTab offerTab = new HtmlTabBlock.HtmlTab("offer_tab", session.tr("Offers"), demandPage, new DemandOfferListComponent(demandPage));




        //Create tab pane
        HtmlTabBlock tabPane = new HtmlTabBlock("demand_tab");


        //Add all tabs from left to right
        tabPane.addTab(descriptionTab);
        tabPane.addTab(participationsTab);
        tabPane.addTab(offerTab);

        //Select the right tab from params
        if (demandPage.getParameters().contains("demand_tab_key")) {
            tabPane.selectTab(demandPage.getParameters().getValue("demand_tab_key"));
        } else {
            tabPane.selectTab("description_tab");
        }
        return tabPane;
    }

    @Override
    protected void extractData() {
        
    }
}
