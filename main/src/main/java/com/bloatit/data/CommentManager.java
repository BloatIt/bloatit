package com.bloatit.data;

import java.util.Collection;

import org.hibernate.Query;

import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.utils.PageIterable;

public class CommentManager {

    private CommentManager() {
        // disactivate CTOR
    }

    /**
     * @return the comments
     */
    public static PageIterable<DaoComment> getComments(final Collection<DaoComment> persistentSetOfComments) {
        final Query allComments = createFilter(persistentSetOfComments, "");
        final Query allCommentsSize = createFilter(persistentSetOfComments, "select count(*)");
        return new QueryCollection<DaoComment>(allComments, allCommentsSize);
    }

    /**
     * @return the last comment
     */
    public static DaoComment getLastComment(final Collection<DaoComment> persistentSetOfComments) {
        final Query allComments = createFilter(persistentSetOfComments, "ORDER BY creationDate DESC");
        final Query allCommentsSize = createFilter(persistentSetOfComments, "select count(*)");
        final QueryCollection<DaoComment> queryCollection = new QueryCollection<DaoComment>(allComments, allCommentsSize);
        if (queryCollection.size() == 0) {
            return null;
        }
        return queryCollection.iterator().next();
    }

    private static Query createFilter(final Collection<DaoComment> persistentSetOfComments, final String filter) {
        return SessionManager.getSessionFactory().getCurrentSession().createFilter(persistentSetOfComments, filter);
    }

}
