package com.bloatit.rest;

import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.webserver.Session;
import com.bloatit.framework.webserver.WebServer;
import com.bloatit.framework.webserver.masters.Linkable;

public class BloatitRestServer extends WebServer{
    @Override
    protected Linkable constructLinkable(String pageCode, Parameters params, Session session) {
        if(pageCode.equals("plop")){
            
        }
        return null;
    }
}
