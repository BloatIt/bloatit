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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Locale;

import org.junit.Test;

import com.bloatit.data.queries.DBRequests;
import com.bloatit.data.search.FeatureSearch;
import com.bloatit.framework.utils.datetime.DateUtils;

/**
 * I assume the DaoGroupMemberTest is run without error.
 */
public class DaoFeatureTest extends DataTestUnit {

    @Test
    public void testCreateFeature() {
        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               null,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               null,
                                                                                               new Locale("fr"),
                                                                                               "Ma super demande !",
                                                                                               "Ceci est la descption de ma demande :) "),
                                                               project);

        assertEquals(feature, yo.getFeatures(false).iterator().next());
    }

    @Test
    public void testRetrieveFeature() {
        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               null,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               null,
                                                                                               new Locale("fr"),
                                                                                               "Ma super demande !",
                                                                                               "Ceci est la descption de ma demande :) "),
                                                               project);

        assertEquals(feature, DBRequests.getAll(DaoFeature.class).iterator().next());

        assertEquals(yo, feature.getMember());
    }

    @Test
    public void testDeleteFeature() {
        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               null,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               null,
                                                                                               new Locale("fr"),
                                                                                               "Ma super demande !",
                                                                                               "Ceci est la descption de ma demande :) "),
                                                               project);
        SessionManager.flush();

        feature.delete();

        assertFalse(DBRequests.getAll(DaoFeature.class).iterator().hasNext());
    }

    @Test
    public void testAddContribution() throws Throwable {
        DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                         null,
                                                         DaoDescription.createAndPersist(yo,
                                                                                         null,
                                                                                         new Locale("fr"),
                                                                                         "Ma super demande !",
                                                                                         "Ceci est la descption de ma demande :) "),
                                                         project);
        fred.getInternalAccount().setAmount(new BigDecimal("100"));
        yo.getInternalAccount().setAmount(new BigDecimal("100"));

        feature.addContribution(fred, null, new BigDecimal("25.00"), "Contribution");
        feature.addContribution(yo, null, new BigDecimal("18.00"), "I'm so generous");

        feature = DBRequests.getById(DaoFeature.class, feature.getId());

        assertEquals(2, feature.getContributions().size());
        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("25")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("75")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("18")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("82")));

        // Reset the db:
        super.closeDB();
        super.createDB();
    }

    @Test
    public void testAddOffer() {
        DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                         null,
                                                         DaoDescription.createAndPersist(yo,
                                                                                         null,
                                                                                         new Locale("fr"),
                                                                                         "Ma super demande !",
                                                                                         "Ceci est la descption de ma demande :) "),
                                                         project);

        feature = DBRequests.getById(DaoFeature.class, feature.getId());
        feature.addOffer(createOffer(feature));
        assertEquals(1, feature.getOffers().size());
    }

    @Test
    public void testAddComment() throws Throwable {
        DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                         null,
                                                         DaoDescription.createAndPersist(yo,
                                                                                         null,
                                                                                         new Locale("fr"),
                                                                                         "Ma super demande !",
                                                                                         "Ceci est la descption de ma demande :) "),
                                                         project);
        feature.addComment(DaoComment.createAndPersist(feature, null, yo, "4"));
        feature.addComment(DaoComment.createAndPersist(feature, null, yo, "3"));
        feature.addComment(DaoComment.createAndPersist(feature, null, yo, "2"));
        feature.addComment(DaoComment.createAndPersist(feature, null, yo, "1"));

        feature = DBRequests.getById(DaoFeature.class, feature.getId());

        assertEquals(4, feature.getComments().size());
    }

    @Test
    public void testAcceptContributions() throws Throwable {
        fred.getInternalAccount().setAmount(new BigDecimal(50));
        yo.getInternalAccount().setAmount(new BigDecimal(50));

        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               null,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               null,
                                                                                               new Locale("fr"),
                                                                                               "Ma super demande !",
                                                                                               "Ceci est la descption de ma demande :) "),
                                                               project);
        final DaoOffer offer = createOffer(feature);
        feature.addOffer(offer);

        feature.addContribution(fred, null, new BigDecimal("25.00"), "I'm so generous too");
        feature.addContribution(yo, null, new BigDecimal("18.00"), "I'm so generous too");


        for (final DaoContribution Contribution : feature.getContributions()) {
            Contribution.validate(offer.getCurrentMilestone(), 100);
        }

        assertEquals(0, fred.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, fred.getInternalAccount().getAmount().compareTo(new BigDecimal("68")));
        assertEquals(0, yo.getInternalAccount().getBlocked().compareTo(new BigDecimal("0")));
        assertEquals(0, yo.getInternalAccount().getAmount().compareTo(new BigDecimal("32")));
    }

    private DaoOffer createOffer(final DaoFeature feature) {
        return new DaoOffer(fred,
                            null,
                            feature,
                            new BigDecimal("200"),
                            DaoDescription.createAndPersist(fred, null, new Locale("fr"), "Ma super offre !", "Ceci est la descption de mon Offre:) "),
                            DateUtils.tomorrow(),
                            0);
    }

    @Test
    public void testRejectContribution() throws Throwable {
        fred = DBRequests.getById(DaoMember.class, fred.getId());
        yo = DBRequests.getById(DaoMember.class, yo.getId());
        DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                         null,
                                                         DaoDescription.createAndPersist(yo,
                                                                                         null,
                                                                                         new Locale("fr"),
                                                                                         "Ma super demande !",
                                                                                         "Ceci est la descption de ma demande :) "),
                                                         project);
        feature.addOffer(createOffer(feature));
        fred.getInternalAccount().setAmount(new BigDecimal("100"));
        yo.getInternalAccount().setAmount(new BigDecimal("100"));
        feature.addContribution(fred, null, new BigDecimal("25.00"), "I'm so generous too");
        feature.addContribution(yo, null, new BigDecimal("18.00"), "I'm so generous too");

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

    @Test
    public void testGetCurrentOffer() {
        fred = DBRequests.getById(DaoMember.class, fred.getId());
        yo = DBRequests.getById(DaoMember.class, yo.getId());
        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               null,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               null,
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

    @Test
    public void testSearchFeature() {
        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               null,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               null,
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

    @Test
    public void testGetComment() {
        final DaoFeature feature = DaoFeature.createAndPersist(yo,
                                                               null,
                                                               DaoDescription.createAndPersist(yo,
                                                                                               null,
                                                                                               new Locale("fr"),
                                                                                               "Ma super demande !",
                                                                                               "Ceci est la descption de ma demande :) "),
                                                               project);
        feature.addComment(DaoComment.createAndPersist(feature, null, yo, "plop"));
        assertNotNull(feature.getComments().iterator().next());
    }

}
