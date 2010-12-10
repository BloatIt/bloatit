package test;

import java.util.Map.Entry;

import com.bloatit.web.server.Session;

public class Url {
    private final Parameters parameters;
    private final String pageName;
    private final Session session;

    public Url(Parameters parameters, Session session, String pageName) {
        super();
        this.parameters = parameters;
        this.pageName = pageName;
        this.session = session;
    }
    
    protected Url(){
        super();
        this.parameters = new Parameters();
        this.pageName = "";
        this.session = null;
    }
    
    public boolean isValid(){
        return session != null && pageName != "";
    }

    public String getPageName() {
        return pageName;
    }
    
    public Url addParameter(String name, String value) {
        parameters.put(name, value);
        return this;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(session.getLanguage().getCode()).append("/").append(pageName).append("/");
        for (Entry<String, String> param : parameters.entrySet()) {
            sb.append(param.getKey());
            sb.append("-");
            sb.append(param.getValue());
            sb.append("/");
        }
        // TODO testme
        return  sb.toString();
    }

    public Parameters getParameters() {
        return parameters;
    }
    
}
