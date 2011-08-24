package com.bloatit.web.linkable.members.tabs;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock.HtmlTab;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.Feature;
import com.bloatit.model.Follow;
import com.bloatit.model.lists.FollowList;
import com.bloatit.model.visitor.AbstractModelClassVisitor;
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
        final HtmlTitleBlock followed = new HtmlTitleBlock(Context.tr("Recent activity"), 1);
        master.add(followed);

        FollowList followeds = actor.getFollowedContent();
        System.out.println(followeds.size());

        for (Follow follow : followeds) {
            System.out.println(follow);
            HtmlElement elem = follow.getFollowed().accept(new AbstractModelClassVisitor<HtmlElement>() {
                @Override
                public HtmlElement visit(Feature model) {
                    HtmlParagraph p = new HtmlParagraph(model.getTitle());
                    return p;
                }
            });
            followed.add(elem);
        }

        return master;
    }
}
