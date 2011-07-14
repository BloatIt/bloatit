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

import org.junit.After;
import org.junit.Before;

import com.bloatit.data.SessionManager;
import com.bloatit.framework.model.ModelAccessor;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.right.AuthToken;

public class ModelTestUnit {
    protected Member memberYo;
    protected Member memberTom;
    protected Member memeberFred;
    protected Member memberAdmin;
    protected Member loser;
    
    private static boolean firstInit = true;
    protected static SimpleTestDB db;

    @Before
    public void setUp() throws Exception {
        if (firstInit) {
            SessionManager.generateTestSessionFactory();
            db = new SimpleTestDB();
            ModelAccessor.initialize(new Model());
            firstInit = false;
        }
        ModelAccessor.open();
        memberYo = MemberManager.getMemberByLogin("Yoann");
        memberTom = MemberManager.getMemberByLogin("Thomas");
        memeberFred = MemberManager.getMemberByLogin("Fred");
        loser = MemberManager.getMemberByLogin("loser");
        memberAdmin = MemberManager.getMemberByLogin("admin");
    }

    @After
    public void tearDown() throws Exception {
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.rollback();
        }
        CacheManager.clear();
    }
}
