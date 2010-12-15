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
    private final List<Parameter> parameters = new ArrayList<Parameter>();
    private final List<UrlComponent> components = new ArrayList<UrlComponent>();
    private boolean isRegistered = false;

    protected abstract void doRegister();

    @Override
    public abstract UrlComponent clone();

    protected UrlComponent() {
        super();

    }

    protected final void register(final Parameter param) {
        parameters.add(param);
    }

    protected void register(final UrlComponent component) {
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
        final StringBuilder sb = new StringBuilder();
        constructUrl(sb);
        return sb.toString();
    }

    protected void constructUrl(final StringBuilder sb) {
        for (final UrlComponent comp : components) {
            sb.append(comp.toString());
        }
        for (final Parameter param : parameters) {
            if (param.getValue() != null && param.getRole() != Role.POST) {
                sb.append("/").append(param.getName()).append("-").append(param.getStringValue());
            }
        }
    }

    public Messages getMessages() {
        final Messages messages = new Messages();
        for (final Parameter param : parameters) {
            final Message message = param.getMessage();
            if (message != null) {
                messages.add(message);
            }
        }
        for (final UrlComponent cmp : components) {
            messages.addAll(cmp.getMessages());
        }
        return messages;
    }

    protected final void parseParameters(final Parameters params) {
        registerIfNotAlreadyDone();
        for (final Parameter param : parameters) {
            final String value = params.look(param.getName());
            if (value != null) {
                param.valueFromString(value);
            }
        }
        for (final UrlComponent comp : components) {
            comp.parseParameters(params);
        }
    }

    public void addParameter(final String name, final String value) {
        registerIfNotAlreadyDone();
        for (final Parameter param : parameters) {
            if (param.getName().equals(name)) {
                param.valueFromString(value);
                break;
            }
        }
    }

    public HtmlNode getHtmlLink(final HtmlNode data) {
        return new HtmlLink(toString(), data);
    }

    public HtmlNode getHtmlLink(final String text) {
        return new HtmlLink(toString(), new HtmlText(text));
    }
}
