package com.bloatit.framework.demand;

import java.util.Date;

import com.bloatit.framework.PlannedTask;

public class TaskSelectedOfferTimeOut extends PlannedTask {
    private static final long serialVersionUID = 5639581628713974313L;
    private final Demand demand;

    public TaskSelectedOfferTimeOut(Demand demand, Date time) {
        super(time, demand.getId());
        this.demand = demand;
    }

    @Override
    public void doRun() {
        demand.setSelectedOfferTimeOut();
    }

}
