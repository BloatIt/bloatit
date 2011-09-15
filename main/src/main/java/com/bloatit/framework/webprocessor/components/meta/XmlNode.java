package com.bloatit.framework.webprocessor.components.meta;


public abstract class XmlNode extends Node<XmlNode> {

    public XmlNode(String tag) {
        super(tag);
    }

    @Override
    protected boolean autoCloseTag() {
        return !hasChild();
    }
    
    

}
