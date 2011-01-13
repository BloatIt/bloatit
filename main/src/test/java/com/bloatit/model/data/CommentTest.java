package com.bloatit.model.data;

import java.util.Iterator;

import com.bloatit.common.FatalErrorException;
import com.bloatit.model.data.util.NonOptionalParameterException;

public class CommentTest extends ModelTestUnit {

    public void testCreateAndPersist() {
        DaoComment comment = DaoComment.createAndPersist(DaoMember.getByLogin(yo.getLogin()), "A text");
        assertEquals("A text", comment.getText());
        try {
            DaoComment.createAndPersist(null, "A text");
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoComment.createAndPersist(DaoMember.getByLogin(yo.getLogin()), "");
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            DaoComment.createAndPersist(DaoMember.getByLogin(yo.getLogin()), null);
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
    }

    public void testAddChildComment() {
        DaoComment comment = DaoComment.createAndPersist(DaoMember.getByLogin(yo.getLogin()), "A text");
        DaoComment commentChild = DaoComment.createAndPersist(DaoMember.getByLogin(fred.getLogin()), "A comment");
        DaoComment commentChildChild = DaoComment.createAndPersist(DaoMember.getByLogin(tom.getLogin()), "An other text");
        DaoComment commentChild1 = DaoComment.createAndPersist(DaoMember.getByLogin(yo.getLogin()), "hello");

        comment.addChildComment(commentChild);
        comment.addChildComment(commentChild1);
        commentChild.addChildComment(commentChildChild);


        try {
            comment.addChildComment(comment);
            fail();
        } catch (FatalErrorException e) {
            assertTrue(true);
        }

        try {
            comment.addChildComment(null);
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
    }

    public void testGetChildrenFromQuery() {
        DaoComment comment = DaoComment.createAndPersist(DaoMember.getByLogin(yo.getLogin()), "A text");
        DaoComment commentChild = DaoComment.createAndPersist(DaoMember.getByLogin(fred.getLogin()), "A comment");
        DaoComment commentChild1 = DaoComment.createAndPersist(DaoMember.getByLogin(yo.getLogin()), "hello");
        DaoComment commentChild2 = DaoComment.createAndPersist(DaoMember.getByLogin(tom.getLogin()), "An other text");
        DaoComment commentChildChild = DaoComment.createAndPersist(DaoMember.getByLogin(tom.getLogin()), "An other text");

        comment.addChildComment(commentChild);
        comment.addChildComment(commentChild1);
        comment.addChildComment(commentChild2);
        commentChild1.addChildComment(commentChildChild);

        Iterator<DaoComment> it = comment.getChildren().iterator();
        assertEquals(it.next().getId(), commentChild.getId());
        assertEquals(it.next().getId(), commentChild1.getId());
        assertEquals(it.next().getId(), commentChild2.getId());
        assertEquals(it.hasNext(), false);
    }

}
