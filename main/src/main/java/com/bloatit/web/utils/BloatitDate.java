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
package com.bloatit.web.utils;

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
 * Date format is represented by 2 elements : * The locale that gives the order of the
 * elements (example : fr : dd/mm/yyyy) * The style that determines the length of the date
 * representation
 * </p>
 * <p>
 * BloatidDate can be used to parse string into dates (usually after a user wrote a date
 * in a form) or to find the representation of a date for a given locale (usually to
 * display a date stored in the database)
 * </p>
 * <p>
 * When parsing a string, Parser will <b>NOT</b> use heuristics meaning the input has to
 * match the pattern, or an exception will be thrown. This comportment allows much better
 * error detections
 * </p>
 * <p>
 * For obvious reasons, parser will not be able to detect errors involving inversion of
 * days and month when day is inferior to 13
 * </p>
 * <p>
 * Note that, when constructing a BloatitDate from a String, no proactive validity
 * checking needs to be done, the parser will make sure the string matches the pattern.
 * Therefore, the methods getPattern need only be used for display purpose, to indicate
 * the user how he should input the date
 * </p>
 * </p>Also note that parser is fairly tolerant to structure of the date. For the US date
 * 1/20/1990, the following input strings will <b>all</b> give correct result : *
 * 1/20/1990 * 01/20/1990 * 1/20/90 * 01/20/90</p>
 * <p>
 * While tolerant with short day/months/years, parser is not tolerant with incorrect use
 * of separator. Therefore, patterns using a '/' must be inputed using a '/' to separate
 * months, days and years. No other symbol (such as '-' or '.') will be valid.
 * </p>
 */
public final class BloatitDate {

    private Date javaDate;
    private String dateString;
    private final Locale locale;
    private FormatStyle style = FormatStyle.SHORT;
    private final DateFormat formatter;

    /**
     * <p>
     * Describes the format of the date
     * </p>
     * <p>
     * * SHORT is completely numeric, such as 12.13.52 or 3:30pm * MEDIUM is longer, such
     * as Jan 12, 1952 * LONG is longer, such as January 12, 1952 or 3:30:32pm * FULL is
     * pretty completely specified, such as Tuesday, April 12, 1952 AD or 3:30:42pm PST.
     * </p>
     */
    public enum FormatStyle {
        SHORT, MEDIUM, LONG, FULL
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
     * @throws DateParsingException When dateString doesn't contain a String that matches
     *         the current user locale
     */
    public BloatitDate(final String dateString, final Locale locale) throws DateParsingException {
        this(dateString, locale, FormatStyle.SHORT);
    }

    /**
     * <p>
     * Creates a BloatItDate based on a string and a locale
     * </p>
     * 
     * @param dateString the String description of the date (in Short format)
     * @param locale the user locale to determine date order
     * @param style the style of the date (aka SHORT, LONG, MEDIUM or FULL)
     * @throws DateParsingException When dateString doesn't contain a String that matches
     *         the current user locale
     */
    public BloatitDate(final String dateString, final Locale locale, final FormatStyle style) throws DateParsingException {
        this.dateString = dateString;
        this.locale = locale;
        this.style = style;
        this.formatter = DateFormat.getDateInstance(this.getJavaStyle(), locale);
        parseDate();
    }

    /**
     * <p>
     * Creates a BloatItDate based on a java Date with a defaut SHORT style
     * </p>
     * 
     * @param javaDate the Date to convert
     * @param locale the locale for the date
     */
    public BloatitDate(final Date javaDate, final Locale locale) {
        this(javaDate, locale, FormatStyle.SHORT);
    }

    /**
     * <p>
     * Creates a BloatItDate based on a java Date
     * </p>
     * 
     * @param javaDate the Date to convert
     * @param locale the locale for the date
     * @param style the style of the date
     */
    public BloatitDate(final Date javaDate, final Locale locale, final FormatStyle style) {
        this.locale = locale;
        this.javaDate = javaDate;
        this.style = style;
        this.formatter = DateFormat.getTimeInstance(this.getJavaStyle(), locale);
        this.dateString = formatter.format(javaDate);
    }

    /**
     * <p>
     * Modifies the date.
     * </p>
     * <p>
     * This method is provided for efficiency only. Clean usage should involve creating a
     * new object for every dates
     * </p>
     * 
     * @param dateString the String representing the new date value
     * @throws DateParsingException When dateString doesn't contain a String that matches
     *         the current user locale
     */
    public void setDate(final String dateString) throws DateParsingException {
        try {
            this.dateString = dateString;
            parseDate();
        } catch (final DateParsingException e) {
            // Leave the object in its previous state
            throw new DateParsingException();
        }
    }

    public void setDate(final Date javaDate) {
        this.javaDate = javaDate;
    }

    /**
     * @return the date as a Java Date object
     */
    public Date getJavaDate() {
        return this.javaDate;
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
     * @return
     */
    public String toString(final FormatStyle style) {
        final DateFormat df = DateFormat.getDateInstance(getJavaStyle(style), this.locale);
        return df.format(javaDate);
    }

    /**
     * Parses the String into a java Date object
     * 
     * @throws DateParsingException When this.dateString doesn't contain a String that
     *         matches the current user locale
     */
    private void parseDate() throws DateParsingException {
        this.formatter.setLenient(false);
        try {
            this.javaDate = formatter.parse(this.dateString);
        } catch (final ParseException e) {
            throw new DateParsingException();
        }
    }

    /**
     * <p>
     * Converts this.style into a style usable by the Java DateFormat Object
     * </p>
     * 
     * @return the converted style
     */
    private int getJavaStyle() {
        return getJavaStyle(this.style);
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
     * 
     * @return
     */
    public String getPattern() {
        return BloatitDate.getPattern(this.locale, this.style);
    }

    /**
     * Returns a String representing the short
     * 
     * @param locale
     * @return
     */
    static public String getPattern(final Locale locale) {
        return BloatitDate.getPattern(locale, FormatStyle.SHORT);
    }

    /**
     * Returns a String representing the pattern for the SHORT variant
     * 
     * @param locale
     * @return
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
     * Make sure the pattern is ready to be displayed to the user : * Days and months will
     * be shown with 2 digits * Year will be shown with 4 digits * Remove all caps
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
