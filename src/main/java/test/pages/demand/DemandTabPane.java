/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with
 * BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package test.pages.demand;

import test.Context;
import test.HtmlElement;
import test.Parameters;
import test.pages.components.HtmlTabBlock;
import test.pages.components.HtmlTabBlock.HtmlTabHeader;
import test.pages.demand.DemandPage.Request;

import com.bloatit.web.server.Session;

public class DemandTabPane extends HtmlElement {


    public DemandTabPane(Request request) {
        super();
        Session session = Context.getSession();

        // Create description tab
        HtmlTabHeader descriptionTab = new HtmlTabHeader(session.tr("Description"), request.createUrl(new Parameters("demand_tab_key", "description_tab")));

        // Create participations tab
        HtmlTabHeader participationsTab = new HtmlTabHeader(session.tr("Participations"), request.createUrl(new Parameters("demand_tab_key",
                                                                                                                           "participations_tab")));

        // Create Comments tab
        HtmlTabHeader offerTab = new HtmlTabHeader(session.tr("Offers"), request.createUrl(new Parameters("demand_tab_key", "offer_tab")));

        // Create tab pane
        HtmlTabBlock tabPane = new HtmlTabBlock(); // id = participations_tab

        // Add all tabs from left to right
        if (request.activeTabKey == "description_tab") {
            tabPane.addActiveTab(new HtmlTabBlock.HtmlTab(descriptionTab, new DemandDescriptionComponent(request)));
        } else {
            tabPane.addTabHeader(descriptionTab);
        }
        if (request.activeTabKey == "participations_tab") {
            tabPane.addActiveTab(new HtmlTabBlock.HtmlTab(participationsTab, new DemandContributorsComponent(request)));
        } else {
            tabPane.addTabHeader(participationsTab);
        }
        if (request.activeTabKey == "offer_tab") {
            tabPane.addActiveTab(new HtmlTabBlock.HtmlTab(offerTab, new DemandOfferListComponent(request)));
        } else {
            tabPane.addTabHeader(offerTab);
        }
        
        add(tabPane);

    }
}
