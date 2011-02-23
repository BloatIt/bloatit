package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Bug;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestBug;

@XmlRootElement
public class RestBugList extends RestListBinder<RestBug, Bug> {
    public RestBugList(PageIterable<Bug> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "bugs")
    @XmlElement(name = "bug")
    public RestBugList getBugs() {
        return this;
    }
}

