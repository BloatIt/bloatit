package com.bloatit.web.utils.url;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.web.annotations.Message;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.utils.annotations.RequestParamSetter.Messages;

public abstract class UrlComponent {
    protected Messages messages = new Messages();
    private List<Parameter> parameters = new ArrayList<Parameter>();
    private List<UrlComponent> components = new ArrayList<UrlComponent>();
    private boolean isRegistered = false;

    protected abstract void doRegister();
    
    public abstract UrlComponent clone();

    protected UrlComponent() {
        super();

    }

    protected final void register(Parameter param) {
        parameters.add(param);
    }
    
    protected void register(UrlComponent component) {
        components.add(component);
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
        for (UrlComponent comp : components) {
            sb.append(comp.toString());
        }
        for (Parameter param : parameters) {
            if (param.getValue() != null && param.getRole() != Role.POST) {
                sb.append("/").append(param.getName()).append("-").append(param.getStringValue());
            }
        }
    }

    public Messages getMessages() {
        Messages messages = new Messages();
        for (Parameter param : parameters) {
            Message message = param.getMessage();
            if (message != null) {
                messages.add(message);
            }
        }
        for (UrlComponent cmp : components) {
            messages.addAll(cmp.getMessages());
        }
        return messages;
    }

    protected final void parseParameters(Parameters params) {
        registerIfNotAlreadyDone();
        for (Parameter param : parameters) {
            String value = params.look(param.getName());
            if (value != null) {
                param.valueFromString(value);
            }
        }
        for (UrlComponent comp : components) {
            comp.parseParameters(params);
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
    
    public HtmlNode getHtmlLink(HtmlNode data) {
        return new HtmlLink(this.toString(), data);
    }

    public HtmlNode getHtmlLink(String text) {
        return new HtmlLink(this.toString(), new HtmlText(text));
    }
}
