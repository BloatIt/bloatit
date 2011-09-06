package com.bloatit.framework.xcgiserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.bloatit.framework.utils.parameters.HttpParameter;
import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.webprocessor.context.Context;

public class HttpBloatitRequest implements HttpServletRequest {

    private static final class EnumerationImplementation<T> implements Enumeration<String> {

        private final Iterator<Entry<String, T>> it;

        private EnumerationImplementation(final Set<Entry<String, T>> values) {
            super();
            this.it = values.iterator();
        }

        @Override
        public boolean hasMoreElements() {
            return it.hasNext();
        }

        @Override
        public String nextElement() {
            return it.next().getKey();
        }
    }

    private final Map<String, Object> attributes = new HashMap<String, Object>();
    private String characterEncoding;
    private final HttpHeader header;
    private final Parameters parameter;

    public HttpBloatitRequest(final HttpHeader header, final Parameters parameter) {
        this.header = header;
        this.parameter = parameter;
        characterEncoding = "UTF-8";
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public AsyncContext getAsyncContext() {
        throw new IllegalAccessError();
    }

    @Override
    public Object getAttribute(final String arg0) {
        return attributes.get(arg0);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        final Enumeration<String> enumeration = new EnumerationImplementation<Object>(attributes.entrySet());
        return enumeration;
    }

    @Override
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }

    @Override
    public int getContentLength() {
        return header.getContentLength();
    }

    @Override
    public String getContentType() {
        return header.getContentType();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public DispatcherType getDispatcherType() {
        throw new IllegalAccessError();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        throw new IllegalAccessError();
    }

    @Override
    public String getLocalAddr() {
        return header.getServerAddr();
    }

    @Override
    public String getLocalName() {
        return header.getServerName();
    }

    @Override
    public int getLocalPort() {
        return header.getServerPort();
    }

    @Override
    public Locale getLocale() {
        return Context.getLocalizator().getLocale();
    }

    @Override
    public Enumeration<Locale> getLocales() {
        // TODO make me as soon as possible !
        return null;
    }

    @Override
    public String getParameter(final String arg0) {
        final HttpParameter look = parameter.look(arg0);
        if (look != null) {
            return look.getSimpleValue();
        }
        return null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        final Map<String, String[]> ret = new HashMap<String, String[]>();
        for (final Entry<String, HttpParameter> param : parameter.entrySet()) {
            final String[] values = new String[param.getValue().size()];
            int i = 0;
            for (final String value : param.getValue()) {
                values[i++] = value;
            }
            ret.put(param.getKey(), values);
        }
        return ret;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new EnumerationImplementation<HttpParameter>(parameter.entrySet());
    }

    @Override
    public String[] getParameterValues(final String arg0) {
        final List<String> values = new ArrayList<String>();
        for (final Entry<String, HttpParameter> param : parameter.entrySet()) {
            for (final String value : param.getValue()) {
                values.add(value);
            }
        }
        return values.toArray(new String[values.size()]);
    }

    @Override
    public String getProtocol() {
        return header.getServerProtocol();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public BufferedReader getReader() throws IOException {
        throw new IllegalAccessError();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public String getRealPath(final String arg0) {
        throw new IllegalAccessError();
    }

    @Override
    public String getRemoteAddr() {
        return header.getRemoteAddr();
    }

    @Override
    public String getRemoteHost() {
        // TODO correct me
        return header.getRemoteAddr();
    }

    @Override
    public int getRemotePort() {
        return header.getRemotePort();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public RequestDispatcher getRequestDispatcher(final String arg0) {
        throw new IllegalAccessError();
    }

    @Override
    public String getScheme() {
        if (header.isHttps() && getProtocol().startsWith("HTTP")) {
            return "https";
        }
        if (!header.isHttps() && getProtocol().startsWith("HTTP")) {
            return "http";
        }
        if (getProtocol().startsWith("FTPS")) {
            return "ftps";
        }
        if (getProtocol().startsWith("FTP")) {
            return "ftp";
        }
        return null;
    }

    @Override
    public String getServerName() {
        return header.getServerName();
    }

    @Override
    public int getServerPort() {
        return header.getServerPort();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public ServletContext getServletContext() {
        throw new IllegalAccessError();
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public void removeAttribute(final String arg0) {
        attributes.remove(arg0);
    }

    @Override
    public void setAttribute(final String arg0, final Object arg1) {
        this.attributes.put(arg0, arg1);
    }

    @Override
    public void setCharacterEncoding(final String arg0) throws UnsupportedEncodingException {
        characterEncoding = arg0;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        throw new IllegalStateException();
    }

    @Override
    public AsyncContext startAsync(final ServletRequest arg0, final ServletResponse arg1) throws IllegalStateException {
        throw new IllegalStateException();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public boolean authenticate(final HttpServletResponse arg0) throws IOException, ServletException {
        throw new IllegalAccessError();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public String getAuthType() {
        throw new IllegalAccessError();
    }

    @Override
    public String getContextPath() {
        return header.getPathInfo();
    }

    @Override
    public Cookie[] getCookies() {
        final Cookie[] cookies = new Cookie[header.getHttpCookie().size()];
        int i = 0;
        for (final Entry<String, String> cook : header.getHttpCookie().entrySet()) {
            cookies[i++] = new Cookie(cook.getKey(), cook.getValue());
        }
        return cookies;
    }

    // TODO maybe there is a way to implement Header based getter
    @Override
    public long getDateHeader(final String arg0) {
        return -1;
    }

    @Override
    public String getHeader(final String arg0) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return false;
            }

            @Override
            public String nextElement() {
                return null;
            }
        };
    }

    @Override
    public Enumeration<String> getHeaders(final String arg0) {
        return new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return false;
            }

            @Override
            public String nextElement() {
                return null;
            }
        };
    }

    @Override
    public int getIntHeader(final String arg0) {
        return -1;
    }

    @Override
    public String getMethod() {
        return header.getRequestMethod();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public Part getPart(final String arg0) throws IOException, ServletException {
        throw new IllegalAccessError();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        throw new IllegalAccessError();
    }

    @Override
    public String getPathInfo() {
        return header.getPathInfo();
    }

    @Override
    public String getPathTranslated() {
        return header.getPathTranslated();
    }

    @Override
    public String getQueryString() {
        return header.getQueryString();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public String getRemoteUser() {
        throw new IllegalAccessError();
    }

    @Override
    public String getRequestURI() {
        return header.getRequestUri();
    }

    @Override
    public StringBuffer getRequestURL() {
        final StringBuffer sb = new StringBuffer();
        sb.append(getScheme()).append("://").append(getServerName()).append(header.getScriptName());
        return sb;
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public String getRequestedSessionId() {
        throw new IllegalAccessError();
    }

    @Override
    public String getServletPath() {
        return header.getScriptName();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public HttpSession getSession() {
        throw new IllegalAccessError();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public HttpSession getSession(final boolean arg0) {
        throw new IllegalAccessError();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public Principal getUserPrincipal() {
        throw new IllegalAccessError();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw new IllegalAccessError();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new IllegalAccessError();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new IllegalAccessError();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public boolean isRequestedSessionIdValid() {
        throw new IllegalAccessError();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public boolean isUserInRole(final String arg0) {
        throw new IllegalAccessError();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public void login(final String arg0, final String arg1) throws ServletException {
        throw new IllegalAccessError();
    }

    /**
     * Meaningless method in the bloatit framework. Throw a
     * {@link IllegalAccessError}.
     */
    @Override
    public void logout() throws ServletException {
        throw new IllegalAccessError();
    }

}
