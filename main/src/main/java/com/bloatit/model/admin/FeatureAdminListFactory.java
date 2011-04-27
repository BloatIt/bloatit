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
package com.bloatit.model.admin;

import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.queries.DaoFeatureQuery;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Feature;
import com.bloatit.model.feature.FeatureList;

public class FeatureAdminListFactory extends KudosableAdminListFactory<DaoFeature, Feature> {

    public FeatureAdminListFactory() {
        super(new DaoFeatureQuery());
    }

    @Override
    protected DaoFeatureQuery getfactory() {
        return (DaoFeatureQuery) super.getfactory();
    }

    @Override
    public PageIterable<Feature> list() {
        return new FeatureList(getfactory().createCollection());
    }

    public void stateEquals(final FeatureState state) {
        getfactory().stateEquals(state);
    }

    public void withSelectedOffer() {
        getfactory().withSelectedOffer();
    }

    public void withoutSelectedOffer() {
        getfactory().withoutSelectedOffer();
    }

    public void withOffer() {
        getfactory().withOffer();
    }

    public void withoutOffer() {
        getfactory().withoutOffer();
    }

    public void withContribution() {
        getfactory().hasContributions();
    }

    public void withoutContribution() {
        getfactory().hasNoContribution();
    }

}
