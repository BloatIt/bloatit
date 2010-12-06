package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HtmlElement extends HtmlNode {

    private List<HtmlNode> children = new ArrayList<HtmlNode>();
    private Tag tag;

    public HtmlElement(String tag) {
        super();
        this.tag = new Tag(tag);
    }

    public HtmlElement addAttribute(String name, String value) {
        tag.addAttribute(name, value);
        return this;
    }

    public HtmlElement add(HtmlNode html) {
        children.add(html);
        return this;
    }
    
    public HtmlElement addText(String text) {
        children.add(new HtmlText(text));
        return this;
    }

    @Override
    public Iterator<HtmlNode> iterator() {
        return children.iterator();
    }

    @Override
    public void write(Text txt) {
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
    }

}
