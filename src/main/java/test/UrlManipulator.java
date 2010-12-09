package test;

import java.util.Map;

import com.bloatit.web.server.Page;

public abstract class UrlManipulator {

    Class<? extends Page> theClass;
    Map<String, Object> parameters;

    protected UrlManipulator() {
        super();
    }

}
