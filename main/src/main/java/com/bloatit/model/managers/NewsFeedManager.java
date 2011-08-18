package com.bloatit.model.managers;

import com.bloatit.data.DaoNewsFeed;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.model.NewsFeed;
import com.bloatit.model.lists.NewsFeedList;

public class NewsFeedManager {
    /**
     * Gets the news feed matching a given id
     */
    public static NewsFeed getById(final Integer id) {
        return NewsFeed.create(DBRequests.getById(DaoNewsFeed.class, id));
    }

    /**
     * @param i
     * @return all the non deleted news feed
     */
    public static NewsFeedList getAll() {
        return getAll(false);
    }

    /**
     * @param deleted <i>true</i> if the request must return deleted news feed,
     *            <i>false</i> otherwise
     * @return the list of news feed
     */
    public static NewsFeedList getAll(final boolean isDeleted) {
        if (isDeleted) {
            return new NewsFeedList(DBRequests.getAll(DaoNewsFeed.class));
        } else {
            return new NewsFeedList(DaoNewsFeed.getAll(isDeleted));
        }
    }
}
