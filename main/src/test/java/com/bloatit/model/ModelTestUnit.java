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
package com.bloatit.model;

import junit.framework.TestCase;

import com.bloatit.data.SessionManager;
import com.bloatit.framework.webprocessor.ModelAccessor;
import com.bloatit.model.right.AuthToken;

public class ModelTestUnit extends TestCase {
    protected AuthToken yoAuthToken;
    protected AuthToken tomAuthToken;
    protected AuthToken fredAuthToken;
    protected AuthToken loser;
    
    protected static SimpleTestDB db;
    static int init = init();

    private static int init() {
        SessionManager.generateTestSessionFactory();
        ModelAccessor.initialize(new Model());
        db = new SimpleTestDB();
        return 0;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ModelAccessor.open();
        yoAuthToken = new AuthToken("Yoann", "plop");
        tomAuthToken = new AuthToken("Thomas", "password");
        fredAuthToken = new AuthToken("Fred", "other");
        loser = new AuthToken("loser", "loser");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.rollback();
        }
    }
}
