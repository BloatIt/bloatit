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

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.Demand;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.custom.HtmlProgressBar;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.pages.master.HtmlPageComponent;

public final class IdeaProgressBarComponent extends HtmlPageComponent {

    private final Demand demand;

    public IdeaProgressBarComponent(final Demand demand) {
        super();
        this.demand = demand;
        add(produce());
    }

    protected HtmlElement produce() {

        final HtmlDiv progressBlock = new HtmlDiv("progress_block");
        {

            progressBlock.add(new IdeaContributeButtonComponent(demand));

            final HtmlDiv progressBarBlock = new HtmlDiv("column");
            try {
                final float progressValue = (float) Math.floor(demand.getProgression());
                if (progressValue < Demand.PROGRESSION_PERCENT) {
                    progressBarBlock.add(new HtmlProgressBar(progressValue));
                } else {
                    progressBarBlock.add(new HtmlProgressBar(Demand.PROGRESSION_PERCENT));
                }
                if (demand.getOffers().size() > 0) {
                    progressBarBlock.addText(progressValue + "%");
                }
                progressBarBlock.addText(demand.getContribution().toString());
            } catch (final UnauthorizedOperationException e) {
                // No right, no progress bar
            }

            progressBlock.add(progressBarBlock);

            progressBlock.add(new IdeaMakeOfferButtonComponent(demand));

        }

        return progressBlock;
    }
}
