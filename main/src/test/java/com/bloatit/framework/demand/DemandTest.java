package com.bloatit.framework.demand;


import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import javassist.NotFoundException;
import mockit.Mock;
import mockit.Mockit;

import com.bloatit.common.DateUtils;
import com.bloatit.common.FatalErrorException;
import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.AuthToken;
import com.bloatit.framework.FrameworkMutex;
import com.bloatit.framework.FrameworkTestUnit;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoDemand;
import com.bloatit.model.data.DaoDemand.DemandState;
import com.bloatit.model.data.DaoDescription;
import com.bloatit.model.data.DaoMember;
import com.bloatit.model.data.DaoMember.Role;
import com.bloatit.model.data.util.NonOptionalParameterException;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

public class DemandTest extends FrameworkTestUnit {

    public void testCreate() {
        Demand demand = Demand.create(DaoDemand.createAndPersist(tomAuthToken.getMember().getDao(), DaoDescription.createAndPersist(tomAuthToken
                .getMember().getDao(), Locale.FRANCE, "title", "description")));
        assertNotNull(demand);
        assertNull(Demand.create(null));
    }

    private Demand createDemandByThomas() {
        return Demand.create(DaoDemand.createAndPersist(tomAuthToken.getMember().getDao(), DaoDescription.createAndPersist(tomAuthToken.getMember()
                .getDao(), Locale.FRANCE, "title", "description")));
    }

    public void testDemand() {
        Demand demand = new Demand(tomAuthToken.getMember(), Locale.FRANCE, "title", "Description");
        assertEquals(demand.getAuthor(), tomAuthToken.getMember());
        try {
            assertEquals(demand.getDescription().getDefaultLocale(), Locale.FRANCE);
            assertEquals(demand.getDescription().getDefaultTranslation().getTitle(), "title");
            assertEquals(demand.getDescription().getDefaultTranslation().getText(), "Description");
        } catch (UnauthorizedOperationException e) {
            fail();
        }
    }

    public void testCanAccessComment() {
        Demand demand = createDemandByThomas();
        assertTrue(demand.canAccessComment(Action.READ));
        assertFalse(demand.canAccessComment(Action.WRITE));
        assertFalse(demand.canAccessComment(Action.DELETE));

        // other
        demand.authenticate(fredAuthToken);
        assertTrue(demand.canAccessComment(Action.READ));
        assertTrue(demand.canAccessComment(Action.WRITE));
        assertFalse(demand.canAccessComment(Action.DELETE));

        // demand creator
        demand.authenticate(tomAuthToken);
        assertTrue(demand.canAccessComment(Action.READ));
        assertTrue(demand.canAccessComment(Action.WRITE));
        assertFalse(demand.canAccessComment(Action.DELETE));

        // For now nobody can delete a comment
    }

    public void testCanAccessContribution() {
        Demand demand = createDemandByThomas();
        assertTrue(demand.canAccessContribution(Action.READ));
        assertFalse(demand.canAccessContribution(Action.WRITE));
        assertFalse(demand.canAccessContribution(Action.DELETE));

        // owner of demand.
        demand.authenticate(tomAuthToken);
        assertTrue(demand.canAccessContribution(Action.READ));
        assertTrue(demand.canAccessContribution(Action.WRITE));
        assertFalse(demand.canAccessContribution(Action.DELETE));

        // other
        demand.authenticate(fredAuthToken);
        assertTrue(demand.canAccessContribution(Action.READ));
        assertTrue(demand.canAccessContribution(Action.WRITE));
        assertFalse(demand.canAccessContribution(Action.DELETE));
    }

    public void testCanAccessOffer() {
        Demand demand = createDemandByThomas();
        assertTrue(demand.canAccessOffer(Action.READ));
        assertFalse(demand.canAccessOffer(Action.WRITE));
        assertFalse(demand.canAccessOffer(Action.DELETE));

        // other
        demand.authenticate(fredAuthToken);
        assertTrue(demand.canAccessOffer(Action.READ));
        assertTrue(demand.canAccessOffer(Action.WRITE));
        assertFalse(demand.canAccessOffer(Action.DELETE));

        // owner of demand.
        demand.authenticate(tomAuthToken);
        assertTrue(demand.canAccessOffer(Action.READ));
        assertTrue(demand.canAccessOffer(Action.WRITE));
        assertFalse(demand.canAccessOffer(Action.DELETE));
    }

    public void testCanAccessDescription() {
        Demand demand = createDemandByThomas();
        assertTrue(demand.canAccessDescription());
        demand.authenticate(yoAuthToken);
        assertTrue(demand.canAccessDescription());
        demand.authenticate(tomAuthToken);
        assertTrue(demand.canAccessDescription());
    }

    public void testAddContribution() throws UnauthorizedOperationException {
        Demand demand = createDemandByThomas();

        assertEquals(DemandState.PENDING, demand.getDemandState());

        demand.authenticate(fredAuthToken);
        try {
            demand.addContribution(new BigDecimal("10"), "comment");
        } catch (NotEnoughMoneyException e) {
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(demand.getContribution(), new BigDecimal("10"));

        try {
            demand.addContribution(new BigDecimal("10"), null);
        } catch (NotEnoughMoneyException e) {
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(demand.getContribution(), new BigDecimal("20"));

        try {
            demand.addContribution(new BigDecimal("10"), "");
        } catch (NotEnoughMoneyException e) {
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(demand.getContribution(), new BigDecimal("30"));

        try {
            demand.addContribution(null, "comment");
            fail();
        } catch (NotEnoughMoneyException e) {
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }

        assertEquals(demand.getContribution(), new BigDecimal("30"));

        try {
            demand.addContribution(new BigDecimal("-10"), "comment");
            fail();
        } catch (NotEnoughMoneyException e) {
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        } catch (FatalErrorException e) {
            assertTrue(true);
        }

        assertEquals(demand.getContribution(), new BigDecimal("30"));

        try {
            demand.addContribution(new BigDecimal("0"), "comment");
            fail();
        } catch (NotEnoughMoneyException e) {
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        } catch (FatalErrorException e) {
            assertTrue(true);
        }

        assertEquals(demand.getContribution(), new BigDecimal("30"));
        demand.authenticate(null);

        try {
            demand.addContribution(new BigDecimal("10"), "comment");
            fail();
        } catch (NotEnoughMoneyException e) {
            fail();
        } catch (UnauthorizedOperationException e) {
            assertTrue(true);
        }

        assertEquals(demand.getContribution(), new BigDecimal("30"));

        // Tom have 1000 money.
        demand.authenticate(tomAuthToken);
        try {
            demand.addContribution(new BigDecimal("1001"), "comment");
            fail();
        } catch (NotEnoughMoneyException e) {
            assertTrue(true);
        } catch (UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(demand.getContribution(), new BigDecimal("30"));
        assertEquals(DemandState.PENDING, demand.getDemandState());
    }

    public void testAddOffer() {
        Demand demand = createDemandByThomas();

        assertEquals(DemandState.PENDING, demand.getDemandState());
        demand.authenticate(fredAuthToken);

        try {
            assertNull(demand.getSelectedOffer());
            assertEquals(0, demand.getOffers().getPageSize());
        } catch (UnauthorizedOperationException e1) {
            fail();
        }

        try {
            demand.addOffer(new BigDecimal("120"), Locale.FRENCH, "title", "description", DateUtils.tomorrow());
        } catch (UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(DemandState.PREPARING, demand.getDemandState());

        try {
            demand.addOffer(null, Locale.FRENCH, "title", "description", DateUtils.tomorrow());
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            demand.addOffer(new BigDecimal("120"), null, "title", "description", DateUtils.tomorrow());
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            demand.addOffer(new BigDecimal("120"), Locale.FRENCH, null, "description", DateUtils.tomorrow());
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            demand.addOffer(new BigDecimal("120"), Locale.FRENCH, "", "description", DateUtils.tomorrow());
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            demand.addOffer(new BigDecimal("120"), Locale.FRENCH, "title", null, DateUtils.tomorrow());
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            demand.addOffer(new BigDecimal("120"), Locale.FRENCH, "title", "", DateUtils.tomorrow());
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            demand.addOffer(new BigDecimal("120"), Locale.FRENCH, "title", "description", null);
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        } catch (NonOptionalParameterException e) {
            assertTrue(true);
        }

        try {
            demand.addOffer(new BigDecimal("120"), Locale.FRENCH, "title", "description", DateUtils.yesterday());
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        } catch (FatalErrorException e) {
            assertTrue(true);
        }

        try {
            demand.addOffer(new BigDecimal("-1"), Locale.FRENCH, "title", "description", DateUtils.yesterday());
            fail();
        } catch (UnauthorizedOperationException e) {
            fail();
        } catch (FatalErrorException e) {
            assertTrue(true);
        }

        try {
            assertNotNull(demand.getSelectedOffer());
        } catch (UnauthorizedOperationException e) {
            fail();
        }
        assertEquals(DemandState.PREPARING, demand.getDemandState());
    }

    public static class MockDemandValidationTimeOut {
        @Mock
        public Date getValidationDate() {
            return DateUtils.yesterday();
        }
    }

    public void testBeginDevelopment() throws NotEnoughMoneyException, UnauthorizedOperationException {
        Demand demand = createDemandByThomas();
        assertEquals(DemandState.PENDING, demand.getDemandState());

        demand.authenticate(fredAuthToken);
        demand.addContribution(new BigDecimal("100"), "plop");
        assertEquals(DemandState.PENDING, demand.getDemandState());

        demand.authenticate(tomAuthToken);
        demand.addOffer(new BigDecimal("120"), Locale.FRENCH, "title", "description", DateUtils.tomorrow());
        assertEquals(DemandState.PREPARING, demand.getDemandState());

        demand.authenticate(yoAuthToken);
        demand.addContribution(new BigDecimal("20"), "plip");
        assertEquals(DemandState.PREPARING, demand.getDemandState());

        Mockit.setUpMock(DaoDemand.class, new MockDemandValidationTimeOut());

        new TaskSelectedOfferTimeOut(demand, new Date());
        FrameworkMutex.unLock();
        try {
            Thread.sleep(1000);
            FrameworkMutex.lock();
        } catch (InterruptedException e) {
            fail();
        }

        Mockit.tearDownMocks();

        assertEquals(DemandState.DEVELOPPING, demand.getDemandState());
    }

    public void testRemoveOffer() throws NotEnoughMoneyException, UnauthorizedOperationException, NotFoundException {
        Demand demand = createDemandByThomas();
        DaoMember admin = DaoMember.createAndPersist("admin", "admin", "admin", Locale.FRANCE);
        admin.setRole(Role.ADMIN);
        assertEquals(DemandState.PENDING, demand.getDemandState());

        demand.authenticate(fredAuthToken);
        demand.addContribution(new BigDecimal("100"), "plop");
        assertEquals(DemandState.PENDING, demand.getDemandState());

        demand.authenticate(tomAuthToken);
        demand.addOffer(new BigDecimal("120"), Locale.FRENCH, "title", "description", DateUtils.tomorrow());
        assertEquals(DemandState.PREPARING, demand.getDemandState());

        assertNotNull(demand.getSelectedOffer());

        try {
            demand.authenticate(tomAuthToken);
            demand.removeOffer(demand.getSelectedOffer());
            fail();
        } catch (UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            demand.authenticate(new AuthToken("admin", "admin"));
            demand.removeOffer(demand.getSelectedOffer());
            assertTrue(true);
        } catch (UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(0, demand.getOffers().size());
    }

    public void testCancelDevelopment() throws NotEnoughMoneyException, UnauthorizedOperationException {
        Demand demand = createDemandAddOffer120AddContribution120BeginDev();

        try {
            demand.cancelDevelopment();
            fail();
        } catch (UnauthorizedOperationException e) {
            assertEquals(UnauthorizedOperationException.SpecialCode.NON_DEVELOPER_CANCEL_DEMAND, e.getCode());
        }

        demand.authenticate(tomAuthToken);

        assertEquals(new BigDecimal("120"), demand.getContribution());

        demand.cancelDevelopment();

        assertEquals(BigDecimal.ZERO, demand.getContribution());

        assertEquals(DemandState.DISCARDED, demand.getDemandState());

    }

    private Demand createDemandAddOffer120AddContribution120BeginDev() throws NotEnoughMoneyException, UnauthorizedOperationException {
        Demand demand = createDemandByThomas();
        assertEquals(DemandState.PENDING, demand.getDemandState());

        demand.authenticate(fredAuthToken);
        demand.addContribution(new BigDecimal("100"), "plop");
        assertEquals(DemandState.PENDING, demand.getDemandState());

        demand.authenticate(tomAuthToken);
        demand.addOffer(new BigDecimal("120"), Locale.FRENCH, "title", "description", DateUtils.tomorrow());
        assertEquals(DemandState.PREPARING, demand.getDemandState());

        demand.authenticate(yoAuthToken);
        demand.addContribution(new BigDecimal("20"), "plip");
        assertEquals(DemandState.PREPARING, demand.getDemandState());

        Mockit.setUpMock(DaoDemand.class, new MockDemandValidationTimeOut());

        new TaskSelectedOfferTimeOut(demand, new Date());
        FrameworkMutex.unLock();
        try {
            Thread.sleep(1000);
            FrameworkMutex.lock();
        } catch (InterruptedException e) {
            fail();
        }

        Mockit.tearDownMocks();

        assertEquals(DemandState.DEVELOPPING, demand.getDemandState());
        return demand;
    }

    public void testFinishedDevelopment() throws NotEnoughMoneyException, UnauthorizedOperationException {
        Demand demand = createDemandAddOffer120AddContribution120BeginDev();

        try {
            demand.finishDevelopment();
            fail();
        } catch (UnauthorizedOperationException e) {
            assertEquals(UnauthorizedOperationException.SpecialCode.NON_DEVELOPER_FINISHED_DEMAND, e.getCode());
        }

        demand.authenticate(tomAuthToken);
        demand.finishDevelopment();

        assertEquals(DemandState.INCOME, demand.getDemandState());
        assertEquals(new BigDecimal("120"), demand.getContribution());
    }

    // public void testAddComment() {
    // fail("Not yet implemented");
    // }
    //
    // public void testGetContributionMax() {
    // fail("Not yet implemented");
    // }
    //
    // public void testGetContributionMin() {
    // fail("Not yet implemented");
    // }
    //
    // public void testGetSelectedOffer() {
    // fail("Not yet implemented");
    // }
}
