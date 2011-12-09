//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.activity;

import java.util.EnumSet;

import com.bloatit.data.DaoMember.EmailStrategy;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlRenderer;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.form.Displayable;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.Feature;
import com.bloatit.model.FollowActor;
import com.bloatit.model.FollowFeature;
import com.bloatit.model.FollowSoftware;
import com.bloatit.model.Image;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.HtmlFollowButton.HtmlFollowAllButton;
import com.bloatit.web.components.HtmlFollowButton.HtmlFollowFeatureButton;
import com.bloatit.web.components.MemberListRenderer;
import com.bloatit.web.components.SoftwareListRenderer;
import com.bloatit.web.components.TeamListRenderer;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.features.FeaturesTools.FeatureContext;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.linkable.master.sidebar.SideBarElementLayout;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.url.ActivityPageUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.FollowFeatureActionUrl;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.ManageFollowActionUrl;
import com.bloatit.web.url.ManageFollowPageUrl;

@ParamContainer("activity/settings")
public class ManageFollowPage extends LoggedElveosPage {
    private final ManageFollowPageUrl url;

    public ManageFollowPage(final ManageFollowPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException, UnauthorizedPrivateAccessException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.setCssClass("manage-activity-page");

        final HtmlDiv menuBarItemBackToActivity = new HtmlDiv("menu_bar_item");
        layout.addLeft(menuBarItemBackToActivity);
        {
            final HtmlDiv menuBarItemImage = new HtmlDiv("menu_bar_item_image");
            menuBarItemBackToActivity.add(menuBarItemImage);
            menuBarItemImage.add(new HtmlImage(new Image(WebConfiguration.getImgActivitySmall()), ""));
            final HtmlDiv menuBarItemLink = new HtmlDiv("menu_bar_item_link");
            menuBarItemBackToActivity.add(menuBarItemLink);
            final ActivityPageUrl activityPageUrl = new ActivityPageUrl();
            activityPageUrl.setMember(loggedUser);
            menuBarItemLink.add(activityPageUrl.getHtmlLink(Context.tr("Back to activity")));
        }

        final HtmlTitle title = new HtmlTitle(1);
        title.addText(Context.tr("Manage activity"));
        layout.addLeft(title);

        final PageIterable<FollowActor> followedActors = loggedUser.getFollowedActors();
        final PageIterable<FollowSoftware> followedSoftwares = loggedUser.getFollowedSoftware();
        final PageIterable<FollowFeature> followedFeatures = loggedUser.getFollowedFeatures();

        final HtmlDiv smallContentList = new HtmlDiv();
        layout.addLeft(smallContentList);

        smallContentList.add(new FollowAllBox());

        for (final FollowSoftware followedSoftware : followedSoftwares) {
            smallContentList.add(new SoftwareListRenderer().generate(followedSoftware.getFollowed()));
        }

        for (final FollowActor followedActor : followedActors) {
            final Actor<?> actor = followedActor.getFollowed();
            if (actor.isTeam()) {
                smallContentList.add(new TeamListRenderer().generate((Team) actor));
            } else {
                smallContentList.add(new MemberListRenderer().generate((Member) actor));
            }
        }

        final HtmlDiv longContentList = new HtmlDiv();
        layout.addLeft(longContentList);

        for (final FollowFeature followedFeature : followedFeatures) {
            longContentList.add(new FeatureListRenderer(followedFeature).generate(followedFeature.getFollowed()));

        }

        // Manage emails
        final SideBarElementLayout manageFollowSideElement = new SideBarElementLayout();
        layout.addRight(manageFollowSideElement);

        manageFollowSideElement.add(new HtmlTitle(Context.tr("Email settings"), 1));

        final ManageFollowActionUrl doModifyUrl = new ManageFollowActionUrl(Context.getSession().getShortKey());

        // Create the form stub
        final HtmlForm globalSettingsForm = new HtmlForm(doModifyUrl.urlString());
        manageFollowSideElement.add(globalSettingsForm);

        // Email strategy
        final FieldData emailStratedyFieldData = doModifyUrl.getEmailStrategyParameter().pickFieldData();
        final HtmlDropDown emailStategyInput = new HtmlDropDown(emailStratedyFieldData.getName(), Context.tr("Email strategy"));
        emailStategyInput.addErrorMessages(emailStratedyFieldData.getErrorMessages());
        emailStategyInput.addDropDownElements(EnumSet.allOf(BindedEmailStrategy.class));
        final String suggestedEmailStrategy = emailStratedyFieldData.getSuggestedValue();
        if (suggestedEmailStrategy != null) {
            emailStategyInput.setDefaultValue(suggestedEmailStrategy);
        } else {
            emailStategyInput.setDefaultValue(BindedEmailStrategy.getBindedEmailStrategy(loggedUser.getEmailStrategy()).getEmailStrategy().toString());
        }
        emailStategyInput.setComment(Context.tr("New email strategy. Current strategy is ''{0}''.",
                                                BindedEmailStrategy.getBindedEmailStrategy(loggedUser.getEmailStrategy()).getDisplayName()));
        globalSettingsForm.add(emailStategyInput);

        // Button
        globalSettingsForm.add(new HtmlSubmit(Context.tr("Save settings")));

        return layout;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to manage contents followed");
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Manage contents followed");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = ActivityPage.generateBreadcrumb(AuthToken.getMember());
        breadcrumb.pushLink(new ManageFollowPageUrl().getHtmlLink(Context.tr("settings")));
        return breadcrumb;
    }

    public static class FeatureListRenderer implements HtmlRenderer<Feature> {

        private final FollowFeature followFeature;

        FeatureListRenderer(final FollowFeature followFeature) {
            this.followFeature = followFeature;

        }

        @Override
        public HtmlNode generate(final Feature feature) {
            final FeaturePageUrl featureUrl = new FeaturePageUrl(feature, FeatureTabKey.description);
            final HtmlDiv box = new HtmlDiv("feature-box");

            box.add(new HtmlDiv("feature-box-avatar").add((new SoftwaresTools.Logo(feature.getSoftware()))));

            final HtmlDiv content = new HtmlDiv("feature-box-content");
            box.add(content);

            // Name
            final HtmlDiv nameBox = new HtmlDiv("feature-box-title");
            HtmlLink htmlLink;
            htmlLink = featureUrl.getHtmlLink(FeaturesTools.getTitle(feature));
            htmlLink.setCssClass("feature-link");
            nameBox.add(htmlLink);
            content.add(nameBox);

            // Subtitle
            final HtmlDiv subtitle = new HtmlDiv("feature-box-subtitle");
            content.add(subtitle);
            subtitle.add(FeaturesTools.generateDetails(feature, false));

            content.add(FeaturesTools.generateProgress(feature, FeatureContext.FEATURE_LIST_PAGE));

            // Follow
            content.add(new HtmlFollowFeatureButton(feature));

            if (followFeature.isFeatureComment()) {
                content.add(new FollowFeatureActionUrl(Context.getSession().getShortKey(),
                                                       feature,
                                                       true,
                                                       followFeature.isBugComment(),
                                                       false,
                                                       followFeature.isMail()).getHtmlLink(Context.tr("stop following comments"))
                                                                              .setCssClass("follow-comments"));
            } else {
                content.add(new FollowFeatureActionUrl(Context.getSession().getShortKey(),
                                                       feature,
                                                       true,
                                                       followFeature.isBugComment(),
                                                       true,
                                                       followFeature.isMail()).getHtmlLink(Context.tr("follow comments"))
                                                                              .setCssClass("follow-comments"));
            }

            if (followFeature.isBugComment()) {
                content.add(new FollowFeatureActionUrl(Context.getSession().getShortKey(),
                                                       feature,
                                                       true,
                                                       false,
                                                       followFeature.isFeatureComment(),
                                                       followFeature.isMail()).getHtmlLink(Context.tr("stop following bugs"))
                                                                              .setCssClass("follow-bugs"));
            } else {
                content.add(new FollowFeatureActionUrl(Context.getSession().getShortKey(),
                                                       feature,
                                                       true,
                                                       true,
                                                       followFeature.isFeatureComment(),
                                                       followFeature.isMail()).getHtmlLink(Context.tr("follow bugs")).setCssClass("follow-bugs"));
            }

            return box;
        }
    }

    public enum BindedEmailStrategy implements Displayable {
        VERY_FREQUENTLY(EmailStrategy.VERY_FREQUENTLY, tr("Very frequently")), HOURLY(EmailStrategy.HOURLY, tr("Hourly")), DAILY(EmailStrategy.DAILY,
                                                                                                                                 tr("Daily")), WEEKLY(
                                                                                                                                                      EmailStrategy.WEEKLY,
                                                                                                                                                      tr("Weekly"));

        private final String label;
        private final EmailStrategy emailStrategy;

        private BindedEmailStrategy(final EmailStrategy emailStrategy, final String label) {
            this.emailStrategy = emailStrategy;
            this.label = label;
        }

        protected static BindedEmailStrategy getBindedEmailStrategy(final EmailStrategy emailStrategy) {
            return Enum.valueOf(BindedEmailStrategy.class, emailStrategy.name());
        }

        @Override
        public String getDisplayName() {
            return Context.tr(label);
        }

        public EmailStrategy getEmailStrategy() {
            return emailStrategy;
        }

        // Fake tr
        private static String tr(final String fake) {
            return fake;
        }

    }

    public class FollowAllBox extends HtmlDiv {

        public FollowAllBox() {
            super("actor-box");

            add(new HtmlDiv("actor-box-avatar").add(new HtmlImage(new Image(WebConfiguration.getImgLogoSmall()), "")));

            final HtmlDiv content = new HtmlDiv("actor-box-content");
            add(content);

            // Name
            final HtmlDiv nameBox = new HtmlDiv("actor-box-actor-name");
            HtmlLink htmlLink;
            htmlLink = new IndexPageUrl().getHtmlLink(Context.tr("Whole Elveos"));
            nameBox.add(htmlLink);
            content.add(nameBox);

            // Subtitle
            final HtmlDiv subtitle = new HtmlDiv("actor-box-subtitle");
            content.add(subtitle);
            subtitle.addText(Context.tr("Follow all current and future features"));

            // Follow
            content.add(new HtmlFollowAllButton());

        }
    }
}
