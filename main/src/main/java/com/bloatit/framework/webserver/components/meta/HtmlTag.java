package com.bloatit.framework.webserver.components.meta;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * An internal class used to represent an html tag.
 * </p>
 * <p>
 * <code>HtmlTag</code> is no more than a facilitator used to convert simple strings into tags
 * by adding markers such as <code>'<'</code> or <code>'>'</code> or <code>'/>'</code>
 * </p>
 */
public class HtmlTag {

    private final String tag;
    private final Map<String, String> attributes = new HashMap<String, String>();

    /**
     * <p>
     * Creates a new Tag
     * </p>
     *
     * @param tag the string representation of the tag (img to create a new
     *            {@code <img>} tag)
     */
    protected HtmlTag(final String tag) {
        super();
        this.tag = tag;
    }

    /**
     * <p>
     * Adds an attribute to a tag
     * </p>
     * <p>
     * Example :
     *
     * <pre>
     * HtmlTag img = new HtmlTag(&quot;img&quot;);
     * img.addAttribute(&quot;src&quot;, &quot;example.com/example.png&quot;);
     * </pre>
     *
     * will later be rendered as {@code <img src="example.com/example.png" />}
     * </p>
     *
     * @param name The name of the attribute
     * @param value the value of the attribute
     * @return itself
     */
    protected HtmlTag addAttribute(final String name, final String value) {
        if (value != null && name != null && !name.isEmpty()) {
            attributes.put(name, value);
        }
        return this;
    }

    /**
     * <p>
     * A convenience method to add an <code>id</code> attribute to the tag.
     * </p>
     *
     * @param value the id of the tag
     * @return itself
     */
    protected HtmlTag addId(final String value) {
        if (!value.equals("")) {
            attributes.put("id", value);
        }
        return this;
    }

    /**
     * Finds the id of the tag
     *
     * @return the value entetered for the field "id" or null if no field id
     *         exists
     */
    protected String getId() {
        return this.attributes.get("id");
    }

    /**
     * <p>
     * Gets the <code>String</code> used to represent an opening tag.
     * </p>
     * <p>
     * For example :
     *
     * <pre>
     * HtmlTag img = new HtmlTag(&quot;img&quot;);
     * img.addAttribute(&quot;src&quot;, &quot;example.com/example.png&quot;);
     * System.out.println(img.getOpenTag()):
     * </pre>
     *
     * will display {@code  <img src="example.com/example.png" >} (which is not a
     * valid Html element by the way)
     * </p>
     *
     * @return the <code>String</code> representing an opening tag
     */
    protected String getOpenTag() {
        return createOpenTagButWithoutLastChar().append(">").toString();
    }

    /**
     * <p>
     * Gets the <code>String</code> used to represent a self closing tag.
     * </p>
     * <p>
     * For example :
     *
     * <pre>
     * HtmlTag img = new HtmlTag(&quot;img&quot;);
     * img.addAttribute(&quot;src&quot;, &quot;example.com/example.png&quot;);
     * System.out.println(img.getClosedTag()):
     * </pre>
     *
     * will display {@code  <img src="example.com/example.png" />}
     * </p>
     *
     * @return the <code>String</code> representing a self closing tag
     */
    protected String getSelfClosingTag() {
        return createOpenTagButWithoutLastChar().append("/>").toString();
    }

    /**
     * <p>
     * Gets the <code>String</code> used to represent a closing tag.
     * </p>
     * <p>
     * For example :
     *
     * <pre>
     * HtmlTag img = new HtmlTag(&quot;img&quot;);
     * img.addAttribute(&quot;src&quot;, &quot;example.com/example.png&quot;);
     * System.out.println(img.getClosedTag()):
     * </pre>
     *
     * will display {@code  </img>} (which is not a valid Html element by the
     * way)
     * </p>
     * <p>
     * <b>NOTE</b> : this method should obviously be used after
     * {@link #getOpenTag()}
     * </p>
     *
     * @return the <code>String</code> representing an opening tag
     */
    protected String getCloseTag() {
        return "</" + tag + ">";
    }

    /**
     * <p>
     * Creates an opening tag ({@code <tag ...}) with all its attributes, but
     * without the closing mark. This is the base method to create an opening
     * tag, which should be called for self-closing tag (which will add
     * {@code />}) and normal open tags (which will add {@code >}).
     *
     * @return the String representing the opening tag, with all its attributes
     *         but not the closing marker
     */
    private StringBuilder createOpenTagButWithoutLastChar() {
        final StringBuilder openTag = new StringBuilder();
        openTag.append("<");
        openTag.append(tag);
        for (final Entry<String, String> att : attributes.entrySet()) {
            openTag.append(" ");
            openTag.append(att.getKey()).append("=").append("\"").append(att.getValue()).append("\"");
        }
        return openTag;
    }
}
