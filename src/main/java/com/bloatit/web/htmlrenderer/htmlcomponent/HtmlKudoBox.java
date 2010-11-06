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
package com.bloatit.web.htmlrenderer.htmlcomponent;

import com.bloatit.framework.Kudosable;
import com.bloatit.web.actions.LogoutAction;
import com.bloatit.web.htmlrenderer.HtmlResult;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.server.Session;

public class HtmlKudoBox extends HtmlComponent {

    private final Kudosable kudosable;
    private final Session session;

    public HtmlKudoBox(Kudosable kudosable, Session session) {
        this.kudosable = kudosable;
        this.session = session;
    }

    @Override
    public void generate(HtmlResult htmlResult) {
        HtmlBlock kudoBox = new HtmlBlock("kudo_box");

        HtmlBlock kudoBoxUp = new HtmlBlock("kudo_box_up");

        HtmlBlock kudoBoxDown = new HtmlBlock("kudo_box_down");

        kudoBoxUp.setText("kudo up");
        kudoBoxDown.setText("kudo down");

        String kudoUpLink = HtmlTools.getActionLink(session, new LogoutAction(session));
        String kudoDownLink = HtmlTools.getActionLink(session, new LogoutAction(session));

        kudoBox.add(new HtmlLinkComponent(kudoUpLink, kudoBoxUp));
        kudoBox.add(new HtmlText("kudo_box_score", HtmlTools.compressKarma(kudosable.getPopularity())));
        kudoBox.add(new HtmlLinkComponent(kudoDownLink, kudoBoxDown));

        kudoBox.generate(htmlResult);
    }
}
