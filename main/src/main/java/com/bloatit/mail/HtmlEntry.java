package com.bloatit.mail;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;

public class HtmlEntry {
    private final Date when;
    private final HtmlImage logo;
    private HtmlNode content;

    public HtmlEntry(final Date when, final HtmlImage logo, final String content) {
        this(when, logo, new HtmlText(content));
        
    }

    public HtmlEntry(Date when, HtmlImage logo, HtmlNode content) {
        this.when = when;
        this.logo = logo;
        this.content = content;
    }
    
    public HtmlElement generateForWebSite() {
        HtmlDiv div = new HtmlDiv("event-entry");
        div.add(new HtmlDiv("event-entry-logo").add(logo));
        div.add(new HtmlDiv("event-entry-content").add(content));
        div.add(new HtmlDiv("date").addText(new SimpleDateFormat("HH:mm").format(when).toString()));
        return div;
    }
    
    public Date getDate() {
        return when;
    }
}
