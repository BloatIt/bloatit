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

import java.util.Date;

import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoHighlightFeature;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.model.feature.FeatureImplementation;

public final class HighlightFeature extends Identifiable<DaoHighlightFeature> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoHighlightFeature, HighlightFeature> {
        @SuppressWarnings("synthetic-access")
        @Override
        public HighlightFeature doCreate(final DaoHighlightFeature dao) {
            return new HighlightFeature(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static HighlightFeature create(final DaoHighlightFeature dao) {
        return new MyCreator().create(dao);
    }

    public HighlightFeature(final Feature feature, final int position, final String reason, final Date activationDate, final Date desactivationDate) {
        super(DaoHighlightFeature.createAndPersist(DBRequests.getById(DaoFeature.class, feature.getId()),
                                                   position,
                                                   reason,
                                                   activationDate,
                                                   desactivationDate));
    }

    private HighlightFeature(final DaoHighlightFeature dao) {
        super(dao);
    }

    public int getPosition() {
        return getDao().getPosition();
    }

    public Date getActivationDate() {
        return getDao().getActivationDate();
    }

    public Feature getFeature() {
        return FeatureImplementation.create(getDao().getFeature());
    }

    public String getReason() {
        return getDao().getReason();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

}
