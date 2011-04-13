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
import com.bloatit.model.Description;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestDescription;

/**
 * <p>
 * Wraps a list of Description into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Description<br />
 * Example:
 * 
 * <pre>
 * {@code <Descriptions>}
 *     {@code <Description name=Description1 />}
 *     {@code <Description name=Description2 />}
 * {@code </Descriptions>}
 * </pre>
 * <p>
 */
@XmlRootElement(name = "descriptions")
public class RestDescriptionList extends RestListBinder<RestDescription, Description> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestDescriptionList() {
        super();
    }

    /**
     * Creates a RestDescriptionList from a {@codePageIterable<Description>}
     * 
     * @param collection the list of elements from the model
     */
    public RestDescriptionList(final PageIterable<Description> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "description")
    @XmlIDREF
    public List<RestDescription> getDescriptions() {
        final List<RestDescription> descriptions = new ArrayList<RestDescription>();
        for (final RestDescription description : this) {
            descriptions.add(description);
        }
        return descriptions;
    }
}
