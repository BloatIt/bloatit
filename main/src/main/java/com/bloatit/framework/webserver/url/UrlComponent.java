package com.bloatit.framework.webserver.url;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.meta.HtmlNode;
import com.bloatit.framework.webserver.components.meta.HtmlText;

public abstract class UrlComponent extends UrlNode {
    private boolean isRegistered = false;
    private final List<UrlNode> nodes = new ArrayList<UrlNode>();

    protected abstract void doRegister();

    @Override
    public abstract UrlComponent clone();

    protected UrlComponent() {
        super();
    }

    @Override
    protected void constructUrl(final StringBuilder sb) {
        registerIfNotAlreadyDone();
        for (final UrlNode node : this) {
            node.constructUrl(sb);
        }
    }

    protected final void register(final UrlNode node) {
        nodes.add(node);
    }

    private final void registerIfNotAlreadyDone() {
        if (!isRegistered) {
            doRegister();
            isRegistered = true;
        }
    }

    @Override
    protected final void parseParameters(final Parameters params, final boolean pickValue) {
        if (params == null) {
            return;
        }
        registerIfNotAlreadyDone();
        for (final UrlNode node : nodes) {
            node.parseParameters(params, pickValue);
        }
    }

    @Override
    @Deprecated
    public void addParameter(final String name, final String value) {
        registerIfNotAlreadyDone();
        for (final UrlNode node : nodes) {
            node.addParameter(name, value);
        }
    }

    public final HtmlLink getHtmlLink(final HtmlNode data) {
        return new HtmlLink(urlString(), data);
    }

    public final HtmlLink getHtmlLink(final String text) {
        return new HtmlLink(urlString(), new HtmlText(text));
    }

    @Override
    public final Iterator<UrlNode> iterator() {
        registerIfNotAlreadyDone();
        return nodes.iterator();
    }

    @Override
    public final Messages getMessages() {
        registerIfNotAlreadyDone();
        final Messages messages = new Messages();
        for (final UrlNode node : nodes) {
            messages.addAll(node.getMessages());
        }
        return messages;
    }
}
