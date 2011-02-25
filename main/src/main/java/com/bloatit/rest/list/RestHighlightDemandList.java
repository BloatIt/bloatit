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
import com.bloatit.model.HighlightDemand;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestHighlightDemand;

/**
 * <p>
 * Wraps a list of HighlightDemand into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of HighlightDemand<br />
 * Example:
 *
 * <pre>
 * {@code <HighlightDemands>}
 *     {@code <HighlightDemand name=HighlightDemand1 />}
 *     {@code <HighlightDemand name=HighlightDemand2 />}
 * {@code </HighlightDemands>}
 * </pre>
 * <p>
 */
@XmlRootElement(name = "highlightdemands")
public class RestHighlightDemandList extends RestListBinder<RestHighlightDemand, HighlightDemand> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestHighlightDemandList() {
        super();
    }

    /**
     * Creates a RestHighlightDemandList from a
     * {@codePageIterable<HighlightDemand>}
     *
     * @param collection the list of elements from the model
     */
    public RestHighlightDemandList(PageIterable<HighlightDemand> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "highlightdemand")
    @XmlIDREF
    public List<RestHighlightDemand> getHighlightDemands() {
        List<RestHighlightDemand> highlightdemands = new ArrayList<RestHighlightDemand>();
        for (RestHighlightDemand highlightdemand : this) {
            highlightdemands.add(highlightdemand);
        }
        return highlightdemands;
    }
}
