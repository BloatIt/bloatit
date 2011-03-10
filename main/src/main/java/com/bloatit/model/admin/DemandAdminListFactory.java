package com.bloatit.model.admin;

import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.queries.DaoFeatureQuery;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Feature;
import com.bloatit.model.feature.DemandList;

public class DemandAdminListFactory extends KudosableAdminListFactory<DaoFeature, Feature> {

    public DemandAdminListFactory() {
        super(new DaoFeatureQuery());
    }

    @Override
    protected DaoFeatureQuery getfactory() {
        return (DaoFeatureQuery) super.getfactory();
    }

    @Override
    public PageIterable<Feature> list() {
        return new DemandList(getfactory().createCollection());
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
