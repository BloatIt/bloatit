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

import java.util.Locale;

import org.junit.Test;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

public class DaoDescriptionTest extends DataTestUnit {

    @Test
    public void testCreateAndPersist() {
        DaoDescription.createAndPersist(DaoMember.getByLogin(yo.getLogin()), null, Locale.FRANCE, "A title", "a text");

        try {
            DaoDescription.createAndPersist(DaoMember.getByLogin(yo.getLogin()), null, Locale.FRANCE, "", "a text");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoDescription.createAndPersist(DaoMember.getByLogin(yo.getLogin()), null, Locale.FRANCE, "A title", "");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoDescription.createAndPersist(DaoMember.getByLogin(yo.getLogin()), null, Locale.FRANCE, "A title", null);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoDescription.createAndPersist(DaoMember.getByLogin(yo.getLogin()), null, Locale.FRANCE, null, "a text");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoDescription.createAndPersist(DaoMember.getByLogin(yo.getLogin()), null, null, "A title", "a text");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoDescription.createAndPersist(null, null, Locale.FRANCE, "A title", "a text");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
    }
}
