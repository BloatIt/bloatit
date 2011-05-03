//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bloatit.data.DaoExternalAccount.AccountType;
import com.bloatit.data.exceptions.NotEnoughMoneyException;

public class DaoTransactionTest {

    @Test
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

    private DaoTeam b219;

    @Before
    public void setUp() throws Exception {
        SessionManager.generateTestSessionFactory();
        SessionManager.beginWorkUnit();
        {
            tom = DaoMember.createAndPersist("Thomas", "password", "salt", "tom@gmail.com", Locale.FRANCE);
            tom.setFullname("Thomas Guyard");
            SessionManager.flush();
        }
        {
            fred = DaoMember.createAndPersist("Fred", "other", "salt", "fred@gmail.com", Locale.FRANCE);
            fred.setFullname("Frédéric Bertolus");
            SessionManager.flush();
        }
        {
            yo = DaoMember.createAndPersist("Yoann", "plop", "salt", "yo@gmail.com", Locale.FRANCE);
            yo.setFullname("Yoann Plénet");
            SessionManager.flush();

            DaoTeam.createAndPersiste("Other", "plop@plop.com", "A group description", DaoTeam.Right.PUBLIC).addMember(yo, false);
            DaoTeam.createAndPersiste("myGroup", "plop1@plop.com", "A group description", DaoTeam.Right.PUBLIC).addMember(yo, false);
            (b219 = DaoTeam.createAndPersiste("b219", "plop2@plop.com", "A group description", DaoTeam.Right.PROTECTED)).addMember(yo, true);
        }

        SessionManager.endWorkUnitAndFlush();
    }

    @After
    public void tearDown() throws Exception {
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.endWorkUnitAndFlush();
        }
        SessionManager.getSessionFactory().close();
    }

}
