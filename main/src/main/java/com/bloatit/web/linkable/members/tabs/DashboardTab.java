package com.bloatit.web.linkable.members.tabs;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock.HtmlTab;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.Feature;
import com.bloatit.model.Follow;
import com.bloatit.model.lists.FollowList;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.visitor.AbstractModelClassVisitor;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.features.FeaturesTools.FeatureContext;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.url.MemberPageUrl;

@ParamContainer(value = "dashboardTab", isComponent = true)
public class DashboardTab extends HtmlTab {
    private final Actor<?> actor;
    private final MemberPageUrl url;

    public DashboardTab(final Actor<?> actor, final String title, final String tabKey, final MemberPageUrl url) {
        super(title, tabKey);
        this.actor = actor;
        this.url = url;
    }

    @Override
    public XmlNode generateBody() {
        final HtmlDiv master = new HtmlDiv("tab_pane");

        // Displaying list of user recent activity
        final HtmlTitleBlock followed = new HtmlTitleBlock(Context.tr("Content you follow"), 1);
        master.add(followed);

        FollowList followeds = actor.getFollowedContent();

        HtmlDiv dash = new HtmlDiv("dashboard");
        followed.add(dash);

        for (Follow follow : followeds) {
            Feature f = follow.getFollowed();

            HtmlDiv entry = new HtmlDiv("entry");
            dash.add(entry);

            // Generate LEFT column of a dashboard entry
            HtmlDiv leftColumn = new HtmlDiv("left_column");
            entry.add(leftColumn);

            HtmlDiv leftColumnBlock = new HtmlDiv("block");
            leftColumn.add(leftColumnBlock);

            HtmlDiv progress;
            HtmlElement title;
            HtmlDiv logo;
            try {
                logo = new SoftwaresTools.Logo(f.getSoftware());
                progress = FeaturesTools.generateProgress(f, FeatureContext.OTHER);
                title = FeaturesTools.generateFeatureTitle(f);
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
            HtmlParagraph nextStepContent = new HtmlParagraph("Something TODO here !!");
            nextStepContent.setCssClass("content");
            nextStep.add(nextStepContent);

            // Generate RIGHT column of a dashboard entry
            HtmlDiv rightColumn = new HtmlDiv("right_column");
            entry.add(rightColumn);

            HtmlDiv rgtColumnEntry = new HtmlDiv("right_entry");
            HtmlDiv featureState = new HtmlDiv("state");
            featureState.addText("Offer");
            rgtColumnEntry.add(featureState);

            HtmlDiv statusBox = new HtmlDiv("status_box");
            HtmlDiv stateStatus = new HtmlDiv("state_status");
            rgtColumnEntry.add(statusBox);
            statusBox.add(stateStatus);
            stateStatus.addText("Do something");

            rightColumn.add(rgtColumnEntry);
            rightColumn.add(rgtColumnEntry);
            rightColumn.add(rgtColumnEntry);
        }

        return master;
    }
}
