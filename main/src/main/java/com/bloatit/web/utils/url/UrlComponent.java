package com.bloatit.web.utils.url;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.RequestParamSetter.Messages;

public abstract class UrlComponent {
    protected Messages messages = new Messages();
    private List<Parameter> parameters = new ArrayList<Parameter>();

    protected abstract void doRegister(Messages messages);
    
    protected final void register(Parameter param) {
        parameters.add(param);
    }
    
    @Override
    public String toString() {
        doRegister(messages);
        StringBuilder sb = new StringBuilder();
        for (Parameter param : parameters) {
            if (param.getValue() != null && param.getRole() != Role.POST) {
                sb.append("/").append(param.getName()).append("-").append(param.getStringValue());
            }
        }
        return sb.toString();
    }

    public Messages getMessages() {
        return messages;
    }
    
    protected Messages parseParameterMap(Map<String, String> params) {
        doRegister(messages);
        for (Parameter param : parameters) {
            if (params.containsKey(param.getName())) {
                param.valueFromString(params.get(param.getName()));
            }
        }
        return messages;
    }
}
