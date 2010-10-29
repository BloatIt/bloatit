package com.bloatit.model.data;

import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.model.data.util.SessionManager;

public class DescriptionTranslatableTest extends TestCase {
	private DaoMember yo;
	private DaoMember tom;
	private DaoMember fred;

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
			DaoGroup.createAndPersiste("b219", "plop@plop.com", DaoGroup.Right.PRIVATE).addMember(yo, true);
		}

		SessionManager.endWorkUnitAndFlush();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
			SessionManager.endWorkUnitAndFlush();
		}
		SessionManager.getSessionFactory().close();
	}

	public void testCreateDescritpion() {
		SessionManager.beginWorkUnit();

		DaoDescription description = DaoDescription.createAndPersist(yo, new Locale("fr"), "title", "description");
		assertEquals(description, DBRequests.getAll(DaoDescription.class).iterator().next());

		SessionManager.endWorkUnitAndFlush();

	}

	public void testCreateDescritpionAndTranslation() {
		SessionManager.beginWorkUnit();

		DaoDescription description = DaoDescription.createAndPersist(yo, new Locale("en"), "title", "description");
		if (description.getTranslation(new Locale("fr")) == null) {
			description.addTranslation(new DaoTranslation(fred, description, new Locale("fr"), "titre", "description"));
		} else {
			fail("translation already exists");
		}

		assertEquals("titre", description.getTranslation(new Locale("fr")).getTitle());

		SessionManager.endWorkUnitAndFlush();

	}

}
