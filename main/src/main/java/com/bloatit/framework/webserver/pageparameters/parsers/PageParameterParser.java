package com.bloatit.framework.webserver.pageparameters.parsers;

import java.util.Iterator;

import com.bloatit.framework.webserver.pageparameters.PageParameter;
import com.sun.tools.javac.util.List;

public abstract class PageParameterParser implements Iterable<PageParameter> {
    
    private List<PageParameter> parameters;
    
    public abstract String readNext();

    public abstract String read(String key);
    
    public Iterator<PageParameter> iterator(){
        return parameters.iterator();
    }
}
