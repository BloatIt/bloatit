package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.model.data.util.SessionManager;

/**
 * I assume the GroupMemberTest is run without error.
 */
public class DemandTest extends TestCase {

    private DaoMember yo;
    private DaoMember tom;
    private DaoMember fred;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SessionManager.reCreateSessionFactory();
        SessionManager.beginWorkUnit();
        {
            tom = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com");
            tom.setFullname("Thomas Guyard");
            tom.getInternalAccount().setAmount(new BigDecimal(100));
            SessionManager.flush();
        }
        {
            fred = DaoMember.createAndPersist("Fred", "other", "fred@gmail.com");
            fred.setFullname("Frédéric Bertolus");
            fred.getInternalAccount().setAmount(new BigDecimal(100));
            SessionManager.flush();
        }
        {
            yo = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com");
            yo.setFullname("Yoann Plénet");
            yo.getInternalAccount().setAmount(new BigDecimal(100));
            SessionManager.flush();

            DaoGroup.createAndPersiste("Other", "plop@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
            DaoGroup.createAndPersiste("myGroup", "plop@plop.com", DaoGroup.Right.PUBLIC).addMember(yo, false);
        }

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.endWorkUnitAndFlush();
        }
        SessionManager.getSessionFactory().close();
    }

    public void testCreateDemand() {
        final DaoDemand demand = DaoDemand.createAndPersist(yo,
                DaoDescription.createAndPersist(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));

        assertEquals(demand, yo.getDemands().iterator().next());
    }

    public void testAddSpecification() {
        final DaoDemand demand = DaoDemand.createAndPersist(yo,
                DaoDescription.createAndPersist(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));

        demand.createSpecification(tom, "This is the spécification");

        assertNotNull(demand.getSpecification());
    }

    public void testRetrieveDemand() {
        final DaoDemand demand = DaoDemand.createAndPersist(yo,
                DaoDescription.createAndPersist(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));

        demand.createSpecification(tom, "This is the spécification");

        assertEquals(demand, DBRequests.getAll(DaoDemand.class).iterator().next());

        assertEquals(yo, demand.getAuthor());
    }

    public void testDeleteDemand() {
        final DaoDemand demand = DaoDemand.createAndPersist(yo,
                DaoDescription.createAndPersist(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        demand.createSpecification(tom, "This is the spécification");

        SessionManager.flush();

        demand.delete();

        assertFalse(DBRequests.getAll(DaoDemand.class).iterator().hasNext());
    }

    public void testAddContribution() throws Throwable {
        DaoDemand demand = DaoDemand.createAndPersist(yo,
                DaoDescription.createAndPersist(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        demand.createSpecification(tom, "This is the spécification");
        demand.addContribution(fred, new BigDecimal("25.00"), "Contribution");
        demand.addContribution(yo, new BigDecimal("18.00"), "I'm so generous");

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();

        demand = DBRequests.getById(DaoDemand.class, demand.getId());

        assertEquals(2, demand.getContributions().size());
        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("25")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("75")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("18")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("82")));
    }

    public void testAddOffer() throws Throwable {
        DaoDemand demand = DaoDemand.createAndPersist(yo,
                DaoDescription.createAndPersist(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        demand.createSpecification(tom, "This is the spécification");

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();
        demand = DBRequests.getById(DaoDemand.class, demand.getId());

        demand.addOffer(fred,
                new BigDecimal("200"),
                DaoDescription.createAndPersist(fred, new Locale("fr"), "Ma super offre !", "Ceci est la descption de mon Offre:) "),
                new Date());

        assertEquals(1, demand.getOffers().size());
    }

    public void testAddComment() throws Throwable {
        DaoDemand demand = DaoDemand.createAndPersist(yo,
                DaoDescription.createAndPersist(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        demand.addComment(DaoComment.createAndPersist(yo, "4"));
        demand.addComment(DaoComment.createAndPersist(yo, "3"));
        demand.addComment(DaoComment.createAndPersist(yo, "2"));
        demand.addComment(DaoComment.createAndPersist(yo, "1"));

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();
        demand = DBRequests.getById(DaoDemand.class, demand.getId());

        assertEquals(4, demand.getComments().size());
        // TODO correct order.
        // assertEquals("4", demand.getComments().iterator().next().getText());
    }

    public void testAcceptContributions() throws Throwable {
        fred.getInternalAccount().setAmount(new BigDecimal(50));
        yo.getInternalAccount().setAmount(new BigDecimal(50));

        final DaoDemand demand = DaoDemand.createAndPersist(yo,
                DaoDescription.createAndPersist(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        demand.createSpecification(tom, "This is the spécification");
        final DaoOffer Offer = demand.addOffer(fred,
                new BigDecimal("200"),
                DaoDescription.createAndPersist(fred, new Locale("fr"), "Ma super offre !", "Ceci est la descption de mon Offre:) "),
                new Date());

        demand.addContribution(fred, new BigDecimal("25.00"), "I'm so generous too");
        demand.addContribution(yo, new BigDecimal("18.00"), "I'm so generous too");

        for (final DaoContribution Contribution : demand.getContributions()) {
            Contribution.accept(Offer);
        }

        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("68")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("32")));
    }

    public void testRejectContribution() throws Throwable {
        fred = DBRequests.getById(DaoMember.class, fred.getId());
        yo = DBRequests.getById(DaoMember.class, yo.getId());
        DaoDemand demand = DaoDemand.createAndPersist(yo,
                DaoDescription.createAndPersist(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        demand.createSpecification(tom, "This is the spécification");
        demand.addOffer(fred,
                new BigDecimal("200"),
                DaoDescription.createAndPersist(fred, new Locale("fr"), "Ma super offre !", "Ceci est la descption de mon Offre:) "),
                new Date());
        demand.addContribution(fred, new BigDecimal("25.00"), "I'm so generous too");
        demand.addContribution(yo, new BigDecimal("18.00"), "I'm so generous too");

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();
        demand = DBRequests.getById(DaoDemand.class, demand.getId());
        fred = DBRequests.getById(DaoMember.class, fred.getId());
        yo = DBRequests.getById(DaoMember.class, yo.getId());

        for (final DaoContribution Contribution : demand.getContributions()) {
            Contribution.cancel();
        }

        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("100")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("100")));
    }

    public void testSearchDemand() {
        final DaoDemand demand = DaoDemand.createAndPersist(yo,
                DaoDescription.createAndPersist(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        demand.createSpecification(tom, "This is the spécification");
        demand.addOffer(fred,
                new BigDecimal("200"),
                DaoDescription.createAndPersist(fred, new Locale("fr"), "Ma super offre !", "Ceci est la descption de mon Offre:) "),
                new Date());
        SessionManager.flush();

        assertTrue(DBRequests.searchDemands("super").iterator().hasNext());
    }

    public void testGetComment() {
        final DaoDemand demand = DaoDemand.createAndPersist(yo,
                DaoDescription.createAndPersist(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        demand.addComment(DaoComment.createAndPersist(yo, "plop"));
        assertNotNull(demand.getCommentsFromQuery().iterator().next());
    }

}
