package com.bloatit.model.data;

import junit.framework.TestCase;

import com.bloatit.model.data.DaoExternalAccount.AccountType;
import com.bloatit.model.data.util.SessionManager;

public class AccountTest extends TestCase {
    private DaoMember yo;
    private DaoMember tom;
    private DaoMember fred;
    private DaoGroup b219;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.generateTestSessionFactory();
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
            (b219 = DaoGroup.createAndPersiste("b219", "plop@plop.com", DaoGroup.Right.PROTECTED)).addMember(yo, true);
        }

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

    public void testDaoAccountDaoActor() {
        SessionManager.beginWorkUnit();

        tom.setExternalAccount(DaoExternalAccount.createAndPersist(tom, AccountType.IBAN, "Bank code !"));
        b219.setExternalAccount(DaoExternalAccount.createAndPersist(b219, AccountType.IBAN, "Bank code !"));

        SessionManager.endWorkUnitAndFlush();
    }

}
