package com.bloatit.model;

import com.bloatit.framework.exceptions.specific.UnauthorizedOperationException;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;

public interface Commentable {

    /**
     * Add a comment at the end of the comment list.
     *
     * @param text is the text of the comment.
     * @throws UnauthorizedOperationException if you do not have the
     *             {@link Action#WRITE} right on the <code>Comment</code>
     *             property.
     * @see #authenticate(AuthToken)
     */
    Comment addComment(final String text) throws UnauthorizedOperationException;

}
