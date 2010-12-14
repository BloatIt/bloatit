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
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlList;
import com.bloatit.web.html.components.standard.HtmlListItem;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.utils.url.Request;

public class DemandTimelineComponent extends HtmlPageComponent {

    public DemandTimelineComponent(final Request request, final Demand demand) {
        super();

        final HtmlDiv timelineBlock = new HtmlDiv("timeline_block");
        {
            final HtmlList timelineList = new HtmlList();

            final String creationDate = "Creation: " + demand.getCreationDate().toString();
            {
                timelineList.add(creationDate);
            }
            timelineBlock.add(timelineList);
        }
        add(timelineBlock);
    }
}
