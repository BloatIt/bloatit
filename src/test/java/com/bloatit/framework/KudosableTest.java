package com.bloatit.framework;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.managers.DemandManager;

public class KudosableTest extends FrameworkTestUnit {

    public void testCanKudos() {
        final Demand demand = DemandManager.GetDemandById(db.getDemand().getId());

        try {
            demand.canKudos();
            fail();
        } catch (final UnauthorizedOperationException e) {}

        demand.authenticate(yoAuthToken);
        assertTrue(demand.canKudos());
        demand.kudos();
        assertFalse(demand.canKudos());

        demand.authenticate(fredAuthToken);
        assertTrue(demand.canKudos());
        demand.kudos();
        assertFalse(demand.canKudos());
    }

    public void testUnkudos() {
        final Demand demand = DemandManager.GetDemandById(db.getDemand().getId());

        assertEquals(0, demand.getPopularity());
        demand.authenticate(yoAuthToken);
        demand.unkudos();
        assertEquals(-1, demand.getPopularity());
    }

    public void testKudos() {
        final Demand demand = DemandManager.GetDemandById(db.getDemand().getId());

        demand.authenticate(yoAuthToken);
        demand.kudos();
        assertEquals(1, demand.getPopularity());
    }

    // TODO
    // public void testGetState() {
    // fail("Not yet implemented");
    // }

}
