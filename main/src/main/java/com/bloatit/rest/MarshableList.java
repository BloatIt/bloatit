package com.bloatit.rest;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
public class MarshableList<T> extends ArrayList<T> {
    private static final long serialVersionUID = -5218175310168474618L;

    @XmlElement
    public MarshableList<T> getElements() {
        return this;
    }
    
}
