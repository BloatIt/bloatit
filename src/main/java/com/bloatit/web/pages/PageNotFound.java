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
package com.bloatit.web.pages;

import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTitle;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;
import java.util.Map;

public class PageNotFound extends Page {
    
    public PageNotFound(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    public PageNotFound(Session session) {
        super(session);
    }

    @Override
    protected HtmlComponent generateContent() {
        HtmlTitle errorTitle = new HtmlTitle(this.session.tr("Page not found"), "");

        return errorTitle;
        
    }

    @Override
    public String getCode() {
        return "pagenotfound";
    }

    @Override
    protected String getTitle() {
        return "Page not found";
    }
}