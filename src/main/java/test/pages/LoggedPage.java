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

import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;


public abstract class LoggedPage extends Page {

    protected LoggedPage(Session session) {
        this(session, new HashMap<String, String>());
    }

    protected LoggedPage(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    public LoggedPage(Session session, Parameters parameters) {
        super(session, parameters);
    }

    @Override
    final protected HtmlComponent generateContent() {
        if(session.isLogged()) {
            return generateRestrictedContent();
        } else {
            session.notifyBad(getRefusalReason());
            session.setTargetPage(this);
            htmlResult.setRedirect(new LoginPage(session));
            return null;
        }
    }

    
    public abstract HtmlComponent generateRestrictedContent();
    public abstract String getRefusalReason();

}
