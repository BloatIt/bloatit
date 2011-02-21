package com.bloatit.framework.webserver.components.meta;

/**
 * <p>
 * HtmlTagText are used to input <b>un</b>escaped text
 * </p>
 * <p>
 * Should be used in the weird situations where other standard tags are not
 * flexible enough.
 * </p>
 * <p>
 * Usage :
 * 
 * <pre>
 * {@code another_component.add(new HtmlTagText("<span class="plop">foo</span>));}
 * </pre>
 * 
 * <b>Note : </b> In the previous example, class <code>HtmlSpan</code> should be
 * used
 * </p>
 */
public class HtmlTagText extends XmlText {
    public HtmlTagText() {
        super();
    }

    public HtmlTagText(final String content) {
        super(content);
    }
}
