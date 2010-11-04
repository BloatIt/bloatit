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

package com.bloatit.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class CryptoTools {

    static private final String defaultAlphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-@#&'(!?)$%?:;/.?,";

    public static String getRandomString(int length) {

        final StringBuilder result = new StringBuilder();

        final Random rand = new Random();
        for (int i = 0; i < length; i++) {
            result.append(defaultAlphabet.charAt(rand.nextInt(defaultAlphabet.length())));
        }

        return result.toString();
    }

    public static String sha256(String digest) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (final NoSuchAlgorithmException ex) {
            throw new FatalErrorException("Algorithm Sha256 not available", ex);
        }
        md.update(digest.getBytes());
        final byte byteData[] = md.digest();

        final StringBuilder sb = new StringBuilder();
        for (byte element : byteData) {
            sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static String generateKey() {
        return sha256(getRandomString(100));
    }
}
