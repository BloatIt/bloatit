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

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import com.bloatit.framework.utils.Hash;

/**
 * A data manager is a class containing some static methods launch at the begin
 * / end of the program.
 * <p>
 * It is basically the class that will manage the lifetime of the data layer.
 * </p>
 * 
 * @author Thomas Guyard
 */
public class DataManager {

    private DataManager() {
        // disactivate ctor;
    }

    /**
     * Initialize the data layer. You can put here all the work that is needed
     * to be done on the db at the launching of the program.
     */
    public static void initialize() {
        // Verify that we do not have old not Hashed password:
        open();
        // "NO-SALT !" is the default value when adding the salt (in liquibase).
        final Query query = SessionManager.createQuery("FROM DaoMember WHERE salt=:salt").setString("salt", "NO-SALT !");
        @SuppressWarnings("unchecked") final List<DaoMember> members = query.list();
        for (final DaoMember member : members) {
            final String salt = RandomStringUtils.randomAscii(50);
            final String password = Hash.calculateHash(member.getPassword(), salt);
            member.setPassword(password);
            member.setSalt(salt);
        }
        close();
    }

    /**
     * For now it does nothing. But it is called when the data layer have to
     * stop. Put here your shutdown routines.
     */
    public static void shutdown() {
        // For now do nothing.
    }

    /**
     * Set the current transaction read only.
     * 
     * @see DataManager#open()
     * @see DataManager#close()
     */
    public static void setReadOnly() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        session.setDefaultReadOnly(true);
        session.setFlushMode(FlushMode.MANUAL);
    }

    /**
     * Open a transaction (Or a "work unit"). Tells to the data layer that some
     * work will be done. You can change the beaver of the current transaction
     * using {@link #setReadOnly()}
     * <p>
     * You cannot open 2 transactions in the same thread. Make sure you have
     * called {@link #close()} before.
     * </p>
     */
    public static void open() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        SessionManager.beginWorkUnit();
        session.setDefaultReadOnly(false);
        session.setFlushMode(FlushMode.AUTO);
    }

    /**
     * Close the current transaction (Or "work unit").
     */
    public static void close() {
        SessionManager.endWorkUnitAndFlush();
    }

    /**
     * Rollback the current transaction.
     */
    public static void rollback() {
        SessionManager.rollback();
    }

}
