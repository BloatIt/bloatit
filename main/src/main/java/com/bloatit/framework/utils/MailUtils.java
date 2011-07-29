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
        final String expression = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")"
                + "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";
        final Pattern pattern = Pattern.compile(expression);

        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
