package com.bloatit.web.linkable.members.tabs.dashboard;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlLeaf;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.features.FeaturesTools.FeatureContext;
import com.bloatit.web.linkable.softwares.SoftwaresTools;

/**
 * Renders a dashboard into Html
 */
public class DashboardRenderer extends HtmlLeaf {
    private final Dashboard dashboard;

    public DashboardRenderer(Dashboard dashboard) {
        this.dashboard = dashboard;

        HtmlDiv dash = new HtmlDiv("dashboard");
        add(dash);

        for (DashboardEntry entry : dashboard) {
            HtmlDiv entryDiv = new HtmlDiv("entry");
            dash.add(entryDiv);

            // Generate LEFT column of a dashboard entry
            HtmlDiv leftColumn = new HtmlDiv("left_column");
            entryDiv.add(leftColumn);

            HtmlDiv leftColumnBlock = new HtmlDiv("block");
            leftColumn.add(leftColumnBlock);

            HtmlDiv progress;
            HtmlElement title;
            HtmlDiv logo;
            try {
                logo = new SoftwaresTools.Logo(entry.getSoftware());
                progress = FeaturesTools.generateProgress(entry.getFeature(), FeatureContext.DASHBOARD);
                title = FeaturesTools.generateFeatureTitle(entry.getFeature());
            } catch (UnauthorizedOperationException e) {
                throw new ShallNotPassException("Cannot access feature in dashboard");
            }

            logo.setCssClass("float_left");
            leftColumnBlock.add(logo);

            HtmlDiv featureBox = new HtmlDiv("feature_box");
            leftColumnBlock.add(featureBox);
            featureBox.add(title);

            HtmlDiv progressBlock = new HtmlDiv("progress_box");
            featureBox.add(progressBlock);
            progressBlock.add(progress);

            HtmlDiv nextStep = new HtmlDiv("next_step");
            leftColumnBlock.add(nextStep);

            nextStep.add(new HtmlTitle(Context.tr("Next step"), 2));
            HtmlParagraph nextStepContent = new HtmlParagraph(entry.getNextStep());
            nextStepContent.setCssClass("content");
            nextStep.add(nextStepContent);

            // Generate RIGHT column of a dashboard entry
            HtmlDiv rightColumn = new HtmlDiv("right_column");
            entryDiv.add(rightColumn);

            for (DashboardStep step : entry.getSteps()) {
                HtmlDiv rgtColumnEntry = new HtmlDiv("right_entry");
                HtmlDiv featureState = new HtmlDiv("state");
                featureState.addText(step.getStep());
                rgtColumnEntry.add(featureState);

                HtmlDiv statusBox = new HtmlDiv("status_box");
                rgtColumnEntry.add(statusBox);
                HtmlDiv stateStatus = new HtmlDiv("state_status");
                statusBox.add(stateStatus);
                HtmlList itemsList = new HtmlList();
                stateStatus.add(itemsList);

                for (String str : step.getItems()) {
                    itemsList.add(str);
                }

                rightColumn.add(rgtColumnEntry);
            }
        }
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    @Override
    public boolean selfClosable() {
        return false;
    }
}
