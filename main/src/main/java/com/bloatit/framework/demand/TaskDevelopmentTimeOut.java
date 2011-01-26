package com.bloatit.framework.demand;

import java.util.Date;

import com.bloatit.common.Log;
import com.bloatit.common.WrongStateException;
import com.bloatit.framework.PlannedTask;

public class TaskDevelopmentTimeOut extends PlannedTask {
    private static final long serialVersionUID = 5639581628713974313L;
    private final Demand demand;

    public TaskDevelopmentTimeOut(final Demand demand, final Date time) {
        super(time, demand.getId());
        this.demand = demand;
    }

    @Override
    public void doRun() {
        try {
            demand.developmentTimeOut();
        } catch (WrongStateException e) {
            Log.framework().fatal(e);
        }
    }

}
