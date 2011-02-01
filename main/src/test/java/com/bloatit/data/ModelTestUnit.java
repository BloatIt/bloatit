package com.bloatit.data;

import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.data.DaoFileMetadata.FileType;

public class ModelTestUnit extends TestCase {

    protected DaoMember tom;
    protected DaoMember fred;
    protected DaoMember yo;
    protected DaoGroup other;
    protected DaoGroup myGroup;
    protected DaoGroup b219;
    protected DaoProject project;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.generateTestSessionFactory();

        SessionManager.beginWorkUnit();
        tom = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com", Locale.FRANCE);
        tom.setFullname("Thomas Guyard");
        fred = DaoMember.createAndPersist("Fred", "other", "fred@gmail.com", Locale.FRANCE);
        fred.setFullname("Frédéric Bertolus");
        yo = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com", Locale.FRANCE);
        yo.setFullname("Yoann Plénet");

        other = DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC);
        other.addMember(yo, false);
        myGroup = DaoGroup.createAndPersiste("myGroup", "plop1@plop.com", DaoGroup.Right.PUBLIC);
        myGroup.addMember(yo, false);
        b219 = DaoGroup.createAndPersiste("b219", "plop2@plop.com", DaoGroup.Right.PROTECTED);
        b219.addMember(yo, true);

        project = DaoProject.createAndPersist("VLC",
                                    b219,
                                    DaoDescription.createAndPersist(fred, Locale.FRANCE, "title", "descrip"),
                                    DaoFileMetadata.createAndPersist(fred, null, "/dev/", "null", FileType.JPG, 12));
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