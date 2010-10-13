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

import com.bloatit.model.exceptions.ElementNotFoundException;
import com.bloatit.web.htmlrenderer.HtmlResult;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class Request {
    protected HtmlResult htmlResult;
    protected Map<String, String> query;
    protected Map<String, String> post;
    protected Map<String, String> parameters;
    protected Session session;

    public Request(){
        
    }
    
    protected Request(Session session,  Map<String, String> parameters){
        this.session = session;
        this.parameters = parameters;
    }

    public void init(Session session,  Map<String, String> parameters){
        this.session = session;
        this.parameters = parameters;
    }

    public void init(Session session) {
        this.init(session, new HashMap<String, String>());
    }

    public void doProcess(HtmlResult htmlResult, Map<String, String> query, Map<String, String> post) {
        try {
            this.htmlResult = htmlResult;
            this.query = query;
            this.post = post;
            this.process();
        } catch (ElementNotFoundException ex) {
            //TODO
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    abstract public String getCode();

    abstract protected void process() throws ElementNotFoundException;
}
