package com.bloatit.model.admin;

import java.math.BigDecimal;

import com.bloatit.data.DaoMilestone;
import com.bloatit.data.DaoMilestone.MilestoneState;
import com.bloatit.data.queries.DaoAbstractQuery.Comparator;
import com.bloatit.data.queries.DaoMilestoneQuery;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Milestone;
import com.bloatit.model.lists.MilestoneList;

public class MilestoneAdminListFactory extends IdentifiableAdminListFactory<DaoMilestone, Milestone> {

    public MilestoneAdminListFactory() {
        super(new DaoMilestoneQuery());
    }

    @Override
    protected DaoMilestoneQuery getfactory() {
        return (DaoMilestoneQuery) super.getfactory();
    }

    @Override
    public PageIterable<Milestone> list() {
        return new MilestoneList(getfactory().createCollection());
    }

    public void amount(Comparator cmp, BigDecimal value) {
        getfactory().amount(cmp, value);
    }

    public void stateEquals(MilestoneState state) {
        getfactory().stateEquals(state);
    }

    public void withRelease() {
        getfactory().withRelease();
    }

    public void withoutRelease() {
        getfactory().withoutRelease();
    }

}
