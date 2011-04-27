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

import java.util.Iterator;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

public class DaoCommentTest extends DataTestUnit {

    public void testCreateAndPersist() {
        final DaoComment comment = DaoComment.createAndPersist((DaoComment) null, null, DaoMember.getByLogin(yo.getLogin()), "A text");
        assertEquals("A text", comment.getText());
        try {
            DaoComment.createAndPersist((DaoComment) null, null, null, "A text");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoComment.createAndPersist((DaoComment) null, null, DaoMember.getByLogin(yo.getLogin()), "");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoComment.createAndPersist((DaoComment) null, null, DaoMember.getByLogin(yo.getLogin()), null);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
    }

    public void testAddChildComment() {
        final DaoComment comment = DaoComment.createAndPersist((DaoComment) null, null, DaoMember.getByLogin(yo.getLogin()), "A text");
        final DaoComment commentChild = DaoComment.createAndPersist(comment, null, DaoMember.getByLogin(fred.getLogin()), "A comment");
        comment.addChildComment(commentChild);
        final DaoComment commentChild1 = DaoComment.createAndPersist(comment, null, DaoMember.getByLogin(yo.getLogin()), "hello");
        comment.addChildComment(commentChild1);
        final DaoComment commentChild2 = DaoComment.createAndPersist(comment, null, DaoMember.getByLogin(tom.getLogin()), "An other text");
        comment.addChildComment(commentChild2);
        final DaoComment commentChildChild = DaoComment.createAndPersist(commentChild1, null, DaoMember.getByLogin(tom.getLogin()), "An other text");
        commentChild1.addChildComment(commentChildChild);

        try {
            comment.addChildComment(comment);
            fail();
        } catch (final BadProgrammerException e) {
            assertTrue(true);
        }

        try {
            comment.addChildComment(null);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
    }

    public void testGetChildrenFromQuery() {
        final DaoComment comment = DaoComment.createAndPersist((DaoComment) null, null, DaoMember.getByLogin(yo.getLogin()), "A text");
        final DaoComment commentChild = DaoComment.createAndPersist(comment, null, DaoMember.getByLogin(fred.getLogin()), "A comment");
        comment.addChildComment(commentChild);
        final DaoComment commentChild1 = DaoComment.createAndPersist(comment, null, DaoMember.getByLogin(yo.getLogin()), "hello");
        comment.addChildComment(commentChild1);
        final DaoComment commentChild2 = DaoComment.createAndPersist(comment, null, DaoMember.getByLogin(tom.getLogin()), "An other text");
        comment.addChildComment(commentChild2);
        final DaoComment commentChildChild = DaoComment.createAndPersist(commentChild1, null, DaoMember.getByLogin(tom.getLogin()), "An other text");
        commentChild1.addChildComment(commentChildChild);

        final Iterator<DaoComment> it = comment.getChildren().iterator();
        assertEquals(it.next().getId(), commentChild.getId());
        assertEquals(it.next().getId(), commentChild1.getId());
        assertEquals(it.next().getId(), commentChild2.getId());
        assertEquals(it.hasNext(), false);
    }

}
