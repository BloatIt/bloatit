package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Offer;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestOffer;

@XmlRootElement
public class RestOfferList extends RestListBinder<RestOffer, Offer> {
    public RestOfferList(PageIterable<Offer> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "offers")
    @XmlElement(name = "offer")
    public RestOfferList getOffers() {
        return this;
    }
}

