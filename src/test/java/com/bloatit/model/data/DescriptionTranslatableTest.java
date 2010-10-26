package com.bloatit.model.data;

import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.model.data.util.SessionManger;

public class DescriptionTranslatableTest extends TestCase {
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

			Group.createAndPersiste("Other", "plop@plop.com", Group.Right.PUBLIC).addMember(yo, false);
			Group.createAndPersiste("myGroup", "plop@plop.com", Group.Right.PUBLIC).addMember(yo, false);
			Group.createAndPersiste("b219", "plop@plop.com", Group.Right.PRIVATE).addMember(yo, true);
		}

		SessionManger.endWorkUnitAndFlush();
	}

    protected void tearDown() throws Exception {
        super.tearDown();
        if (SessionManger.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManger.endWorkUnitAndFlush();
        }
        SessionManger.getSessionFactory().close();
    }
    public void testCreateDescritpion() {
        SessionManger.beginWorkUnit();

        Description description = Description.createAndPersist(yo, new Locale("fr"), "title", "description");

        SessionManger.endWorkUnitAndFlush();

    }

	
}
