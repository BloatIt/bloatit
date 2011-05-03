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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.bloatit.data.DaoExternalAccount.AccountType;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

/**
 * The class <code>DaoAccountTest</code> contains tests for the class
 * <code>{@link DaoAccount}</code>.
 * 
 * @generatedBy CodePro at 27/01/11 16:38
 * @author tom
 * @version $Revision: 1.0 $
 */
public class DaoAccountTest extends DataTestUnit {

    @Test
    public void testDaoAccountDaoActor() {
        final DaoMember localTom = DaoMember.getByLogin(tom.getLogin());
        localTom.setExternalAccount(DaoExternalAccount.createAndPersist(localTom, AccountType.IBAN, "Bank code !"));

        final DaoTeam localB219 = DaoTeam.getByName(b219.getLogin());
        localB219.setExternalAccount(DaoExternalAccount.createAndPersist(localB219, AccountType.IBAN, "Bank code !"));

        try {
            localTom.setExternalAccount(DaoExternalAccount.createAndPersist(null, AccountType.IBAN, "Bank code !"));
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            localTom.setExternalAccount(DaoExternalAccount.createAndPersist(localTom, null, "Bank code !"));
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            localTom.setExternalAccount(DaoExternalAccount.createAndPersist(localTom, AccountType.IBAN, null));
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            localTom.setExternalAccount(DaoExternalAccount.createAndPersist(localTom, AccountType.IBAN, ""));
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }

        try {
            localTom.setExternalAccount(DaoExternalAccount.createAndPersist(localB219, AccountType.IBAN, "code"));
            fail();
        } catch (final BadProgrammerException e) {
            assertTrue(true);
        }
    }

}
