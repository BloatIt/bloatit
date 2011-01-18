package com.bloatit.framework;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.managers.DemandManager;

public class KudosableTest extends FrameworkTestUnit {

    public void testCanKudos() throws UnauthorizedOperationException {
        final Demand demand = DemandManager.getDemandById(db.getDemand().getId());

        demand.authenticate(yoAuthToken);
        assertTrue(demand.canKudos());
        demand.kudos();
        assertFalse(demand.canKudos());

        demand.authenticate(fredAuthToken);
        assertTrue(demand.canKudos());
        demand.kudos();
        assertFalse(demand.canKudos());
    }

    public void testUnkudos() throws UnauthorizedOperationException {
        final Demand demand = DemandManager.getDemandById(db.getDemand().getId());

        assertEquals(0, demand.getPopularity());
        demand.authenticate(yoAuthToken);
        demand.unkudos();
        assertEquals(-1, demand.getPopularity());
    }

    public void testKudos() throws UnauthorizedOperationException {
        final Demand demand = DemandManager.getDemandById(db.getDemand().getId());

        demand.authenticate(yoAuthToken);
        demand.kudos();
        assertEquals(1, demand.getPopularity());
    }

}
