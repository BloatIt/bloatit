package com.bloatit.model.managers;

import com.bloatit.data.DaoFollow;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.model.Follow;
import com.bloatit.model.lists.FollowList;

public class FollowManager {

    private FollowManager() {
        // Disable CTOR
    }

    /**
     * Gets the follow matching a given id
     */
    public static Follow getById(final Integer id) {
        return Follow.create(DBRequests.getById(DaoFollow.class, id));
    }

    /**
     * @return all the follow
     */
    public static FollowList getAll() {
        return new FollowList(DBRequests.getAll(DaoFollow.class));
    }
}
