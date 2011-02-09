package com.bloatit.model.demand;

import java.util.Date;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.WrongStateException;
import com.bloatit.model.PlannedTask;

/**
 * This is a planned task. It cannot store object from the Model layer (it would introduce
 * multithred bugs)
 */
public class TaskSelectedOfferTimeOut extends PlannedTask {
    private static final long serialVersionUID = 5639581628713974313L;
    private final int id;

    public TaskSelectedOfferTimeOut(final int id, final Date time) {
        super(time, id);
        this.id = id;
    }

    @Override
    public void doRun() {
        try {

            DemandImplementation demand = DemandManager.getDemandImplementationById(id);
            if (demand != null) {
                demand.selectedOfferTimeOut();
            } else {
                Log.framework().fatal("Cannot perform the selectedOfferTimeOut. DemandImplementation not found: " + id);
            }

        } catch (final WrongStateException e) {
            Log.model().fatal("Wrong state when trying to perform the selectedOfferTimeOut", e);
        }
    }

}
