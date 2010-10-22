package com.bloatit.model;

import java.util.Locale;

import com.bloatit.model.data.Draft;
import com.bloatit.model.data.Member;
import com.bloatit.model.data.Demand;
import com.bloatit.model.util.HibernateUtil;

import junit.framework.TestCase;

public class HibernateDraftCommentTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		HibernateUtil.beginWorkUnit();
		{
			Member theMember = Member.createAndPersist("Thomas", "password", "tom@gmail.com");
			theMember.setFirstname("Thomas");
			theMember.setLastname("Guyard");
			HibernateUtil.flush();
		}
		{
			Member theMember = Member.createAndPersist("Fred", "other", "fred@gmail.com");
			theMember.setFirstname("Frédéric");
			theMember.setLastname("Bertolus");
			HibernateUtil.flush();
		}
		{
			Member theMember = Member.createAndPersist("Yo", "plop", "yo@gmail.com");
			theMember.setFirstname("Yoann");
			theMember.setLastname("Plénet");
		}
		HibernateUtil.EndWorkUnitAndFlush();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateDemand(){
		HibernateUtil.beginWorkUnit();
		Demand.createAndPersist(Member.getByLogin("Fred"), new Locale("fr"), "Title", "description", "Specification");
		HibernateUtil.EndWorkUnitAndFlush();
	}
}
