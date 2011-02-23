package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Kudos;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestKudos;

@XmlRootElement
public class RestKudosList extends RestListBinder<RestKudos, Kudos> {
    public RestKudosList(PageIterable<Kudos> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "kudoss")
    @XmlElement(name = "kudos")
    public RestKudosList getKudoss() {
        return this;
    }
}

