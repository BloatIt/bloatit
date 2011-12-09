package com.bloatit.mail;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;

public class HtmlEntry extends HtmlDiv {
    private final Date when;

    public HtmlEntry(final Date when, final HtmlImage logo, final String content) {
        this(when, logo, new HtmlText(content));
    }

    public HtmlEntry(final Date when, final HtmlImage logo, final HtmlNode content) {
        super("event-entry");
        this.when = when;
        add(new HtmlDiv("event-entry-logo").add(logo));
        add(new HtmlDiv("event-entry-content").add(content));
        add(new HtmlDiv("date").addText(new SimpleDateFormat("HH:mm").format(when).toString()));
    }

    public Date getDate() {
        return when;
    }
}
