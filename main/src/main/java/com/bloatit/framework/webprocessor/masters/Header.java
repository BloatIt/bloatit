package com.bloatit.framework.webprocessor.masters;

import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlScript;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.ModelConfiguration;
import com.bloatit.web.WebConfiguration;

public final class Header extends HtmlElement {
    private final PlaceHolderElement cssPh;
    private final PlaceHolderElement jsPh;

    public Header(final String title, String description) {
        super("head");

        // Additiong of charset
        final HtmlBranch metaCharset = new HtmlGenericElement("meta") {
            @Override
            public boolean selfClosable() {
                return true;
            }
        };
        metaCharset.addAttribute("charset", "UTF-8");

        // Addition of keywords
        final HtmlBranch metaKeywords = new HtmlGenericElement("meta") {
            @Override
            public boolean selfClosable() {
                return true;
            }
        };
        metaKeywords.addAttribute("keywords", Context.tr("free software funding, open-source, bulk purchases"));

        // Addition of page description
        final HtmlBranch metaDescription = new HtmlGenericElement("meta") {
            @Override
            public boolean selfClosable() {
                return true;
            }
        };
        metaDescription.addAttribute("name", "description");
        metaDescription.addAttribute("content", description);

        // Adds link to css
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

        add(metaCharset);
        add(metaKeywords);
        add(link);

        cssPh = new PlaceHolderElement();
        add(cssPh);

        jsPh = new PlaceHolderElement();
        add(jsPh);

        // Javascript to handle libravatar
        String liburi = ModelConfiguration.getLibravatarURI();
        HtmlScript js = new HtmlScript();
        js.append("$(document).ready(function(){");
        js.append("$(\".libravatar\").each(function() {");
        js.append("var digest = $(this).attr(\"libravatar\");");
        js.append("var uri = \"" + liburi + "\" + digest + \"?s=64&d=http://elveos.org/resources/commons/img/none.png\";");
        js.append("$(this).attr(\"src\", uri);");
        js.append("});");
        js.append("});");
        add(js);

        add(new HtmlGenericElement("title").addText(title));
    }

    /**
     * Adds a new css link to the page
     * 
     * @param css the string describing the name of the css
     */
    public void addCss(final String css) {
        final HtmlElement cssLink = new HtmlGenericElement("link") {
            @Override
            public boolean selfClosable() {
                return true;
            }
        }.addAttribute("rel", "stylesheet");

        cssLink.addAttribute("href", css);
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
    public void addJs(final String js) {
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
