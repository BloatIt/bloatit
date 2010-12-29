package com.bloatit.web.utils.url;

import com.bloatit.web.utils.annotations.Messages;

public abstract class UrlNode implements Iterable<UrlNode> , Cloneable{

    public UrlNode() {
        super();
    }

    @Override
    public abstract UrlNode clone() throws CloneNotSupportedException;

    public final String urlString() {
        final StringBuilder sb = new StringBuilder();
        constructUrl(sb);
        return sb.toString();
    }

    public abstract Messages getMessages();

    protected abstract void parseParameters(final Parameters params, boolean pickValue);

    /**
     * Begin with a '/' and no slash at the end.
     */
    protected abstract void constructUrl(final StringBuilder sb);

    @Deprecated
    public abstract void addParameter(String name, String value);
}