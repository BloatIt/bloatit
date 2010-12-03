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
import com.bloatit.web.utils.RequestParamResult;

public abstract class Request {
    protected HtmlResult htmlResult;
    protected Map<String, String> parameters;
    protected Session session;
    private RequestParamResult query;

    static public class ParserInit {
    }

    protected Request(Session session, Map<String, String> parameters) {
        this.session = session;
        this.parameters = parameters;
        this.query = new RequestParamResult(parameters);
    }

    protected void init(Object obj) {
        query.parse(obj);
    }

    abstract public String getCode();

    abstract protected void process();

    public final void doProcess(HtmlResult htmlResult) {
        this.htmlResult = htmlResult;
        process();
    }

    /**
     * <p>
     * Indicates wether the page is considered stable or not. When the user is
     * interrupted in his navigation (he is not logged ...) he'll return to the
     * last stable page he visited.
     * </p>
     * <p>
     * Therefore stable pages should only be main pages of the website
     * navigation, and not the result of an action
     * </p>
     * 
     * @return
     *         <i>true</i> if the page is stable
     *         <i>false</i> otherwise
     */
    public abstract boolean isStable();

    public abstract String getUrl();

    public abstract String getUrl(Map<String, String> outputParameters);

    public RequestParamResult getQuery() {
        return query;
    }

}
