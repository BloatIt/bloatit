//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework.webprocessor.url;

import com.bloatit.common.Log;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.xcgiserver.HttpHeader;

/**
 * Represent a web Url. A Url is a kind of {@link UrlComponent}, with a page
 * name. It also can have a ahchor part.
 */
public abstract class Url implements Cloneable {

    private String anchor = null;

    protected Url() {
        super();
    }

    protected Url(final Url other) {
        this.anchor = other.anchor;
    }

    public boolean hasError() {
        return !getMessages().isEmpty();
    }

    public abstract boolean isAction();

    public abstract String getCode();

    protected abstract void doConstructUrl(StringBuilder sb);

    protected abstract void doGetParametersAsStrings(Parameters parameters);

    public abstract void addParameter(String key, String value);

    public abstract Messages getMessages();

    public abstract Protocol getProtocol();

    @Override
    public abstract Url clone();

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(final String anchor) {
        this.anchor = anchor;
    }

    public String urlString() {
        if(getProtocol() == Protocol.AUTO) {
            return internalUrlString();
        }
        return externalUrlString();
    }

    private String internalUrlString() {
        final StringBuilder sb = new StringBuilder();
        if (Context.getSession() != null) {
            sb.append('/').append(Context.getLocalizator().getCode());
        }
        sb.append('/').append(getCode());
        doConstructUrl(sb);
        if (anchor != null) {
            sb.append('#').append(anchor);
        }
        return sb.toString();
    }

    public Parameters getStringParameters() {

        final Parameters parameters = new Parameters();
        doGetParametersAsStrings(parameters);
        return parameters;
    }

    public final String externalUrlString() {
        final HttpHeader header = Context.getHeader().getHttpHeader();


        if (FrameworkConfiguration.isHttpsEnabled() && (getProtocol() == Protocol.HTTPS || (header.getServerProtocol().startsWith("HTTPS") && getProtocol() == Protocol.AUTO))) {
            return "https://" + header.getHttpHost() + internalUrlString();
        }

        if (!FrameworkConfiguration.isHttpsEnabled() || getProtocol() == Protocol.HTTP || (header.getServerProtocol().startsWith("HTTP") && getProtocol() == Protocol.AUTO)) {
            return "http://" + header.getHttpHost() + internalUrlString();
        }

        Log.framework().error("Cannot parse the server protocol: " + header.getServerProtocol());
        return "http://" + header.getHttpHost() + internalUrlString();
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
