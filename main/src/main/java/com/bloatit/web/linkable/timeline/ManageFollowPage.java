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

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoActor;
import com.bloatit.data.DaoTeam;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlRenderer;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownEditor;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownPreviewer;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlCheckbox;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.FollowActor;
import com.bloatit.model.FollowFeature;
import com.bloatit.model.FollowSoftware;
import com.bloatit.model.Member;
import com.bloatit.model.Software;
import com.bloatit.model.Team;
import com.bloatit.model.lists.FollowList;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.components.MemberListRenderer;
import com.bloatit.web.components.SoftwareListRenderer;
import com.bloatit.web.components.TeamListRenderer;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.team.TeamPage;
import com.bloatit.web.url.ManageFollowPageUrl;
import com.bloatit.web.url.ModifyTeamActionUrl;
import com.bloatit.web.url.ModifyTeamPageUrl;
import com.bloatit.web.url.SoftwareListPageUrl;

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
}
