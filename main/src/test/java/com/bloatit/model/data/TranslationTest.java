package com.bloatit.model.data;

import java.util.Locale;

import com.bloatit.model.data.util.NonOptionalParameterException;
import com.bloatit.model.data.util.SessionManager;

public class TranslationTest extends ModelTestUnit {

    public void testDaoTranslationDaoMemberDaoDescriptionLocaleStringString() {
        final DaoDescription description = DaoDescription.createAndPersist(yo, Locale.ENGLISH, "English title", "English body");

        new DaoTranslation(fred, description, Locale.FRANCE, "plop", "plip");
        try {
            new DaoTranslation(fred, description, Locale.FRANCE, "plop", "");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, description, Locale.FRANCE, "", "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, description, Locale.FRANCE, "plop", null);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, description, Locale.FRANCE, null, "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, description, null, "plop", "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, null, Locale.FRANCE, "plop", "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(null, description, Locale.FRANCE, "plop", "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
    }

    public void testUniqunessOfTranslation() {
        final DaoDescription description = DaoDescription.createAndPersist(yo, Locale.ENGLISH, "English title", "English body");

        description.addTranslation(new DaoTranslation(fred, description, Locale.FRANCE, "plop", "plip"));
        description.addTranslation(new DaoTranslation(yo, description, Locale.FRANCE, "plup", "plyp"));

        try {
            SessionManager.flush();
            fail();
        } catch (final Exception e) {
            assertTrue(true);
        }
    }
}
