package test;

import java.util.Map.Entry;

import com.bloatit.web.server.Session;
import com.bloatit.web.utils.PageName;

public class Url {
    private final Parameters parameters;
    private final Class<? extends Linkable> linkable;
    private final Session session;

    public static String getPageName(final Class<? extends Linkable> linkable) {
        if (linkable.getAnnotation(PageName.class) != null) {
            return linkable.getAnnotation(PageName.class).value();
        } else {
            return linkable.getName().toLowerCase();
        }
    }

    public Url(final Class<? extends Linkable> linkable, final Parameters parameters) {
        super();
        this.parameters = parameters;
        this.linkable = linkable;
        this.session = Context.getSession();
    }

    protected Url() {
        super();
        this.parameters = new Parameters();
        this.linkable = null;
        this.session = null;
    }

    public boolean isValid() {
        return session != null && linkable != null;
    }

    public String getPageName() {
        // find name
        if (linkable.getAnnotation(PageName.class) != null) {
            return linkable.getAnnotation(PageName.class).value();
        } else {
            return linkable.getName().toLowerCase();
        }

    }

    public Url addParameter(final String name, final String value) {
        parameters.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(session.getLanguage().getCode()).append("/").append(getPageName()).append("/");
        for (final Entry<String, String> param : parameters.entrySet()) {
            sb.append(param.getKey());
            sb.append("-");
            sb.append(param.getValue());
            sb.append("/");
        }
        // TODO testme
        return sb.toString();
    }

    public Parameters getParameters() {
        return parameters;
    }

}
