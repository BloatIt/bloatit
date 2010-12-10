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
import test.Request;
import test.UrlBuilder;
import test.html.HtmlElement;
import test.html.components.standard.HtmlDiv;
import test.html.components.standard.form.HtmlButton;
import test.html.components.standard.form.HtmlForm;
import test.pages.OfferPage;

import com.bloatit.framework.Demand;
import com.bloatit.web.server.Session;

public class DemandMakeOfferButtonComponent extends HtmlElement {

    public DemandMakeOfferButtonComponent(final Request request, final Demand demand) {
        super();
        final Session session = Context.getSession();

        final HtmlDiv makeOfferBlock = new HtmlDiv("make_offer_block");
        {

            final UrlBuilder urlBuilder = new UrlBuilder(OfferPage.class);
            urlBuilder.addParameter("idea", demand);

            final HtmlForm makeOfferForm = new HtmlForm(urlBuilder.buildUrl());
            {
                final HtmlButton makeOfferButton = new HtmlButton(session.tr("Make an offer"));
                makeOfferForm.add(makeOfferButton);
            }
            makeOfferBlock.add(makeOfferForm);
        }
        add(makeOfferBlock);
    }
}
