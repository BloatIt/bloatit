package com.bloatit.model.data;

import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.model.data.util.SessionManger;

public class KudosableTest extends TestCase {

	public void testAddKudos() {
		demand.addKudos(fred, 12);
		demand.addKudos(yo, -12);
		demand.addKudos(tom, 42);
	}

	public void testSetValidated() {
		demand.setValidated();
		assertEquals(DaoKudosable.State.VALIDATED, demand.getState());
	}

	public void testSetRejected() {
		demand.setRejected();
		assertEquals(DaoKudosable.State.REJECTED, demand.getState());
	}

	private DaoMember yo;
	private DaoMember tom;
	private DaoMember fred;

	private DaoDemand demand;

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
			(DaoGroup.createAndPersiste("b219", "plop@plop.com", DaoGroup.Right.PRIVATE)).addMember(yo, true);
		}

		demand = DaoDemand.createAndPersist(yo, new DaoDescription(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));

		SessionManger.endWorkUnitAndFlush();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if (SessionManger.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
			SessionManger.endWorkUnitAndFlush();
		}
		SessionManger.getSessionFactory().close();
	}

}
