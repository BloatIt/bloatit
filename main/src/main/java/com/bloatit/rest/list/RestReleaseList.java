package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Release;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestRelease;

@XmlRootElement
public class RestReleaseList extends RestListBinder<RestRelease, Release> {
    public RestReleaseList(PageIterable<Release> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "releases")
    @XmlElement(name = "release")
    public RestReleaseList getReleases() {
        return this;
    }
}

