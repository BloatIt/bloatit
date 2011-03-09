package com.bloatit.framework.webserver.components.meta;

/**
 * <p>
 * HtmlMixedText are used to display a text with embed html element.
 * </p>
 * <p>
 * The main use is to insert link in a translated text flow. The text must
 * contains tag like that:
 * </p>
 * <code>Hello, you can <0:follow this link:comment>.</code>
 * <p>
 * In this exemple <0:follow this link> will be replaced by the parameters at
 * the index 0. The second field is is a comment, the text . If a third text
 * will be add to the node.
 * </p>
 */
public class HtmlMixedText extends HtmlBranch {

    public HtmlMixedText(final String content, final HtmlBranch... parameters) {

        String[] split = content.split("<[0-9]+(:[^>]+)*>");

        int index = 0;

        for (String string : split) {
            add(new XmlText(string));
            index += string.length();

            // If it not the end, replace a tag
            int startIndex = content.indexOf("<", index);
            if (startIndex != -1) {
                int endIndex = content.indexOf(">", index);

                parseTag(content.substring(startIndex + 1, endIndex), parameters);

                index = endIndex + 1;
            }
        }

    }

    public void parseTag(final String tag, final HtmlBranch parameters[]) {
        String[] split = tag.split(":");

        if (split.length < 1) {
            add(new XmlText(" ### Invalid tag '" + tag + "'. Not enough content ### "));
            return;
        }

        int paramIndex = Integer.valueOf(split[0]);
        // Check out of bound
        if (paramIndex >= parameters.length) {
            add(new XmlText(" ### Invalid index '" + paramIndex + "' in tag '" + tag + "'. Param length: " + parameters.length + " ### "));
            return;
        }

        HtmlBranch node = (HtmlBranch) parameters[paramIndex].clone();

        if (split.length >= 3) {
            node.addText(split[2]);
        }

        add(node);

    }

}
