package test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import test.UrlReader.ParamNotFoundException;

import com.bloatit.common.FatalErrorException;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.Loaders;
import com.bloatit.web.utils.PageComponent;
import com.bloatit.web.utils.PageName;
import com.bloatit.web.utils.RequestParam;

public class UrlBuilder extends UrlManipulator {

    private final Map<String, Object> parameters = new HashMap<String, Object>();
    private final Class<? extends Page> pageClass;
    private final Session session;

    public UrlBuilder(Page page, Session session) {
        pageClass = page.getClass();
        this.session = session;
        fillParameters(page, pageClass);
    }

    public UrlBuilder(Class<? extends Page> pageClass, Session session) {
        super();
        this.pageClass = pageClass;
        this.session = session;
    }

    public void addParameter(String name, Object value) {
        parameters.put(name, value);
    }

    public void buildUrl() throws ParamNotFoundException {
        StringBuilder sb = new StringBuilder();

        // find language
        sb.append("/").append(session.getLanguage().getCode()).append("/");

        // find name
        if (pageClass.getAnnotation(PageName.class) != null) {
            sb.append(pageClass.getAnnotation(PageName.class).value());
        } else {
            sb.append(pageClass.getName().toLowerCase());
        }

        sb.append("/");

        buildUrl(sb, pageClass);
    }

    private void buildUrl(StringBuilder sb, Class<?> pageClass) throws ParamNotFoundException {
        for (Field f : pageClass.getDeclaredFields()) {
            RequestParam param = f.getAnnotation(RequestParam.class);
            if (param != null) {

                String name = param.name() == "" ? f.getName() : param.name();
                String strValue = null;
                Object value = parameters.get(name);
                if (value != null) {
                    strValue = Loaders.toStr(value);
                } else if (param.defaultValue() == "") {
                    throw new UrlReader.ParamNotFoundException("Parameter " + name + " needs a value.");
                }

                if (strValue != param.defaultValue()) {
                    sb.append(name).append("-").append(strValue).append("/");
                }

            } else if (f.getAnnotation(PageComponent.class) != null) {
                buildUrl(sb, f.getClass());
            }
        }
    }

    private void fillParameters(Object page, Class<? extends Object> pageClass) {
        for (Field f : pageClass.getDeclaredFields()) {
            RequestParam param = f.getAnnotation(RequestParam.class);
            try {
                if (param != null) {
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    addParameter(param.name() == "" ? f.getName() : param.name(), f.get(page));
                } else if (f.getAnnotation(PageComponent.class) != null) {
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    fillParameters(f.get(page), f.getDeclaringClass());
                }
            } catch (IllegalArgumentException e) {
                throw new FatalErrorException("Cannot parse the pageComponent", e);
            } catch (IllegalAccessException e) {
                throw new FatalErrorException("Cannot parse the pageComponent", e);
            }
        }
    }
    
}
