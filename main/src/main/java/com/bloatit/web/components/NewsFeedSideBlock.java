package com.bloatit.web.components;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.renderer.HtmlCachedMarkdownRenderer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Image;
import com.bloatit.model.NewsFeed;
import com.bloatit.model.managers.NewsFeedManager;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.linkable.master.sidebar.SideBarElementLayout;

public class NewsFeedSideBlock extends SideBarElementLayout {

    public NewsFeedSideBlock(final int maxNews) {
        super();
        final HtmlDiv master = new HtmlDiv("news_feed");
        add(master);

        // small icons to display the host of the feed (twitter or identica)
        final HtmlDiv socialFeedIcons = new HtmlDiv("feed_icons");
        master.add(socialFeedIcons);

        final HtmlImage identicaImg = new HtmlImage(new Image(WebConfiguration.getImgIdenticaIcon()), "", "feed_icon");
        final HtmlLink identicaLink = new HtmlLink("http://identi.ca/elveos", identicaImg);
        final HtmlImage twitterImg = new HtmlImage(new Image(WebConfiguration.getImgTwitterIcon()), "", "feed_icon");
        final HtmlLink twitterLink = new HtmlLink("http://twitter.com/#!/elveos", twitterImg);
        socialFeedIcons.add(identicaLink);
        socialFeedIcons.add(twitterLink);
        master.add(new HtmlTitle(Context.tr("News feed"), 1));

        final HtmlList feedList = new HtmlList();
        master.add(feedList);
        feedList.setCssClass("feed_list");

        int count = 1;
        for (final NewsFeed news : NewsFeedManager.getAll()) {
            if (count > maxNews) {
                break;
            }

            final HtmlDiv feedItem = new HtmlDiv("feed_item");
            feedList.add(feedItem);

            final HtmlDiv itemContent = new HtmlDiv("item_content");
            final HtmlDiv itemDate = new HtmlDiv("item_date");
            itemContent.add(new HtmlCachedMarkdownRenderer(news.getMessage()));
            itemDate.addText(HtmlTools.formatDate(Context.getLocalizator().getDate(news.getCreationDate())));
            feedItem.add(itemContent);
            feedItem.add(itemDate);

            count++;
        }
    }
}
