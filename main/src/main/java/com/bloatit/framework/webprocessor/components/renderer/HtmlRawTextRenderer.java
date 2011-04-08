package com.bloatit.framework.webprocessor.components.renderer;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import com.bloatit.web.HtmlTools;

/**
 * <p>
 * Renders a user text the same way he entered it
 * </p>
 */
public final class HtmlRawTextRenderer extends HtmlTextRenderer {

    public HtmlRawTextRenderer(final String text) {
        super(text);
    }

    @Override
    protected String doRender(final String text) {
        final String t = HtmlTools.escape(text);
        final StringBuilder sb = new StringBuilder();
        final StringCharacterIterator it = new StringCharacterIterator(t);

        char previous = Character.MIN_VALUE;
        for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
            if (ch == ' ' && previous == ' ') {
                sb.append("&nbsp;");
            } else if (ch == '\n') {
                sb.append("<br />\n");
            } else {
                sb.append(ch);
            }
            previous = ch;
        }
        return sb.toString();
    }
}
