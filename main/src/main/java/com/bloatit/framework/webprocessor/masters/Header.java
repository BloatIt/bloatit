//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework.webprocessor.masters;

import java.util.Set;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlScript;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.web.WebConfiguration;

public final class Header extends HtmlElement {
    /**
     * Indicates the various possible elements for robot
     */
    public enum Robot {
        // @formatter:off
        NO_INDEX("noindex"), NO_FOLLOW("nofollow"), INDEX("index"), FOLLOW("follow"), ALL("all"),
        NONE("none"), NO_SNIPPET("nosnippet"), NO_ARCHIVE("noarchive"), NO_ODP("noodp");
        // @formatter:on

        private String robot;

        private Robot(final String robot) {
            this.robot = robot;
        }

        @Override
        public String toString() {
            return robot;
        }
    }

    private final PlaceHolderElement cssPh;
    private final PlaceHolderElement jsPh;
    private final PlaceHolderElement linkPh;
    private final PlaceHolderElement metaPh;

    protected Header(final String title, final String description, final String keywords, final Set<Robot> robots) {
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
        metaKeywords.addAttribute("name", "keywords");
        metaKeywords.addAttribute("content", keywords);
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
            final StringBuilder content = new StringBuilder();
            for (final Robot robot : robots) {
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

        // Place for custom meta from page
        metaPh = new PlaceHolderElement();
        add(metaPh);

        // Place for custom links from page
        linkPh = new PlaceHolderElement();
        add(linkPh);

        // Place for custom page CSS if needed
        cssPh = new PlaceHolderElement();
        add(cssPh);

        // Place for custom page Javascript, if needed
        jsPh = new PlaceHolderElement();
        add(jsPh);

        // Javascript to handle libravatar
        final String liburi = FrameworkConfiguration.getLibravatarURI();
        final HtmlScript js = new HtmlScript();
        js.append("$(document).ready(function(){");
        js.append("$(\".libravatar\").each(function() {");
        js.append("var digest = $(this).attr(\"libravatar\");");
        js.append("var uri = \"" + liburi + "\" + digest + \"?s=64&d=http://elveos.org/resources/commons/img/none.png\";");
        js.append("$(this).attr(\"src\", uri);");
        js.append("});");
        js.append("});");
        add(js);

        String googleAnalyticId = FrameworkConfiguration.getGoogleAnalyticId();
        if (googleAnalyticId != null) {
            final HtmlScript gaJs = new HtmlScript();
            gaJs.append("var _gaq = _gaq || [];\n");
            gaJs.append("_gaq.push(['_setAccount', '" + googleAnalyticId + "']);\n");
            gaJs.append("_gaq.push(['_trackPageview']);\n");
            gaJs.append("(function() {\n");
            gaJs.append("  var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;\n");
            gaJs.append("  ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';\n");
            gaJs.append("  var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);\n");
            gaJs.append("})();");
            add(gaJs);
        }

        // Page title
        add(new HtmlGenericElement("title").addText(title));
    }

    /**
     * Adds a new css link to the page
     * 
     * @param css the string describing the name of the css
     */
    protected void addCss(final String css) {
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
    protected void addJs(final String js) {
        final HtmlElement jsLink = new HtmlGenericElement("script");
        jsLink.addAttribute("type", "text/javascript");
        jsLink.addAttribute("src", js);

        jsPh.add(jsLink);
    }

    public void addMeta(HtmlElement meta) {
        metaPh.add(meta);
    }

    protected void addHeaderLink(HtmlHeaderLink link) {
        final HtmlElement headerLink = new HtmlGenericElement("link") {
            @Override
            public boolean selfClosable() {
                return true;
            }
        };
        headerLink.addAttribute("href", link.getHref());
        headerLink.addAttribute("type", link.getType());
        headerLink.addAttribute("rel", link.getRel());
        headerLink.addAttribute("title", link.getTitle());
        linkPh.add(headerLink);
    }

    @Override
    public boolean selfClosable() {
        return false;
    }
}
