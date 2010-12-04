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

package com.bloatit.web.server;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.bloatit.web.htmlrenderer.HtmlResult;
import com.bloatit.web.utils.RequestParamSetter;

public abstract class Request {
    protected HtmlResult htmlResult;
    protected Session session;
    private Parameters parameters;

    public static class Parameter {
        private String value;
        private String name;

        public Parameter(String value, String name) {
            super();
            this.value = value;
            this.name = name;
        }

        protected String getValue() {
            return value;
        }

        protected String getName() {
            return name;
        }

    }

    public static class Parameters implements Iterable<Entry<String, String>> {
        private Map<String, String> parameters;
        private RequestParamSetter query;
        private RequestParamSetter.Messages messages;

        protected Parameters(Map<String, String> parameters) {
            super();
            this.parameters = parameters;
            this.query = new RequestParamSetter(parameters);
        }

        public Parameters(Parameters other) {
            this(other.parameters);
            this.messages = other.messages;
        }

        private void setValue(Object obj) {
            query.setValues(obj);
        }

        public boolean contains(String name) {
            return parameters.containsKey(name);
        }

        public String getValue(String name) {
            String value;
            if ((value = parameters.get(name)) == null) {
                return "";
            }
            return value;
        }

        public Set<String> getNames() {
            return parameters.keySet();
        }

        public RequestParamSetter.Messages getMessages() {
            return messages;
        }

        public void add(String name, String value) {
            parameters.put(name, value);
        }

        public void addAll(Map<String, String> parameters) {
            parameters.putAll(parameters);
        }

        @Override
        public Iterator<Entry<String, String>> iterator() {
            return parameters.entrySet().iterator();
        }

    }

    static public class ParserInit {}

    /**
     * Do not forget to call load !
     * 
     * @param session
     * @param parameters
     */
    protected Request(Session session, Map<String, String> parameters) {
        this.session = session;
        this.parameters = new Parameters(parameters);
    }

    protected Request(Session session, Parameters results) {
        this.session = session;
        this.parameters = results;
    }

    public final void load() {
        parameters.setValue(this);
    }

    abstract public String getCode();

    abstract protected void process();

    public final void doProcess(HtmlResult htmlResult) {
        this.htmlResult = htmlResult;
        process();
    }

    /**
     * <p>
     * Indicates wether the page is considered stable or not. When the user is interrupted
     * in his navigation (he is not logged ...) he'll return to the last stable page he
     * visited.
     * </p>
     * <p>
     * Therefore stable pages should only be main pages of the website navigation, and not
     * the result of an action
     * </p>
     * 
     * @return <i>true</i> if the page is stable <i>false</i> otherwise
     */
    public abstract boolean isStable();

    public abstract String getUrl();

    public abstract String getUrl(Map<String, String> outputParameters);

    public Parameters getParameters() {
        return parameters;
    }

    public Session getSession() {
        return session;
    }
}
