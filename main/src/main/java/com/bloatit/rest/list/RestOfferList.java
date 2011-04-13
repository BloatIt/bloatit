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
import com.bloatit.model.Offer;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestOffer;

/**
 * <p>
 * Wraps a list of Offer into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Offer<br />
 * Example:
 * 
 * <pre>
 * {@code <Offers>}
 *     {@code <Offer name=Offer1 />}
 *     {@code <Offer name=Offer2 />}
 * {@code </Offers>}
 * </pre>
 * <p>
 */
@XmlRootElement(name = "offers")
public class RestOfferList extends RestListBinder<RestOffer, Offer> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestOfferList() {
        super();
    }

    /**
     * Creates a RestOfferList from a {@codePageIterable<Offer>}
     * 
     * @param collection the list of elements from the model
     */
    public RestOfferList(final PageIterable<Offer> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "offer")
    @XmlIDREF
    public List<RestOffer> getOffers() {
        final List<RestOffer> offers = new ArrayList<RestOffer>();
        for (final RestOffer offer : this) {
            offers.add(offer);
        }
        return offers;
    }
}
