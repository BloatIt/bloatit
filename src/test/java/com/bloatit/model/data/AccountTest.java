package com.bloatit.model.data;

import junit.framework.TestCase;

import com.bloatit.model.data.DaoExternalAccount.AccountType;
import com.bloatit.model.data.util.SessionManager;

public class AccountTest extends TestCase {
	private DaoMember yo;
	private DaoMember tom;
	private DaoMember fred;
	private DaoGroup b219;

	protected void setUp() throws Exception {
		super.setUp();
		SessionManager.reCreateSessionFactory();
		SessionManager.beginWorkUnit();
		{
			tom = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com");
			tom.setFirstname("Thomas");
			tom.setLastname("Guyard");
			SessionManager.flush();
		}
		{
			fred = DaoMember.createAndPersist("Fred", "other", "fred@gmail.com");
			fred.setFirstname("Frédéric");
			fred.setLastname("Bertolus");
			SessionManager.flush();
		}
		{
			yo = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com");
			yo.setFirstname("Yoann");
			yo.setLastname("Plénet");
			SessionManager.flush();

			DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
			DaoGroup.createAndPersiste("myGroup", "plop@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
			(b219 = DaoGroup.createAndPersiste("b219", "plop@plop.com", DaoGroup.Right.PRIVATE)).addMember(yo, true);
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

	public void testDaoAccountDaoActor() {
		SessionManager.beginWorkUnit();

		tom.setExternalAccount(DaoExternalAccount.createAndPersist(tom, AccountType.IBAN, "Bank code !"));
		b219.setExternalAccount(DaoExternalAccount.createAndPersist(b219, AccountType.IBAN, "Bank code !"));

		SessionManager.endWorkUnitAndFlush();
	}

}
