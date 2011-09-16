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
package com.bloatit.framework.webprocessor.components.renderer;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.web.HtmlTools;

/**
 * <p>
 * Renders a user text the same way he entered it
 * </p>
 */
public final class HtmlRawTextRenderer extends HtmlText {

    public HtmlRawTextRenderer(final String text) {
        super(doRender(text), false);
    }

    private static String doRender(final String text) {
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
