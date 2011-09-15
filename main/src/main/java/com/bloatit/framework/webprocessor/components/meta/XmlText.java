package com.bloatit.framework.webprocessor.components.meta;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;

import org.springframework.web.util.HtmlUtils;

public class XmlText extends XmlNode {

    private final String content;

    public XmlText(String content) {
        super(null);
        this.content = HtmlUtils.htmlEscape(content);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<XmlNode> iterator() {
        return Collections.EMPTY_LIST.iterator();
    }
    
    @Override
    public final void write(OutputStream out) throws IOException {
        out.write(content.getBytes());
    }

}
