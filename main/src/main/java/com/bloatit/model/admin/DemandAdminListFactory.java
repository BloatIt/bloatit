package com.bloatit.model.admin;

import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.data.queries.DaoDemandListFactory;
import com.bloatit.framework.utils.PageIterable;

public class DemandAdminListFactory extends KudosableAdminListFactory<DaoDemand, DemandAdmin> {

    public DemandAdminListFactory() {
        super(new DaoDemandListFactory());
    }

    @Override
    protected DaoDemandListFactory getfactory() {
        return (DaoDemandListFactory) super.getfactory();
    }

    @Override
    public PageIterable<DemandAdmin> list() {
        return new AdminList<DaoDemand, DemandAdmin>(getfactory().createCollection());
    }

    public void stateEquals(final DemandState state) {
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
