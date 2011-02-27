package com.bloatit.data;

import java.util.Iterator;

import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.NonOptionalParameterException;

public class DaoCommentTest extends DataTestUnit {

    public void testCreateAndPersist() {
        final DaoComment comment = DaoComment.createAndPersist((DaoComment)null, DaoMember.getByLogin(yo.getLogin()), "A text");
        assertEquals("A text", comment.getText());
        try {
            DaoComment.createAndPersist((DaoComment)null, null, "A text");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoComment.createAndPersist((DaoComment)null, DaoMember.getByLogin(yo.getLogin()), "");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoComment.createAndPersist((DaoComment)null, DaoMember.getByLogin(yo.getLogin()), null);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
    }

    public void testAddChildComment() {
        final DaoComment comment = DaoComment.createAndPersist((DaoComment) null, DaoMember.getByLogin(yo.getLogin()), "A text");
        final DaoComment commentChild = DaoComment.createAndPersist(comment, DaoMember.getByLogin(fred.getLogin()), "A comment");
        comment.addChildComment(commentChild);
        final DaoComment commentChild1 = DaoComment.createAndPersist(comment, DaoMember.getByLogin(yo.getLogin()), "hello");
        comment.addChildComment(commentChild1);
        final DaoComment commentChild2 = DaoComment.createAndPersist(comment, DaoMember.getByLogin(tom.getLogin()), "An other text");
        comment.addChildComment(commentChild2);
        final DaoComment commentChildChild = DaoComment.createAndPersist(commentChild1, DaoMember.getByLogin(tom.getLogin()), "An other text");
        commentChild1.addChildComment(commentChildChild);

        try {
            comment.addChildComment(comment);
            fail();
        } catch (final FatalErrorException e) {
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
        final DaoComment comment = DaoComment.createAndPersist((DaoComment) null, DaoMember.getByLogin(yo.getLogin()), "A text");
        final DaoComment commentChild = DaoComment.createAndPersist(comment, DaoMember.getByLogin(fred.getLogin()), "A comment");
        comment.addChildComment(commentChild);
        final DaoComment commentChild1 = DaoComment.createAndPersist(comment, DaoMember.getByLogin(yo.getLogin()), "hello");
        comment.addChildComment(commentChild1);
        final DaoComment commentChild2 = DaoComment.createAndPersist(comment, DaoMember.getByLogin(tom.getLogin()), "An other text");
        comment.addChildComment(commentChild2);
        final DaoComment commentChildChild = DaoComment.createAndPersist(commentChild1, DaoMember.getByLogin(tom.getLogin()), "An other text");
        commentChild1.addChildComment(commentChildChild);


        final Iterator<DaoComment> it = comment.getChildren().iterator();
        assertEquals(it.next().getId(), commentChild.getId());
        assertEquals(it.next().getId(), commentChild1.getId());
        assertEquals(it.next().getId(), commentChild2.getId());
        assertEquals(it.hasNext(), false);
    }

}
