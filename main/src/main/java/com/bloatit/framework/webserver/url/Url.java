package com.bloatit.framework.webserver.url;

import com.bloatit.common.Log;
import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.meta.HtmlText;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.framework.xcgiserver.HttpHeader;

/**
 * Represent a web Url. A Url is a kind of {@link UrlComponent}, with a page
 * name. It also can have a ahchor part.
 */
public abstract class Url implements Cloneable {

    private final String name;
    private String anchor = null;

    /**
     * Create a Url using its name.
     */
    protected Url(final String name) {
        super();
        this.name = name;
    }

    protected Url(final Url other) {
        this.name = other.name;
        this.anchor = other.anchor;
    }

    public abstract boolean isAction();

    public abstract String getCode();

    protected abstract void doConstructUrl(StringBuilder sb);

    protected abstract void doGetStringParameters(Parameters parameters);

    public abstract void addParameter(String key, String value);

    public abstract Messages getMessages();

    @Override
    public abstract Url clone();

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(final String anchor) {
        this.anchor = anchor;
    }

    public String urlString() {
        final StringBuilder sb = new StringBuilder();
        if (Context.getSession() != null) {
            sb.append("/").append(Context.getLocalizator().getCode());
        }
        sb.append("/").append(name);
        doConstructUrl(sb);
        if (anchor != null) {
            sb.append("#").append(anchor);
        }
        return sb.toString();
    }

    public Parameters getStringParameters() {

        Parameters parameters = new Parameters();
        doGetStringParameters(parameters);
        return parameters;
    }


    public final String externalUrlString(final HttpHeader header) {
        if (header.getServerProtocol().startsWith("HTTPS")) {
            return "https://" + header.getHttpHost() + urlString();
        }
        if (header.getServerProtocol().startsWith("HTTP")) {
            return "http://" + header.getHttpHost() + urlString();
        }
        Log.framework().error("Cannot parse the server protocol: " + header.getServerProtocol());
        return "http://" + header.getHttpHost() + urlString();
    }

    public final HtmlLink getHtmlLink(final XmlNode data) {
        return new HtmlLink(urlString(), data);
    }

    public final HtmlLink getHtmlLink() {
        return new HtmlLink(urlString());
    }

    public final HtmlLink getHtmlLink(final String text) {
        return new HtmlLink(urlString(), new HtmlText(text));
    }


}
