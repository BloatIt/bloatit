package com.bloatit.model.admin;

import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.queries.DaoDemandQuery;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Feature;
import com.bloatit.model.feature.DemandList;

public class DemandAdminListFactory extends KudosableAdminListFactory<DaoFeature, Feature> {

    public DemandAdminListFactory() {
        super(new DaoDemandQuery());
    }

    @Override
    protected DaoDemandQuery getfactory() {
        return (DaoDemandQuery) super.getfactory();
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
