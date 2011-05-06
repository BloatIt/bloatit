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
package com.bloatit.model.managers;

import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.DaoTranslation;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.model.Comment;
import com.bloatit.model.FeatureImplementation;
import com.bloatit.model.Kudosable;
import com.bloatit.model.Offer;
import com.bloatit.model.Translation;

/**
 * The Class KudosableManager is an utility class containing static methods for
 * {@link Kudosable} loading etc.
 */
public final class KudosableManager {

    /**
     * Desactivated constructor on utility class.
     */
    private KudosableManager() {
        // Desactivate default ctor
    }

    /**
     * Gets a kudosable by id.
     * 
     * @param id the id
     * @return the {@link Kudosable} or <code>null</code> if not found.
     */
    protected static Kudosable<?> getById(final Integer id) {

        // Try with comment
        final DaoComment comment = DBRequests.getById(DaoComment.class, id);
        if (comment != null) {
            return Comment.create(comment);
        }

        // Try with feature
        final DaoFeature feature = DBRequests.getById(DaoFeature.class, id);
        if (feature != null) {
            return FeatureImplementation.create(feature);
        }

        // Try with offer
        final DaoOffer offer = DBRequests.getById(DaoOffer.class, id);
        if (offer != null) {
            return Offer.create(offer);
        }

        // Try with translation
        final DaoTranslation translation = DBRequests.getById(DaoTranslation.class, id);
        if (translation != null) {
            return Translation.create(translation);
        }

        return null;
    }

}
