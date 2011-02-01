package com.bloatit.model;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.demand.Demand;
import com.bloatit.model.demand.DemandManager;

public class KudosableTest extends FrameworkTestUnit {

    public void testCanKudos() throws UnauthorizedOperationException {
        final Demand demand = DemandManager.getDemandById(db.getDemand().getId());

        demand.authenticate(fredAuthToken);
        assertTrue(demand.canKudos().isEmpty());
        demand.kudos();
        assertFalse(demand.canKudos().isEmpty());

        demand.authenticate(yoAuthToken);
        assertTrue(demand.canKudos().isEmpty());
        demand.kudos();
        assertFalse(demand.canKudos().isEmpty());

        demand.authenticate(tomAuthToken);
        assertTrue(demand.canKudos().isEmpty());
        demand.kudos();
        assertFalse(demand.canKudos().isEmpty());
    }

    public void testUnkudos() {
        final Demand demand = DemandManager.getDemandById(db.getDemand().getId());

        assertEquals(0, demand.getPopularity());
        demand.authenticate(yoAuthToken);
//        demand.unkudos();
//        assertEquals(-1, demand.getPopularity());
    }

    public void testKudos() throws UnauthorizedOperationException {
        final Demand demand = DemandManager.getDemandById(db.getDemand().getId());

        demand.authenticate(yoAuthToken);
        demand.kudos();
        assertEquals(1, demand.getPopularity());
    }

}
