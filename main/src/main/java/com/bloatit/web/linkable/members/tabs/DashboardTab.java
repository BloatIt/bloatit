package com.bloatit.web.linkable.members.tabs;

import java.util.Date;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock.HtmlTab;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.Feature;
import com.bloatit.model.Follow;
import com.bloatit.model.lists.FollowList;
import com.bloatit.web.linkable.members.tabs.dashboard.Dashboard;
import com.bloatit.web.linkable.members.tabs.dashboard.DashboardEntry;
import com.bloatit.web.linkable.members.tabs.dashboard.DashboardRenderer;
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
        Dashboard dashboard = new Dashboard();
        for (Follow follow : followeds) {
            Feature f = follow.getFollowed();
            DashboardEntry entry = new DashboardEntry(f, "Next step ...", "Title ...", new Date());
            dashboard.addEntry(entry);
        }
        followed.add(new DashboardRenderer(dashboard));

        return master;
    }
}
