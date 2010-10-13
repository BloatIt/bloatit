/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.htmlrenderer;

import com.bloatit.web.server.Action;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;

public class HtmlTools {

    public static String generateLink(Session session, String displayedText, Page linkPage) {
        return "<a href=\"/" + session.getLanguage().getCode() + "/" + linkPage.getCode() + "\">" + displayedText + "</a>";
    }

    public static String generateActionLink(Session session, String text, Action linkAction) {
        return "<a href=\"/" + session.getLanguage().getCode() + "/action/" + linkAction.getCode() + "\">" + text + "</a>";
    }

    public static String generateUrl(Session session, Page urlPage) {
        return "/" + session.getLanguage().getCode() + "/" + urlPage.getCode();
    }

    public static String generateLogo() {
        return "<span class='logo_bloatit'><span class='logo_bloatit_bloat'>Bloat</span><span class='logo_bloatit_it'>It</span></span>";
    }

    public static String escapeUrlString(String str) {
        // TODO return (urllib.parse.quote_plus(str)).replace('+','_');
        return str;
    }

    public static String unescapeUrlString(String str) {
        // TODO return (urllib.parse.quote_plus(str)).replace('+','_');
        return str;
    }

    /**
     * Compress karma and make it easier to display
     *
     * Example of results :
     * 1 = 1
     * 100 = 100
     * 1 000 = 1.0k
     * 100 000 = 100.0k
     * 1 000 000 = 1.0M
     * 100 000 000 = 100.0M
     * 1 000 000 000 = 1.0T
     * 100 000 000 000 = 100.0T
     * 1 000 000 000 000 = ∞
     * @param karma the karma value to compress
     * @return the compressed String to display
     */
    public static String compressKarma(long karma) {
        String[] append = {"", "k", "M", "T"};
        String s = new Long(karma).toString();
        if (karma < 1000) {
            return s;
        }
        if (s.length() > 12) {
            return "∞";
        }
        int modL = (s.length() % 3 == 0) ? 3 : s.length() % 3;
        return s.substring(0, modL) + "." + s.substring(modL, modL + 1) + append[(int) (Math.floor((s.length() - 1) / 3))];
    }
}