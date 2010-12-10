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
package test.html.components.advanced;

import test.html.components.standard.HtmlText;
import test.html.components.standard.HtmlDiv;
import com.bloatit.framework.Kudosable;
import com.bloatit.web.server.Session;
import test.UrlBuilder;
import test.actions.LogoutAction;
import test.html.HtmlTools;
import test.html.components.standard.HtmlLink;

public class HtmlKudoBox extends HtmlDiv {

    public HtmlKudoBox(Kudosable kudosable, Session session) {
        super("kudo_box");

        HtmlLink kudoUpLink = new UrlBuilder(LogoutAction.class).getHtmlLink("kudo up");
        HtmlLink kudoDownLink = new UrlBuilder(LogoutAction.class).getHtmlLink("kudo down");

        add(new HtmlDiv("kudo_box_up").add(kudoUpLink));
        add(new HtmlText(HtmlTools.compressKarma(kudosable.getPopularity()), "kudo_box_score"));
        add(new HtmlDiv("kudo_box_down").add(kudoDownLink));

    }
}
