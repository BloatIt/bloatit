package com.bloatit.web.components;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Image;
import com.bloatit.model.NewsFeed;
import com.bloatit.model.managers.NewsFeedManager;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.pages.master.sidebar.SideBarElementLayout;

public class NewsFeedSideBlock extends SideBarElementLayout {
    public NewsFeedSideBlock() {
        super();
        HtmlDiv master = new HtmlDiv("news_feed");
        add(master);

        // small icons to display the host of the feed (twitter or identica)
        HtmlDiv socialFeedIcons = new HtmlDiv("feed_icons");
        master.add(socialFeedIcons);
        HtmlImage identicaImg = new HtmlImage(new Image(WebConfiguration.getImgIdenticaIcon()), "", "feed_icon");
        HtmlImage twitterImg = new HtmlImage(new Image(WebConfiguration.getImgTwitterIcon()), "", "feed_icon");
        socialFeedIcons.add(identicaImg);
        socialFeedIcons.add(twitterImg);

        master.add(new HtmlTitle(Context.tr("News feed"), 1));

        HtmlList feedList = new HtmlList();
        master.add(feedList);
        feedList.setCssClass("feed_list");

        for (final NewsFeed news : NewsFeedManager.getAll()) {
            HtmlDiv feedItem = new HtmlDiv("feed_item");
            feedList.add(feedItem);

            HtmlDiv itemContent = new HtmlDiv("item_content");
            HtmlDiv itemDate = new HtmlDiv("item_date");
            itemContent.addText(news.getMessage());
            itemDate.addText(HtmlTools.formatDate(Context.getLocalizator().getDate(news.getCreationDate())));
            feedItem.add(itemContent);
            feedItem.add(itemDate);
        }
    }
}
