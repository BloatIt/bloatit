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
package com.bloatit.web.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

class SessionManager {

    private static HashMap<String, Session> activeSessions = new HashMap<String, Session>();

    public static Session createSession() throws FatalErrorException {
        // TODO handle exception in there ?
        Session session = new Session();
        String d = "abcd"; // TODO generate random string
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new FatalErrorException("Algorithm Sha256 not available",ex);
        }
        md.update(d.getBytes());
        byte byteData[] = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        String key = sb.toString();
        session.setKey(key);
        activeSessions.put(key, session);
        return session;
    }

    public static Session getByKey(String key) {
        if (activeSessions.containsKey(key)) {
            return activeSessions.get(key);
        } else {
            return null;
        }
    }
}