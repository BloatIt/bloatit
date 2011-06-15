package com.bloatit.model.lists;

import com.bloatit.data.DaoNewsFeed;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.NewsFeed;

public class NewsFeedList extends ListBinder<NewsFeed, DaoNewsFeed> {
    public NewsFeedList(final PageIterable<DaoNewsFeed> daoCollection) {
        super(daoCollection);
    }

}
