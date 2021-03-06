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
package com.bloatit.model.managers;

import java.util.Date;

import com.bloatit.data.DaoBug;
import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoCommentable;
import com.bloatit.data.DaoFeature;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.model.Bug;
import com.bloatit.model.Comment;
import com.bloatit.model.Commentable;
import com.bloatit.model.FeatureImplementation;
import com.bloatit.model.lists.CommentList;

/**
 * The Class CommentManager. Utility class containing static methods to get
 * {@link Comment}s from the DB.
 */
public final class CommentManager {

    /**
     * Desactivated constructor on utility class.
     */
    private CommentManager() {
        // Desactivate default ctor
    }

    /**
     * Gets a Comment by id.
     *
     * @param id the {@link Comment} id
     * @return the Comment or null if not found.
     */
    public static Comment getById(final Integer id) {
        return Comment.create(DBRequests.getById(DaoComment.class, id));
    }

    public static CommentList getAll() {
        return new CommentList(DBRequests.getAll(DaoComment.class));
    }

    public static Long getNbComments(Date from, Date to) {
        return DaoComment.getCommentCount(from, to);
    }

    public static Commentable getCommentable(final int id) {
        DaoCommentable commentable = DaoComment.getCommentable(id);
        if (commentable instanceof DaoBug) {
            return Bug.create((DaoBug) commentable);
        }
        if (commentable instanceof DaoComment) {
            return Comment.create((DaoComment) commentable);
        }
        if (commentable instanceof DaoFeature) {
            return FeatureImplementation.create((DaoFeature) commentable);
        }
        return null;
    }
}
