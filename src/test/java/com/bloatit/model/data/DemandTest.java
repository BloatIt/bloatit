package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

import com.bloatit.model.data.DaoDemand;
import com.bloatit.model.data.DaoGroup;
import com.bloatit.model.data.DaoMember;
import com.bloatit.model.data.DaoDescription;
import com.bloatit.model.data.util.SessionManger;

/**
 * I assume the GroupMemberTest is run without error.
 */
public class DemandTest extends TestCase {

    private DaoMember yo;
    private DaoMember tom;
    private DaoMember fred;
    
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

        DaoDemand Demand = DaoDemand.createAndPersist(yo, new DaoDescription(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));

        assertEquals(Demand, yo.getDemands().iterator().next());

        SessionManger.endWorkUnitAndFlush();

    }

    public void testAddSpecification() {
        SessionManger.beginWorkUnit();

        DaoDemand Demand = DaoDemand.createAndPersist(yo, new DaoDescription(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));

        Demand.createSpecification(tom, "This is the spécification");

        assertNotNull(Demand.getSpecification());

        SessionManger.endWorkUnitAndFlush();
    }

    public void testRetrieveDemand() {
        SessionManger.beginWorkUnit();

        DaoDemand Demand = DaoDemand.createAndPersist(yo, new DaoDescription(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));

        Demand.createSpecification(tom, "This is the spécification");

        assertEquals(Demand, DBRequests.getAll(DaoDemand.class).iterator().next());

        assertEquals(yo, Demand.getAuthor());

        SessionManger.endWorkUnitAndFlush();
    }

    public void testDeleteDemand() {
        SessionManger.beginWorkUnit();

        DaoDemand Demand = DaoDemand.createAndPersist(yo, new DaoDescription(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        Demand.createSpecification(tom, "This is the spécification");

        SessionManger.flush();

        Demand.delete();

        assertFalse(DBRequests.getAll(DaoDemand.class).iterator().hasNext());

        SessionManger.endWorkUnitAndFlush();

    }

    public void testAddContribution() throws Throwable {
        SessionManger.beginWorkUnit();

        DaoDemand Demand = DaoDemand.createAndPersist(yo, new DaoDescription(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        Demand.createSpecification(tom, "This is the spécification");

        SessionManger.flush();

        Demand.addContribution(fred, new BigDecimal("25.00"));
        Demand.addContribution(yo, new BigDecimal("18.00"));

        SessionManger.flush();

        assertEquals(2, Demand.getContributions().size());

        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("25")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("-25")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("18")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("-18")));

        SessionManger.endWorkUnitAndFlush();
    }

    public void testAddOffer() throws Throwable {
        SessionManger.beginWorkUnit();

        DaoDemand Demand = DaoDemand.createAndPersist(yo, new DaoDescription(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        Demand.createSpecification(tom, "This is the spécification");

        SessionManger.flush();

        Demand.addOffer(fred, new DaoDescription(fred, new Locale("fr"), "Ma super offre !", "Ceci est la descption de mon Offre:) "), new Date());

        SessionManger.flush();

        assertEquals(1, Demand.getOffers().size());

        SessionManger.endWorkUnitAndFlush();

    }

    public void testAcceptContributions() throws Throwable {
        SessionManger.beginWorkUnit();

        DaoDemand Demand = DaoDemand.createAndPersist(yo, new DaoDescription(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        Demand.createSpecification(tom, "This is the spécification");

        DaoOffer Offer = Demand.addOffer(fred, new DaoDescription(fred, new Locale("fr"), "Ma super offre !", "Ceci est la descption de mon Offre:) "), new Date());
        SessionManger.flush();

        Demand.addContribution(fred, new BigDecimal("25.00"));
        Demand.addContribution(yo, new BigDecimal("18.00"));

        SessionManger.flush();

        for (DaoContribution Contribution : Demand.getContributions()) {
            Contribution.accept(Offer);
        }

        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("18")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("-18")));

        SessionManger.endWorkUnitAndFlush();

    }

    public void testRejectContribution() throws Throwable {
        SessionManger.beginWorkUnit();

        DaoDemand Demand = DaoDemand.createAndPersist(yo, new DaoDescription(yo, new Locale("fr"), "Ma super demande !", "Ceci est la descption de ma demande :) "));
        Demand.createSpecification(tom, "This is the spécification");

        Demand.addOffer(fred, new DaoDescription(fred, new Locale("fr"), "Ma super offre !", "Ceci est la descption de mon Offre:) "), new Date());
        SessionManger.flush();

        Demand.addContribution(fred, new BigDecimal("25.00"));
        Demand.addContribution(yo, new BigDecimal("18.00"));

        SessionManger.flush();

        for (DaoContribution Contribution : Demand.getContributions()) {
            Contribution.cancel();
        }

        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("0")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("0")));

        SessionManger.endWorkUnitAndFlush();

    }

}
