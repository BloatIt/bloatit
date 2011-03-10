package com.bloatit.model.demand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import javassist.NotFoundException;
import mockit.Mock;
import mockit.Mockit;

import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMember.ActivationState;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoProject;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.DateUtils;
import com.bloatit.framework.webserver.ModelAccessor;
import com.bloatit.model.Feature;
import com.bloatit.model.ModelTestUnit;
import com.bloatit.model.Offer;
import com.bloatit.model.Project;
import com.bloatit.model.feature.DemandImplementation;
import com.bloatit.model.feature.DemandManager;
import com.bloatit.model.feature.TaskUpdateDevelopingState;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;

public class DemandImplementationTest extends ModelTestUnit {

    public void testCreate() {
        final Feature demand = DemandImplementation.create(DaoFeature.createAndPersist(tomAuthToken.getMember().getDao(),
                                                                                     DaoDescription.createAndPersist(tomAuthToken.getMember()
                                                                                                                                 .getDao(),
                                                                                                                     Locale.FRANCE,
                                                                                                                     "title",
                                                                                                                     "description"),
                                                                                     DaoProject.getByName("VLC")));
        assertNotNull(demand);
        assertNull(DemandImplementation.create(null));
    }

    private Feature createDemandByThomas() {
        return DemandImplementation.create(DaoFeature.createAndPersist(tomAuthToken.getMember().getDao(),
                                                                      DaoDescription.createAndPersist(tomAuthToken.getMember().getDao(),
                                                                                                      Locale.FRANCE,
                                                                                                      "title",
                                                                                                      "description"),
                                                                      DaoProject.getByName("VLC")));
    }

    public void testDemand() {
        final Feature demand = new DemandImplementation(tomAuthToken.getMember(),
                                                       Locale.FRANCE,
                                                       "title",
                                                       "Description",
                                                       Project.create(DaoProject.getByName("VLC")));
        assertEquals(demand.getAuthor(), tomAuthToken.getMember());
        try {
            assertEquals(demand.getDescription().getDefaultLocale(), Locale.FRANCE);
            assertEquals(demand.getDescription().getDefaultTranslation().getTitle(), "title");
            assertEquals(demand.getDescription().getDefaultTranslation().getText(), "Description");
        } catch (final UnauthorizedOperationException e) {
            fail();
        }
    }

    public void testCanAccessComment() {
        final Feature demand = createDemandByThomas();
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
        final Feature demand = createDemandByThomas();
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
        final Feature demand = createDemandByThomas();
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
        final Feature demand = createDemandByThomas();
        assertTrue(demand.canAccessDescription());
        demand.authenticate(yoAuthToken);
        assertTrue(demand.canAccessDescription());
        demand.authenticate(tomAuthToken);
        assertTrue(demand.canAccessDescription());
    }

    public void testAddContribution() throws UnauthorizedOperationException {
        final Feature demand = createDemandByThomas();

        assertEquals(FeatureState.PENDING, demand.getDemandState());

        demand.authenticate(fredAuthToken);
        try {
            demand.addContribution(new BigDecimal("10"), "comment");
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(demand.getContribution(), new BigDecimal("10"));

        try {
            demand.addContribution(new BigDecimal("10"), null);
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(demand.getContribution(), new BigDecimal("20"));

        try {
            demand.addContribution(new BigDecimal("10"), "");
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(demand.getContribution(), new BigDecimal("30"));

        try {
            demand.addContribution(null, "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }

        assertEquals(demand.getContribution(), new BigDecimal("30"));

        try {
            demand.addContribution(new BigDecimal("-10"), "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        } catch (final FatalErrorException e) {
            assertTrue(true);
        }

        assertEquals(demand.getContribution(), new BigDecimal("30"));

        try {
            demand.addContribution(new BigDecimal("0"), "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        } catch (final FatalErrorException e) {
            assertTrue(true);
        }

        assertEquals(demand.getContribution(), new BigDecimal("30"));
        demand.authenticate(null);

        try {
            demand.addContribution(new BigDecimal("10"), "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        assertEquals(demand.getContribution(), new BigDecimal("30"));

        // Tom have 1000 money.
        demand.authenticate(tomAuthToken);
        try {
            demand.addContribution(new BigDecimal("1001"), "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            assertTrue(true);
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(demand.getContribution(), new BigDecimal("30"));
        assertEquals(FeatureState.PENDING, demand.getDemandState());
    }

    public void testAddOffer() {
        final Feature demand = createDemandByThomas();

        assertEquals(FeatureState.PENDING, demand.getDemandState());
        demand.authenticate(fredAuthToken);

        try {
            assertNull(demand.getSelectedOffer());
            assertEquals(0, demand.getOffers().getPageSize());
        } catch (final UnauthorizedOperationException e1) {
            fail();
        }

        try {
            demand.authenticate(fredAuthToken);
            demand.addOffer(fredAuthToken.getMember(), new BigDecimal("120"), "description", Locale.FRENCH, DateUtils.tomorrow(), 0);
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(FeatureState.PREPARING, demand.getDemandState());

        try {
            assertNotNull(demand.getSelectedOffer());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }
        assertEquals(FeatureState.PREPARING, demand.getDemandState());
    }

    public static class MockDemandValidationTimeOut {
        @Mock
        public Date getValidationDate() {
            return DateUtils.yesterday();
        }
    }

    public void testBeginDevelopment() throws NotEnoughMoneyException, UnauthorizedOperationException {
        Feature demand = createDemandByThomas();
        assertEquals(FeatureState.PENDING, demand.getDemandState());

        demand.authenticate(fredAuthToken);
        demand.addContribution(new BigDecimal("100"), "plop");
        assertEquals(FeatureState.PENDING, demand.getDemandState());

        demand.authenticate(tomAuthToken);
        demand.addOffer(tomAuthToken.getMember(), new BigDecimal("120"), "description", Locale.FRENCH, DateUtils.tomorrow(), 0);
        assertEquals(FeatureState.PREPARING, demand.getDemandState());

        demand.authenticate(yoAuthToken);
        demand.addContribution(new BigDecimal("20"), "plip");
        assertEquals(FeatureState.PREPARING, demand.getDemandState());

        demand = passeIntoDev(demand);

        assertEquals(FeatureState.DEVELOPPING, demand.getDemandState());
    }

    public void testRemoveOffer() throws NotEnoughMoneyException, UnauthorizedOperationException, NotFoundException {
        final Feature demand = createDemandByThomas();
        final DaoMember admin = DaoMember.createAndPersist("admin1", "admin1", "admin1", Locale.FRANCE);
        admin.setActivationState(ActivationState.ACTIVE);
        admin.setRole(Role.ADMIN);
        assertEquals(FeatureState.PENDING, demand.getDemandState());

        demand.authenticate(fredAuthToken);
        demand.addContribution(new BigDecimal("100"), "plop");
        assertEquals(FeatureState.PENDING, demand.getDemandState());

        demand.authenticate(tomAuthToken);

        demand.addOffer(tomAuthToken.getMember(), new BigDecimal("120"), "description", Locale.FRENCH, DateUtils.tomorrow(), 0);
        assertEquals(FeatureState.PREPARING, demand.getDemandState());

        assertNotNull(demand.getSelectedOffer());

        try {
            demand.authenticate(tomAuthToken);
            demand.removeOffer(demand.getSelectedOffer());
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            demand.authenticate(new AuthToken("admin", "admin"));
            demand.removeOffer(demand.getSelectedOffer());
            assertTrue(true);
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(0, demand.getOffers().size());
    }

    public void testCancelDevelopment() throws NotEnoughMoneyException, UnauthorizedOperationException {
        final Feature demand = createDemandAddOffer120AddContribution120BeginDev();

        try {
            demand.cancelDevelopment();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertEquals(UnauthorizedOperationException.SpecialCode.AUTHENTICATION_NEEDED, e.getCode());
        }

        try {
            demand.authenticate(yoAuthToken);
            demand.cancelDevelopment();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertEquals(UnauthorizedOperationException.SpecialCode.NON_DEVELOPER_CANCEL_DEMAND, e.getCode());
        }

        demand.authenticate(tomAuthToken);

        assertEquals(120, demand.getContribution().intValue());

        demand.cancelDevelopment();

        assertEquals(0, demand.getContribution().intValue());

        assertEquals(FeatureState.DISCARDED, demand.getDemandState());

    }

    private Feature createDemandAddOffer120AddContribution120BeginDev() throws NotEnoughMoneyException, UnauthorizedOperationException {
        Feature demand = createDemandByThomas();
        assertEquals(FeatureState.PENDING, demand.getDemandState());

        demand.authenticate(fredAuthToken);
        demand.addContribution(new BigDecimal("100"), "plop");
        assertEquals(FeatureState.PENDING, demand.getDemandState());

        demand.authenticate(tomAuthToken);

        demand.addOffer(tomAuthToken.getMember(), new BigDecimal("120"), "description", Locale.FRENCH, DateUtils.tomorrow(), 0);

        assertEquals(FeatureState.PREPARING, demand.getDemandState());

        demand.authenticate(yoAuthToken);
        demand.addContribution(new BigDecimal("20"), "plip");
        assertEquals(FeatureState.PREPARING, demand.getDemandState());

        demand = passeIntoDev(demand);

        assertEquals(FeatureState.DEVELOPPING, demand.getDemandState());
        return demand;
    }

    public void testFinishedDevelopment() throws NotEnoughMoneyException, UnauthorizedOperationException {
        // final Demand demand =
        // createDemandAddOffer120AddContribution120BeginDev();
        //
        // try {
        // demand.getSelectedOffer().;
        // fail();
        // } catch (final UnauthorizedOperationException e) {
        // assertEquals(UnauthorizedOperationException.SpecialCode.AUTHENTICATION_NEEDED,
        // e.getCode());
        // }
        //
        // try {
        // demand.authenticate(yoAuthToken);
        // demand.releaseCurrentBatch();
        // fail();
        // } catch (final UnauthorizedOperationException e) {
        // assertEquals(UnauthorizedOperationException.SpecialCode.NON_DEVELOPER_FINISHED_DEMAND,
        // e.getCode());
        // }
        //
        // demand.authenticate(tomAuthToken);
        // demand.releaseCurrentBatch();
        //
        // assertEquals(DemandState.DEVELOPPING, demand.getDemandState());
        // assertEquals(120, demand.getContribution().intValue());
        // TODO
    }

    public void testOfferWithALotOfBatch() throws UnauthorizedOperationException, NotEnoughMoneyException {
        Feature demand = createDemandByThomas();

        demand.authenticate(tomAuthToken);
        final Offer offer = demand.addOffer(tomAuthToken.getMember(),
                                            BigDecimal.TEN,
                                            "description",
                                            Locale.FRENCH,
                                            DateUtils.tomorrow(),
                                            DateUtils.SECOND_PER_WEEK);

        offer.addBatch(BigDecimal.TEN, "description", Locale.FRENCH, DateUtils.tomorrow(), DateUtils.SECOND_PER_WEEK);
        offer.addBatch(BigDecimal.TEN, "description", Locale.FRENCH, DateUtils.nowPlusSomeDays(2), DateUtils.SECOND_PER_WEEK);
        offer.addBatch(BigDecimal.TEN, "description", Locale.FRENCH, DateUtils.nowPlusSomeDays(4), DateUtils.SECOND_PER_WEEK);
        offer.addBatch(BigDecimal.TEN, "description", Locale.FRENCH, DateUtils.nowPlusSomeDays(8), DateUtils.SECOND_PER_WEEK);

        demand.authenticate(yoAuthToken);
        demand.addContribution(new BigDecimal("12"), null);
        demand.addContribution(new BigDecimal("13"), null);
        demand.authenticate(fredAuthToken);
        demand.addContribution(new BigDecimal("14"), null);
        demand.addContribution(new BigDecimal("15"), null);
        demand.authenticate(tomAuthToken);
        demand.addContribution(new BigDecimal("16"), null);

        demand = passeIntoDev(demand);

        assertEquals(FeatureState.DEVELOPPING, demand.getDemandState());

        // demand.authenticate(tomAuthToken);
        // demand.releaseCurrentBatch();
        //
        // assertEquals(DemandState.DEVELOPPING, demand.getDemandState());
        // assertTrue(demand.validateCurrentBatch(true));
        // assertEquals(DemandState.DEVELOPPING, demand.getDemandState());
        //
        // demand.releaseCurrentBatch();
        // assertEquals(DemandState.DEVELOPPING, demand.getDemandState());
        // assertTrue(demand.validateCurrentBatch(true));
        // assertEquals(DemandState.DEVELOPPING, demand.getDemandState());
        //
        // demand.releaseCurrentBatch();
        // assertEquals(DemandState.DEVELOPPING, demand.getDemandState());
        // assertTrue(demand.validateCurrentBatch(true));
        // assertEquals(DemandState.DEVELOPPING, demand.getDemandState());
        //
        // demand.releaseCurrentBatch();
        // assertEquals(DemandState.DEVELOPPING, demand.getDemandState());
        // assertTrue(demand.validateCurrentBatch(true));
        // assertEquals(DemandState.DEVELOPPING, demand.getDemandState());
        //
        // demand.releaseCurrentBatch();
        // assertEquals(DemandState.DEVELOPPING, demand.getDemandState());
        // assertTrue(demand.validateCurrentBatch(true));
        // assertEquals(DemandState.FINISHED, demand.getDemandState());
        // TODO

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
    // We assume that all the model has been closed, then the time out append,
    // and then
    // the model is re-closed
    // So you have to reload from the db the demand. (So it return it ...)
    private Feature passeIntoDev(final Feature demand) {

        ModelAccessor.close();
        ModelAccessor.open();

        Mockit.setUpMock(DaoFeature.class, new MockDemandValidationTimeOut());

        new TaskUpdateDevelopingState(demand.getId(), new Date());
        try {
            Thread.sleep(1000);
        } catch (final InterruptedException e) {
            fail();
        }
        Mockit.tearDownMocks();

        // Some times has been spent. Model must have been closed and reopened.
        ModelAccessor.close();
        ModelAccessor.open();

        return DemandManager.getDemandById(demand.getId());

    }
}
