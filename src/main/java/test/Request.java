package test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import test.pages.demand.DemandPage;

import com.bloatit.web.utils.RequestParam;
import com.bloatit.web.utils.RequestParam.Role;
import com.bloatit.web.utils.RequestParamSetter;
import com.bloatit.web.utils.RequestParamSetter.Messages;
import com.bloatit.web.utils.RequestParamSetter.ParamNotFoundException;

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

    // TODO COMMENT ME !!
    protected Request(String pageName) {
        super();
        this.pageName = pageName;
    }

    public String createUrl(Map<String, String> parameters) {

        String url = getPageName() + "/";

        Class<?> aClass = getClass();
        for (Field f : aClass.getDeclaredFields()) {
            RequestParam param = f.getAnnotation(RequestParam.class);
            // TODO make pretty the pretty params.
            if (param != null && param.role() != Role.POST) {
                try {
                    RequestParamSetter.FieldParser fieldParser = new RequestParamSetter.FieldParser(f, param, getParameters());
                    if (parameters.containsKey(fieldParser.getName())) {
                        url += fieldParser.getName() + "-" + parameters.get(fieldParser.getName());
                    } else {
                        url += fieldParser.getName() + "-" + fieldParser.getvalue();
                    }
                } catch (ParamNotFoundException e) {
                    // TODO handle error !
                    e.printStackTrace();
                }
            }
        }
        return url;
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

    public void setValues(Object demandPage) {
        errors = (new RequestParamSetter(parameters).setValues(demandPage));
    }

    public void clone(Request other) {
        other.pageName = pageName;
        other.parameters = new HashMap<String, String>(parameters);
        other.errors = (new RequestParamSetter(parameters).setValues(other));
    }

}
