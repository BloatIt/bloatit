package com.bloatit.web.linkable.bugs;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoBug.BugState;
import com.bloatit.framework.webprocessor.components.form.Displayable;

public enum BindedState implements Displayable {
    PENDING(BugState.PENDING, tr("PENDING")), DEVELOPING(BugState.DEVELOPING, tr("DEVELOPING")), RESOLVED(BugState.RESOLVED, tr("RESOLVED"));

    private final String label;
    private final BugState state;

    private BindedState(BugState state, String label) {
        this.state = state;
        this.label = label;
    }

    public static BindedState getBindedState(BugState state) {
        return Enum.valueOf(BindedState.class, state.name());
    }

    @Override
    public String getDisplayName() {
        return label;
    }

    public BugState getState() {
        return state;
    }

}
