package com.bloatit.data;

import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.data.DaoFileMetadata.FileType;

public class DaoUserContentTest extends TestCase {

    public void testGetAuthor() {
        assertEquals(yo, feature.getMember());
    }

    private DaoMember yo;
    private DaoMember tom;
    private DaoMember fred;
    private DaoTeam b219;

    private DaoFeature feature;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.generateTestSessionFactory();
        SessionManager.beginWorkUnit();
        {
            tom = DaoMember.createAndPersist("Thomas", "password", "salt", "tom@gmail.com", Locale.FRANCE);
            tom.setFullname("Thomas Guyard");
            SessionManager.flush();
        }
        {
            fred = DaoMember.createAndPersist("Fred", "other", "salt", "fred@gmail.com", Locale.FRANCE);
            fred.setFullname("Frédéric Bertolus");
            SessionManager.flush();
        }
        {
            yo = DaoMember.createAndPersist("Yo", "plop", "salt", "yo@gmail.com", Locale.FRANCE);
            yo.setFullname("Yoann Plénet");
            SessionManager.flush();

            DaoTeam.createAndPersiste("Other", "plop@plop.com", "A group description", DaoTeam.Right.PUBLIC).addMember(yo, false);
            DaoTeam.createAndPersiste("myGroup", "plop1@plop.com", "A group description", DaoTeam.Right.PUBLIC).addMember(yo, false);
            (b219 = DaoTeam.createAndPersiste("b219", "plop2@plop.com", "A group description", DaoTeam.Right.PROTECTED)).addMember(yo, true);
        }

        final DaoSoftware project = DaoSoftware.createAndPersist("VLC", DaoDescription.createAndPersist(fred, null, Locale.FRANCE, "title", "descrip"));
        project.setImage(DaoFileMetadata.createAndPersist(fred, null, null, "/dev/", "null", FileType.JPG, 12));

        feature = DaoFeature.createAndPersist(yo, null, DaoDescription.createAndPersist(yo, null,
                                                                                  new Locale("fr"),
                                                                                  "Ma super demande !",
                                                                                  "Ceci est la descption de ma demande :) "), project);

        SessionManager.endWorkUnitAndFlush();
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
