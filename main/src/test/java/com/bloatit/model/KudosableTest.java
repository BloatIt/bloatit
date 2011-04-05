package com.bloatit.model;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.model.feature.FeatureManager;

public class KudosableTest extends ModelTestUnit {

    public void testCanKudos() throws UnauthorizedOperationException {
        final Feature feature = FeatureManager.getFeatureById(db.getFeature().getId());

        feature.authenticate(fredAuthToken);
        assertTrue(feature.canVoteUp().isEmpty());
        feature.voteUp();
        assertFalse(feature.canVoteUp().isEmpty());

        // Yo is the author of the feature
        feature.authenticate(yoAuthToken);
        assertFalse(feature.canVoteUp().isEmpty());

        feature.authenticate(tomAuthToken);
        assertTrue(feature.canVoteUp().isEmpty());
        feature.voteUp();
        assertFalse(feature.canVoteUp().isEmpty());
    }

    public void testUnkudos() {
        final Feature feature = FeatureManager.getFeatureById(db.getFeature().getId());

        assertEquals(0, feature.getPopularity());
        feature.authenticate(yoAuthToken);
        // feature.unkudos();
        // assertEquals(-1, feature.getPopularity());
    }

    public void testKudos() throws UnauthorizedOperationException {
        final Feature feature = FeatureManager.getFeatureById(db.getFeature().getId());

        feature.authenticate(fredAuthToken);
        feature.voteUp();
        assertEquals(1, feature.getPopularity());
    }

}
