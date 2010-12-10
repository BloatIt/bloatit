package test;

import java.util.HashMap;

public class Parameters extends HashMap<String, String> {
    private static final long serialVersionUID = 1L;

    public Parameters() {
        super();
    }

    public Parameters(final String name, final String value) {
        super();
        super.put(name, value);
    }

    public Parameters add(final String name, final String value) {
        super.put(name, value);
        return this;
    }

}
