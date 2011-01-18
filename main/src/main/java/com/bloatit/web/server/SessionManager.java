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
package com.bloatit.web.server;

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
import com.bloatit.framework.AuthToken;

public final class SessionManager {

    private static Map<UUID, Session> activeSessions = new HashMap<UUID, Session>();
    private static final long CLEAN_EXPIRED_SESSION_COOLDOWN = 172800; // 2 days
    private static long nextCleanExpiredSession = 0;

    private SessionManager(){
        // desactivate CTOR
    }

    public static Session createSession() {
        final Session session = new Session();
        activeSessions.put(session.getKey(), session);
        return session;
    }

    public static void destroySession(Session session) {
        if (activeSessions.containsKey(session.getKey())) {
            Log.server().info("destroy session " + session.getKey());
            activeSessions.remove(session.getKey());
        }
    }

    public static Session getByKey(final String key) {
        if (activeSessions.containsKey(key)) {
            return activeSessions.get(key);
        }
        return null;
    }

    private static void restoreSession(final String key, final int memberId) {
        UUID uuidKey = UUID.fromString(key);
        final Session session = new Session(uuidKey);
        try {
            session.setAuthToken(new AuthToken(memberId));
        } catch (NotFoundException e) {
            Log.server().error(e);
        }
        activeSessions.put(uuidKey, session);
    }

    public static void SaveSessions() {
        // TODO find a good place to save this

        com.bloatit.model.data.util.SessionManager.beginWorkUnit();

        String dir = System.getProperty("user.home") + "/.local/share/bloatit/";
        String dump = dir + "/sessions.dump";

        if (new File(dir).mkdirs()) {
            Log.server().debug("Creating a new dir: " + dir);
        }

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(new File(dump));

            for (Entry<UUID, Session> session : activeSessions.entrySet()) {

                if (session.getValue().isLogged()) {
                    StringBuilder sessionDump = new StringBuilder();

                    sessionDump.append(session.getValue().getKey().toString());
                    sessionDump.append(" ");
                    sessionDump.append(session.getValue().getAuthToken().getMember().getDao().getId());
                    sessionDump.append("\n");

                    fileOutputStream.write(sessionDump.toString().getBytes());
                }
            }

            fileOutputStream.close();

        } catch (IOException e) {
            Log.server().error("Failed to save sessions.", e);
        }

        com.bloatit.model.data.util.SessionManager.endWorkUnitAndFlush();
    }

    public static void loadSessions() {
        com.bloatit.model.data.util.SessionManager.beginWorkUnit();

        String dir = System.getProperty("user.home") + "/.local/share/bloatit/";
        String dump = dir + "/sessions.dump";

        try {
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream;
            fstream = new FileInputStream(dump);

            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            // Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                // Print the content on the console
                String[] split = strLine.split(" ");

                if (split.length == 2) {
                    restoreSession(split[0], Integer.valueOf(split[1]));
                }

            }

            // Close the streams
            in.close();
            br.close();

            if (new File(dump).delete()) {
                Log.server().info("deleting dump file: " + dump);
            }else{
                Log.server().error("Cannot delete dump file: " + dump);
            }

        } catch (IOException e) {
            // Failed to restore sessions
            Log.server().error("Failed to restore sessions.", e);
        }

        com.bloatit.model.data.util.SessionManager.endWorkUnitAndFlush();

    }

    public static void clearExpiredSessions() {
        if (nextCleanExpiredSession < Context.getTime()) {
            performClearExpiredSessions();
            nextCleanExpiredSession = Context.getTime() + CLEAN_EXPIRED_SESSION_COOLDOWN;
        }
    }

    public static void performClearExpiredSessions() {

        Iterator<Session> it = activeSessions.values().iterator();

        while (it.hasNext()) {
            Session session = it.next();
            if (session.isExpired()) {
                it.remove();
            }
        }
    }

}
