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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.Map.Entry;

import com.bloatit.common.Log;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.utils.i18n.Localizator.LanguageDescriptor;
import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.xcgiserver.HttpHeader;

/**
 * Represent a web Url. A Url is a kind of {@link UrlComponent}, with a page
 * name. It also can have a ahchor part.
 */
public abstract class Url implements Cloneable {

    private String anchor = null;
    private static final String UTF_8 = "UTF-8";

    protected static void parseGetParameters(final Parameters params, final String getParameters) {
        // Extract get params
        for (final String param : getParameters.split("&")) {
            final String[] pair = param.split("=");
            if (pair.length >= 2) {
                try {
                    params.add(URLDecoder.decode(pair[0], UTF_8), URLDecoder.decode(pair[1], UTF_8));
                } catch (final UnsupportedEncodingException e) {
                    throw new ExternalErrorException(e);
                }
            }
        }
    }

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
        if (getProtocol() == Protocol.AUTO) {
            return internalUrlString(false);
        }
        return externalUrlString();
    }

    private String internalUrlString(boolean multilanguage) {
        return internalUrlString(multilanguage, null);
    }
    
    private String internalUrlString(boolean multilanguage, Locale forcedLanguage) {
        final StringBuilder sb = new StringBuilder();
        if (Context.getSession() != null && !multilanguage) {
            Context.getLocalizator();
            boolean found = false;
            String langCode;
            if(forcedLanguage == null) {
                langCode = Context.getLocalizator().getCode();
            } else {
                langCode = forcedLanguage.getLanguage();
            }
                
            for (Entry<String, LanguageDescriptor> lang : Localizator.getAvailableLanguages().entrySet()) {
                if (lang.getValue().getCode().equals(langCode)) {
                    found = true;
                }
            }
            if (found) {
                sb.append('/').append(langCode);
            } else {
                sb.append("/en");
            }
        }
        sb.append('/').append(getCode());

        final StringBuilder params = new StringBuilder();
        doConstructUrl(params);
        if (params.length() > 0 && params.charAt(params.length() - 1) == '&') {
            params.deleteCharAt(params.length() - 1);
        }
        if (params.length() > 0) {
            sb.append("?");
            sb.append(params);
        }
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

    public final String externalUrlString(Locale forcedLanguage) {
        return externalUrlString(false, forcedLanguage);
    }
    
    public final String externalUrlString() {
        return externalUrlString(false, null);
    }

    public final String externalUrlString(boolean multilanguage) {
       return externalUrlString(multilanguage, null);
    }
    
    public final String externalUrlString(boolean multilanguage, Locale forcedLanguage) {
        if (Context.getHeader() != null) {
            final HttpHeader header = Context.getHeader();
            if (FrameworkConfiguration.isHttpsEnabled() && (getProtocol() == Protocol.HTTPS || (header.isHttps() && getProtocol() == Protocol.AUTO))) {
                return "https://" + header.getHttpHost() + internalUrlString(multilanguage, forcedLanguage);
            }

            if (!FrameworkConfiguration.isHttpsEnabled() || getProtocol() == Protocol.HTTP || (!header.isHttps() && getProtocol() == Protocol.AUTO)) {
                return "http://" + header.getHttpHost() + internalUrlString(multilanguage, forcedLanguage);
            }

            Log.framework().error("Cannot parse the server protocol: " + header.getServerProtocol());
            return "http://" + header.getHttpHost() + internalUrlString(multilanguage, forcedLanguage);
        }
        return "http://elveos.org/" + internalUrlString(multilanguage, forcedLanguage); // FIXME
                                                                        // :
                                                                        // Replace
        // http://elveos.org
        // by configuration
    }

    public final HtmlLink getHtmlLink(final HtmlNode data) {
        return new HtmlLink(urlString(), data);
    }

    public final HtmlLink getHtmlLink() {
        return new HtmlLink(urlString());
    }

    public final HtmlLink getHtmlLink(final String text) {
        return new HtmlLink(urlString(), new HtmlText(text));
    }

}
