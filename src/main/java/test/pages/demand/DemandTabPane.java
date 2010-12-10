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
import test.html.HtmlElement;
import test.Parameters;
import test.html.components.advanced.HtmlTabBlock;
import test.html.components.advanced.HtmlTabBlock.HtmlTabHeader;

import test.Request;

import com.bloatit.framework.Demand;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.RequestParam;

public class DemandTabPane extends HtmlElement {

    @RequestParam(name = "demand_tab_key", defaultValue = "description_tab")
    private String activeTabKey;

    public DemandTabPane(Request request, Demand demand) {
        super();
        request.setValues(this);
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
        if (activeTabKey == "description_tab") {
            tabPane.addActiveTab(new HtmlTabBlock.HtmlTab(descriptionTab, new DemandDescriptionComponent(request, demand)));
        } else {
            tabPane.addTabHeader(descriptionTab);
        }
        if (activeTabKey == "participations_tab") {
            tabPane.addActiveTab(new HtmlTabBlock.HtmlTab(participationsTab, new DemandContributorsComponent(request, demand)));
        } else {
            tabPane.addTabHeader(participationsTab);
        }
        if (activeTabKey == "offer_tab") {
            tabPane.addActiveTab(new HtmlTabBlock.HtmlTab(offerTab, new DemandOfferListComponent(request, demand)));
        } else {
            tabPane.addTabHeader(offerTab);
        }
        
        add(tabPane);

    }
}
