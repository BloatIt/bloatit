package com.bloatit.framework.webserver.url;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.utils.SessionParameters;

public abstract class UrlComponent extends UrlNode {
    private static final EmptyComponent EMPTY_COMPONENT = new EmptyComponent();
    private boolean isRegistered = false;
    private final List<UrlNode> nodes = new ArrayList<UrlNode>();

    protected abstract void doRegister();

    @Override
    public abstract UrlComponent clone();

    protected UrlComponent() {
        super();
    }

    @Override
    public final void constructUrl(final StringBuilder sb) {
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
    protected final void parseParameters(final Parameters params) {
        if (params == null) {
            return;
        }
        registerIfNotAlreadyDone();
        for (final UrlNode node : nodes) {
            node.parseParameters(params);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bloatit.framework.webserver.url.UrlNode#parseSessionParameters(com
     * .bloatit.framework.utils.SessionParameters)
     */
    @Override
    protected void parseSessionParameters(SessionParameters params) {
        if (params == null) {
            return;
        }
        registerIfNotAlreadyDone();
        for (final UrlNode node : nodes) {
            node.parseSessionParameters(params);
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

    public static UrlComponent getEmptyComponent() {
        return EMPTY_COMPONENT;
    }

    private static class EmptyComponent extends UrlComponent {

        public EmptyComponent() {
            // do nothing. the component is empty
        }

        @Override
        protected void doRegister() {
            // do nothing. the component is empty
        }

        @Override
        public UrlComponent clone() {
            // every empty component is the same. So return this
            return this;
        }
    }

}
