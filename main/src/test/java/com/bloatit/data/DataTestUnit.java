package com.bloatit.data;

import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.data.DaoFileMetadata.FileType;

public class DataTestUnit extends TestCase {

    protected DaoMember tom;
    protected DaoMember fred;
    protected DaoMember yo;
    protected DaoTeam other;
    protected DaoTeam myGroup;
    protected DaoTeam b219;
    protected DaoSoftware project;

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

        other = DaoTeam.createAndPersiste("Other", "plop@plop.com", "A group description", DaoTeam.Right.PUBLIC);
        other.addMember(yo, false);
        myGroup = DaoTeam.createAndPersiste("myGroup", "plop1@plop.com", "A group description", DaoTeam.Right.PUBLIC);
        myGroup.addMember(yo, false);
        b219 = DaoTeam.createAndPersiste("b219", "plop2@plop.com", "A group description", DaoTeam.Right.PROTECTED);
        b219.addMember(yo, true);

        project = DaoSoftware.createAndPersist("VLC", DaoDescription.createAndPersist(fred, Locale.FRANCE, "title", "descrip"));
        project.setImage(DaoFileMetadata.createAndPersist(fred, null, "/dev/", "null", FileType.JPG, 12));
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
