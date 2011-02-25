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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlIDREF;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Group;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestGroup;

/**
 * <p>
 * Wraps a list of Group into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Group<br />
 * Example:
 *
 * <pre>
 * {@code <Groups>}
 *     {@code <Group name=Group1 />}
 *     {@code <Group name=Group2 />}
 * {@code </Groups>}
 * </pre>
 * <p>
 */
@XmlRootElement(name = "groups")
public class RestGroupList extends RestListBinder<RestGroup, Group> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestGroupList() {
        super();
    }

    /**
     * Creates a RestGroupList from a {@codePageIterable<Group>}
     *
     * @param collection the list of elements from the model
     */
    public RestGroupList(PageIterable<Group> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "group")
    @XmlIDREF
    public List<RestGroup> getGroups() {
        List<RestGroup> groups = new ArrayList<RestGroup>();
        for (RestGroup group : this) {
            groups.add(group);
        }
        return groups;
    }
}
