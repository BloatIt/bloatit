package com.bloatit.model.data;

import java.util.Iterator;
import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.model.data.util.SessionManager;

public class CommentTest extends TestCase {
    private DaoMember yo;
    private DaoMember tom;
    private DaoMember fred;

    private DaoDemand demand;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.reCreateSessionFactory();
        SessionManager.beginWorkUnit();
        {
            tom = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com");
            tom.setFullname("Thomas Guyard");
            SessionManager.flush();
        }
        {
            fred = DaoMember.createAndPersist("Fred", "other", "fred@gmail.com");
            fred.setFullname("Frédéric Bertolus");
            SessionManager.flush();
        }
        {
            yo = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com");
            yo.setFullname("Yoann Plénet");
            SessionManager.flush();

            DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
            DaoGroup.createAndPersiste("myGroup", "plop@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
            (DaoGroup.createAndPersiste("b219", "plop@plop.com", DaoGroup.Right.PROTECTED)).addMember(yo, true);
        }

        demand = DaoDemand.createAndPersist(yo, new DaoDescription(yo, new Locale("fr"), "Ma super demande !",
                "Ceci est la descption de ma demande :) "));

        SessionManager.endWorkUnitAndFlush();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.endWorkUnitAndFlush();
        }
        SessionManager.getSessionFactory().close();
    }

    public void testDaoCommentDaoActorString() {
        SessionManager.beginWorkUnit();

        final DaoComment comment = DaoComment.createAndPersist(yo, "Prems !");
        demand.addComment(comment);

        assertEquals("Prems !", comment.getText());

        SessionManager.endWorkUnitAndFlush();

        SessionManager.beginWorkUnit();
        assertEquals("Prems !", DBRequests.getAll(DaoComment.class).iterator().next().getText());
        SessionManager.endWorkUnitAndFlush();
    }

    public void testAddChildComment() {
        SessionManager.beginWorkUnit();

        final DaoComment comment = DaoComment.createAndPersist(yo, "Prems !");
        demand.addComment(comment);
        comment.addChildComment(DaoComment.createAndPersist(tom, "Pff espèce de Kevin !"));

        SessionManager.endWorkUnitAndFlush();

        SessionManager.beginWorkUnit();
        final Iterator<DaoComment> it = DBRequests.getAll(DaoComment.class).iterator();

        final DaoComment first = it.next();
        final DaoComment next = it.next();

        // TODO find why it does not work directly
        assertEquals(yo.getLogin(), first.getAuthor().getLogin());
        assertEquals(tom.getLogin(), next.getAuthor().getLogin());
        assertFalse(it.hasNext());

        SessionManager.endWorkUnitAndFlush();
    }

    public void testGetChildren() {
        SessionManager.beginWorkUnit();

        final DaoComment comment = DaoComment.createAndPersist(yo, "Prems !");
        demand.addComment(comment);
        final DaoComment child = DaoComment.createAndPersist(tom, "Pff espèce de Kevin !");
        comment.addChildComment(child);

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();

        assertEquals(1, comment.getChildren().size());
        assertTrue(comment.getChildren().contains(child));

        SessionManager.endWorkUnitAndFlush();
    }
}
