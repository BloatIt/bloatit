/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.html;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

import com.bloatit.web.server.Context;
import com.bloatit.web.server.Session;

public class HtmlTools {

    private static final double TRILLION = 1000000000000d;
	private static final double BILLION = 1000000000d;
	private static final double MILLION = 1000000d;
	private static final int THOUSAND = 1000;
	public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;

    /*
     * public static String generateLink(Session session, String text, Request
     * linkRequest) { return "<a href=\"" + linkRequest.getUrl() + "\">" + text + "</a>";
     * } public static String generateLink(Session session, String text, Request
     * linkRequest, Map<String, String> outputParams) { return "<a href=\"" +
     * linkRequest.getUrl(outputParams) + "\">" + text + "</a>"; }
     */

    public static HtmlTagText generateLogo() {
        return new HtmlTagText("<span class='logo_bloatit'><span class='logo_bloatit_bloat'>Bloat</span><span class='logo_bloatit_it'>It</span></span>");
    }

    /**
     * Compress karma and make it easier to display <br>
     * Example of results : 
     * 1 = 1 <br>
     * 100 = 100 <br>
     * 1000 = 1k <br>
     * 100 000 = 100k <br>
     * 1 000 000 = 1M <br>
     * 100 000 000 = 100M <br>
     * 1 000 000 000 = 1T <br>
     * 100 000 000 000 = 100T <br>
     * 1 000 000 000 000 = ∞<br>
     * 
     * @param karma the karma value to compress
     * @return the compressed String to display
     */
    public static String compressKarma(final long karma) {
        final Double abs_karma = new Double(Math.abs(karma));
        String result = "";
        if (abs_karma < THOUSAND) {
            result = cutNumber(abs_karma.toString());
        } else if (abs_karma < MILLION) {
            result = cutNumber(new Double(abs_karma / THOUSAND).toString()) + "K";
        } else if (abs_karma < BILLION) {
            result = cutNumber(new Double(abs_karma / MILLION).toString()) + "M";

        } else if (abs_karma < TRILLION) {
            result = cutNumber(new Double(abs_karma / BILLION).toString()) + "T";
        } else {
            result = "∞";
        }
        if (karma >= 0) {
            return result;
        } else {
            return "-" + result;
        }
    }

    public static String cutNumber(final String karma) {
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

    @Deprecated
    public static String formatDate(final Session session, final Date date) {
        final Date currentDate = new Date();

        final long diff = currentDate.getTime() - date.getTime();

        if (diff < SECOND) {
            return Context.tr("now");
        } else if (diff < MINUTE) {
            return Context.tr("%d seconds ago", new Object[] { new Long(diff / SECOND) });
        }

        final SimpleDateFormat dateFormat = new SimpleDateFormat(Context.tr("MMM d, ''yy 'at' HH:mm"));

        return dateFormat.format(date);
    }
    
    public static String escape(String str){
    	return StringEscapeUtils.escapeHtml(str);
    }
    
    public static String unescape(String str){
    	return StringEscapeUtils.unescapeHtml(str);
    }

}
