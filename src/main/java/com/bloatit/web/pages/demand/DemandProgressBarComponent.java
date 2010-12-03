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
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlProgressBar;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlText;
import com.bloatit.web.pages.components.PageComponent;

public class DemandProgressBarComponent extends PageComponent {

    private final DemandPage demandPage;
    private float progressValue;
    private HtmlText totalText;

    public DemandProgressBarComponent(DemandPage demandPage) {
        super(demandPage.getSession());
        this.demandPage = demandPage;

    }

    @Override
    protected HtmlComponent produce() {


        final HtmlBlock progressBlock = new HtmlBlock("progress_block");
        {

            progressBlock.add(new DemandContributeButtonComponent(demandPage));
            
            final HtmlBlock progressBarBlock = new HtmlBlock("column");
            {
                progressBarBlock.add(new HtmlProgressBar(progressValue));
            }


            progressBlock.add(progressBarBlock);
            
            progressBlock.add(new  DemandMakeOfferButtonComponent(demandPage));



        }

        return progressBlock;
    }

    @Override
    protected void extractData() {
        progressValue = 0;
        progressValue = 42 * (1 - 1 / (1 + demandPage.getDemand().getContribution().floatValue() / 200));

        totalText = new HtmlText(demandPage.getDemand().getContribution().toPlainString() + "€");

    }
}
