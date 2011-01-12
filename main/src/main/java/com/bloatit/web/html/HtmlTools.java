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

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

import com.bloatit.web.html.components.standard.HtmlSpan;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.i18n.DateLocale;
import com.bloatit.web.utils.i18n.DateLocale.FormatStyle;

/**
 * Provides various tools to generate HtmlPages
 */
public class HtmlTools {

    private static final double TRILLION = 1000000000000d;
    private static final double BILLION = 1000000000d;
    private static final double MILLION = 1000000d;
    private static final int THOUSAND = 1000;
    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;

    /**
     * <p>
     * Creates an HtmlElement that can be directly used to display the website
     * logo
     * </p>
     * <p>
     * Logo should be placed into a container that will specify its font size
     * </p>
     * 
     * @return the HtmlElement to be placed into a page
     */
    public static HtmlElement generateLogo() {
        HtmlSpan logoBloatit = new HtmlSpan("logo_bloatit");

        // Rendering Bloat
        HtmlSpan logoBloatitBloat = new HtmlSpan("logo_bloatit_bloat");
        logoBloatitBloat.addText("Bloat");
        logoBloatit.add(logoBloatitBloat);

        // Rendering It
        HtmlSpan logoBloatitIt = new HtmlSpan("logo_bloatit_it");
        logoBloatitIt.addText("It");
        logoBloatit.add(logoBloatitIt);

        return logoBloatit;
    }

    /**
     * <p>
     * Compress karma and make it easier to display
     * </p>
     * <p>
     * Example of results :
     * <li>1 = 1</li>
     * <li>100 = 100</li>
     * <li>1000 = 1k</li>
     * <li>100 000 = 100k</li>
     * <li>1 000 000 = 1M</li>
     * <li>100 000 000 = 100M</li>
     * <li>1 000 000 000 = 1T</li>
     * <li>100 000 000 000 = 100T</li>
     * <li>1 000 000 000 000 = ∞</li>
     * </p>
     * 
     * @param karma
     *            the karma value to compress
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

    private static String cutNumber(final String karma) {
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

    /**
     * <p>
     * Clean and formats the date
     * </p>
     * <p>
     * Date is rendered :
     * <li> <code>now</code> if it's been posted less than one second ago</li>
     * <li> <code>x seconds ago</code> if it's been posted less than 1 minute ago
     * </li>
     * <li> <code>Jan 12, 1952</code> otherwise</li>
     * </p>
     * <p>
     * Note, this method will completly ignore <i>any</i> time information, and
     * display only the date. To display time, use
     * {@link #formatDateTime(DateLocale)}.
     * </p>
     * 
     * @param date
     *            the localized date to render
     * @return the rendered date
     */
    public static String formatDate(final DateLocale date) {
        final Date currentDate = new Date();

        final long diff = currentDate.getTime() - date.getJavaDate().getTime();

        if (diff < SECOND) {
            return Context.tr("now");
        } else if (diff < MINUTE) {
            return Context.trn("{0} second ago", "{0} seconds ago", Long.valueOf(diff / SECOND), Long.valueOf(diff / SECOND));
        } else if (diff < HOUR ){
            return Context.trn("{0} minute ago", "{0} minutes ago", Long.valueOf(diff / MINUTE), Long.valueOf(diff / SECOND));
        } else if (diff < DAY ){
            return Context.trn("{0} hour ago", "{0} hours ago", Long.valueOf(diff / HOUR), Long.valueOf(diff / HOUR));
        }

        return date.toString(FormatStyle.MEDIUM);
    }

    /**
     * <p>
     * Clean and formats date and time
     * </p>
     * <p>
     * DateTime is rendered :
     * <li> <code>now</code> if it's been posted less than one second ago</li>
     * <li> <code>x seconds ago</code> if it's been posted less than 1 minute ago
     * </li>
     * <li> <code>Jan 12, 1952  3:30pm</code> otherwise</li>
     * </p>
     * <p>
     * Note, this method will always display date & time. To display only the
     * date, use {@link #formatDate(DateLocale)}
     * </p>
     * 
     * @param date
     *            the localized date to render
     * @return the rendered date
     */
    public static String formatDateTime(final DateLocale date) {
        final Date currentDate = new Date();

        final long diff = currentDate.getTime() - date.getJavaDate().getTime();

        if (diff < SECOND) {
            return Context.tr("now");
        } else if (diff < MINUTE) {
            return Context.tr("{0} seconds ago", Long.valueOf(diff / SECOND));
        }

        return date.toDateTimeString(FormatStyle.MEDIUM, FormatStyle.SHORT);
    }

    /**
     * <p>
     * Escapes the characters in a String using XML entities.
     * </p>
     * <p>
     * For example: <code>"bread" & "butter"</code> =>
     * <code>&quot;bread&quot; &amp; &quot;butter&quot;</code> .
     * </p>
     * <p>
     * Supports only the five basic XML entities (gt, lt, quot, amp, apos). Does
     * not support DTDs or external entities.
     * </p>
     * <p>
     * Note that unicode characters greater than 0x7f are currently escaped to
     * their numerical \\u equivalent. This may change with future releases of
     * the library used
     * </p>
     */
    public static String escape(String str) {
        return StringEscapeUtils.escapeXml(str);
    }

    /**
     * <p>
     * Unescapes a string containing XML entity escapes to a string containing
     * the actual Unicode characters corresponding to the escapes.
     * </p>
     * <p>
     * Supports only the five basic XML entities (gt, lt, quot, amp, apos). Does
     * not support DTDs or external entities.
     * </p>
     * <p>
     * Note that numerical \\u unicode codes are unescaped to their respective
     * unicode characters. This may change in future releases of the underlying
     * library
     * </p>
     */
    public static String unescape(String str) {
        return StringEscapeUtils.unescapeXml(str);
    }

}
