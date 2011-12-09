package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "size")
@XmlAccessorType(XmlAccessType.NONE)
public class RestSize {
    @XmlAttribute
    final Long size;

    public RestSize() {
        size = 0L;
    }

    public RestSize(Long size) {
        super();
        this.size = size;
    }

}
