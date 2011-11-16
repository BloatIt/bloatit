package com.bloatit.framework.webprocessor.masters;

public class HtmlHeaderLink {
    private final String href;
    private final String rel;
    private String type;
    private String title;
    
    public HtmlHeaderLink(String href, String rel) {
        super();
        this.href = href;
        this.rel = rel;
    }

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
