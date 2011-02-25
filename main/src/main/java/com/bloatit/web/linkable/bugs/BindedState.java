package com.bloatit.web.linkable.bugs;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoBug.State;
import com.bloatit.framework.webserver.components.form.Displayable;

public enum BindedState implements Displayable {
    PENDING(State.PENDING, tr("PENDING")), DEVELOPING(State.DEVELOPING, tr("DEVELOPING")), RESOLVED(State.RESOLVED, tr("RESOLVED"));

    private final String label;
    private final State state;

    private BindedState(State state, String label) {
        this.state = state;
        this.label = label;
    }

    public static BindedState getBindedState(State state) {
        return Enum.valueOf(BindedState.class, state.name());
    }

    @Override
    public String getDisplayName() {
        return label;
    }

    public State getState() {
        return state;
    }

}
