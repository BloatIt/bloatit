package com.bloatit.framework.webprocessor.components.meta;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.lang.StringEscapeUtils;

public class XmlText extends XmlNode {

    private final String content;

    public XmlText(String content) {
        super(null);
        this.content = StringEscapeUtils.escapeXml(content);
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
