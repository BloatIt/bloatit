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
package com.bloatit.model.feature;

import com.bloatit.data.DaoFeature;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Feature;
import com.bloatit.model.FeatureImplementation;
import com.bloatit.model.right.AuthenticatedUserToken;

/**
 * The Class FeatureManager is a class with only static member. Use it to do
 * some requests on the DB returning features.
 */
public final class FeatureManager {

    /**
     * Desactivate default ctor
     */
    private FeatureManager() {
        // Desactivate default ctor
    }

    /**
     * Gets the all features stored in the DB.
     *
     * @return the features
     */
    public static PageIterable<Feature> getFeatures() {
        return new FeatureList(DBRequests.getAllUserContentOrderByDate(DaoFeature.class));
    }

    /**
     * Gets the feature by id.
     *
     * @param id the id of the feature we are looking for.
     * @return the feature or null if not found.
     */
    public static Feature getFeatureById(final Integer id) {
        return getFeatureImplementationById(id);
    }

    public static int getFeatureCount(){
        return DBRequests.count(DaoFeature.class);
    }

    /**
     * Gets the featureImplementation by id.
     *
     * @param id the id of the feature
     * @return the feature or null if not found
     */
    static FeatureImplementation getFeatureImplementationById(final Integer id) {
        return FeatureImplementation.create(DBRequests.getById(DaoFeature.class, id));
    }

    /**
     * Gets the number of feature.
     *
     * @return the number of feature.
     */
    public static int getFeaturesCount() {
        return DBRequests.count(DaoFeature.class);
    }

    // Can create if authenticated.
    /**
     * Tells if a user can create a feature.
     *
     * @param userToken the auth token representing the user wanting to create a
     *            feature.
     * @return true, if successful
     */
    public static boolean canCreate(final AuthenticatedUserToken userToken) {
        return userToken.isAuthenticated();
    }

}
