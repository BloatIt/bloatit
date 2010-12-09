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

import java.util.HashMap;

import test.Context;
import test.HtmlElement;
import test.Request;
import test.pages.components.HtmlBlock;
import test.pages.components.HtmlButton;
import test.pages.components.HtmlForm;

import com.bloatit.framework.Demand;
import com.bloatit.web.pages.ContributePage;
import com.bloatit.web.server.Session;

public class DemandContributeButtonComponent extends HtmlElement {

    private HashMap<String, String> params;

    public DemandContributeButtonComponent(Request request, Demand demand) {
        super();
        params = new HashMap<String, String>();
        params.put("idea", String.valueOf(demand.getId()));
        add(produce(request));
    }

    protected HtmlElement produce(Request request) {

        final HtmlBlock contributeBlock = new HtmlBlock("contribute_block");
        {

            Session session = Context.getSession();
            HtmlForm contributeForm = new HtmlForm(new ContributePage(session, params), HtmlForm.Method.GET);
            {
                // Add button
                HtmlButton contributeButton = new HtmlButton(session.tr("Contribute"));
                contributeForm.add(contributeButton);

            }
            contributeBlock.add(contributeForm);
        }

        return contributeBlock;
    }

}
