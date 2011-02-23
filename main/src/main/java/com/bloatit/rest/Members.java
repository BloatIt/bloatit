package com.bloatit.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({ Member.class })
public class Members extends ArrayList<Member> {
    private static final long serialVersionUID = -6854206394315558823L;

    public Members() {
        super();
    }

    @XmlElement
    public List<Member> getMembers() {
        return this;
    }

}
