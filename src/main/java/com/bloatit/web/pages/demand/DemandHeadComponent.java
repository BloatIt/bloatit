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

import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.pages.components.PageComponent;

public class DemandHeadComponent extends PageComponent {

    private final DemandPage demandPage;

    public DemandHeadComponent(DemandPage demandPage) {
        super(demandPage.getSession());
        this.demandPage = demandPage;

    }

    @Override
    protected HtmlComponent produce() {
        HtmlBlock demandHead = new HtmlBlock("demand_head");
        {
            //Add progress bar
            HtmlBlock demandHeadProgress = new HtmlBlock("demand_head_progress");
            {
                demandHeadProgress.add(new DemandProgressBarComponent(demandPage));
            }
            demandHead.add(demandHeadProgress);

            //Add kudo box
            HtmlBlock demandHeadKudo = new HtmlBlock("demand_head_kudo");
            {
                demandHeadKudo.add(new DemandKudoComponent(demandPage));
            }
            demandHead.add(demandHeadKudo);


            
        }
        return demandHead;
    }

    @Override
    protected void extractData() {

    }
}
