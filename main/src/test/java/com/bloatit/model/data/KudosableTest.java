package com.bloatit.model.data;

import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.model.data.util.SessionManager;

public class KudosableTest extends TestCase {

    public void testAddKudos() {
        demand.addKudos(fred, 12);
        demand.addKudos(yo, -12);
        demand.addKudos(tom, 42);
    }

    private DaoMember yo;
    private DaoMember tom;
    private DaoMember fred;

    private DaoDemand demand;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.reCreateSessionFactory();
        SessionManager.beginWorkUnit();
        {
            tom = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com");
            tom.setFullname("Thomas Guyard");
            SessionManager.flush();
        }
        {
            fred = DaoMember.createAndPersist("Fred", "other", "fred@gmail.com");
            fred.setFullname("Frédéric Bertolus");
            SessionManager.flush();
        }
        {
            yo = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com");
            yo.setFullname("Yoann Plénet");
            SessionManager.flush();

            DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
            DaoGroup.createAndPersiste("myGroup", "plop@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
            (DaoGroup.createAndPersiste("b219", "plop@plop.com", DaoGroup.Right.PROTECTED)).addMember(yo, true);
        }

        demand = DaoDemand.createAndPersist(yo,
                DaoDescription.createAndPersist(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.endWorkUnitAndFlush();
        }
        SessionManager.getSessionFactory().close();
    }

}
