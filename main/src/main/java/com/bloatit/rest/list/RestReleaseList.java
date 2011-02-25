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
import com.bloatit.model.Release;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestRelease;

/**
 * <p>
 * Wraps a list of Release into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Release<br />
 * Example:
 *
 * <pre>
 * {@code <Releases>}
 *     {@code <Release name=Release1 />}
 *     {@code <Release name=Release2 />}
 * {@code </Releases>}
 * </pre>
 * <p>
 */
@XmlRootElement(name = "releases")
public class RestReleaseList extends RestListBinder<RestRelease, Release> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestReleaseList() {
        super();
    }

    /**
     * Creates a RestReleaseList from a {@codePageIterable<Release>}
     *
     * @param collection the list of elements from the model
     */
    public RestReleaseList(PageIterable<Release> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "release")
    @XmlIDREF
    public List<RestRelease> getReleases() {
        List<RestRelease> releases = new ArrayList<RestRelease>();
        for (RestRelease release : this) {
            releases.add(release);
        }
        return releases;
    }
}
