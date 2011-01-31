package com.bloatit.data;

import java.math.BigDecimal;
import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.data.DaoExternalAccount.AccountType;
import com.bloatit.data.exceptions.NotEnoughMoneyException;

public class DaoTransactionTest extends TestCase {

    public void testCreateAndPersist() {
        SessionManager.beginWorkUnit();

        try {
            yo.getInternalAccount().setAmount(new BigDecimal("200"));
            fred.getInternalAccount().setAmount(new BigDecimal("200"));

            DaoTransaction.createAndPersist(yo.getInternalAccount(), tom.getInternalAccount(), new BigDecimal("120"));
            assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("80")));
            assertEquals(0, tom.getInternalAccount().getAmount().compareTo(new BigDecimal("120")));

            SessionManager.flush();
            b219.setExternalAccount(DaoExternalAccount.createAndPersist(b219, AccountType.IBAN, "plop"));

            DaoTransaction.createAndPersist(fred.getInternalAccount(), b219.getExternalAccount(), new BigDecimal("120"));
            assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("80")));
            assertEquals(0, b219.getExternalAccount().getAmount().compareTo(new BigDecimal("120")));
        } catch (final NotEnoughMoneyException e) {
            fail();
        }

        SessionManager.endWorkUnitAndFlush();
    }

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
            tom = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com", Locale.FRANCE);
            tom.setFullname("Thomas Guyard");
            SessionManager.flush();
        }
        {
            fred = DaoMember.createAndPersist("Fred", "other", "fred@gmail.com", Locale.FRANCE);
            fred.setFullname("Frédéric Bertolus");
            SessionManager.flush();
        }
        {
            yo = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com", Locale.FRANCE);
            yo.setFullname("Yoann Plénet");
            SessionManager.flush();

            DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
            DaoGroup.createAndPersiste("myGroup", "plop1@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
            (b219 = DaoGroup.createAndPersiste("b219", "plop2@plop.com", DaoGroup.Right.PROTECTED)).addMember(yo, true);
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

}
