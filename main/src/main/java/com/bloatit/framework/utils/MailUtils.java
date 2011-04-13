package com.bloatit.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailUtils {

    /**
     * <p>
     * Indicates wether a given string is a valid email address
     * </p>
     * <p>
     * Email format is :
     * <li>starts with A-Za-z or '-'</li>
     * <li>then has any number of A-Za-z (can be zero)</li>
     * <li>has any number of '.' followed by one of more characters</li>
     * <li>has an '@'</li>
     * <li>has any number of A-Za-z0-9</li>
     * <li>has a '.' then 2 to 4 A-Za-z</li>
     * </p>
     * 
     * @param email the string to validate
     * @return <code>true</code> if the string is a valid email address,
     *         <code>false</code> otherwise
     */
    public static boolean isValidEmail(final String email) {
        final String expression = "^[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$";
        final Pattern pattern = Pattern.compile(expression);

        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
