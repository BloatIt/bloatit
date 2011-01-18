package com.bloatit.model.data;

import java.util.Locale;

import com.bloatit.model.data.util.NonOptionalParameterException;

public class DescriptionTest extends ModelTestUnit {

    public void testCreateAndPersist() {
        DaoDescription.createAndPersist(DaoMember.getByLogin(yo.getLogin()), Locale.FRANCE, "A title", "a text");

        try {
            DaoDescription.createAndPersist(DaoMember.getByLogin(yo.getLogin()), Locale.FRANCE, "", "a text");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoDescription.createAndPersist(DaoMember.getByLogin(yo.getLogin()), Locale.FRANCE, "A title", "");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoDescription.createAndPersist(DaoMember.getByLogin(yo.getLogin()), Locale.FRANCE, "A title", null);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoDescription.createAndPersist(DaoMember.getByLogin(yo.getLogin()), Locale.FRANCE, null, "a text");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoDescription.createAndPersist(DaoMember.getByLogin(yo.getLogin()), null, "A title", "a text");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoDescription.createAndPersist(null, Locale.FRANCE, "A title", "a text");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
    }
}
