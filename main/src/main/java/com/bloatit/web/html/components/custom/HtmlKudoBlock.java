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
package com.bloatit.web.html.components.custom;

import com.bloatit.framework.Kudosable;
import com.bloatit.web.html.HtmlTools;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.url.LogoutActionUrl;

public class HtmlKudoBlock extends HtmlDiv {

    public HtmlKudoBlock(final Kudosable kudosable, final Session session) {
        super("kudo_box");

        final HtmlLink kudoUpLink = new LogoutActionUrl().getHtmlLink(new HtmlDiv("kudo_box_up"));
        kudoUpLink.setTitle("kudo up");
        final HtmlLink kudoDownLink = new LogoutActionUrl().getHtmlLink(new HtmlDiv("kudo_box_down"));
        kudoUpLink.setTitle("kudo down");

        add(kudoUpLink);
        add(new HtmlParagraph(HtmlTools.compressKarma(kudosable.getPopularity()), "kudo_box_score"));
        add(kudoDownLink);

    }
}
