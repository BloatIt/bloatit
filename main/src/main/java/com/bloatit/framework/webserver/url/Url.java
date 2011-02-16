package com.bloatit.framework.webserver.url;

import java.util.Iterator;

import com.bloatit.common.Log;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.meta.HtmlNode;
import com.bloatit.framework.webserver.components.meta.HtmlText;
import com.bloatit.framework.xcgiserver.HttpHeader;

/**
 * Represent a web Url. A Url is a kind of {@link UrlComponent}, with a page
 * name. It also can have a ahchor part.
 */
public abstract class Url {

    private final String name;
    private UrlComponent component;
    private String anchor = null;

    /**
     * Create a Url using its name.
     */
    protected Url(final String name, UrlComponent component) {
        super();
        this.name = name;
        this.setComponent(component);
    }

    // public abstract Linkable createPage();

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(final String anchor) {
        this.anchor = anchor;
    }

    public String urlString() {
        StringBuilder sb = new StringBuilder();
        if (Context.getSession() != null) {
            sb.append("/").append(Context.getLocalizator().getCode());
        }
        sb.append("/").append(name);
        getComponent().constructUrl(sb);
        if (anchor != null) {
            sb.append("#").append(anchor);
        }
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

    @Override
    public abstract Url clone();

    public final HtmlLink getHtmlLink(final HtmlNode data) {
        return new HtmlLink(urlString(), data);
    }

    public final HtmlLink getHtmlLink(final String text) {
        return new HtmlLink(urlString(), new HtmlText(text));
    }

    public final Iterator<UrlNode> iterator() {
        return getComponent().iterator();
    }

    public final Messages getMessages() {
        return getComponent().getMessages();
    }

    public UrlComponent getComponent() {
        return component;
    }

    protected void setComponent(UrlComponent component) {
        this.component = component;
    }

    public void addParameter(String key, String value) {
        this.component.addParameter(key, value);
    }
}
