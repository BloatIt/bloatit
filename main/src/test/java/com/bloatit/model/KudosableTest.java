package com.bloatit.model;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.demand.DemandManager;

public class KudosableTest extends ModelTestUnit {

    public void testCanKudos() throws UnauthorizedOperationException {
        final Demand demand = DemandManager.getDemandById(db.getDemand().getId());

        demand.authenticate(fredAuthToken);
        assertTrue(demand.canVoteUp().isEmpty());
        demand.voteUp();
        assertFalse(demand.canVoteUp().isEmpty());

        // Yo is the author of the demand
        demand.authenticate(yoAuthToken);
        assertFalse(demand.canVoteUp().isEmpty());

        demand.authenticate(tomAuthToken);
        assertTrue(demand.canVoteUp().isEmpty());
        demand.voteUp();
        assertFalse(demand.canVoteUp().isEmpty());
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

        demand.authenticate(fredAuthToken);
        demand.voteUp();
        assertEquals(1, demand.getPopularity());
    }

}
