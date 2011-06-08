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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class Hash {
    // Be ware that it may broke the db if the value is changed.
    private final static int ITERATION_NUMBER = 1000;

    /**
     * From a password and a salt, returns the corresponding digest
     * 
     * @param password The password to encrypt
     * @param salt The salt
     * @return String The digested password in base64
     */
    public static String calculateHash(final String password, final String salt) {
        byte[] value = (password + salt).getBytes();
        for (int i = 0; i < ITERATION_NUMBER; i++) {
            value = DigestUtils.sha512(value);
        }
        return Base64.encodeBase64String(value);
    }
    
    public static String shortHash(final String value) {
        return DigestUtils.sha512Hex(value).substring(10, 20);
    }
}
