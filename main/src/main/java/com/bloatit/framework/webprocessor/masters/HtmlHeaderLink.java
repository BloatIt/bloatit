package com.bloatit.framework.webprocessor.masters;

public class HtmlHeaderLink {
    private final String href;
    private final String type;
    private final String rel;
    private final String title;

    public HtmlHeaderLink(String href, String type, String rel, String title) {
        super();
        this.href = href;
        this.type = type;
        this.rel = rel;
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public String getType() {
        return type;
    }

    public String getRel() {
        return rel;
    }

    public String getTitle() {
        return title;
    }
}
