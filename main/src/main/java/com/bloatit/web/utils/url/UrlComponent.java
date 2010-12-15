package com.bloatit.web.utils.url;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.RequestParamSetter.Messages;

public abstract class UrlComponent {
    protected Messages messages = new Messages();
    private List<Parameter> parameters = new ArrayList<Parameter>();
    private boolean isRegistered = false;

    protected abstract void doRegister();

    protected UrlComponent() {
        super();

    }

    protected final void register(Parameter param) {
        parameters.add(param);
    }

    private final void registerIfNotAlreadyDone() {
        if (!isRegistered) {
            doRegister();
            isRegistered = true;
        }
    }

    @Override
    public final String toString() {
        registerIfNotAlreadyDone();
        StringBuilder sb = new StringBuilder();
        constructUrl(sb);
        return sb.toString();
    }

    protected void constructUrl(StringBuilder sb) {
        for (Parameter param : parameters) {
            if (param.getValue() != null && param.getRole() != Role.POST) {
                sb.append("/").append(param.getName()).append("-").append(param.getStringValue());
            }
        }
    }

    public Messages getMessages() {
        return messages;
    }

    void parseParameters(Parameters params) {
        registerIfNotAlreadyDone();
        for (Parameter param : parameters) {
            String value = params.look(param.getName());
            if (value != null) {
                param.valueFromString(value);
            }
        }
    }

    public void addParameter(String name, String value) {
        registerIfNotAlreadyDone();
        for (Parameter param : parameters) {
            if (param.getName().equals(name)) {
                param.valueFromString(value);
                break;
            }
        }
    }
}
