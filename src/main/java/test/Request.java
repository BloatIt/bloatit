package test;

import java.util.HashMap;
import java.util.Map;

import com.bloatit.web.utils.RequestParamSetter;
import com.bloatit.web.utils.RequestParamSetter.Messages;

public class Request {

    private String pageName;
    private Map<String, String> parameters = new HashMap<String, String>();
    private Messages errors;

    public Request(String pageName, Map<String, String> parameters) {
        super();
        this.pageName = pageName;
        this.parameters = parameters;
        this.errors = null;
    }

    protected String getPageName() {
        return pageName;
    }

    protected Map<String, String> getParameters() {
        return parameters;
    }

    protected Messages getError() {
        return errors;
    }

    protected void setValues() {
        errors = (new RequestParamSetter(parameters).setValues(this));
    }

}
