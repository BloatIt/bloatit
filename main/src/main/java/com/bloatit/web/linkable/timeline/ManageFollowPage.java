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
package com.bloatit.web.linkable.timeline;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlRenderer;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.Feature;
import com.bloatit.model.FollowActor;
import com.bloatit.model.FollowFeature;
import com.bloatit.model.FollowSoftware;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.components.HtmlFollowButton.HtmlFollowFeatureButton;
import com.bloatit.web.components.MemberListRenderer;
import com.bloatit.web.components.SoftwareListRenderer;
import com.bloatit.web.components.TeamListRenderer;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.features.FeaturesTools.FeatureContext;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.FollowFeatureActionUrl;
import com.bloatit.web.url.ManageFollowPageUrl;

@ParamContainer("timeline/settings")
public class ManageFollowPage extends LoggedElveosPage {
    private final ManageFollowPageUrl url;


    public ManageFollowPage(final ManageFollowPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);


        final HtmlTitle title = new HtmlTitle(1);
        title.addText(Context.tr("Manage contents followed"));
        layout.addLeft(title);

        
        PageIterable<FollowActor> followedActors = loggedUser.getFollowedActors();
        PageIterable<FollowSoftware> followedSoftwares = loggedUser.getFollowedSoftware();
        PageIterable<FollowFeature> followedFeatures = loggedUser.getFollowedFeatures();
        
        HtmlDiv smallContentList = new HtmlDiv();
        layout.addLeft(smallContentList);
        
        for(FollowSoftware followedSoftware: followedSoftwares) {
            smallContentList.add(new SoftwareListRenderer().generate(followedSoftware.getFollowed()));
            //System.out.println("plop");
        }
        
        for(FollowActor followedActor: followedActors) {
            Actor<?> actor = followedActor.getFollowed();
            if(actor.isTeam()) {
                smallContentList.add(new TeamListRenderer().generate((Team) actor));
            } else {
                smallContentList.add(new MemberListRenderer().generate((Member) actor));
            }
        }
        
        HtmlDiv longContentList = new HtmlDiv();
        layout.addLeft(longContentList );
        
        for(FollowFeature followedFeature: followedFeatures) {
            longContentList.add(new FeatureListRenderer(followedFeature).generate(followedFeature.getFollowed()));
            
        }
        
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
        final Breadcrumb breadcrumb = TimelinePage.generateBreadcrumb();
        breadcrumb.pushLink(new ManageFollowPageUrl().getHtmlLink(Context.tr("settings")));
        return breadcrumb;
    }
    
    public static class FeatureListRenderer implements HtmlRenderer<Feature> {
        
        private final FollowFeature followFeature;

        FeatureListRenderer(FollowFeature followFeature) {
            this.followFeature = followFeature;
            
        }
        
        @Override
        public HtmlNode generate(final Feature feature) {
            final FeaturePageUrl featureUrl = new FeaturePageUrl(feature, FeatureTabKey.description);
            final HtmlDiv box = new HtmlDiv("feature-box");

            box.add(new HtmlDiv("feature-box-avatar").add((new SoftwaresTools.Logo(feature.getSoftware()))));
            
            HtmlDiv content = new HtmlDiv("feature-box-content");
            box.add(content);
            

            // Name
            final HtmlDiv nameBox = new HtmlDiv("feature-box-title");
            HtmlLink htmlLink;
            htmlLink = featureUrl.getHtmlLink(FeaturesTools.getTitle(feature));
            htmlLink.setCssClass("feature-link");
            nameBox.add(htmlLink);
            content.add(nameBox);
            
            // Subtitle
            HtmlDiv subtitle = new HtmlDiv("feature-box-subtitle");
            content.add(subtitle);
            subtitle.add(FeaturesTools.generateDetails(feature, false));

            content.add(FeaturesTools.generateProgress(feature, FeatureContext.FEATURE_LIST_PAGE ));

            // Follow
            content.add(new HtmlFollowFeatureButton(feature));
            
            if(followFeature.isFeatureComment()) {
                content.add(new FollowFeatureActionUrl(Context.getSession().getShortKey(), feature, true, followFeature.isMail(), false, followFeature.isBugComment()).getHtmlLink(Context.tr("stop follow comments")).setCssClass("follow-comments"));
            } else {
                content.add(new FollowFeatureActionUrl(Context.getSession().getShortKey(), feature, true, followFeature.isMail(), true, followFeature.isBugComment()).getHtmlLink(Context.tr("follow comments")).setCssClass("follow-comments"));
            }
            
            if(followFeature.isBugComment()) {
                content.add(new FollowFeatureActionUrl(Context.getSession().getShortKey(), feature, true, followFeature.isMail(), followFeature.isFeatureComment(), false).getHtmlLink(Context.tr("stop follow bugs")).setCssClass("follow-bugs"));
            } else {
                content.add(new FollowFeatureActionUrl(Context.getSession().getShortKey(), feature, true, followFeature.isMail(), followFeature.isFeatureComment(), true).getHtmlLink(Context.tr("follow bugs")).setCssClass("follow-bugs"));
            }
            
            
            return box;
        }
    }
}
