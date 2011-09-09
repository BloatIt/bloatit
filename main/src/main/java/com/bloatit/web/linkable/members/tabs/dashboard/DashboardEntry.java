package com.bloatit.web.linkable.members.tabs.dashboard;

import java.util.ArrayList;
import java.util.Date;

import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.model.Feature;
import com.bloatit.model.Software;

public class DashboardEntry {
    private final HtmlElement nextStep;
    private final String title;
    private final Date updateDate;
    private final ArrayList<DashboardStep> steps;
    private final Feature feature;

    public DashboardEntry(Feature feature, HtmlElement nextStep, String title, Date updateDate) {
        super();
        this.feature = feature;
        this.nextStep = nextStep;
        this.title = title;
        this.updateDate = updateDate;
        this.steps = new ArrayList<DashboardStep>(4);
    }

    public void addStep(DashboardStep step) {
        steps.add(step);
    }

    public HtmlElement getNextStep() {
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
}
