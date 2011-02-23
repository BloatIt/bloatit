package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Description;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestDescription;

@XmlRootElement
public class RestDescriptionList extends RestListBinder<RestDescription, Description> {
    public RestDescriptionList(PageIterable<Description> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "descriptions")
    @XmlElement(name = "description")
    public RestDescriptionList getDescriptions() {
        return this;
    }
}

