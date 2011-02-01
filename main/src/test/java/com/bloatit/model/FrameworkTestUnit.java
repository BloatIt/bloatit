package com.bloatit.model;

import junit.framework.TestCase;

import com.bloatit.data.SessionManager;
import com.bloatit.framework.webserver.ModelManagerAccessor;

public class FrameworkTestUnit extends TestCase {
    protected AuthToken yoAuthToken;
    protected AuthToken tomAuthToken;
    protected AuthToken fredAuthToken;
    protected SimpleTestDB db;


    public static int init = init();

    private static int init() {
        ModelManagerAccessor.launch(new ModelManager());
        return 0;
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.generateTestSessionFactory();
        db = new SimpleTestDB();
        ModelManagerAccessor.open();
        yoAuthToken = new AuthToken("Yo", "plop");
        tomAuthToken = new AuthToken("Thomas", "password");
        fredAuthToken = new AuthToken("Fred", "other");
        ModelManagerAccessor.close();
        ModelManagerAccessor.open();
        ModelManagerAccessor.lock();
    }

    @Override
    protected void tearDown() throws Exception {
        ModelManagerAccessor.unLock();
        ModelManagerAccessor.close();
        super.tearDown();
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.endWorkUnitAndFlush();
        }
        SessionManager.getSessionFactory().close();
    }
}
