package com.bloatit.framework.demand;

import java.util.Date;

import com.bloatit.common.Log;
import com.bloatit.common.WrongDemandStateException;
import com.bloatit.framework.PlannedTask;

public class TaskSelectedOfferTimeOut extends PlannedTask {
    private static final long serialVersionUID = 5639581628713974313L;
    private final Demand demand;

    public TaskSelectedOfferTimeOut(final Demand demand, final Date time) {
        super(time, demand.getId());
        this.demand = demand;
    }

    @Override
    public void doRun() {
        try {
            System.out.println(demand);
            demand.selectedOfferTimeOut();
        } catch (WrongDemandStateException e) {
            Log.framework().fatal(e);
        }
    }

}
