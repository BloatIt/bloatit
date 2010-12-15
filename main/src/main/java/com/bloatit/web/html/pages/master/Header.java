package com.bloatit.web.html.pages.master;

import com.bloatit.web.html.HtmlBranch;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.standard.HtmlGenericElement;

public class Header extends HtmlElement {
    private static final String DESIGN = "/resources/css/core.css";

    public Header(final String title, final String customCss) {
        super("head");
        final HtmlBranch meta = new HtmlGenericElement("meta").addAttribute("http-equiv", "content-type").addAttribute("content",
                "text/html;charset=utf-8");

        final HtmlBranch link = new HtmlGenericElement("link").addAttribute("rel", "stylesheet").addAttribute("href", DESIGN)
                .addAttribute("type", "text/css").addAttribute("media", "handheld, all");

        add(meta);
        add(link);

        if (customCss != null && !customCss.equals("")) {
            final HtmlElement customlink = new HtmlGenericElement("link").addAttribute("rel", "stylesheet")
                    .addAttribute("href", "/resources/css/" + customCss).addAttribute("type", "text/css").addAttribute("media", "handheld, all");
            add(customlink);
        }

        add(new HtmlGenericElement("title").addText("Bloatit - " + title));
    }

    @Override
    public boolean selfClosable() {
        return false;
    }

}
