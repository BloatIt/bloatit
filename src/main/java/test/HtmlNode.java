package test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.bloatit.common.Image;
import com.bloatit.common.Image.ImageType;

import test.htmlComponents.HtmlBlock;
import test.htmlComponents.HtmlButton;
import test.htmlComponents.HtmlDateField;
import test.htmlComponents.HtmlForm;
import test.htmlComponents.HtmlImage;
import test.htmlComponents.HtmlInput;
import test.htmlComponents.HtmlLinkComponent;

public abstract class HtmlNode implements Iterable<HtmlNode> {
    public static class Tag {
        private String tag;
        private Map<String, String> attributes = new HashMap<String, String>();

        public Tag(String tag) {
            super();
            this.tag = tag;
        }

        public Tag addAttribute(String name, String value) {
            if (value != "" && name != "") {
                attributes.put(name, value);
            }
            return this;
        }

        public String getOpenTag() {
            return createOpenTagButWithoutLastChar().append(">").toString();
        }

        public String getClosedTag() {
            return createOpenTagButWithoutLastChar().append("/>").toString();
        }

        public String getCloseTag() {
            return "</" + tag + ">";
        }

        private StringBuilder createOpenTagButWithoutLastChar() {
            StringBuilder openTag = new StringBuilder();
            openTag.append("<");
            openTag.append(tag);
            for (Entry<String, String> att : attributes.entrySet()) {
                openTag.append(" ");
                openTag.append(att.getKey()).append("=").append("\"").append(att.getValue()).append("\"");
            }
            return openTag;
        }
    }

    protected abstract void write(Text txt);
    
    public static void main(String[] args) {
        IndentedHtmlText plop = new IndentedHtmlText() {
            @Override
            protected void append(String text) {
                System.out.print(text);
            }};
        new HtmlBlock().write(plop);
        
        new HtmlBlock("cssClass").add(new HtmlButton("button"))
                                 .add(new HtmlDateField(new Date(), "date !"))
                                 .add(new HtmlImage(new Image("plop", ImageType.LOCAL)))
                                 .add(new HtmlInput("text"))
                                 .add(new HtmlLinkComponent("link", "other"))
                                 .write(plop);
        
        
        
    }
}
