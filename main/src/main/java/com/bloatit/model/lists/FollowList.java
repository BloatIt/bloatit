package com.bloatit.model.lists;

import com.bloatit.data.DaoFollow;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Follow;

public final class FollowList extends ListBinder<Follow, DaoFollow> {
    public FollowList(final PageIterable<DaoFollow> daoCollection) {
        super(daoCollection);
    }
}
