package com.bloatit.framework;

import com.bloatit.common.UnauthorizedOperationException;

public class KudosableTest extends SuperTest {

    public void testCanKudos() {
        Demand demand = Demand.create(db.getDemand());

        try {
            demand.canKudos();
            fail();
        } catch (UnauthorizedOperationException e) {}

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
        Demand demand = Demand.create(db.getDemand());

        demand.authenticate(yoAuthToken);
        demand.unkudos();
        assertEquals(1, demand.getPopularity());
    }

    public void testKudos() {
        Demand demand = Demand.create(db.getDemand());

        demand.authenticate(yoAuthToken);
        demand.kudos();
        assertEquals(1, demand.getPopularity());
    }

//    TODO
//    public void testGetState() {
//        fail("Not yet implemented");
//    }

}
