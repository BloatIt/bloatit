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
import com.bloatit.web.html.components.custom.HtmlKudoBlock;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.Request;

public class DemandKudoComponent extends HtmlPageComponent {

    public DemandKudoComponent(final Request request, final Demand demand) {
        super();

        final HtmlDiv descriptionKudoBlock = new HtmlDiv("description_kudo_block");
        {
            final HtmlKudoBlock kudoBox = new HtmlKudoBlock(demand, Context.getSession());
            descriptionKudoBlock.add(kudoBox);
        }
        add(descriptionKudoBlock);
    }
}
