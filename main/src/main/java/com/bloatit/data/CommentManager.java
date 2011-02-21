package com.bloatit.data;

import java.util.Set;

import org.hibernate.Query;

import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.utils.PageIterable;

public class CommentManager {

    private final Set<DaoComment> comments;

    CommentManager(Set<DaoComment> persistentSetOfComments) {
        super();
        this.comments = persistentSetOfComments;
    }

    /**
     * @return the comments
     */
    public PageIterable<DaoComment> getComments() {
        final Query allComments = SessionManager.getSessionFactory().getCurrentSession().createFilter(comments, "");
        final Query allCommentsSize = SessionManager.getSessionFactory().getCurrentSession().createFilter(comments, "select count(*)");
        return new QueryCollection<DaoComment>(allComments, allCommentsSize);
    }

    /**
     * @return the last comment
     */
    public DaoComment getLastComment() {
        final Query allComments = SessionManager.getSessionFactory().getCurrentSession().createFilter(comments, "ORDER BY creationDate DESC");
        final Query allCommentsSize = SessionManager.getSessionFactory().getCurrentSession().createFilter(comments, "select count(*)");
        final QueryCollection<DaoComment> queryCollection = new QueryCollection<DaoComment>(allComments, allCommentsSize);
        if (queryCollection.size() == 0) {
            return null;
        }
        return queryCollection.iterator().next();
    }

    public void addComment(final DaoComment comment) {
        comments.add(comment);
    }

}
