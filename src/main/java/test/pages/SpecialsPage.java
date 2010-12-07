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

package test.pages;

import java.util.HashMap;
import java.util.Map;

import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlList;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlListItem;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTitle;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;

public class SpecialsPage extends Page {

    public SpecialsPage(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    public SpecialsPage(Session session) {
        this(session, new HashMap<String, String>());
    }

    @Override
    protected HtmlComponent generateContent() {

        final HtmlTitle pageTitle = new HtmlTitle(session.tr("Special pages"), "page_title");

        final HtmlList pageList = new HtmlList();

        pageList.addItem(new HtmlListItem(HtmlTools.generateLink(session, "Members list", new MembersListPage(session))));

        pageTitle.add(pageList);

        return pageTitle;

    }

    @Override
    public String getTitle() {
        return "Special pages list";
    }

    @Override
    public String getCode() {
        return "specials";
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
