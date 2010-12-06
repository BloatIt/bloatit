package test;

import java.util.Iterator;

import javax.management.RuntimeErrorException;

public class HtmlText extends HtmlNode {

    private String content;

    public HtmlText(String content) {
        super();
        this.content = content;
    }

    @Override
    public Iterator<HtmlNode> iterator() {
        return new HtmlNullIterator();
    }

    @Override
    public void write(Text txt) {
        txt.writeLine(content);
    }

    public static class HtmlNullIterator implements Iterator<HtmlNode> {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public HtmlNode next() {
            return null;
        }

        @Override
        public void remove() {
            throw new RuntimeErrorException(null, "Remove impossible on empty collection");
        }
    }

}
