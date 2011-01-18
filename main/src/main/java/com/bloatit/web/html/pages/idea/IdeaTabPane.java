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
package com.bloatit.web.html.pages.idea;

import com.bloatit.framework.Demand;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.components.custom.HtmlTabBlock;
import com.bloatit.web.html.components.custom.HtmlTabBlock.HtmlTab;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.IdeaPageUrl;
import com.bloatit.web.utils.url.IdeaTabPaneUrl;

@ParamContainer(value = "demandTabPane", isComponent = true)
public final class IdeaTabPane extends HtmlPageComponent {

    @RequestParam(name = "demand_tab_key", defaultValue = "description_tab")
    private String activeTabKey;

    public IdeaTabPane(final IdeaTabPaneUrl url, final Demand demand) {
        super();
        activeTabKey = url.getActiveTabKey();

        final IdeaPageUrl demandUrl = new IdeaPageUrl(demand);
        demandUrl.setDemandTabPaneUrl(url);

        // Create tab pane
        final HtmlTabBlock tabPane = new HtmlTabBlock("demand_tab_key", activeTabKey, demandUrl);

        // Create description tab
        tabPane.addTab(new HtmlTab(Context.tr("Description"), "description_tab") {
            @Override
            public HtmlNode generateBody() {
                return new IdeaDescriptionComponent(demand);
            }
        });

        // Create participations tab
        tabPane.addTab(new HtmlTab(Context.tr("Participations"), "participations_tab") {
            @Override
            public HtmlNode generateBody() {
                return new IdeaContributorsComponent(url.getContributionUrl(), demand);
            }
        });

        // Create Comments tab
        tabPane.addTab(new HtmlTab(Context.tr("Offers"), "offer_tab") {
            @Override
            public HtmlNode generateBody() {
                return new IdeaOfferListComponent(demand);
            }
        });

        add(tabPane);

    }
}
