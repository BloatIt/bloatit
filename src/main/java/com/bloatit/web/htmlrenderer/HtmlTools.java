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
     * 1 000 = 1k
     * 100 000 = 100k
     * 1 000 000 = 1M
     * 100 000 000 = 100M
     * 1 000 000 000 = 1T
     * 100 000 000 000 = 100T
     * 1 000 000 000 000 = ∞
     * 
     * @param karma the karma value to compress
     * @return the compressed String to display
     */
    public static String compressKarma(long karma) {
        Double abs_karma = new Double(Math.abs(karma));
        String result = "";
        if (abs_karma < 1000) {
            result = cutNumber(abs_karma.toString());
        } else if (abs_karma < 1000000d) {
            result = cutNumber(new Double(abs_karma / 1000d).toString()) + "k";// TODO why not 'K' ?
        } else if (abs_karma < 1000000000d) {
            result = cutNumber(new Double(abs_karma / 1000000d).toString()) + "M";

        } else if (abs_karma < 1000000000000d) {
            result = cutNumber(new Double(abs_karma / 1000000000d).toString()) + "T";
        } else {

            result = "∞";
        }
        if (karma >= 0) {
            return result;

        } else {
            return "-" + result;

        }
    }

    public static String cutNumber(String karma) {
        String result = karma;
        if (result.length() > 2) {
            if (result.charAt(1) == '.') {
                if (result.charAt(2) == '0') {
                    result = result.substring(0, 1);
                } else {
                    result = result.substring(0, 3);
                }
            } else if (result.charAt(2) == '.') {
                result = result.substring(0, 2);
            } else {

                result = result.substring(0, 3);
            }
        }
        return result;
    }
}
