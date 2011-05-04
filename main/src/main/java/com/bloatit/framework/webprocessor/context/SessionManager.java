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
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javassist.NotFoundException;

import com.bloatit.common.Log;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.model.right.AuthenticatedUserToken;

/**
 * This class is thread safe (synchronized).
 */
public final class SessionManager {
    private static Map<UUID, Session> activeSessions = new HashMap<UUID, Session>();
    private static Map<String, Session> temporarySessions = new HashMap<String, Session>();
    /** Time to next cleaning up in seconds **/
    private static long nextCleanExpiredSession = 0;

    private SessionManager() {
        // desactivate CTOR
    }

    public static synchronized Session createSession() {
        final Session session = new Session();
        activeSessions.put(session.getKey(), session);
        return session;
    }

    public static synchronized void destroySession(final Session session) {
        if (activeSessions.containsKey(session.getKey())) {
            Log.framework().info("destroy session " + session.getKey());
            cleanTemporarySession(session);
            activeSessions.remove(session.getKey());
        }
    }

    public static synchronized Session getByKey(final String key) {
        try {
            final Session session = activeSessions.get(UUID.fromString(key));
            return session;
        } catch (final IllegalArgumentException e) {
            return null;
        }
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

            for (final Entry<UUID, Session> session : activeSessions.entrySet()) {

                if (session.getValue().getUserToken().isAuthenticated()) {
                    final StringBuilder sessionDump = new StringBuilder();

                    sessionDump.append(session.getValue().getKey().toString());
                    sessionDump.append(' ');
                    sessionDump.append(session.getValue().getUserToken().getMember().getId());
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
                    Log.framework().error("Failed to close the file after an other exception.");
                    Log.framework().error(e);
                    e.printStackTrace();
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

                if (split.length == 2) {
                    restoreSession(split[0], Integer.valueOf(split[1]));
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

    private static synchronized void restoreSession(final String key, final int memberId) {
        final UUID uuidKey = UUID.fromString(key);
        final Session session = new Session(uuidKey);
        try {
            session.setAuthToken(new AuthenticatedUserToken(memberId));
        } catch (final NotFoundException e) {
            Log.framework().error("Session not found", e);
        }
        activeSessions.put(uuidKey, session);
    }

    public static synchronized void clearExpiredSessions() {
        if (nextCleanExpiredSession < Context.getResquestTime()) {
            performClearExpiredSessions();
            nextCleanExpiredSession = Context.getResquestTime() + FrameworkConfiguration.getSessionCleanTime() * DateUtils.SECOND_PER_DAY;
        }
    }

    private static synchronized void performClearExpiredSessions() {

        final Iterator<Session> it = activeSessions.values().iterator();

        while (it.hasNext()) {
            final Session session = it.next();
            if (session.isExpired()) {
                cleanTemporarySession(session);
                it.remove();
            }
        }
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
