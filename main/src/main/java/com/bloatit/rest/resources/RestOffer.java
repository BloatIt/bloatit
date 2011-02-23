package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Offer;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestOfferList;

@XmlRootElement
public class RestOffer extends RestElement<Offer> {
    private final Offer model;

    protected RestOffer(final Offer model) {
        this.model = model;
    }

    @REST(name = "offers", method = RequestMethod.GET)
    public static RestOffer getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "offers", method = RequestMethod.GET)
    public static RestOfferList getAll() {
        // TODO auto generated code
        return null;
    }

    Offer getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
