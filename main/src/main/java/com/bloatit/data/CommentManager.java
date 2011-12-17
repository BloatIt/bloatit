//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.data;

import java.util.Collection;

import org.hibernate.Query;

import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.utils.PageIterable;

/**
 * The Class CommentManager contain some useful methods to manage comments.
 */
public class CommentManager {

    private CommentManager() {
        // disactivate CTOR
    }

    /**
     * Gets the comments.
     *
     * @param persistentSetOfComments a persistent set of comments
     * @return the comments
     */
    protected static PageIterable<DaoComment> getComments(final Collection<DaoComment> persistentSetOfComments) {
        final Query allComments = createFilter(persistentSetOfComments, "ORDER BY creationDate DESC");
        final Query allCommentsSize = createFilter(persistentSetOfComments, "select count(*)");
        return new QueryCollection<DaoComment>(allComments, allCommentsSize);
    }

    /**
     * Gets the last comment.
     *
     * @param persistentSetOfComments the persistent set of comments
     * @return the last comment
     */
    protected static DaoComment getLastComment(final Collection<DaoComment> persistentSetOfComments) {
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
