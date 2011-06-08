package com.bloatit.data;

import java.util.List;

import com.bloatit.data.queries.QueryCollection;

public class MappedUserContentList<T extends DaoUserContent> extends QueryCollection<T> {

    public MappedUserContentList(final List<T> content) {
        super(SessionManager.createFilter(content, "where isDeleted = 'false'"),
              SessionManager.createFilter(content, "select count(*) where isDeleted = 'false'"));
    }

}
