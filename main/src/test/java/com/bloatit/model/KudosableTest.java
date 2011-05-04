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
package com.bloatit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bloatit.model.feature.FeatureImplementation;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.model.right.UnauthorizedOperationException;

public class KudosableTest extends ModelTestUnit {

    @Test
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

    @Test
    public void testUnkudos() throws UnauthorizedOperationException {
        final Feature feature = FeatureImplementation.create(db.getFeature());
        
        assertEquals(0, feature.getPopularity());
        feature.authenticate(tomAuthToken);
        assertTrue(feature.canVoteUp().isEmpty());
        feature.voteDown();
        assertEquals(-1, feature.getPopularity());
    }

    @Test
    public void testKudos() throws UnauthorizedOperationException {
        final Feature feature = FeatureManager.getFeatureById(db.getFeature().getId());

        assertEquals(0, feature.getPopularity());
        feature.authenticate(fredAuthToken);
        feature.voteUp();
        assertEquals(1, feature.getPopularity());
    }

}
