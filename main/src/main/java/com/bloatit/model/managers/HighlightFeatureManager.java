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

import java.util.ArrayList;
import java.util.List;

import com.bloatit.data.DaoHighlightFeature;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.HighlightFeature;
import com.bloatit.model.lists.HighlightFeatureList;

/**
 * The Class HighlightFeatureManager is an utility class containing static
 * methods for {@link HighlightFeature} loading etc.
 */
public final class HighlightFeatureManager {

    /**
     * Desactivated constructor on utility class.
     */
    private HighlightFeatureManager() {
        // Desactivate default ctor
    }

    /**
     * Gets a {@link HighlightFeature} by its id.
     *
     * @param id the id
     * @return the {@link HighlightFeature} or null if not found.
     */
    public static HighlightFeature getById(final Integer id) {
        return HighlightFeature.create(DBRequests.getById(DaoHighlightFeature.class, id));
    }

    /**
     * Gets the all th {@link HighlightFeature}s.
     *
     * @return the {@link HighlightFeature} features.
     */
    public static PageIterable<HighlightFeature> getAll() {
        return new HighlightFeatureList(DBRequests.getAll(DaoHighlightFeature.class));
    }

    public static List<HighlightFeature> getPositionArray(int featureCount) {

        final PageIterable<HighlightFeature> hightlightFeatureList = HighlightFeatureManager.getAll();
        final List<HighlightFeature> hightlightFeatureArray = new ArrayList<HighlightFeature>(featureCount);

        for(int i = 0; i< featureCount; i++) {
            hightlightFeatureArray.add(null);
        }

        for (final HighlightFeature highlightFeature : hightlightFeatureList) {
            if(!highlightFeature.isActive()) {
                continue;
            }
            final int position = highlightFeature.getPosition() - 1;
            if (position < featureCount) {
                if (hightlightFeatureArray.get(position) == null) {
                    hightlightFeatureArray.set(position, highlightFeature);
                } else {
                    if (!hightlightFeatureArray.get(position).getActivationDate().after(highlightFeature.getActivationDate())) {
                        hightlightFeatureArray.set(position, highlightFeature);
                    }
                }
            }
        }

        return hightlightFeatureArray;
    }
}
