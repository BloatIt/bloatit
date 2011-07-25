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
package com.bloatit.model;

import com.bloatit.model.right.Action;
import com.bloatit.model.right.UnauthorizedOperationException;

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

    Integer getId();

}
