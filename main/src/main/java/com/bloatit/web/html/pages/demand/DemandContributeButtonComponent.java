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
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlLeaf;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.form.HtmlButton;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.pages.ContributePage;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.url.Request;
import com.bloatit.web.utils.url.UrlBuilder;

public class DemandContributeButtonComponent extends HtmlLeaf {

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
