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

package com.bloatit.web.server;

import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlString;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Action extends Request {

    public Action(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    @Override
    protected void process() {
    }

    @Override
    public String getUrl() {
        HtmlString link = new HtmlString(session);
        link.add("/" + session.getLanguage().getCode() + "/action/" + getCode());

        for (Entry<String, String> entry : parameters.entrySet()) {
            if(!entry.getKey().equals("page") && !entry.getKey().equals("lang")) {
                link.add("/" + entry.getKey() + "-");
                link.secure(entry.getValue());
            }
        }

        return link.toString();
    }

    @Override
    public String getUrl(Map<String, String> outputParameters) {
        Map<String, String> params = new HashMap<String, String>();
        params.putAll(this.parameters);
        params.putAll(outputParameters);


        HtmlString link = new HtmlString(session);
        link.add("/" + session.getLanguage().getCode() + "/action/" + getCode());

        for (Entry<String, String> entry : params.entrySet()) {
            if(!entry.getKey().equals("page") && !entry.getKey().equals("lang")) {
                link.add("/" + entry.getKey() + "-");
                link.secure(entry.getValue());
            }
        }
        return link.toString();
    }




    @Override
    public boolean isStable() {
        return false;
    }

}
