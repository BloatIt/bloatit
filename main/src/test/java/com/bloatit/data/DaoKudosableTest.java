package com.bloatit.data;

import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.data.DaoFileMetadata.FileType;

public class DaoKudosableTest extends TestCase {

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
        SessionManager.generateTestSessionFactory();
        SessionManager.beginWorkUnit();
        {
            tom = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com", Locale.FRANCE);
            tom.setFullname("Thomas Guyard");
            SessionManager.flush();
        }
        {
            fred = DaoMember.createAndPersist("Fred", "other", "fred@gmail.com", Locale.FRANCE);
            fred.setFullname("Frédéric Bertolus");
            SessionManager.flush();
        }
        {
            yo = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com", Locale.FRANCE);
            yo.setFullname("Yoann Plénet");
            SessionManager.flush();

            DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
            DaoGroup.createAndPersiste("myGroup", "plop1@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
            DaoGroup.createAndPersiste("b219", "plop2@plop.com", DaoGroup.Right.PROTECTED).addMember(yo, true);
        }

        DaoProject project = DaoProject.createAndPersist("VLC",
                                              DaoDescription.createAndPersist(fred, Locale.FRANCE, "title", "descrip"),
                                              DaoFileMetadata.createAndPersist(fred, null, "/dev/", "null", FileType.JPG, 12));

        demand = DaoDemand.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                new Locale("fr"),
                                                                                "Ma super demande !",
                                                                                "Ceci est la descption de ma demande :) "), project);

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
