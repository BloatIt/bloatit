/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.rest.list;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Comment;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestComment;

/**
 * <p>
 * Wraps a list of Comment into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Comment<br />
 * Example:
 * 
 * <pre>
 * {@code <Comments>}
 *     {@code <Comment name=Comment1 />}
 *     {@code <Comment name=Comment2 />}
 * {@code </Comments>}
 * </pre>
 * <p>
 */
@XmlRootElement(name = "comments")
public class RestCommentList extends RestListBinder<RestComment, Comment> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestCommentList() {
        super();
    }

    /**
     * Creates a RestCommentList from a {@codePageIterable<Comment>}
     * 
     * @param collection the list of elements from the model
     */
    public RestCommentList(final PageIterable<Comment> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "comment")
    @XmlIDREF
    public List<RestComment> getComments() {
        final List<RestComment> comments = new ArrayList<RestComment>();
        for (final RestComment comment : this) {
            comments.add(comment);
        }
        return comments;
    }
}
