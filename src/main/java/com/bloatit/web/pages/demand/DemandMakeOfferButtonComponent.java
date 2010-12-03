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
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlButton;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlForm;
import com.bloatit.web.pages.OfferPage;
import com.bloatit.web.pages.components.PageComponent;
import java.util.HashMap;

public class DemandMakeOfferButtonComponent extends PageComponent {

    private final DemandPage demandPage;
    private HashMap<String, String> params;

    public DemandMakeOfferButtonComponent(DemandPage demandPage) {
        super(demandPage.getSession());
        this.demandPage = demandPage;
    }

    @Override
    protected HtmlComponent produce() {

        final HtmlBlock makeOfferBlock = new HtmlBlock("make_offer_block");
        {

            HtmlForm makeOfferForm = new HtmlForm(new OfferPage(session, params));
            {
                HtmlButton makeOfferButton = new HtmlButton(session.tr("Make an offer"));
                makeOfferForm.add(makeOfferButton);
            }
            makeOfferBlock.add(makeOfferForm);
        }
        return makeOfferBlock;
    }

    @Override
    protected void extractData() {
        params = new HashMap<String, String>();
        params.put("idea", String.valueOf(this.demandPage.getDemand().getId()));
    }
}
