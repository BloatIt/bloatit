package com.bloatit.web.utils.url;

import java.util.Map;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.RequestParamSetter.Messages;

public class DemandUrl extends Url {
    public DemandUrl() {
        super("demand");
    }

    public DemandUrl(Map<String, String> params) {
        super("demand");
        parseParameterMap(params);
    }

    private com.bloatit.framework.Demand id;
    private java.lang.String title;

    public com.bloatit.framework.Demand getId() {
        return this.id;
    }

    public void setId(com.bloatit.framework.Demand arg0) {
        this.id = arg0;
    }

    public java.lang.String getTitle() {
        return this.title;
    }

    public void setTitle(java.lang.String arg0) {
        this.title = arg0;
    }

    @Override
    protected void doRegister(Messages messages) {
        register(new Parameter(messages, "id", id, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
        register(new Parameter(messages, "title", title, Role.PRETTY, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    }
}
