package com.bloatit.web.utils.url;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;



import com.bloatit.common.FatalErrorException;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Linkable;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.PageComponent;
import com.bloatit.web.utils.annotations.PageName;
import com.bloatit.web.utils.annotations.RequestParam;

public class UrlBuilder {

    private final Map<String, Object> parameters = new HashMap<String, Object>();
    private final Class<? extends Linkable> linkableClass;
    private final Session session;

    public UrlBuilder(final Linkable linkable) {
        linkableClass = linkable.getClass();
        this.session = Context.getSession();
        fillParameters(linkable, linkableClass);
    }

    public UrlBuilder(final Class<? extends Linkable> linkableClass) {
        super();
        this.session = Context.getSession();
        this.linkableClass = linkableClass;
    }

    public UrlBuilder addParameter(final String name, final Object value) {
        parameters.put(name, value);
        return this;
    }

    public String buildUrl() {
        final StringBuilder sb = new StringBuilder();

        // find language
        sb.append("/").append(session.getLanguage().getCode()).append("/");

        // find name
        if (linkableClass.getAnnotation(PageName.class) != null) {
            sb.append(linkableClass.getAnnotation(PageName.class).value());
        } else {
            sb.append(linkableClass.getSimpleName().toLowerCase());
        }

        buildUrl(sb, linkableClass);

        return sb.toString();
    }

    private void buildUrl(final StringBuilder sb, final Class<?> pageClass) {
        for (final Field f : pageClass.getDeclaredFields()) {
            final RequestParam param = f.getAnnotation(RequestParam.class);
            if (param != null && param.role() != RequestParam.Role.POST) {

                final String name = param.name().equals("") ? f.getName() : param.name();
                String strValue = null;
                final Object value = parameters.get(name);
                if (value != null) {
                    strValue = Loaders.toStr(value);
                } else if (param.defaultValue().equals("")) {
                    throw new FatalErrorException("Parameter " + name + " needs a value.", null);
                } else {
                    strValue = param.defaultValue();
                }

                if (!strValue.equals(param.defaultValue())) {
                    sb.append("/").append(name).append("-").append(strValue);
                }

            } else if (f.getAnnotation(PageComponent.class) != null) {
                buildUrl(sb, f.getClass());
            }
        }
    }

    public HtmlLink getHtmlLink(final String text) {
        return new HtmlLink(buildUrl(), text);
    }

    private void fillParameters(final Object linkable, final Class<? extends Object> linkableClass) {
        for (final Field f : linkableClass.getDeclaredFields()) {
            final RequestParam param = f.getAnnotation(RequestParam.class);
            try {
                if (param != null) {
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    addParameter(param.name().equals("") ? f.getName() : param.name(), f.get(linkable));
                } else if (f.getAnnotation(PageComponent.class) != null) {
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    fillParameters(f.get(linkable), f.getDeclaringClass());
                }
            } catch (final IllegalArgumentException e) {
                throw new FatalErrorException("Cannot parse the pageComponent", e);
            } catch (final IllegalAccessException e) {
                throw new FatalErrorException("Cannot parse the pageComponent", e);
            }
        }
    }

}
