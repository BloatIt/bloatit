package com.bloatit.model;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.demand.DemandImplementation;
import com.bloatit.model.demand.DemandManager;

public class KudosableTest extends ModelTestUnit {

    public void testCanKudos() throws UnauthorizedOperationException {
        final DemandImplementation demand = (DemandImplementation) DemandManager.getDemandById(db.getDemand().getId());

        demand.authenticate(fredAuthToken);
        assertTrue(demand.canVoteUp().isEmpty());
        demand.voteUp();
        assertFalse(demand.canVoteUp().isEmpty());

        demand.authenticate(yoAuthToken);
        assertTrue(demand.canVoteUp().isEmpty());
        demand.voteUp();
        assertFalse(demand.canVoteUp().isEmpty());

        demand.authenticate(tomAuthToken);
        assertTrue(demand.canVoteUp().isEmpty());
        demand.voteUp();
        assertFalse(demand.canVoteUp().isEmpty());
    }

    public void testUnkudos() {
        final DemandImplementation demand = (DemandImplementation) DemandManager.getDemandById(db.getDemand().getId());

        assertEquals(0, demand.getPopularity());
        demand.authenticate(yoAuthToken);
//        demand.unkudos();
//        assertEquals(-1, demand.getPopularity());
    }

    public void testKudos() throws UnauthorizedOperationException {
        final DemandImplementation demand = (DemandImplementation) DemandManager.getDemandById(db.getDemand().getId());

        demand.authenticate(yoAuthToken);
        demand.voteUp();
        assertEquals(1, demand.getPopularity());
    }

}
