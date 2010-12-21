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
package com.bloatit.web.html.pages.idea;

import com.bloatit.framework.Demand;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.url.ContributePageUrl;

public class IdeaContributeButtonComponent extends HtmlPageComponent {

    private final Demand demand;

    public IdeaContributeButtonComponent(final Demand demand) {
        super();
        this.demand = demand;
        add(produce());
    }

    protected HtmlElement produce() {
        final HtmlDiv contributeBlock = new HtmlDiv("contribute_block");
        {
            final Session session = Context.getSession();

            final HtmlForm contributeForm = new HtmlForm(new ContributePageUrl(demand).urlString());
            {
                // Add button
                final HtmlSubmit contributeButton = new HtmlSubmit(session.tr("Contribute"));
                contributeForm.add(contributeButton);
            }
            contributeBlock.add(contributeForm);
        }
        return contributeBlock;
    }

}
