package com.bloatit.framework.webprocessor.components.meta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;

public class XmlNonEscapedElement extends XmlNode {

    private final List<XmlNode> children = new ArrayList<XmlNode>();

    public XmlNonEscapedElement(String tag) {
        super(tag);
    }

    /**
     * <p>
     * Add an attribute to an element
     * </p>
     * <p>
     * Special treatment will happen if the attribute <code>name</code> is
     * <code>id<code>
     * </p>
     * <p>
     * Example :
     * 
     * <pre>
     * HtmlElement e = new HtmlElement(&quot;img&quot;);
     * e.addAttribute(&quot;src&quot;, &quot;example.com/plop.png&quot;);
     * 
     * </pre>
     * 
     * will be used to create : {@code <img src="example.com/plop.png />}
     * </p>
     * 
     * @param name the name of the attribute to add
     * @param value the value of the attribute to add
     * @return itself
     */
    public XmlNonEscapedElement addAttribute(final String name, final String value) {
        if (tag == null) {
            throw new BadProgrammerException("Are you trying to add an attribute to a PlaceHolderElement ?");
        }
        tag.addAttribute(name, value);
        return this;
    }

    /**
     * Add a son to this XmlElement
     * 
     * @param xml the xmlNode son to add
     * @return itself
     */
    public XmlNonEscapedElement add(final XmlNode xml) {
        assert(xml != null);
        children.add(xml);
        return this;
    }

    /**
     * Adds some raw text to this HtmlElement
     * 
     * @param text the text to add
     * @return itself
     */
    public XmlNonEscapedElement addText(final String text) {
        children.add(new XmlNonEscapedText(text));
        return this;
    }

    @Override
    public final Iterator<XmlNode> iterator() {
        return children.iterator();
    }

}
