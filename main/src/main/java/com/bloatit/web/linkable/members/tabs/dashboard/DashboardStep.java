package com.bloatit.web.linkable.members.tabs.dashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardStep {
    public enum StepState {
        COMPLETE, ONGOING
    }

    private final String step;
    private final List<String> items;
    private final StepState stepState;

    public DashboardStep(String step, StepState stepState, String... items) {
        super();
        this.step = step;
        this.stepState = stepState;
        this.items = new ArrayList<String>();
        Collections.addAll(this.items, items);
    }

    public String getStep() {
        return step;
    }

    public StepState getStepState() {
        return stepState;
    }

    public List<String> getItems() {
        return items;
    }
}
