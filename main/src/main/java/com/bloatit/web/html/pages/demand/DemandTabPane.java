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
package com.bloatit.web.html.pages.demand;


import com.bloatit.framework.Demand;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.components.custom.HtmlTabBlock;
import com.bloatit.web.html.components.custom.HtmlTabBlock.HtmlTab;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.url.Request;
import com.bloatit.web.utils.url.UrlBuilder;

@ParamContainer(value = "demandTabPane", isComponent = true)
public class DemandTabPane extends HtmlPageComponent {

    @RequestParam(name = "demand_tab_key", defaultValue = "description_tab")
    private String activeTabKey;

    public DemandTabPane(final Request request, final Demand demand) {
        super();
        request.setValues(this);
        final Session session = Context.getSession();

        UrlBuilder tablinks = new UrlBuilder(DemandPage.class, request.getParameters());

        // Create tab pane
        final HtmlTabBlock tabPane = new HtmlTabBlock("demand_tab_key", activeTabKey, tablinks);


        // Create description tab
        tabPane.addTab(new HtmlTab(session.tr("Description"), "description_tab" ) {
            @Override
            public HtmlNode generateBody() {
                return new DemandDescriptionComponent(request, demand);
            }
        });

        // Create participations tab
        tabPane.addTab(new HtmlTab(session.tr("Participations"), "participations_tab" ) {
            @Override
            public HtmlNode generateBody() {
                return new DemandContributorsComponent(request, demand);
            }
        });

        // Create Comments tab
        tabPane.addTab(new HtmlTab(session.tr("Offers"), "offer_tab" ) {
            @Override
            public HtmlNode generateBody() {
                return new DemandOfferListComponent(request, demand);
            }
        });


        add(tabPane);

    }
}
