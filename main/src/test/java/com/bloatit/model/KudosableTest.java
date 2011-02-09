package com.bloatit.model;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.demand.DemandImplementation;
import com.bloatit.model.demand.DemandManager;

public class KudosableTest extends ModelTestUnit {

    public void testCanKudos() throws UnauthorizedOperationException {
        final DemandImplementation demandImplementation = DemandManager.getDemandById(db.getDemand().getId());

        demandImplementation.authenticate(fredAuthToken);
        assertTrue(demandImplementation.canKudos().isEmpty());
        demandImplementation.kudos();
        assertFalse(demandImplementation.canKudos().isEmpty());

        demandImplementation.authenticate(yoAuthToken);
        assertTrue(demandImplementation.canKudos().isEmpty());
        demandImplementation.kudos();
        assertFalse(demandImplementation.canKudos().isEmpty());

        demandImplementation.authenticate(tomAuthToken);
        assertTrue(demandImplementation.canKudos().isEmpty());
        demandImplementation.kudos();
        assertFalse(demandImplementation.canKudos().isEmpty());
    }

    public void testUnkudos() {
        final DemandImplementation demandImplementation = DemandManager.getDemandById(db.getDemand().getId());

        assertEquals(0, demandImplementation.getPopularity());
        demandImplementation.authenticate(yoAuthToken);
//        demandImplementation.unkudos();
//        assertEquals(-1, demandImplementation.getPopularity());
    }

    public void testKudos() throws UnauthorizedOperationException {
        final DemandImplementation demandImplementation = DemandManager.getDemandById(db.getDemand().getId());

        demandImplementation.authenticate(yoAuthToken);
        demandImplementation.kudos();
        assertEquals(1, demandImplementation.getPopularity());
    }

}
