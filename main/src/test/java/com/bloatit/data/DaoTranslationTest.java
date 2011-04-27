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

import java.util.Locale;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

public class DaoTranslationTest extends DataTestUnit {

    public void testDaoTranslationDaoMemberDaoDescriptionLocaleStringString() {
        final DaoDescription description = DaoDescription.createAndPersist(yo, null, Locale.ENGLISH, "English title", "English body");

        new DaoTranslation(fred, null, description, Locale.FRANCE, "plop", "plip");
        try {
            new DaoTranslation(fred, null, description, Locale.FRANCE, "plop", "");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, null, description, Locale.FRANCE, "", "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, null, description, Locale.FRANCE, "plop", null);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, null, description, Locale.FRANCE, null, "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, null, description, null, "plop", "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, null, null, Locale.FRANCE, "plop", "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(null, null, description, Locale.FRANCE, "plop", "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
    }

    public void testUniqunessOfTranslation() {
        final DaoDescription description = DaoDescription.createAndPersist(yo, null, Locale.ENGLISH, "English title", "English body");

        description.addTranslation(new DaoTranslation(fred, null, description, Locale.FRANCE, "plop", "plip"));
        try {
            description.addTranslation(new DaoTranslation(yo, null, description, Locale.FRANCE, "plup", "plyp"));

            SessionManager.endWorkUnitAndFlush();
            fail();
        } catch (final Exception e) {
            assertTrue(true);
            SessionManager.beginWorkUnit();
        }
    }
}
