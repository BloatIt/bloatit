package com.bloatit.web.utils.url;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.utils.annotations.Messages;

public abstract class UrlComponent extends UrlNode {
    private boolean isRegistered = false;
    private List<UrlNode> nodes = new ArrayList<UrlNode>();

    protected abstract void doRegister();

    @Override
    public abstract UrlComponent clone();

    protected UrlComponent() {
        super();
    }

    protected void constructUrl(final StringBuilder sb) {
        registerIfNotAlreadyDone();
        for (UrlNode node : this) {
            node.constructUrl(sb);
        }
    }

    protected final void register(final UrlNode node) {
        nodes.add(node);
    }

    final void registerIfNotAlreadyDone() {
        if (!isRegistered) {
            doRegister();
            isRegistered = true;
        }
    }

    @Override
    protected final void parseParameters(final Parameters params, boolean pickValue) {
        if (params == null) {
            return;
        }
        registerIfNotAlreadyDone();
        for (final UrlNode node : nodes) {
            node.parseParameters(params, pickValue);
        }
    }

    @Deprecated
    public void addParameter(final String name, final String value) {
        registerIfNotAlreadyDone();
        for (final UrlNode node : nodes) {
            node.addParameter(name, value);
        }
    }

    public HtmlLink getHtmlLink(final HtmlNode data) {
        return new HtmlLink(urlString(), data);
    }

    public HtmlLink getHtmlLink(final String text) {
        return new HtmlLink(urlString(), new HtmlText(text));
    }

    @Override
    public Iterator<UrlNode> iterator() {
        registerIfNotAlreadyDone();
        return nodes.iterator();
    }

    @Override
    public Messages getMessages() {
        registerIfNotAlreadyDone();
        Messages messages = new Messages();
        for (UrlNode node : nodes) {
            messages.addAll(node.getMessages());
        }
        return messages;
    }
}
