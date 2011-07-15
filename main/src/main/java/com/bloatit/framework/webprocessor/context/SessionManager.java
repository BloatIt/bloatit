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
package com.bloatit.framework.webprocessor.context;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javassist.NotFoundException;

import com.bloatit.common.Log;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.xcgiserver.RequestKey;
import com.bloatit.framework.xcgiserver.WrongSessionKeyFormatException;

/**
 * This class is thread safe (synchronized).
 */
public final class SessionManager {
    private static Map<String, Session> activeSessions = new HashMap<String, Session>();
    private static Map<String, Session> temporarySessions = new HashMap<String, Session>();

    private SessionManager() {
        // desactivate CTOR
    }

    /**
     * Return the session for the user. Either an existing session or a new
     * session.
     * 
     * @param header
     * @return the session matching the user
     */
    public static Session getOrCreateSession(final RequestKey key) {
        Session sessionByKey = null;
        try {
            if (key != null && (sessionByKey = SessionManager.getByKey(key.getId(), key.getIpAddress())) != null) {
                sessionByKey.resetExpirationTime();
                return sessionByKey;
            }
        } catch (final WrongSessionKeyFormatException e) {
            // Just don't restore session
        }
        return SessionManager.createSession(key);
    }

    public static synchronized void destroySession(final Session session) {
        if (activeSessions.containsKey(session.getKey())) {
            Log.framework().trace("destroy session " + session.getKey());
            cleanTemporarySession(session);
            activeSessions.remove(session.getKey());
        }
    }

    /**
     * Find a session using its id and its ip address.
     * 
     * @param id the id of the session (the one store in the cookie)
     * @param ipAddress the ip address of the user trying to get his session.
     * @return the session or null if not found.
     * @throws WrongSessionKeyFormatException
     */
    private static synchronized Session getByKey(final String key, final String ipAddress) throws WrongSessionKeyFormatException {
        final Session session = activeSessions.get(key);
        if (session != null && session.isValid(ipAddress)) {
            return session;
        }
        return null;
    }

    private static synchronized Session createSession(final RequestKey key) {
        final Session session = new Session(key.getId(), key.getIpAddress());
        activeSessions.put(session.getKey(), session);
        return session;
    }

    public static synchronized String generateNewSessionKey(String key) {
        final Session session = activeSessions.get(key);
        if (session != null) {
            activeSessions.remove(key);
            key = RequestKey.generateRandomId();
            activeSessions.put(key, session);
        }
        return key;
    }

    public static synchronized void saveSessions() {
        com.bloatit.data.SessionManager.beginWorkUnit();

        final String dir = System.getProperty("user.home") + "/.local/share/bloatit/";
        final String dump = dir + "/sessions.dump";

        if (new File(dir).mkdirs()) {
            Log.framework().debug("Creating a new dir: " + dir);
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(dump));

            for (final Entry<String, Session> session : activeSessions.entrySet()) {

                if (session.getValue().getMemberId() != null) {
                    final StringBuilder sessionDump = new StringBuilder();

                    sessionDump.append(session.getValue().getKey());
                    sessionDump.append(' ');
                    sessionDump.append(session.getValue().getShortKey());
                    sessionDump.append(' ');
                    sessionDump.append(session.getValue().getIpAddress());
                    sessionDump.append(' ');
                    sessionDump.append(session.getValue().getMemberId());
                    sessionDump.append(' ');
                    sessionDump.append(session.getValue().getMemberLocale().getLanguage());
                    sessionDump.append(' ');
                    sessionDump.append(session.getValue().getMemberLocale().getCountry());

                    sessionDump.append('\n');

                    fileOutputStream.write(sessionDump.toString().getBytes());
                }
            }

            fileOutputStream.close();

        } catch (final IOException e) {
            Log.framework().error("Failed to save sessions.", e);
        } finally {
            if (fileOutputStream != null) {
                try {

                    fileOutputStream.close();
                } catch (final IOException e) {
                    Log.framework().error("Failed to close the file after an other exception.", e);
                }
            }
            com.bloatit.data.SessionManager.endWorkUnitAndFlush();
        }

    }

    public static synchronized void loadSessions() {
        com.bloatit.data.SessionManager.beginWorkUnit();

        final String dump = FrameworkConfiguration.getSessionDumpfile();
        BufferedReader br = null;

        try {
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream;
            fstream = new FileInputStream(dump);

            // Get the object of DataInputStream
            final DataInputStream in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            // Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                // Print the content on the console
                final String[] split = strLine.split(" ");

                if (split.length == 6) {
                    try {
                        restoreSession(split[0], split[1], split[2], Integer.valueOf(split[3]), split[4], split[5]);
                    } catch (WrongSessionKeyFormatException e) {
                        // Just ignore session
                    }
                }

            }

            // Close the streams
            in.close();
            br.close();

            if (new File(dump).delete()) {
                Log.framework().info("deleting dump file: " + dump);
            } else {
                Log.framework().error("Cannot delete dump file: " + dump);
            }

        } catch (final IOException e) {
            if (br != null) {
                try {
                    br.close();
                } catch (final IOException e1) {
                    Log.framework().error(e1);
                }
            }

            // Failed to restore sessions
            Log.framework().error("Failed to restore sessions.", e);
        } finally {
            com.bloatit.data.SessionManager.endWorkUnitAndFlush();
        }
    }

    private static synchronized void restoreSession(final String key,
                                                    final String shortKey,
                                                    final String ipAddress,
                                                    final int memberId,
                                                    final String language,
                                                    final String country) throws WrongSessionKeyFormatException {
        final Session session = new Session(key, shortKey, ipAddress, memberId, language, country);
        activeSessions.put(key, session);
    }

    static synchronized void clearExpiredSessions() {
        int activeSessionsCountBefore = activeSessions.size();
        
        final Iterator<Session> it = activeSessions.values().iterator();
        while (it.hasNext()) {
            final Session session = it.next();
            if (session.isExpired()) {
                cleanTemporarySession(session);
                it.remove();
            }
        }
        
        int activeSessionsCountAfter = activeSessions.size();
        Log.framework().info("Clean expired session: "+activeSessionsCountBefore+" -> "+ activeSessionsCountAfter);
    }

    public static synchronized void storeTemporarySession(final String key, final Session session) {
        temporarySessions.put(key, session);
    }

    public static synchronized Session pickTemporarySession(final String key) {
        if (temporarySessions.containsKey(key)) {
            final Session session = temporarySessions.get(key);
            temporarySessions.remove(key);
            return session;
        }
        return null;
    }

    private static synchronized void cleanTemporarySession(final Session sessionToClean) {
        final Iterator<Entry<String, Session>> it = temporarySessions.entrySet().iterator();

        while (it.hasNext()) {
            final Session session = it.next().getValue();
            if (session.equals(sessionToClean)) {
                it.remove();
            }
        }
    }
}
