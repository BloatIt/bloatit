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
package com.bloatit.data;

import org.hibernate.FlushMode;
import org.hibernate.classic.Session;

public class DataManager {

    private DataManager() {
        // disactivate ctor;
    }

    public static void launch() {
        // For now do nothing.
    }

    public static void shutdown() {
        // For now do nothing.
    }

    public static void setReadOnly() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        session.setDefaultReadOnly(true);
        session.setFlushMode(FlushMode.MANUAL);
    }

    public static void open() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();

        session.beginTransaction();
        session.setDefaultReadOnly(false);
        session.setFlushMode(FlushMode.AUTO);
    }

    public static void close() {
        SessionManager.endWorkUnitAndFlush();
    }

    public static void rollback() {
        SessionManager.rollback();
    }

}
