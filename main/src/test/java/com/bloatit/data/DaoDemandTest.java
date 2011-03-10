package com.bloatit.data;

import java.math.BigDecimal;
import java.util.Locale;

import com.bloatit.data.queries.DBRequests;
import com.bloatit.data.search.DemandSearch;
import com.bloatit.framework.utils.DateUtils;

/**
 * I assume the DaoGroupMemberTest is run without error.
 */
public class DaoDemandTest extends DataTestUnit {

    public void testCreateDemand() {
        final DaoFeature demand = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                                new Locale("fr"),
                                                                                                "Ma super demande !",
                                                                                                "Ceci est la descption de ma demande :) "), project);

        assertEquals(demand, yo.getDemands().iterator().next());
    }

    public void testRetrieveDemand() {
        final DaoFeature demand = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                                new Locale("fr"),
                                                                                                "Ma super demande !",
                                                                                                "Ceci est la descption de ma demande :) "), project);

        assertEquals(demand, DBRequests.getAll(DaoFeature.class).iterator().next());

        assertEquals(yo, demand.getAuthor());
    }

    public void testDeleteDemand() {
        final DaoFeature demand = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                                new Locale("fr"),
                                                                                                "Ma super demande !",
                                                                                                "Ceci est la descption de ma demande :) "), project);
        SessionManager.flush();

        demand.delete();

        assertFalse(DBRequests.getAll(DaoFeature.class).iterator().hasNext());
    }

    public void testAddContribution() throws Throwable {
        DaoFeature demand = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                          new Locale("fr"),
                                                                                          "Ma super demande !",
                                                                                          "Ceci est la descption de ma demande :) "), project);
        fred.getInternalAccount().setAmount(new BigDecimal("100"));
        yo.getInternalAccount().setAmount(new BigDecimal("100"));

        demand.addContribution(fred, new BigDecimal("25.00"), "Contribution");
        demand.addContribution(yo, new BigDecimal("18.00"), "I'm so generous");

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();

        demand = DBRequests.getById(DaoFeature.class, demand.getId());

        assertEquals(2, demand.getContributions().size());
        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("25")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("75")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("18")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("82")));
    }

    public void testAddOffer() {
        DaoFeature demand = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                          new Locale("fr"),
                                                                                          "Ma super demande !",
                                                                                          "Ceci est la descption de ma demande :) "), project);

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();
        demand = DBRequests.getById(DaoFeature.class, demand.getId());

        demand.addOffer(createOffer(demand));

        assertEquals(1, demand.getOffers().size());
    }

    public void testAddComment() throws Throwable {
        DaoFeature demand = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                          new Locale("fr"),
                                                                                          "Ma super demande !",
                                                                                          "Ceci est la descption de ma demande :) "), project);
        demand.addComment(DaoComment.createAndPersist(demand, yo, "4"));
        demand.addComment(DaoComment.createAndPersist(demand, yo, "3"));
        demand.addComment(DaoComment.createAndPersist(demand, yo, "2"));
        demand.addComment(DaoComment.createAndPersist(demand, yo, "1"));

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();
        demand = DBRequests.getById(DaoFeature.class, demand.getId());

        assertEquals(4, demand.getComments().size());
    }

    public void testAcceptContributions() throws Throwable {
        fred.getInternalAccount().setAmount(new BigDecimal(50));
        yo.getInternalAccount().setAmount(new BigDecimal(50));

        final DaoFeature demand = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                                new Locale("fr"),
                                                                                                "Ma super demande !",
                                                                                                "Ceci est la descption de ma demande :) "), project);
        final DaoOffer offer = createOffer(demand);
        demand.addOffer(offer);

        demand.addContribution(fred, new BigDecimal("25.00"), "I'm so generous too");
        demand.addContribution(yo, new BigDecimal("18.00"), "I'm so generous too");

        for (final DaoContribution Contribution : demand.getContributions()) {
            Contribution.validate(offer, 100);
        }

        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("68")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("32")));
    }

    private DaoOffer createOffer(final DaoFeature demand) {
        return new DaoOffer(fred,
                            demand,
                            new BigDecimal("200"),
                            DaoDescription.createAndPersist(fred, new Locale("fr"), "Ma super offre !", "Ceci est la descption de mon Offre:) "),
                            DateUtils.tomorrow(),
                            0);
    }

    public void testRejectContribution() throws Throwable {
        fred = DBRequests.getById(DaoMember.class, fred.getId());
        yo = DBRequests.getById(DaoMember.class, yo.getId());
        DaoFeature demand = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                          new Locale("fr"),
                                                                                          "Ma super demande !",
                                                                                          "Ceci est la descption de ma demande :) "), project);
        demand.addOffer(createOffer(demand));
        fred.getInternalAccount().setAmount(new BigDecimal("100"));
        yo.getInternalAccount().setAmount(new BigDecimal("100"));
        demand.addContribution(fred, new BigDecimal("25.00"), "I'm so generous too");
        demand.addContribution(yo, new BigDecimal("18.00"), "I'm so generous too");

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();
        demand = DBRequests.getById(DaoFeature.class, demand.getId());
        fred = DBRequests.getById(DaoMember.class, fred.getId());
        yo = DBRequests.getById(DaoMember.class, yo.getId());

        for (final DaoContribution contribution : demand.getContributions()) {
            contribution.cancel();
        }

        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("100")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("100")));
    }

    public void testGetCurrentOffer() {
        fred = DBRequests.getById(DaoMember.class, fred.getId());
        yo = DBRequests.getById(DaoMember.class, yo.getId());
        final DaoFeature demand = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                                new Locale("fr"),
                                                                                                "Ma super demande !",
                                                                                                "Ceci est la descption de ma demande :) "), project);

        final DaoOffer offer = createOffer(demand);
        demand.addOffer(offer);
        SessionManager.flush();

        demand.computeSelectedOffer();
        assertEquals(demand.getSelectedOffer(), offer);
    }

    public void testSearchDemand() {
        final DaoFeature demand = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                                new Locale("fr"),
                                                                                                "Ma super demande !",
                                                                                                "Ceci est la descption de ma demande :) "), project);
        demand.addOffer(createOffer(demand));

        // This is needed to index the new Demand.
        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();

        DemandSearch search = new DemandSearch("super");

        assertTrue(search.doSearch().size() > 0);
    }

    public void testGetComment() {
        final DaoFeature demand = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                                new Locale("fr"),
                                                                                                "Ma super demande !",
                                                                                                "Ceci est la descption de ma demande :) "), project);
        demand.addComment(DaoComment.createAndPersist(demand, yo, "plop"));
        assertNotNull(demand.getComments().iterator().next());
    }

}
