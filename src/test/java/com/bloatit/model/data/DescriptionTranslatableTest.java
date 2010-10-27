package com.bloatit.model.data;

import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.model.data.util.SessionManger;

public class DescriptionTranslatableTest extends TestCase {
	private DaoMember yo;
	private DaoMember tom;
	private DaoMember fred;

	protected void setUp() throws Exception {
		super.setUp();
		SessionManger.reCreateSessionFactory();
		SessionManger.beginWorkUnit();
		{
			tom = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com");
			tom.setFirstname("Thomas");
			tom.setLastname("Guyard");
			SessionManger.flush();
		}
		{
			fred = DaoMember.createAndPersist("Fred", "other", "fred@gmail.com");
			fred.setFirstname("Frédéric");
			fred.setLastname("Bertolus");
			SessionManger.flush();
		}
		{
			yo = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com");
			yo.setFirstname("Yoann");
			yo.setLastname("Plénet");
			SessionManger.flush();

			DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
			DaoGroup.createAndPersiste("myGroup", "plop@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
			DaoGroup.createAndPersiste("b219", "plop@plop.com", DaoGroup.Right.PRIVATE).addMember(yo, true);
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

		DaoDescription description = DaoDescription.createAndPersist(yo, new Locale("fr"), "title", "description");
		assertEquals(description, DBRequests.getAll(DaoDescription.class).iterator().next());

		SessionManger.endWorkUnitAndFlush();

	}

	public void testCreateDescritpionAndTranslation() {
		SessionManger.beginWorkUnit();

		DaoDescription description = DaoDescription.createAndPersist(yo, new Locale("en"), "title", "description");
		if (description.getTranslation(new Locale("fr")) == null) {
			description.addTranslation(new DaoTranslation(fred, description, new Locale("fr"), "titre", "description"));
		} else {
			fail("translation already exists");
		}

		assertEquals("titre", description.getTranslation(new Locale("fr")).getTitle());

		SessionManger.endWorkUnitAndFlush();

	}

}
