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
package test.pages.demand;

import test.HtmlElement;
import test.pages.components.HtmlBlock;
import test.pages.demand.DemandPage.Request;

public class DemandHeadComponent extends HtmlElement{


    public DemandHeadComponent(Request request) {
        super();
        HtmlBlock demandHead = new HtmlBlock("demand_head");
        {
            //Add progress bar
            HtmlBlock demandHeadProgress = new HtmlBlock("demand_head_progress");
            {
                demandHeadProgress.add(new DemandProgressBarComponent(request));
            }
            demandHead.add(demandHeadProgress);

            //Add kudo box
            HtmlBlock demandHeadKudo = new HtmlBlock("demand_head_kudo");
            {
                demandHeadKudo.add(new DemandKudoComponent(request));
            }
            demandHead.add(demandHeadKudo);


            
        }
        add(demandHead);
    }
}
