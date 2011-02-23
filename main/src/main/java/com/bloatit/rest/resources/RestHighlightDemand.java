package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.HighlightDemand;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestHighlightDemandList;

@XmlRootElement
public class RestHighlightDemand extends RestElement<HighlightDemand> {
    private final HighlightDemand model;

    protected RestHighlightDemand(final HighlightDemand model) {
        this.model = model;
    }

    @REST(name = "highlightdemands", method = RequestMethod.GET)
    public static RestHighlightDemand getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "highlightdemands", method = RequestMethod.GET)
    public static RestHighlightDemandList getAll() {
        // TODO auto generated code
        return null;
    }

    HighlightDemand getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
