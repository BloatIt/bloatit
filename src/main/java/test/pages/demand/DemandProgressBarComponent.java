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

import test.Request;
import test.html.HtmlElement;
import test.html.components.advanced.HtmlProgressBar;
import test.html.components.standard.HtmlDiv;
import test.html.components.standard.HtmlText;

import com.bloatit.framework.Demand;

public class DemandProgressBarComponent extends HtmlElement {

    private float progressValue;
    private HtmlText totalText;
    private final Demand demand;

    public DemandProgressBarComponent(final Request request, final Demand demand) {
        super();
        this.demand = demand;
        extractData(request);
        add(produce(request));
    }

    protected HtmlElement produce(final Request request) {

        final HtmlDiv progressBlock = new HtmlDiv("progress_block");
        {

            progressBlock.add(new DemandContributeButtonComponent(request, demand));

            final HtmlDiv progressBarBlock = new HtmlDiv("column");
            {
                progressBarBlock.add(new HtmlProgressBar(progressValue));
            }

            progressBlock.add(progressBarBlock);

            progressBlock.add(new DemandMakeOfferButtonComponent(request, demand));

        }

        return progressBlock;
    }

    protected void extractData(final Request request) {
        progressValue = 0;
        progressValue = 42 * (1 - 1 / (1 + demand.getContribution().floatValue() / 200));

        totalText = new HtmlText(demand.getContribution().toPlainString() + "€");

    }
}
