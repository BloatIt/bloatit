package com.bloatit.web.linkable.bugs;


import com.bloatit.data.DaoBug.BugState;
import com.bloatit.framework.webprocessor.components.form.Displayable;
import com.bloatit.framework.webprocessor.context.Context;

public enum BindedState implements Displayable {
    PENDING(BugState.PENDING, tr("PENDING")), DEVELOPING(BugState.DEVELOPING, tr("DEVELOPING")), RESOLVED(BugState.RESOLVED, tr("RESOLVED"));

    private final String label;
    private final BugState state;

    private BindedState(final BugState state, final String label) {
        this.state = state;
        this.label = label;
    }

    protected static BindedState getBindedState(final BugState state) {
        return Enum.valueOf(BindedState.class, state.name());
    }

    @Override
    public String getDisplayName() {
        return Context.tr(label);
    }

    public BugState getState() {
        return state;
    }
    
    //Fake tr
    private static String tr(String fake) {
        return fake;
    }

}
