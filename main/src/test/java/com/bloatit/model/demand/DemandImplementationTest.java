package com.bloatit.model.demand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import javassist.NotFoundException;
import mockit.Mock;
import mockit.Mockit;

import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoProject;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.DateUtils;
import com.bloatit.framework.webserver.ModelManagerAccessor;
import com.bloatit.model.AuthToken;
import com.bloatit.model.Demand;
import com.bloatit.model.ModelTestUnit;
import com.bloatit.model.Offer;
import com.bloatit.model.Project;
import com.bloatit.model.right.RightManager.Action;

public class DemandImplementationTest extends ModelTestUnit {

    public void testCreate() {
        final Demand demand = DemandImplementation.create(DaoDemand.createAndPersist(tomAuthToken.getMember().getDao(), DaoDescription
                .createAndPersist(tomAuthToken.getMember().getDao(), Locale.FRANCE, "title", "description"), DaoProject.getByName("VLC")));
        assertNotNull(demand);
        assertNull(DemandImplementation.create(null));
    }

    private DemandImplementation createDemandByThomas() {
        return DemandImplementation.create(DaoDemand.createAndPersist(tomAuthToken.getMember().getDao(), DaoDescription.createAndPersist(tomAuthToken.getMember()
                .getDao(), Locale.FRANCE, "title", "description"), DaoProject.getByName("VLC")));
    }

    public void testDemand() {
        final DemandImplementation demandImplementation = new DemandImplementation(tomAuthToken.getMember(), Locale.FRANCE, "title", "Description", Project.create(DaoProject.getByName("VLC")));
        assertEquals(demandImplementation.getAuthor(), tomAuthToken.getMember());
        try {
            assertEquals(demandImplementation.getDescription().getDefaultLocale(), Locale.FRANCE);
            assertEquals(demandImplementation.getDescription().getDefaultTranslation().getTitle(), "title");
            assertEquals(demandImplementation.getDescription().getDefaultTranslation().getText(), "Description");
        } catch (final UnauthorizedOperationException e) {
            fail();
        }
    }

    public void testCanAccessComment() {
        final DemandImplementation demandImplementation = createDemandByThomas();
        assertTrue(demandImplementation.canAccessComment(Action.READ));
        assertFalse(demandImplementation.canAccessComment(Action.WRITE));
        assertFalse(demandImplementation.canAccessComment(Action.DELETE));

        // other
        demandImplementation.authenticate(fredAuthToken);
        assertTrue(demandImplementation.canAccessComment(Action.READ));
        assertTrue(demandImplementation.canAccessComment(Action.WRITE));
        assertFalse(demandImplementation.canAccessComment(Action.DELETE));

        // demandImplementation creator
        demandImplementation.authenticate(tomAuthToken);
        assertTrue(demandImplementation.canAccessComment(Action.READ));
        assertTrue(demandImplementation.canAccessComment(Action.WRITE));
        assertFalse(demandImplementation.canAccessComment(Action.DELETE));

        // For now nobody can delete a comment
    }

    public void testCanAccessContribution() {
        final DemandImplementation demandImplementation = createDemandByThomas();
        assertTrue(demandImplementation.canAccessContribution(Action.READ));
        assertFalse(demandImplementation.canAccessContribution(Action.WRITE));
        assertFalse(demandImplementation.canAccessContribution(Action.DELETE));

        // owner of demandImplementation.
        demandImplementation.authenticate(tomAuthToken);
        assertTrue(demandImplementation.canAccessContribution(Action.READ));
        assertTrue(demandImplementation.canAccessContribution(Action.WRITE));
        assertFalse(demandImplementation.canAccessContribution(Action.DELETE));

        // other
        demandImplementation.authenticate(fredAuthToken);
        assertTrue(demandImplementation.canAccessContribution(Action.READ));
        assertTrue(demandImplementation.canAccessContribution(Action.WRITE));
        assertFalse(demandImplementation.canAccessContribution(Action.DELETE));
    }

    public void testCanAccessOffer() {
        final DemandImplementation demandImplementation = createDemandByThomas();
        assertTrue(demandImplementation.canAccessOffer(Action.READ));
        assertFalse(demandImplementation.canAccessOffer(Action.WRITE));
        assertFalse(demandImplementation.canAccessOffer(Action.DELETE));

        // other
        demandImplementation.authenticate(fredAuthToken);
        assertTrue(demandImplementation.canAccessOffer(Action.READ));
        assertTrue(demandImplementation.canAccessOffer(Action.WRITE));
        assertFalse(demandImplementation.canAccessOffer(Action.DELETE));

        // owner of demandImplementation.
        demandImplementation.authenticate(tomAuthToken);
        assertTrue(demandImplementation.canAccessOffer(Action.READ));
        assertTrue(demandImplementation.canAccessOffer(Action.WRITE));
        assertFalse(demandImplementation.canAccessOffer(Action.DELETE));
    }

    public void testCanAccessDescription() {
        final DemandImplementation demandImplementation = createDemandByThomas();
        assertTrue(demandImplementation.canAccessDescription());
        demandImplementation.authenticate(yoAuthToken);
        assertTrue(demandImplementation.canAccessDescription());
        demandImplementation.authenticate(tomAuthToken);
        assertTrue(demandImplementation.canAccessDescription());
    }

    public void testAddContribution() throws UnauthorizedOperationException {
        final DemandImplementation demandImplementation = createDemandByThomas();

        assertEquals(DemandState.PENDING, demandImplementation.getDemandState());

        demandImplementation.authenticate(fredAuthToken);
        try {
            demandImplementation.addContribution(new BigDecimal("10"), "comment");
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(demandImplementation.getContribution(), new BigDecimal("10"));

        try {
            demandImplementation.addContribution(new BigDecimal("10"), null);
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(demandImplementation.getContribution(), new BigDecimal("20"));

        try {
            demandImplementation.addContribution(new BigDecimal("10"), "");
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(demandImplementation.getContribution(), new BigDecimal("30"));

        try {
            demandImplementation.addContribution(null, "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }

        assertEquals(demandImplementation.getContribution(), new BigDecimal("30"));

        try {
            demandImplementation.addContribution(new BigDecimal("-10"), "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        } catch (final FatalErrorException e) {
            assertTrue(true);
        }

        assertEquals(demandImplementation.getContribution(), new BigDecimal("30"));

        try {
            demandImplementation.addContribution(new BigDecimal("0"), "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        } catch (final FatalErrorException e) {
            assertTrue(true);
        }

        assertEquals(demandImplementation.getContribution(), new BigDecimal("30"));
        demandImplementation.authenticate(null);

        try {
            demandImplementation.addContribution(new BigDecimal("10"), "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        assertEquals(demandImplementation.getContribution(), new BigDecimal("30"));

        // Tom have 1000 money.
        demandImplementation.authenticate(tomAuthToken);
        try {
            demandImplementation.addContribution(new BigDecimal("1001"), "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            assertTrue(true);
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(demandImplementation.getContribution(), new BigDecimal("30"));
        assertEquals(DemandState.PENDING, demandImplementation.getDemandState());
    }

    public void testAddOffer() {
        final DemandImplementation demandImplementation = createDemandByThomas();

        assertEquals(DemandState.PENDING, demandImplementation.getDemandState());
        demandImplementation.authenticate(fredAuthToken);

        try {
            assertNull(demandImplementation.getSelectedOffer());
            assertEquals(0, demandImplementation.getOffers().getPageSize());
        } catch (final UnauthorizedOperationException e1) {
            fail();
        }

        try {
            demandImplementation.authenticate(fredAuthToken);
            final Offer offer = new Offer(fredAuthToken.getMember(), demandImplementation, new BigDecimal("120"), "description", "title", Locale.FRENCH,
                    DateUtils.tomorrow());
            demandImplementation.addOffer(offer);
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(DemandState.PREPARING, demandImplementation.getDemandState());

        try {
            assertNotNull(demandImplementation.getSelectedOffer());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }
        assertEquals(DemandState.PREPARING, demandImplementation.getDemandState());
    }

    public static class MockDemandValidationTimeOut {
        @Mock
        public Date getValidationDate() {
            return DateUtils.yesterday();
        }
    }

    public void testBeginDevelopment() throws NotEnoughMoneyException, UnauthorizedOperationException {
        DemandImplementation demandImplementation = createDemandByThomas();
        assertEquals(DemandState.PENDING, demandImplementation.getDemandState());

        demandImplementation.authenticate(fredAuthToken);
        demandImplementation.addContribution(new BigDecimal("100"), "plop");
        assertEquals(DemandState.PENDING, demandImplementation.getDemandState());

        demandImplementation.authenticate(tomAuthToken);
        final Offer offer = new Offer(tomAuthToken.getMember(), demandImplementation, new BigDecimal("120"), "description", "title", Locale.FRENCH,
                DateUtils.tomorrow());
        demandImplementation.addOffer(offer);
        assertEquals(DemandState.PREPARING, demandImplementation.getDemandState());

        demandImplementation.authenticate(yoAuthToken);
        demandImplementation.addContribution(new BigDecimal("20"), "plip");
        assertEquals(DemandState.PREPARING, demandImplementation.getDemandState());

        demandImplementation = passeIntoDev(demandImplementation);

        assertEquals(DemandState.DEVELOPPING, demandImplementation.getDemandState());
    }

    public void testRemoveOffer() throws NotEnoughMoneyException, UnauthorizedOperationException, NotFoundException {
        final DemandImplementation demandImplementation = createDemandByThomas();
        final DaoMember admin = DaoMember.createAndPersist("admin", "admin", "admin", Locale.FRANCE);
        admin.setRole(Role.ADMIN);
        assertEquals(DemandState.PENDING, demandImplementation.getDemandState());

        demandImplementation.authenticate(fredAuthToken);
        demandImplementation.addContribution(new BigDecimal("100"), "plop");
        assertEquals(DemandState.PENDING, demandImplementation.getDemandState());

        demandImplementation.authenticate(tomAuthToken);
        final Offer offer = new Offer(tomAuthToken.getMember(), demandImplementation, new BigDecimal("120"), "description", "title", Locale.FRENCH,
                DateUtils.tomorrow());
        System.out.println(offer);
        demandImplementation.addOffer(offer);
        assertEquals(DemandState.PREPARING, demandImplementation.getDemandState());

        assertNotNull(demandImplementation.getSelectedOffer());

        try {
            demandImplementation.authenticate(tomAuthToken);
            demandImplementation.removeOffer(demandImplementation.getSelectedOffer());
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            demandImplementation.authenticate(new AuthToken("admin", "admin"));
            demandImplementation.removeOffer(demandImplementation.getSelectedOffer());
            assertTrue(true);
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(0, demandImplementation.getOffers().size());
    }

    public void testCancelDevelopment() throws NotEnoughMoneyException, UnauthorizedOperationException {
        final DemandImplementation demandImplementation = createDemandAddOffer120AddContribution120BeginDev();

        try {
            demandImplementation.cancelDevelopment();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertEquals(UnauthorizedOperationException.SpecialCode.AUTHENTICATION_NEEDED, e.getCode());
        }

        try {
            demandImplementation.authenticate(yoAuthToken);
            demandImplementation.cancelDevelopment();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertEquals(UnauthorizedOperationException.SpecialCode.NON_DEVELOPER_CANCEL_DEMAND, e.getCode());
        }

        demandImplementation.authenticate(tomAuthToken);

        assertEquals(120, demandImplementation.getContribution().intValue());

        demandImplementation.cancelDevelopment();

        assertEquals(0, demandImplementation.getContribution().intValue());

        assertEquals(DemandState.DISCARDED, demandImplementation.getDemandState());

    }

    private DemandImplementation createDemandAddOffer120AddContribution120BeginDev() throws NotEnoughMoneyException, UnauthorizedOperationException {
        DemandImplementation demandImplementation = createDemandByThomas();
        assertEquals(DemandState.PENDING, demandImplementation.getDemandState());

        demandImplementation.authenticate(fredAuthToken);
        demandImplementation.addContribution(new BigDecimal("100"), "plop");
        assertEquals(DemandState.PENDING, demandImplementation.getDemandState());

        demandImplementation.authenticate(tomAuthToken);
        final Offer offer = new Offer(tomAuthToken.getMember(), demandImplementation, new BigDecimal("120"), "description", "title", Locale.FRENCH,
                DateUtils.tomorrow());
        demandImplementation.addOffer(offer);

        assertEquals(DemandState.PREPARING, demandImplementation.getDemandState());

        demandImplementation.authenticate(yoAuthToken);
        demandImplementation.addContribution(new BigDecimal("20"), "plip");
        assertEquals(DemandState.PREPARING, demandImplementation.getDemandState());

        demandImplementation = passeIntoDev(demandImplementation);

        assertEquals(DemandState.DEVELOPPING, demandImplementation.getDemandState());
        return demandImplementation;
    }

    public void testFinishedDevelopment() throws NotEnoughMoneyException, UnauthorizedOperationException {
        final DemandImplementation demandImplementation = createDemandAddOffer120AddContribution120BeginDev();

        try {
            demandImplementation.releaseCurrentBatch();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertEquals(UnauthorizedOperationException.SpecialCode.AUTHENTICATION_NEEDED, e.getCode());
        }

        try {
            demandImplementation.authenticate(yoAuthToken);
            demandImplementation.releaseCurrentBatch();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertEquals(UnauthorizedOperationException.SpecialCode.NON_DEVELOPER_FINISHED_DEMAND, e.getCode());
        }

        demandImplementation.authenticate(tomAuthToken);
        demandImplementation.releaseCurrentBatch();

        assertEquals(DemandState.INCOME, demandImplementation.getDemandState());
        assertEquals(120, demandImplementation.getContribution().intValue());
    }

    public void testOfferWithALotOfBatch() throws UnauthorizedOperationException, NotEnoughMoneyException {
        DemandImplementation demandImplementation = createDemandByThomas();
        final Offer offer = new Offer(tomAuthToken.getMember(), demandImplementation, new BigDecimal("10"), "description", "title", Locale.FRENCH,
                DateUtils.tomorrow());

        offer.addBatch(DateUtils.tomorrow(), BigDecimal.TEN, "title", "title", DateUtils.SECOND_PER_WEEK);
        offer.addBatch(DateUtils.nowPlusSomeDays(2), BigDecimal.TEN, "title", "title", DateUtils.SECOND_PER_WEEK);
        offer.addBatch(DateUtils.nowPlusSomeDays(4), BigDecimal.TEN, "title", "title", DateUtils.SECOND_PER_WEEK);
        offer.addBatch(DateUtils.nowPlusSomeDays(9), BigDecimal.TEN, "title", "title", DateUtils.SECOND_PER_WEEK);

        demandImplementation.authenticate(tomAuthToken);
        demandImplementation.addOffer(offer);

        demandImplementation.authenticate(yoAuthToken);
        demandImplementation.addContribution(new BigDecimal("12"), null);
        demandImplementation.addContribution(new BigDecimal("13"), null);
        demandImplementation.authenticate(fredAuthToken);
        demandImplementation.addContribution(new BigDecimal("14"), null);
        demandImplementation.addContribution(new BigDecimal("15"), null);
        demandImplementation.authenticate(tomAuthToken);
        demandImplementation.addContribution(new BigDecimal("16"), null);

        demandImplementation = passeIntoDev(demandImplementation);

        assertEquals(DemandState.DEVELOPPING, demandImplementation.getDemandState());

        demandImplementation.authenticate(tomAuthToken);
        demandImplementation.releaseCurrentBatch();

        assertEquals(DemandState.INCOME, demandImplementation.getDemandState());
        assertTrue(demandImplementation.validateCurrentBatch(true));
        assertEquals(DemandState.DEVELOPPING, demandImplementation.getDemandState());

        demandImplementation.releaseCurrentBatch();
        assertEquals(DemandState.INCOME, demandImplementation.getDemandState());
        assertTrue(demandImplementation.validateCurrentBatch(true));
        assertEquals(DemandState.DEVELOPPING, demandImplementation.getDemandState());

        demandImplementation.releaseCurrentBatch();
        assertEquals(DemandState.INCOME, demandImplementation.getDemandState());
        assertTrue(demandImplementation.validateCurrentBatch(true));
        assertEquals(DemandState.DEVELOPPING, demandImplementation.getDemandState());

        demandImplementation.releaseCurrentBatch();
        assertEquals(DemandState.INCOME, demandImplementation.getDemandState());
        assertTrue(demandImplementation.validateCurrentBatch(true));
        assertEquals(DemandState.DEVELOPPING, demandImplementation.getDemandState());

        demandImplementation.releaseCurrentBatch();
        assertEquals(DemandState.INCOME, demandImplementation.getDemandState());
        assertTrue(demandImplementation.validateCurrentBatch(true));
        assertEquals(DemandState.FINISHED, demandImplementation.getDemandState());

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

    // Passe into dev simulate the 1 day time to wait.
    // We assume that all the model has been closed, then the time out append, and then
    // the model is re-closed
    // So you have to reload from the db the demandImplementation. (So it return it ...)
    private DemandImplementation passeIntoDev(final DemandImplementation demandImplementation) {

        ModelManagerAccessor.close();
        ModelManagerAccessor.open();

        Mockit.setUpMock(DaoDemand.class, new MockDemandValidationTimeOut());

        new TaskSelectedOfferTimeOut(demandImplementation.getId(), new Date());
        try {
            Thread.sleep(1000);
        } catch (final InterruptedException e) {
            fail();
        }
        Mockit.tearDownMocks();

        // Some times has been spent. Model must have been closed and reopened.
        ModelManagerAccessor.close();
        ModelManagerAccessor.open();

        return DemandManager.getDemandById(demandImplementation.getId());

    }
}
