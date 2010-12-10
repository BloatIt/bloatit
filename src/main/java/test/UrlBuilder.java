package test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


import com.bloatit.common.FatalErrorException;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.Loaders;
import com.bloatit.web.utils.PageComponent;
import com.bloatit.web.utils.PageName;
import com.bloatit.web.utils.RequestParam;
import test.html.components.standard.HtmlLink;

public class UrlBuilder {

    private final Map<String, Object> parameters = new HashMap<String, Object>();
    private final Class<? extends Linkable> linkableClass;
    private final Session session;

    public UrlBuilder(Linkable linkable) {
        linkableClass = linkable.getClass();
        this.session = Context.getSession();
        fillParameters(linkable, linkableClass);
    }

    public UrlBuilder(Class<? extends Linkable> linkableClass) {
        super();
        this.session = Context.getSession();
        this.linkableClass = linkableClass;
    }

    public UrlBuilder addParameter(String name, Object value) {
        parameters.put(name, value);
        return this;
    }

    public String buildUrl() {
        StringBuilder sb = new StringBuilder();

        // find language
        sb.append("/").append(session.getLanguage().getCode()).append("/");

        // find name
        if (linkableClass.getAnnotation(PageName.class) != null) {
            sb.append(linkableClass.getAnnotation(PageName.class).value());
        } else {
            sb.append(linkableClass.getName().toLowerCase());
        }

        sb.append("/");

        buildUrl(sb, linkableClass);

        return sb.toString();
    }

    private void buildUrl(StringBuilder sb, Class<?> pageClass) {
        for (Field f : pageClass.getDeclaredFields()) {
            RequestParam param = f.getAnnotation(RequestParam.class);
            if (param != null) {

                String name = param.name().equals("") ? f.getName() : param.name();
                String strValue = null;
                Object value = parameters.get(name);
                if (value != null) {
                    strValue = Loaders.toStr(value);
                } else if (param.defaultValue().equals("")) {
                    throw new FatalErrorException("Parameter " + name + " needs a value.", null);
                } else {
                    strValue = param.defaultValue();
                }


                if ( !strValue.equals(param.defaultValue())) {
                    sb.append(name).append("-").append(strValue).append("/");
                }

            } else if (f.getAnnotation(PageComponent.class) != null) {
                buildUrl(sb, f.getClass());
            }
        }
    }

    public HtmlLink getHtmlLink(String text) {
        return new HtmlLink(buildUrl(), text);
    }

    private void fillParameters(Object linkable, Class<? extends Object> linkableClass) {
        for (Field f : linkableClass.getDeclaredFields()) {
            RequestParam param = f.getAnnotation(RequestParam.class);
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
            } catch (IllegalArgumentException e) {
                throw new FatalErrorException("Cannot parse the pageComponent", e);
            } catch (IllegalAccessException e) {
                throw new FatalErrorException("Cannot parse the pageComponent", e);
            }
        }
    }
    
}
