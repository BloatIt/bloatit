package test.html;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import test.Text;

/**
 * Class used to describe all preconstructed HtmlElements
 */
public abstract class HtmlElement extends HtmlNode {

    private List<HtmlNode> children = new ArrayList<HtmlNode>();
    private Tag tag;

    public HtmlElement(String tag) {
        super();
        this.tag = new Tag(tag);
    }

    public HtmlElement() {
        super();
        this.tag = null;
    }

    public HtmlElement addAttribute(String name, String value) {
        tag.addAttribute(name, value);
        return this;
    }

    protected HtmlElement add(HtmlNode html) {
        children.add(html);
        return this;
    }

    protected HtmlElement addText(String text) {
        children.add(new HtmlText(text));
        return this;
    }

    /**
     * <p>Sets the id of the html element :
     * <pre><element id="..." /></pre></p>
     * <p>Shortcut to element.addAttribute("id",value)</p>
     * @param id the value of the id
     * @return the element
     */
    public HtmlElement setId(String id) {
        addAttribute("id", id);
        return this;
    }

    /**
     * Finds the id of the element
     * <pre>
     * <element id="value" />
     * </pre>
     * @return The value contained in the attribute id of the element
     */
    public String getId(){
        return this.tag.getId();
    }

    /**
     * Sets the css class of the element
     * <p>Shortcut for element.addattribute("class",cssClass)</p>
     * @param cssClass
     * @return
     */
    public HtmlElement setClass(String cssClass) {
        addAttribute("class", cssClass);
        return this;
    }

    @Override
    public Iterator<HtmlNode> iterator() {
        return children.iterator();
    }

    @Override
    public final void write(Text txt) {
        if (tag != null) {
            if (this.iterator().hasNext()) {
                txt.writeLine(tag.getOpenTag());
                for (HtmlNode html : this) {
                    txt.indent();
                    html.write(txt);
                    txt.unindent();
                }
                txt.writeLine(tag.getCloseTag());
            } else {
                txt.writeLine(tag.getClosedTag());
            }
        } else {
            for (HtmlNode html : this) {
                txt.indent();
                html.write(txt);
                txt.unindent();
            }
        }
    }

}
