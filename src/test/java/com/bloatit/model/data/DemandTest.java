package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.model.data.Demand;
import com.bloatit.model.data.Group;
import com.bloatit.model.data.Member;
import com.bloatit.model.data.Description;
import com.bloatit.model.data.util.SessionManger;

/**
 * I assume the GroupMemberTest is run without error.
 */
public class DemandTest extends TestCase {

    private Member yo;
    private Member tom;
    private Member fred;

    protected void setUp() throws Exception {
        super.setUp();
        SessionManger.reCreateSessionFactory();
        SessionManger.beginWorkUnit();
        {
            tom = Member.createAndPersist("Thomas", "password", "tom@gmail.com");
            tom.setFirstname("Thomas");
            tom.setLastname("Guyard");
            SessionManger.flush();
        }
        {
            fred = Member.createAndPersist("Fred", "other", "fred@gmail.com");
            fred.setFirstname("Frédéric");
            fred.setLastname("Bertolus");
            SessionManger.flush();
        }
        {
            yo = Member.createAndPersist("Yo", "plop", "yo@gmail.com");
            yo.setFirstname("Yoann");
            yo.setLastname("Plénet");
            SessionManger.flush();

            Group.createAndPersiste("Other", "plop@plop.com", Group.Right.PUBLIC).addMember(yo, false);
            Group.createAndPersiste("myGroup", "plop@plop.com", Group.Right.PUBLIC).addMember(yo, false);
            Group.createAndPersiste("b219", "plop@plop.com", Group.Right.PRIVATE).addMember(yo, true);
        }

        SessionManger.endWorkUnitAndFlush();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        if (SessionManger.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManger.endWorkUnitAndFlush();
        }
        SessionManger.getSessionFactory().close();
    }

    public void testCreateDemand() {
        SessionManger.beginWorkUnit();

        Demand demand = Demand.createAndPersist(yo, new Description(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));

        assertEquals(demand, yo.getDemands().iterator().next());

        SessionManger.endWorkUnitAndFlush();

    }

    public void testAddSpecification() {
        SessionManger.beginWorkUnit();

        Demand demand = Demand.createAndPersist(yo, new Description(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));

        demand.createSpecification(tom, "This is the spécification");

        assertNotNull(demand.getSpecification());

        SessionManger.endWorkUnitAndFlush();
    }

    public void testRetrieveDemand() {
        SessionManger.beginWorkUnit();

        Demand demand = Demand.createAndPersist(yo, new Description(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));

        demand.createSpecification(tom, "This is the spécification");

        assertEquals(demand, DBRequests.getAll(Demand.class).iterator().next());

        assertEquals(yo, demand.getAuthor());

        SessionManger.endWorkUnitAndFlush();
    }

    public void testDeleteDemand() {
        SessionManger.beginWorkUnit();

        Demand demand = Demand.createAndPersist(yo, new Description(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        demand.createSpecification(tom, "This is the spécification");

        SessionManger.flush();

        demand.delete();

        assertFalse(DBRequests.getAll(Demand.class).iterator().hasNext());

        SessionManger.endWorkUnitAndFlush();

    }

    public void testAddContribution() throws Throwable {
        SessionManger.beginWorkUnit();

        Demand demand = Demand.createAndPersist(yo, new Description(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        demand.createSpecification(tom, "This is the spécification");

        SessionManger.flush();

        demand.addContribution(fred, new BigDecimal("25.00"));
        demand.addContribution(yo, new BigDecimal("18.00"));

        SessionManger.flush();

        assertEquals(2, demand.getContributions().size());

        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("25")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("-25")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("18")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("-18")));

        SessionManger.endWorkUnitAndFlush();
    }

    public void testAddOffer() throws Throwable {
        SessionManger.beginWorkUnit();

        Demand demand = Demand.createAndPersist(yo, new Description(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        demand.createSpecification(tom, "This is the spécification");

        SessionManger.flush();

        demand.addOffer(fred, new Description(fred, new Locale("fr"), "Ma super offre !", "Ceci est la descption de mon Offre:) "), new Date());

        SessionManger.flush();

        assertEquals(1, demand.getOffers().size());

        SessionManger.endWorkUnitAndFlush();

    }

    public void testAcceptContributions() throws Throwable {
        SessionManger.beginWorkUnit();

        Demand demand = Demand.createAndPersist(yo, new Description(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        demand.createSpecification(tom, "This is the spécification");

        Offer offer = demand.addOffer(fred, new Description(fred, new Locale("fr"), "Ma super offre !", "Ceci est la descption de mon Offre:) "), new Date());
        SessionManger.flush();

        demand.addContribution(fred, new BigDecimal("25.00"));
        demand.addContribution(yo, new BigDecimal("18.00"));

        SessionManger.flush();

        for (Contribution contribution : demand.getContributions()) {
            contribution.accept(offer);
        }

        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("18")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("-18")));

        SessionManger.endWorkUnitAndFlush();

    }

    public void testRejectContribution() throws Throwable {
        SessionManger.beginWorkUnit();

        Demand demand = Demand.createAndPersist(yo, new Description(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        demand.createSpecification(tom, "This is the spécification");

        demand.addOffer(fred, new Description(fred, new Locale("fr"), "Ma super offre !", "Ceci est la descption de mon Offre:) "), new Date());
        SessionManger.flush();

        demand.addContribution(fred, new BigDecimal("25.00"));
        demand.addContribution(yo, new BigDecimal("18.00"));

        SessionManger.flush();

        for (Contribution contribution : demand.getContributions()) {
            contribution.cancel();
        }

        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("0")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("0")));

        SessionManger.endWorkUnitAndFlush();

    }

}
