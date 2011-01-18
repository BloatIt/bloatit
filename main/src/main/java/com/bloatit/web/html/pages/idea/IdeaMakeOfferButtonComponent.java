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

import com.bloatit.framework.Demand;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.OfferPageUrl;

public class IdeaMakeOfferButtonComponent extends HtmlPageComponent {

    public IdeaMakeOfferButtonComponent(final Demand demand) {
        super();

        final HtmlDiv makeOfferBlock = new HtmlDiv("make_offer_block");
        {
            final HtmlLink link = new OfferPageUrl(demand).getHtmlLink(Context.tr("Make an offer"));
            link.setCssClass("button");

            makeOfferBlock.add(link);
        }
        add(makeOfferBlock);
    }
}
