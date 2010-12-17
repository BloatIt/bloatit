package com.bloatit.web.html;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.bloatit.web.server.Text;

/**
 * Class used to describe all elements used to render Html
 */
public abstract class HtmlNode implements Iterable<HtmlNode> {

    public static class Tag {

        private final String tag;
        private final Map<String, String> attributes = new HashMap<String, String>();

        public Tag(final String tag) {
            super();
            this.tag = tag;
        }

        public Tag addAttribute(final String name, final String value) {
            if (value != null && name != null && !value.isEmpty() && !name.isEmpty()) {
                attributes.put(name, value);
            }
            return this;
        }

        public Tag addId(final String value) {
            if (!value.equals("")) {
                attributes.put("id", value);
            }
            return this;
        }

        /**
         * Finds the id of the tag
         * 
         * @return the value entetered for the field "id" or null if no field
         *         id exists
         */
        public String getId() {
            return this.attributes.get("id");
        }

        public String getOpenTag() {
            return createOpenTagButWithoutLastChar().append(">").toString();
        }

        public String getClosedTag() {
            return createOpenTagButWithoutLastChar().append("/>").toString();
        }

        public String getCloseTag() {
            return "</" + tag + ">";
        }

        private StringBuilder createOpenTagButWithoutLastChar() {
            final StringBuilder openTag = new StringBuilder();
            openTag.append("<");
            openTag.append(tag);
            for (final Entry<String, String> att : attributes.entrySet()) {
                openTag.append(" ");
                openTag.append(att.getKey()).append("=").append("\"").append(att.getValue()).append("\"");
            }
            return openTag;
        }
    }

    protected abstract void write(Text txt);
}
