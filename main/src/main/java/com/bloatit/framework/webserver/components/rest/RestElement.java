package com.bloatit.framework.webserver.components.rest;

import com.bloatit.framework.webserver.components.meta.XmlElement;
import com.bloatit.framework.webserver.components.meta.XmlNode;

public class RestElement extends XmlElement {
    public RestElement(String tag) {
        super(tag);
    }

    @Override
    public XmlElement addAttribute(String name, String value) {
        return super.addAttribute(name, value);
    }

    @Override
    public XmlElement add(XmlNode html) {
        return super.add(html);
    }

    @Override
    public XmlElement addText(String text) {
        return super.addText(text);
    }

}
