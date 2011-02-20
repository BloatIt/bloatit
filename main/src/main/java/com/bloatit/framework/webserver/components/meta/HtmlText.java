package com.bloatit.framework.webserver.components.meta;

import com.bloatit.web.HtmlTools;

/**
 * <p>
 * Class used to directly escaped text into a page
 * </p>
 * <p>
 * Usage :
 * 
 * <pre>
 * {@code another_component.add(new HtmlText("A lot of beautiful text here"));}
 * </pre>
 * 
 * </p>
 * <p>
 * All the text inputed into an HtmlText will be escaped using the
 * <code>HtmlTools.escape(text)</code> method. It is therefore safe to display using this
 * method, but cannot be used to display an html tag.<br />
 * Html Tags should be displayed using {@link HtmlTagText}
 * </p>
 */
public class HtmlText extends HtmlTagText {

    /**
     * Creates a component to add text to a page
     * 
     * @param text the Html text to add to add
     */
    public HtmlText(final String text) {
        super(HtmlTools.escape(text));
    }
}
