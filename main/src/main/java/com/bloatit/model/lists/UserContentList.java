package com.bloatit.model.lists;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.UserContent;

public class UserContentList extends ListBinder<UserContent<? extends DaoUserContent>, DaoUserContent> {
    public UserContentList(final PageIterable<DaoUserContent> daoCollection) {
        super(daoCollection);
    }
}
