package com.bloatit.model.demand;

import java.util.Date;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.WrongStateException;
import com.bloatit.model.PlannedTask;

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
        } catch (final WrongStateException e) {
            Log.model().fatal(e);
        }
    }

}
