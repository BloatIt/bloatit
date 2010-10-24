package com.bloatit.model;

import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.model.data.Demand;
import com.bloatit.model.data.Group;
import com.bloatit.model.data.Member;
import com.bloatit.model.data.Translatable;
import com.bloatit.model.data.util.SessionManger;

/**
 * Here I assume the HibernateTest is run without error.
 * @author thomas
 */
public class HibernateDraftCommentTest extends TestCase {

    private Member yo;
    private Member tom;
    private Member fred;

    protected void setUp() throws Exception {
        super.setUp();
        SessionManger.reCreateSessionFactory();
        SessionManger.beginWorkUnit();
        {
            tom = Member.createAndPersist("Thomas", "password", "tom@gmail.com");
            tom.setFirstname("Thomas");
            tom.setLastname("Guyard");
            SessionManger.flush();
        }
        {
            fred = Member.createAndPersist("Fred", "other", "fred@gmail.com");
            fred.setFirstname("Frédéric");
            fred.setLastname("Bertolus");
            SessionManger.flush();
        }
        {
            yo = Member.createAndPersist("Yo", "plop", "yo@gmail.com");
            yo.setFirstname("Yoann");
            yo.setLastname("Plénet");
            SessionManger.flush();

            Group.createAndPersiste("Other", yo, Group.Right.PUBLIC).addMember(yo, false);
            Group.createAndPersiste("myGroup", yo, Group.Right.PUBLIC).addMember(yo, false);
            Group.createAndPersiste("b219", yo, Group.Right.PRIVATE).addMember(yo, true);
        }

        SessionManger.EndWorkUnitAndFlush();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        if (SessionManger.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManger.EndWorkUnitAndFlush();
        }
        SessionManger.getSessionFactory().close();
    }

    public void testCreateDemand() {

        new Demand(yo, new Translatable(yo, new Locale("fr"), "Ma super demande !"), new Translatable(yo,
                                                                                                      new Locale("fr"),
                                                                                                      "Ceci est la descption de ma demande :) "));

    }
}
