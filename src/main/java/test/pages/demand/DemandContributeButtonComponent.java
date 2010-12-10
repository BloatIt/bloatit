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
import test.pages.ContributePage;

import com.bloatit.framework.Demand;
import com.bloatit.web.server.Session;

public class DemandContributeButtonComponent extends HtmlElement {

    private final Demand demand;

    public DemandContributeButtonComponent(final Request request, final Demand demand) {
        super();
        this.demand = demand;
        add(produce(request));
    }

    protected HtmlElement produce(final Request request) {

        final HtmlDiv contributeBlock = new HtmlDiv("contribute_block");
        {

            final Session session = Context.getSession();

            final UrlBuilder urlBuilder = new UrlBuilder(ContributePage.class);
            urlBuilder.addParameter("targetIdea", demand);

            final HtmlForm contributeForm = new HtmlForm(urlBuilder.buildUrl());
            {
                // Add button
                final HtmlButton contributeButton = new HtmlButton(session.tr("Contribute"));
                contributeForm.add(contributeButton);

            }
            contributeBlock.add(contributeForm);
        }

        return contributeBlock;
    }

}
