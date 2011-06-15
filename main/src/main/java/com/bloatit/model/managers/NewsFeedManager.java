package com.bloatit.model.managers;

import com.bloatit.data.DaoNewsFeed;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.model.NewsFeed;
import com.bloatit.model.lists.NewsFeedList;

public class NewsFeedManager {
    /**
     * Gets the money withdrawal matching a given id
     */
    public static NewsFeed getById(final Integer id) {
        return NewsFeed.create(DBRequests.getById(DaoNewsFeed.class, id));
    }

    /**
     * @return all the money withdrawals
     */
    public static NewsFeedList getAll() {
        return new NewsFeedList(DBRequests.getAll(DaoNewsFeed.class));
    }
}
