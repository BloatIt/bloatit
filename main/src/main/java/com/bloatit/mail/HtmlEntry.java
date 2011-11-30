package com.bloatit.mail;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;

public class HtmlEntry extends HtmlDiv {
    public HtmlEntry(Date when, HtmlImage logo, String content) {
        this(when, logo, new HtmlText(content));
    }

    public HtmlEntry(Date when, HtmlImage logo, HtmlNode content) {
        super("event-entry");
        add(new HtmlDiv("event-entry-logo").add(logo));
        add(new HtmlDiv("event-entry-content").add(content));
        add(new HtmlDiv("date").addText(new SimpleDateFormat("HH:mm").format(when).toString()));
    }
}