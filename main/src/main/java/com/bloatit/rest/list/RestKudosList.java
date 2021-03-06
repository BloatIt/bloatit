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
import com.bloatit.model.Kudos;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestKudos;

/**
 * <p>
 * Wraps a list of Kudos into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Kudos<br />
 * Example:
 * 
 * <pre>
 * {@code <Kudoss>}
 *     {@code <Kudos name=Kudos1 />}
 *     {@code <Kudos name=Kudos2 />}
 * {@code </Kudoss>}
 * </pre>
 * <p>
 */
@XmlRootElement(name = "kudos")
public class RestKudosList extends RestListBinder<RestKudos, Kudos> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestKudosList() {
        super();
    }

    /**
     * Creates a RestKudosList from a {@codePageIterable<Kudos>}
     * 
     * @param collection the list of elements from the model
     */
    public RestKudosList(final PageIterable<Kudos> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "kudo")
    @XmlIDREF
    public List<RestKudos> getKudoss() {
        final List<RestKudos> kudoss = new ArrayList<RestKudos>();
        for (final RestKudos kudos : this) {
            kudoss.add(kudos);
        }
        return kudoss;
    }
}
