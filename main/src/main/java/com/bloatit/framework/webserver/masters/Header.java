package com.bloatit.framework.webserver.masters;

import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.web.WebConfiguration;

public final class Header extends HtmlElement {
    private final PlaceHolderElement cssPh;
    private final PlaceHolderElement jsPh;

    public Header(final String title) {
        super("head");

        final HtmlBranch meta = new HtmlGenericElement("meta") {
            @Override
            public boolean selfClosable() {
                return true;
            }
        };
        meta.addAttribute("charset", "UTF-8");

        final HtmlBranch link = new HtmlGenericElement("link") {

            @Override
            public boolean selfClosable() {
                return true;
            }

        };
        link.addAttribute("rel", "stylesheet");
        link.addAttribute("href", WebConfiguration.getCss());
        link.addAttribute("type", "text/css");
        link.addAttribute("media", "handheld, all");

        add(meta);
        add(link);

        cssPh = new PlaceHolderElement();
        add(cssPh);

        jsPh = new PlaceHolderElement();
        add(jsPh);

        add(new HtmlGenericElement("title").addText(title));
    }

    /**
     * Adds a new css link to the page
     * 
     * @param css the string describing the name of the css
     */
    public void addCss(String css) {
        final HtmlElement cssLink = new HtmlGenericElement("link") {
            @Override
            public boolean selfClosable() {
                return true;
            }
        }.addAttribute("rel", "stylesheet");

        cssLink.addAttribute("href", "/resources/css/" + css);
        cssLink.addAttribute("type", "text/css");
        cssLink.addAttribute("media", "handheld, all");
        cssPh.add(cssLink);
    }

    /**
     * Adds a new javascript link to the page
     * <p>
     * The string describing the javascript link can be any following format :
     * <li>Relative URI to the application (myScript.js), it will be formatted
     * with a valid relative path to the web server</li>
     * <li>Absolute URI (http://host.com/script.js), and will be left as is.
     * Absolute URI MUST start with http:// or https://</li>
     * </p>
     * 
     * @param js a string describing the URI of the js link, either relative to
     *            the application or absolute (and starting with http://)
     */
    public void addJs(String js) {
        final HtmlElement jsLink = new HtmlGenericElement("script");
        jsLink.addAttribute("type", "text/javascript");
        jsLink.addAttribute("src", js);

        jsPh.add(jsLink);
    }

    @Override
    public boolean selfClosable() {
        return false;
    }

}
