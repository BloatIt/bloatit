package com.bloatit.model;

import junit.framework.TestCase;

import com.bloatit.data.SessionManager;

public class FrameworkTestUnit extends TestCase {
    protected AuthToken yoAuthToken;
    protected AuthToken tomAuthToken;
    protected AuthToken fredAuthToken;
    protected SimpleTestDB db;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.generateTestSessionFactory();
        db = new SimpleTestDB();
        SessionManager.beginWorkUnit();
        yoAuthToken = new AuthToken("Yo", "plop");
        tomAuthToken = new AuthToken("Thomas", "password");
        fredAuthToken = new AuthToken("Fred", "other");
        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();
        Framework.lock();
    }

    @Override
    protected void tearDown() throws Exception {
        Framework.unLock();
        super.tearDown();
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.endWorkUnitAndFlush();
        }
        SessionManager.getSessionFactory().close();
    }
}
