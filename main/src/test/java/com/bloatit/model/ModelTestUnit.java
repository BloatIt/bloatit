package com.bloatit.model;

import junit.framework.TestCase;

import com.bloatit.data.SessionManager;
import com.bloatit.framework.webserver.ModelManagerAccessor;

public class ModelTestUnit extends TestCase {
    protected AuthToken yoAuthToken;
    protected AuthToken tomAuthToken;
    protected AuthToken fredAuthToken;
    protected SimpleTestDB db;

    public static int init = init();

    private static int init() {
        ModelManagerAccessor.init(new Model());
        SessionManager.generateTestSessionFactory();
        return 0;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.generateTestSessionFactory();
        db = new SimpleTestDB();
        ModelManagerAccessor.open();
        try {
            yoAuthToken = new AuthToken("Yo", "plop");
            tomAuthToken = new AuthToken("Thomas", "password");
            fredAuthToken = new AuthToken("Fred", "other");
        } finally {
            ModelManagerAccessor.close();
        }
        ModelManagerAccessor.open();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        ModelManagerAccessor.close();
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.endWorkUnitAndFlush();
        }
        SessionManager.getSessionFactory().close();
    }
}
