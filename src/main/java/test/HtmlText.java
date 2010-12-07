package test;

import java.util.Collections;
import java.util.Iterator;

public class HtmlText extends HtmlNode {

    private String content;

    public HtmlText(String content) {
        super();
        this.content = content;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<HtmlNode> iterator() {
        return Collections.EMPTY_LIST.iterator();
    }

    @Override
    public void write(Text txt) {
        txt.writeLine(content);
    }

}
