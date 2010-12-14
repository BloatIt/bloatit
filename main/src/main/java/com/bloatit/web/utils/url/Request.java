package com.bloatit.web.utils.url;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.RequestParamSetter;
import com.bloatit.web.utils.annotations.RequestParamSetter.Messages;
import com.bloatit.web.utils.annotations.RequestParamSetter.ParamNotFoundException;

public class Request {

    private String pageName;
    private Map<String, String> parameters = new HashMap<String, String>();
    private Messages errors;

    public Request(final String pageName, final Map<String, String> parameters) {
        super();
        this.pageName = pageName;
        this.parameters = parameters;
        this.errors = null;
    }

    // TODO COMMENT ME !!
    protected Request(final String pageName) {
        super();
        this.pageName = pageName;
    }

    public String getPageName() {
        return pageName;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Messages getMessages() {
        return errors;
    }

    public void setValues(final Object demandPage) {
        errors = (new RequestParamSetter(parameters).setValues(demandPage));
    }

    public void clone(final Request other) {
        other.pageName = pageName;
        other.parameters = new HashMap<String, String>(parameters);
        other.errors = (new RequestParamSetter(parameters).setValues(other));
    }

}
