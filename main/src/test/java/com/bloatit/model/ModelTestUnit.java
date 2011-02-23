package com.bloatit.model;

import junit.framework.TestCase;

import com.bloatit.data.SessionManager;
import com.bloatit.framework.webserver.ModelAccessor;
import com.bloatit.model.right.AuthToken;

public class ModelTestUnit extends TestCase {
    protected AuthToken yoAuthToken;
    protected AuthToken tomAuthToken;
    protected AuthToken fredAuthToken;
    protected SimpleTestDB db;

    public static int init = init();

    private static int init() {
        ModelAccessor.initialize(new Model());
        SessionManager.generateTestSessionFactory();
        return 0;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.generateTestSessionFactory();
        db = new SimpleTestDB();
        ModelAccessor.open();
        try {
            yoAuthToken = new AuthToken("Yo", "plop");
            tomAuthToken = new AuthToken("Thomas", "password");
            fredAuthToken = new AuthToken("Fred", "other");
        } finally {
            ModelAccessor.close();
        }
        ModelAccessor.open();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        ModelAccessor.close();
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.endWorkUnitAndFlush();
        }
        SessionManager.getSessionFactory().close();
    }
}
