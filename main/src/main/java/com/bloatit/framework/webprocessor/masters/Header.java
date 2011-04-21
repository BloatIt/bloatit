package com.bloatit.framework.webprocessor.masters;

import java.util.Set;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlScript;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.ModelConfiguration;
import com.bloatit.web.WebConfiguration;

public final class Header extends HtmlElement {
    /**
     * Indicates the various possible elements for robot
     */
    public enum Robot {
        //  @formatter:off
        NO_INDEX("noindex"), NO_FOLLOW("nofollow"), INDEX("index"), FOLLOW("follow"), ALL("all"),
        NONE("none"), NO_SNIPPET("nosnippet"), NO_ARCHIVE("noarchive"), NO_ODP("noodp");
        // @formatter:on

        private String robot;

        private Robot(String robot) {
            this.robot = robot;
        }

        @Override
        public String toString() {
            return robot;
        }
    }

    private final PlaceHolderElement cssPh;
    private final PlaceHolderElement jsPh;

    public Header(final String title, String description, Set<Robot> robots) {
        super("head");

        // Additiong of charset
        final HtmlBranch metaCharset = new HtmlGenericElement("meta") {
            @Override
            public boolean selfClosable() {
                return true;
            }
        };
        metaCharset.addAttribute("charset", "UTF-8");
        add(metaCharset);

        // Addition of keywords
        final HtmlBranch metaKeywords = new HtmlGenericElement("meta") {
            @Override
            public boolean selfClosable() {
                return true;
            }
        };
        metaKeywords.addAttribute("keywords", Context.tr("free software funding, open-source, bulk purchases"));
        add(metaKeywords);

        // Addition of page description
        final HtmlBranch metaDescription = new HtmlGenericElement("meta") {
            @Override
            public boolean selfClosable() {
                return true;
            }
        };
        metaDescription.addAttribute("name", "description");
        metaDescription.addAttribute("content", description);
        add(metaDescription);

        // Robot markup
        if (robots != null && robots.size() > 0) {
            final HtmlBranch metaRobots = new HtmlGenericElement("meta") {
                @Override
                public boolean selfClosable() {
                    return true;
                }
            };
            metaRobots.addAttribute("name", "robots");
            StringBuilder content = new StringBuilder();
            for (Robot robot : robots) {
                if (content.length() > 0) {
                    content.append(',');
                }
                content.append(robot);
            }
            metaRobots.addAttribute("content", content.toString());
            add(metaRobots);
        }

        final HtmlBranch favicon = new HtmlGenericElement("link") {
            @Override
            public boolean selfClosable() {
                return true;
            }
        };
        favicon.addAttribute("rel", "icon");
        favicon.addAttribute("href", FrameworkConfiguration.getImgFavicon());
        favicon.addAttribute("type", "image/png");
        add(favicon);

        // Adds link to website CSS css
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
        add(link);

        // Place for custome page CSS if needed
        cssPh = new PlaceHolderElement();
        add(cssPh);

        // Place for custome page Javascript, if needed
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

        // Page title
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
