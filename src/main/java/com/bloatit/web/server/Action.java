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

import java.util.HashMap;
import java.util.Map;

public abstract class Action extends Request {

    public Action(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    @Override
    protected void process() {
    }

    @Override
    public String getUrl() {
        return "/" + session.getLanguage().getCode() + "/action/" + getCode();
    }

    @Override
    public String getUrl(Map<String, String> outputParameters) {

        Map<String, String> params = new HashMap<String, String>();
        params.putAll(parameters);
        params.putAll(outputParameters);
        

        return "/" + session.getLanguage().getCode() + "/action/" + getCode();
    }




    @Override
    public boolean isStable() {
        return false;
    }

}
