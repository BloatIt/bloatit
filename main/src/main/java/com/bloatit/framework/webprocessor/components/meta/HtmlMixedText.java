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
package com.bloatit.framework.webprocessor.components.meta;

/**
 * <p>
 * HtmlMixedText are used to display a text with embed html element.
 * </p>
 * <p>
 * The main use is to insert link in a translated text flow. The text must
 * contains tag like that:
 * </p>
 * <pre>Hello, you can <0:follow this link:comment>.</pre>
 * <p>
 * In this exemple <0:follow this link> will be replaced by the parameters at
 * the index 0. The second field is is a comment, the text . If a third text
 * will be add to the node.
 * </p>
 */
public class HtmlMixedText extends HtmlBranch {

    public HtmlMixedText(String content, final HtmlElement... parameters) {
        content = " " + content; // Handle the cast where it starts by a tag
        final String[] split = content.split("<[0-9]+(:[^>]+)*>");
        int index = 0;

        for (final String string : split) {
            if (index == 0) {
                add(new XmlText(string.substring(1, string.length())));
            } else {
                add(new XmlText(string));
            }
            index += string.length();

            // If it not the end, replace a tag
            final int startIndex = content.indexOf("<", index);
            if (startIndex != -1) {
                final int endIndex = content.indexOf(">", index);
                parseTag(content.substring(startIndex + 1, endIndex), parameters);
                index = endIndex + 1;
            }
        }
    }

    private void parseTag(final String tag, final HtmlElement parameters[]) {
        final String[] split = tag.split(":");

        if (split.length < 1) {
            add(new XmlText(" ### Invalid tag '" + tag + "'. Not enough content ### "));
            return;
        }

        final int paramIndex = Integer.valueOf(split[0]);
        // Check out of bound
        if (paramIndex >= parameters.length) {
            add(new XmlText(" ### Invalid index '" + paramIndex + "' in tag '" + tag + "'. Param length: " + parameters.length + " ### "));
            return;
        }



        final HtmlElement node = parameters[paramIndex];

        if (split.length >= 3 && node instanceof HtmlBranch) {
            ((HtmlBranch) node).addText(split[2]);
        }

        add(node);
    }
}
