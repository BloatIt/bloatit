package com.bloatit.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Test;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

public class DaoVersionedStringTest extends DataTestUnit {

    @Test
    public final void testDaoVersionedStringStringDaoMember() {
        final DaoVersionedString str = DaoVersionedString.createAndPersist("plop", fred);
        assertEquals(str.getContent(), "plop");

        final DaoVersionedString str1 = DaoVersionedString.createAndPersist("", fred);
        assertEquals(str1.getContent(), "");

        try {
            DaoVersionedString.createAndPersist(null, fred);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
    }

    @Test
    public final void testAddVersion() {
        final DaoVersionedString str = DaoVersionedString.createAndPersist("plop", fred);
        str.addVersion("plip", tom);
        assertEquals(str.getContent(), "plip");

        assertEquals(str.getCurrentVersion().getAuthor(), tom);
    }

    @Test
    public final void testChangeCurrentVersion() {
        final DaoVersionedString str = DaoVersionedString.createAndPersist("plop", fred);
        str.addVersion("plip", tom);
        assertEquals(str.getContent(), "plip");
        assertEquals(str.getCurrentVersion().getAuthor(), tom);

        final DaoStringVersion version = str.getVersions().iterator().next();
        str.useVersion(version);
        assertEquals(str.getContent(), "plop");
        assertEquals(str.getCurrentVersion().getAuthor(), fred);
    }

    @Test
    public final void testCompact() {
        final DaoVersionedString str = DaoVersionedString.createAndPersist("plop", fred);
        str.addVersion("plop\nplip\n", fred);
        str.addVersion("plop\nplup\n", fred);
        str.addVersion("plop\nplup\nplop\ncoucou\nHello\n", fred);
        str.addVersion("plop\nplup\nplop\ncoucou\n", fred);

        str.compact();
        Iterator<DaoStringVersion> iterator = str.getVersions().iterator();
        assertEquals(iterator.next().getContent(), "plop");
        assertEquals(iterator.next().getContentUncompacted("plop"), "plop\nplip\n");
        assertEquals(iterator.next().getContentUncompacted("plop\nplip\n"), "plop\nplup\n");
        assertEquals(iterator.next().getContentUncompacted("plop\nplup\n"), "plop\nplup\nplop\ncoucou\nHello\n");
        assertEquals(iterator.next().getContentUncompacted("plop\nplup\nplop\ncoucou\nHello\n"), "plop\nplup\nplop\ncoucou\n");

        iterator = str.getVersions().iterator();
        assertFalse(iterator.next().isCompacted());
        assertTrue(iterator.next().isCompacted());
        assertTrue(iterator.next().isCompacted());
        assertTrue(iterator.next().isCompacted());
        assertTrue(iterator.next().isCompacted());
    }

}
