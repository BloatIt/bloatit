package com.bloatit.data;

import java.math.BigDecimal;
import java.util.Locale;

import com.bloatit.data.queries.DBRequests;
import com.bloatit.data.search.FeatureSearch;
import com.bloatit.framework.utils.datetime.DateUtils;

/**
 * I assume the DaoGroupMemberTest is run without error.
 */
public class DaoFeatureTest extends DataTestUnit {

    public void testCreateFeature() {
        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               new Locale("fr"),
                                                                                               "Ma super demande !",
                                                                                               "Ceci est la descption de ma demande :) "),
                                                               project);

        assertEquals(feature, yo.getFeatures(false).iterator().next());
    }

    public void testRetrieveFeature() {
        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               new Locale("fr"),
                                                                                               "Ma super demande !",
                                                                                               "Ceci est la descption de ma demande :) "),
                                                               project);

        assertEquals(feature, DBRequests.getAll(DaoFeature.class).iterator().next());

        assertEquals(yo, feature.getMember());
    }

    public void testDeleteFeature() {
        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               new Locale("fr"),
                                                                                               "Ma super demande !",
                                                                                               "Ceci est la descption de ma demande :) "),
                                                               project);
        SessionManager.flush();

        feature.delete();

        assertFalse(DBRequests.getAll(DaoFeature.class).iterator().hasNext());
    }

    public void testAddContribution() throws Throwable {
        DaoFeature feature = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                             new Locale("fr"),
                                                                                             "Ma super demande !",
                                                                                             "Ceci est la descption de ma demande :) "), project);
        fred.getInternalAccount().setAmount(new BigDecimal("100"));
        yo.getInternalAccount().setAmount(new BigDecimal("100"));

        feature.addContribution(fred, new BigDecimal("25.00"), "Contribution");
        feature.addContribution(yo, new BigDecimal("18.00"), "I'm so generous");

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();

        feature = DBRequests.getById(DaoFeature.class, feature.getId());

        assertEquals(2, feature.getContributions().size());
        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("25")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("75")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("18")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("82")));
    }

    public void testAddOffer() {
        DaoFeature feature = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                             new Locale("fr"),
                                                                                             "Ma super demande !",
                                                                                             "Ceci est la descption de ma demande :) "), project);

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();
        feature = DBRequests.getById(DaoFeature.class, feature.getId());

        feature.addOffer(createOffer(feature));

        assertEquals(1, feature.getOffers().size());
    }

    public void testAddComment() throws Throwable {
        DaoFeature feature = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                             new Locale("fr"),
                                                                                             "Ma super demande !",
                                                                                             "Ceci est la descption de ma demande :) "), project);
        feature.addComment(DaoComment.createAndPersist(feature, yo, "4"));
        feature.addComment(DaoComment.createAndPersist(feature, yo, "3"));
        feature.addComment(DaoComment.createAndPersist(feature, yo, "2"));
        feature.addComment(DaoComment.createAndPersist(feature, yo, "1"));

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();
        feature = DBRequests.getById(DaoFeature.class, feature.getId());

        assertEquals(4, feature.getComments().size());
    }

    public void testAcceptContributions() throws Throwable {
        fred.getInternalAccount().setAmount(new BigDecimal(50));
        yo.getInternalAccount().setAmount(new BigDecimal(50));

        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               new Locale("fr"),
                                                                                               "Ma super demande !",
                                                                                               "Ceci est la descption de ma demande :) "),
                                                               project);
        final DaoOffer offer = createOffer(feature);
        feature.addOffer(offer);

        feature.addContribution(fred, new BigDecimal("25.00"), "I'm so generous too");
        feature.addContribution(yo, new BigDecimal("18.00"), "I'm so generous too");

        for (final DaoContribution Contribution : feature.getContributions()) {
            Contribution.validate(offer, 100);
        }

        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("68")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("32")));
    }

    private DaoOffer createOffer(final DaoFeature feature) {
        return new DaoOffer(fred,
                            feature,
                            new BigDecimal("200"),
                            DaoDescription.createAndPersist(fred, new Locale("fr"), "Ma super offre !", "Ceci est la descption de mon Offre:) "),
                            DateUtils.tomorrow(),
                            0);
    }

    public void testRejectContribution() throws Throwable {
        fred = DBRequests.getById(DaoMember.class, fred.getId());
        yo = DBRequests.getById(DaoMember.class, yo.getId());
        DaoFeature feature = DaoFeature.createAndPersist(yo, DaoDescription.createAndPersist(yo,
                                                                                             new Locale("fr"),
                                                                                             "Ma super demande !",
                                                                                             "Ceci est la descption de ma demande :) "), project);
        feature.addOffer(createOffer(feature));
        fred.getInternalAccount().setAmount(new BigDecimal("100"));
        yo.getInternalAccount().setAmount(new BigDecimal("100"));
        feature.addContribution(fred, new BigDecimal("25.00"), "I'm so generous too");
        feature.addContribution(yo, new BigDecimal("18.00"), "I'm so generous too");

        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();
        feature = DBRequests.getById(DaoFeature.class, feature.getId());
        fred = DBRequests.getById(DaoMember.class, fred.getId());
        yo = DBRequests.getById(DaoMember.class, yo.getId());

        for (final DaoContribution contribution : feature.getContributions()) {
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
        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               new Locale("fr"),
                                                                                               "Ma super demande !",
                                                                                               "Ceci est la descption de ma demande :) "),
                                                               project);

        final DaoOffer offer = createOffer(feature);
        feature.addOffer(offer);
        SessionManager.flush();

        feature.computeSelectedOffer();
        assertEquals(feature.getSelectedOffer(), offer);
    }

    public void testSearchFeature() {
        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               new Locale("fr"),
                                                                                               "Ma super demande !",
                                                                                               "Ceci est la descption de ma demande :) "),
                                                               project);
        feature.addOffer(createOffer(feature));

        // This is needed to index the new Feature.
        SessionManager.endWorkUnitAndFlush();
        SessionManager.beginWorkUnit();

        final FeatureSearch search = new FeatureSearch("super");

        assertTrue(search.doSearch().size() > 0);
    }

    public void testGetComment() {
        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               new Locale("fr"),
                                                                                               "Ma super demande !",
                                                                                               "Ceci est la descption de ma demande :) "),
                                                               project);
        feature.addComment(DaoComment.createAndPersist(feature, yo, "plop"));
        assertNotNull(feature.getComments().iterator().next());
    }

}
