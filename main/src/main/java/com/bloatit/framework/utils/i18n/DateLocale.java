/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.utils.i18n;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <p>
 * A class to handle dates for the front web
 * </p>
 * <p>
 * Encapsulates all operations concerning date parsing
 * </p>
 * <p>
 * Date format is represented by 2 elements :
 * <li>The locale that gives the order of the elements (example : fr :
 * dd/mm/yyyy)</li>
 * <li>The style that determines the length of the date representation</li>
 * </p>
 * <p>
 * BloatidDate can be used to parse string into dates (usually after a user
 * wrote a date in a form) or to find the representation of a date for a given
 * locale (usually to display a date stored in the database)
 * </p>
 * <p>
 * When parsing a string, Parser will <b>NOT</b> use heuristics meaning the
 * input has to match the pattern, or an exception will be thrown. This
 * comportment allows much better error detections
 * </p>
 * <p>
 * For obvious reasons, parser will not be able to detect errors involving
 * inversion of days and month when day is inferior to 13
 * </p>
 * <p>
 * Note that, when constructing a DateLocale from a String, no proactive
 * validity checking needs to be done, the parser will make sure the string
 * matches the pattern. Therefore, the methods getPattern need only be used for
 * display purpose, to indicate the user how he should input the date
 * </p>
 * </p>Also note that parser is fairly tolerant to structure of the date. For
 * the US date 1/20/1990, the following input strings will <b>all</b> give
 * correct result : <li>1/20/1990</li> <li>01/20/1990</li> <li>1/20/90</li> <li>
 * 01/20/90</li> </p>
 * <p>
 * While tolerant with short day/months/years, parser is not tolerant with
 * incorrect use of separator. Therefore, patterns using a '/' must be inputed
 * using a '/' to separate months, days and years. No other symbol (such as '-'
 * or '.') will be valid.
 * </p>
 */
public final class DateLocale {

    private Date javaDate;
    private final String dateString;
    private final Locale locale;
    private final DateFormat formatter;
    private DateFormat parser;

    /**
     * <p>
     * Describes the format of the date
     * </p>
     * <p>
     * <li>SHORT is completely numeric, such as 12.13.52 or 3:30pm</li>
     * <li>MEDIUM is longer, such as Jan 12, 1952</li>
     * <li>LONG is longer, such as January 12, 1952 or 3:30:32pm</li>
     * <li>FULL is complete, such as Tuesday, April 12, 1952 AD or 3:30:42pm
     * PST.</li>
     * </p>
     */
    public enum FormatStyle {
        SHORT, MEDIUM, LONG, FULL
    }

    public enum DisplayedTime {
        DATE, TIME, DATETIME
    }

    /**
     * <p>
     * Creates a BloatItDate based on a string and a locale
     * </p>
     * <p>
     * The date must be short format, aka 31/12/2010
     * </p>
     * 
     * @param dateString the String description of the date (in Short format)
     * @param locale the user locale to determine date order
     * @throws DateParsingException When dateString doesn't contain a String
     *             that matches the current user locale
     */
    public DateLocale(final String dateString, final Locale locale) throws DateParsingException {
        this.dateString = dateString;
        this.locale = locale;
        this.formatter = DateFormat.getDateInstance(getJavaStyle(FormatStyle.SHORT), locale);
        this.parser = new SimpleDateFormat("yyyy-MM-dd");
        parseDate();
    }

    /**
     * <p>
     * Creates a BloatItDate based on a java Date with a defaut SHORT style
     * </p>
     * 
     * @param javaDate the Date to convert. (Param is cloned.)
     * @param locale the locale for the date
     */
    public DateLocale(final Date javaDate, final Locale locale) {
        this.locale = locale;
        this.javaDate = (Date) javaDate.clone();
        this.formatter = DateFormat.getTimeInstance(getJavaStyle(FormatStyle.SHORT), locale);
        // this.formatter = new SimpleDateFormat("yyyy-mm-dd");
        this.dateString = formatter.format(javaDate);
    }

    /**
     * @return the date as a Java Date object
     */
    public Date getJavaDate() {
        return (Date) this.javaDate.clone();
    }

    /**
     * Returns the string representation of the Date, short style
     * 
     * @return the string representation of the date, using Short style
     */
    @Override
    public String toString() {
        return this.toString(FormatStyle.SHORT);
    }

    /**
     * Converts the date into aString using a given style
     * 
     * @param style the style of the output (SHORT, MEDIUM, LONG, FULL)
     */
    public String toString(final FormatStyle style) {
        final DateFormat df = DateFormat.getDateInstance(getJavaStyle(style), this.locale);
        return df.format(javaDate);
    }

    public String toString(final FormatStyle style, final DisplayedTime toDisplay) {
        final DateFormat df;
        switch (toDisplay) {
            case DATE:
                df = DateFormat.getDateInstance(getJavaStyle(style), this.locale);
                break;
            case TIME:
                df = DateFormat.getTimeInstance(getJavaStyle(style), this.locale);
                break;
            case DATETIME:
            default:
                df = DateFormat.getDateTimeInstance(getJavaStyle(style), getJavaStyle(style), this.locale);
        }
        return df.format(javaDate);
    }

    public String toTimeString(final FormatStyle style) {
        final DateFormat df = DateFormat.getTimeInstance(getJavaStyle(style), this.locale);
        return df.format(javaDate);
    }

    public String toDateTimeString(final FormatStyle dateFormat, final FormatStyle timeFormat) {
        final DateFormat df = DateFormat.getDateTimeInstance(getJavaStyle(dateFormat), getJavaStyle(timeFormat), this.locale);
        return df.format(javaDate);
    }

    public String getIsoDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(javaDate);
    }

    /**
     * Parses the String into a java Date object
     * 
     * @throws DateParsingException When this.dateString doesn't contain a
     *             String that matches the current user locale
     */
    private void parseDate() throws DateParsingException {
        this.parser.setLenient(false);
        try {
            this.javaDate = parser.parse(this.dateString);
        } catch (final ParseException e) {
            throw new DateParsingException(e);
        }
    }

    /**
     * <p>
     * Converts a given style into a style usable by the Java DateFormat Object
     * </p>
     * 
     * @param style the style to convert
     * @return the converted style
     */
    private static int getJavaStyle(final FormatStyle style) {
        switch (style) {
            case MEDIUM:
                return DateFormat.MEDIUM;
            case LONG:
                return DateFormat.LONG;
            case FULL:
                return DateFormat.FULL;
            case SHORT:
            default:
                return DateFormat.SHORT;
        }
    }

    /**
     * Returns the current pattern used to display the date
     */
    public String getPattern(final FormatStyle style) {
        return DateLocale.getPattern(this.locale, style);
    }

    /**
     * Returns a String representing the short
     * 
     * @param locale
     */
    static public String getPattern(final Locale locale) {
        return DateLocale.getPattern(locale, FormatStyle.SHORT);
    }

    /**
     * Returns a String representing the pattern for the SHORT variant
     * 
     * @param locale
     */
    static public String getPattern(final Locale locale, final FormatStyle format) {
        final SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance(getJavaStyle(format), locale);
        return cleanPattern(sdf.toPattern());
    }

    /**
     * <p>
     * Make the pattern shiny for user interaction
     * </p>
     * <p>
     * Make sure the pattern is ready to be displayed to the user : * Days and
     * months will be shown with 2 digits * Year will be shown with 4 digits *
     * Remove all caps
     * </p>
     * 
     * @param pattern the ugly pattern
     * @return the nice pattern
     */
    static private String cleanPattern(final String pattern) {
        String separator = "";
        final String[] separatorList = new String[] { "-", "/", "." };

        // Find separator
        for (final String sep : separatorList) {
            if (pattern.contains(sep)) {
                separator = sep;
                break;
            }
        }

        // Build the new string
        final String[] dateElements = pattern.toLowerCase().split(separator);
        final StringBuilder outDate = new StringBuilder();

        for (final String element : dateElements) {
            if (outDate.length() > 0) {
                outDate.append(separator);
            }

            if (element.contains("y")) {
                outDate.append("yyyy");
            } else if (element.contains("d")) {
                outDate.append("dd");
            } else if (element.contains("m")) {
                outDate.append("mm");
            }
        }
        return outDate.toString();
    }
}
