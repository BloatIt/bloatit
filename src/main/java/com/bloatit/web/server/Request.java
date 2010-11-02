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

import java.util.Map;

import com.bloatit.web.htmlrenderer.HtmlResult;
import java.util.HashMap;

public abstract class Request {
    protected HtmlResult htmlResult;
    protected Map<String, String> parameters;
    protected Map<String, String> outputParameters = new HashMap<String, String>();
    protected Session session;
    
    protected Request(Session session, Map<String, String> parameters) {
        this.session = session;
        this.parameters = parameters;
    }

    abstract public String getCode();

    abstract protected void process();

    public final void doProcess(HtmlResult htmlResult) {
        this.htmlResult = htmlResult;
        process();
    }

    public abstract boolean isStable();

    public void setOutputParam(String key, String value) {
        outputParameters.put(key,value);
    }

    public void resetOutputParameters() {
        outputParameters.clear();
    }

    public Map<String, String> getOutputParameters() {
        return outputParameters;
    }


}
