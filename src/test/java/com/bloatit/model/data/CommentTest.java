package com.bloatit.model.data;

import java.util.Iterator;
import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.model.data.util.SessionManger;

public class CommentTest extends TestCase {
	private DaoMember yo;
	private DaoMember tom;
	private DaoMember fred;

	private DaoDemand demand;

	protected void setUp() throws Exception {
		super.setUp();
		SessionManger.reCreateSessionFactory();
		SessionManger.beginWorkUnit();
		{
			tom = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com");
			tom.setFirstname("Thomas");
			tom.setLastname("Guyard");
			SessionManger.flush();
		}
		{
			fred = DaoMember.createAndPersist("Fred", "other", "fred@gmail.com");
			fred.setFirstname("Frédéric");
			fred.setLastname("Bertolus");
			SessionManger.flush();
		}
		{
			yo = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com");
			yo.setFirstname("Yoann");
			yo.setLastname("Plénet");
			SessionManger.flush();

			DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
			DaoGroup.createAndPersiste("myGroup", "plop@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
			(DaoGroup.createAndPersiste("b219", "plop@plop.com", DaoGroup.Right.PRIVATE)).addMember(yo, true);
		}

		demand = DaoDemand.createAndPersist(yo, new DaoDescription(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));

		SessionManger.endWorkUnitAndFlush();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if (SessionManger.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
			SessionManger.endWorkUnitAndFlush();
		}
		SessionManger.getSessionFactory().close();
	}

	public void testDaoCommentDaoActorString() {
		SessionManger.beginWorkUnit();

		DaoComment comment = DaoComment.createAndPersist(yo, "Prems !");
		demand.addComment(comment);

		assertEquals("Prems !", comment.getText());

		SessionManger.endWorkUnitAndFlush();

		SessionManger.beginWorkUnit();
		assertEquals("Prems !", DBRequests.getAll(DaoComment.class).iterator().next().getText());
		SessionManger.endWorkUnitAndFlush();
	}

	public void testAddChildComment() {
		SessionManger.beginWorkUnit();

		DaoComment comment = DaoComment.createAndPersist(yo, "Prems !");
		demand.addComment(comment);
		comment.addChildComment(DaoComment.createAndPersist(tom, "Pff espèce de Kevin !"));

		SessionManger.endWorkUnitAndFlush();

		SessionManger.beginWorkUnit();
		Iterator<DaoComment> it = DBRequests.getAll(DaoComment.class).iterator();
		
		DaoComment first = it.next();
		DaoComment next = it.next();
		
		System.out.println(first.getAuthor().getLogin());
		
		// TODO find why it does not work directly 
		assertEquals(yo.getLogin(), first.getAuthor().getLogin());
		assertEquals(tom.getLogin(), next.getAuthor().getLogin());
		assertFalse(it.hasNext());

		SessionManger.endWorkUnitAndFlush();
	}

	public void testGetChildren() {
		SessionManger.beginWorkUnit();

		DaoComment comment = DaoComment.createAndPersist(yo, "Prems !");
		demand.addComment(comment);
		DaoComment child = DaoComment.createAndPersist(tom, "Pff espèce de Kevin !");
		comment.addChildComment(child);

		SessionManger.endWorkUnitAndFlush();
		SessionManger.beginWorkUnit();

		assertEquals(1, comment.getChildren().size());
		assertTrue(comment.getChildren().contains(child));

		SessionManger.endWorkUnitAndFlush();
	}
}
