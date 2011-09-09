package com.bloatit.web.linkable.members.tabs.dashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.bloatit.model.Feature;
import com.bloatit.model.Software;

public class DashboardEntry {
    private final String nextStep;
    private final String title;
    private final Date updateDate;
    private final ArrayList<DashboardStep> steps;
    private final Feature feature;

    public DashboardEntry(Feature feature, String nextStep, String title, Date updateDate) {
        super();
        this.feature = feature;
        this.nextStep = nextStep;
        this.title = title;
        this.updateDate = updateDate;
        this.steps = new ArrayList<DashboardEntry.DashboardStep>(4);
    }

    public void addStep(DashboardStep step) {
        steps.add(step);
    }

    public String getNextStep() {
        return nextStep;
    }

    public String getTitle() {
        return title;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public ArrayList<DashboardStep> getSteps() {
        return steps;
    }
    
    public Software getSoftware() {
        return feature.getSoftware();
    }

    public Feature getFeature() {
        return feature;
    }

    protected static class DashboardStep {
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
}
