package com.bloatit.model.data;

import junit.framework.TestCase;

import com.bloatit.model.data.DaoExternalAccount.AccountType;
import com.bloatit.model.data.util.SessionManger;

public class AccountTest extends TestCase {
	private DaoMember yo;
	private DaoMember tom;
	private DaoMember fred;
	private DaoGroup b219;

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
			(b219 = DaoGroup.createAndPersiste("b219", "plop@plop.com", DaoGroup.Right.PRIVATE)).addMember(yo, true);
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

	public void testDaoAccountDaoActor() {
		SessionManger.beginWorkUnit();

		tom.setExternalAccount(DaoExternalAccount.createAndPersist(tom, AccountType.IBAN, "Bank code !"));
		b219.setExternalAccount(DaoExternalAccount.createAndPersist(b219, AccountType.IBAN, "Bank code !"));

		SessionManger.endWorkUnitAndFlush();
	}

}
