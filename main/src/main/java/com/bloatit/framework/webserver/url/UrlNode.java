package com.bloatit.framework.webserver.url;

import com.bloatit.common.Log;
import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.utils.SessionParameters;
import com.bloatit.framework.xcgiserver.HttpHeader;

public abstract class UrlNode implements Iterable<UrlNode>, Cloneable {

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

    public final String externalUrlString(HttpHeader header) {
        if (header.getServerProtocol().startsWith("HTTPS")) {
            return "https://" + header.getHttpHost() + urlString();
        }
        if (header.getServerProtocol().startsWith("HTTP")) {
            return "http://" + header.getHttpHost() + urlString();
        }
        Log.framework().error("Cannot parse the server protocol: " + header.getServerProtocol());
        return "http://" + header.getHttpHost() + urlString();
    }

    public abstract Messages getMessages();

    protected abstract void parseSessionParameters(final SessionParameters params);

    protected abstract void parseParameters(final Parameters params);

    /**
     * Begin with a '/' and no slash at the end.
     */
    protected abstract void constructUrl(final StringBuilder sb);

    @Deprecated
    public abstract void addParameter(String name, String value);
}