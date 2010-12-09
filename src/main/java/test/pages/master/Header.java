package test.pages.master;

import test.HtmlElement;

public class Header extends HtmlElement{
    private static final String DESIGN = "/resources/css/core.css";

    public Header(String title, String customCss) {
        super("head");
        HtmlElement meta = new HtmlElement("meta").addAttribute("http-equiv", "content-type").addAttribute("content", "text/html;charset=utf-8");

        HtmlElement link = new HtmlElement("link").addAttribute("rel", "stylesheet")
                                                  .addAttribute("href", DESIGN)
                                                  .addAttribute("type", "text/css")
                                                  .addAttribute("media", "handheld, all");

        add(meta);
        add(link);

        if (customCss != null && !customCss.equals("")) {
            HtmlElement customlink = new HtmlElement("link").addAttribute("rel", "stylesheet")
                                                            .addAttribute("href", "/resources/css/" + customCss)
                                                            .addAttribute("type", "text/css")
                                                            .addAttribute("media", "handheld, all");
            add(customlink);
        }

        add(new HtmlElement("title").addText("Bloatit - " + title));
    }

}
