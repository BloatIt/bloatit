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

import java.util.HashMap;

import com.bloatit.common.CryptoTools;
import com.bloatit.common.FatalErrorException;

// TODO make me non public !
public class SessionManager {

    private static HashMap<String, Session> activeSessions = new HashMap<String, Session>();

    public static Session createSession() throws FatalErrorException {

        final String key = CryptoTools.generateKey();
        final Session session = new Session(key);
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
