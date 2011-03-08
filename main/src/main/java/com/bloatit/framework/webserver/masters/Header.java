package com.bloatit.framework.webserver.masters;

import java.util.List;

import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlElement;

public final class Header extends HtmlElement {
    private static final String DESIGN = "/resources/css/core.css";

    public Header(final String title, final String customCss, final List<String> customJs) {
        super("head");

        final HtmlBranch meta = new HtmlGenericElement("meta").addAttribute("charset", "UTF-8");

        final HtmlBranch link = new HtmlGenericElement("link").addAttribute("rel", "stylesheet")
                                                              .addAttribute("href", DESIGN)
                                                              .addAttribute("type", "text/css")
                                                              .addAttribute("media", "handheld, all");

        add(meta);
        add(link);

        if (customCss != null && !customCss.equals("")) {
            final HtmlElement customCssLink = new HtmlGenericElement("link").addAttribute("rel", "stylesheet");
            customCssLink.addAttribute("href", "/resources/css/" + customCss);
            customCssLink.addAttribute("type", "text/css");
            customCssLink.addAttribute("media", "handheld, all");
            add(customCssLink);
        }

        if (customJs != null) {
            for (String js : customJs) {
                final HtmlElement customJsLink = new HtmlGenericElement("script");
                customJsLink.addAttribute("type", "text/javascript");
                if (js.startsWith("http://") || js.startsWith("https://")) {
                    customJsLink.addAttribute("src", js);
                } else {
                    customJsLink.addAttribute("src", "/resources/js/" + js);
                }

                add(customJsLink);
            }
        }

        add(new HtmlGenericElement("title").addText(title));
    }

    @Override
    public boolean selfClosable() {
        return false;
    }

}
