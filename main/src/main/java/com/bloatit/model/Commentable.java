package com.bloatit.model;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.model.right.Action;

public interface Commentable {

    /**
     * Add a comment at the end of the comment list.
     * 
     * @param text is the text of the comment.
     * @throws UnauthorizedOperationException if you do not have the
     *             {@link Action#WRITE} right on the <code>Comment</code>
     *             property.
     */
    Comment addComment(final String text) throws UnauthorizedOperationException;

}
