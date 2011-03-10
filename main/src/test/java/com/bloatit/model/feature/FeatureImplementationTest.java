package com.bloatit.model.feature;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import javassist.NotFoundException;
import mockit.Mock;
import mockit.Mockit;

import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMember.ActivationState;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoSoftware;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.DateUtils;
import com.bloatit.framework.webserver.ModelAccessor;
import com.bloatit.model.Feature;
import com.bloatit.model.ModelTestUnit;
import com.bloatit.model.Offer;
import com.bloatit.model.Software;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;

public class FeatureImplementationTest extends ModelTestUnit {

    public void testCreate() {
        final Feature feature = FeatureImplementation.create(DaoFeature.createAndPersist(tomAuthToken.getMember().getDao(),
                                                                                     DaoDescription.createAndPersist(tomAuthToken.getMember()
                                                                                                                                 .getDao(),
                                                                                                                     Locale.FRANCE,
                                                                                                                     "title",
                                                                                                                     "description"),
                                                                                     DaoSoftware.getByName("VLC")));
        assertNotNull(feature);
        assertNull(FeatureImplementation.create(null));
    }

    private Feature createFeatureByThomas() {
        return FeatureImplementation.create(DaoFeature.createAndPersist(tomAuthToken.getMember().getDao(),
                                                                      DaoDescription.createAndPersist(tomAuthToken.getMember().getDao(),
                                                                                                      Locale.FRANCE,
                                                                                                      "title",
                                                                                                      "description"),
                                                                      DaoSoftware.getByName("VLC")));
    }

    public void testFeature() {
        final Feature feature = new FeatureImplementation(tomAuthToken.getMember(),
                                                       Locale.FRANCE,
                                                       "title",
                                                       "Description",
                                                       Software.create(DaoSoftware.getByName("VLC")));
        assertEquals(feature.getAuthor(), tomAuthToken.getMember());
        try {
            assertEquals(feature.getDescription().getDefaultLocale(), Locale.FRANCE);
            assertEquals(feature.getDescription().getDefaultTranslation().getTitle(), "title");
            assertEquals(feature.getDescription().getDefaultTranslation().getText(), "Description");
        } catch (final UnauthorizedOperationException e) {
            fail();
        }
    }

    public void testCanAccessComment() {
        final Feature feature = createFeatureByThomas();
        assertTrue(feature.canAccessComment(Action.READ));
        assertFalse(feature.canAccessComment(Action.WRITE));
        assertFalse(feature.canAccessComment(Action.DELETE));

        // other
        feature.authenticate(fredAuthToken);
        assertTrue(feature.canAccessComment(Action.READ));
        assertTrue(feature.canAccessComment(Action.WRITE));
        assertFalse(feature.canAccessComment(Action.DELETE));

        // feature creator
        feature.authenticate(tomAuthToken);
        assertTrue(feature.canAccessComment(Action.READ));
        assertTrue(feature.canAccessComment(Action.WRITE));
        assertFalse(feature.canAccessComment(Action.DELETE));

        // For now nobody can delete a comment
    }

    public void testCanAccessContribution() {
        final Feature feature = createFeatureByThomas();
        assertTrue(feature.canAccessContribution(Action.READ));
        assertFalse(feature.canAccessContribution(Action.WRITE));
        assertFalse(feature.canAccessContribution(Action.DELETE));

        // owner of feature.
        feature.authenticate(tomAuthToken);
        assertTrue(feature.canAccessContribution(Action.READ));
        assertTrue(feature.canAccessContribution(Action.WRITE));
        assertFalse(feature.canAccessContribution(Action.DELETE));

        // other
        feature.authenticate(fredAuthToken);
        assertTrue(feature.canAccessContribution(Action.READ));
        assertTrue(feature.canAccessContribution(Action.WRITE));
        assertFalse(feature.canAccessContribution(Action.DELETE));
    }

    public void testCanAccessOffer() {
        final Feature feature = createFeatureByThomas();
        assertTrue(feature.canAccessOffer(Action.READ));
        assertFalse(feature.canAccessOffer(Action.WRITE));
        assertFalse(feature.canAccessOffer(Action.DELETE));

        // other
        feature.authenticate(fredAuthToken);
        assertTrue(feature.canAccessOffer(Action.READ));
        assertTrue(feature.canAccessOffer(Action.WRITE));
        assertFalse(feature.canAccessOffer(Action.DELETE));

        // owner of feature.
        feature.authenticate(tomAuthToken);
        assertTrue(feature.canAccessOffer(Action.READ));
        assertTrue(feature.canAccessOffer(Action.WRITE));
        assertFalse(feature.canAccessOffer(Action.DELETE));
    }

    public void testCanAccessDescription() {
        final Feature feature = createFeatureByThomas();
        assertTrue(feature.canAccessDescription());
        feature.authenticate(yoAuthToken);
        assertTrue(feature.canAccessDescription());
        feature.authenticate(tomAuthToken);
        assertTrue(feature.canAccessDescription());
    }

    public void testAddContribution() throws UnauthorizedOperationException {
        final Feature feature = createFeatureByThomas();

        assertEquals(FeatureState.PENDING, feature.getFeatureState());

        feature.authenticate(fredAuthToken);
        try {
            feature.addContribution(new BigDecimal("10"), "comment");
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(feature.getContribution(), new BigDecimal("10"));

        try {
            feature.addContribution(new BigDecimal("10"), null);
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(feature.getContribution(), new BigDecimal("20"));

        try {
            feature.addContribution(new BigDecimal("10"), "");
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(feature.getContribution(), new BigDecimal("30"));

        try {
            feature.addContribution(null, "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }

        assertEquals(feature.getContribution(), new BigDecimal("30"));

        try {
            feature.addContribution(new BigDecimal("-10"), "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        } catch (final FatalErrorException e) {
            assertTrue(true);
        }

        assertEquals(feature.getContribution(), new BigDecimal("30"));

        try {
            feature.addContribution(new BigDecimal("0"), "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            fail();
        } catch (final FatalErrorException e) {
            assertTrue(true);
        }

        assertEquals(feature.getContribution(), new BigDecimal("30"));
        feature.authenticate(null);

        try {
            feature.addContribution(new BigDecimal("10"), "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        assertEquals(feature.getContribution(), new BigDecimal("30"));

        // Tom have 1000 money.
        feature.authenticate(tomAuthToken);
        try {
            feature.addContribution(new BigDecimal("1001"), "comment");
            fail();
        } catch (final NotEnoughMoneyException e) {
            assertTrue(true);
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(feature.getContribution(), new BigDecimal("30"));
        assertEquals(FeatureState.PENDING, feature.getFeatureState());
    }

    public void testAddOffer() {
        final Feature feature = createFeatureByThomas();

        assertEquals(FeatureState.PENDING, feature.getFeatureState());
        feature.authenticate(fredAuthToken);

        try {
            assertNull(feature.getSelectedOffer());
            assertEquals(0, feature.getOffers().getPageSize());
        } catch (final UnauthorizedOperationException e1) {
            fail();
        }

        try {
            feature.authenticate(fredAuthToken);
            feature.addOffer(fredAuthToken.getMember(), new BigDecimal("120"), "description", Locale.FRENCH, DateUtils.tomorrow(), 0);
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(FeatureState.PREPARING, feature.getFeatureState());

        try {
            assertNotNull(feature.getSelectedOffer());
        } catch (final UnauthorizedOperationException e) {
            fail();
        }
        assertEquals(FeatureState.PREPARING, feature.getFeatureState());
    }

    public static class MockFeatureValidationTimeOut {
        @Mock
        public Date getValidationDate() {
            return DateUtils.yesterday();
        }
    }

    public void testBeginDevelopment() throws NotEnoughMoneyException, UnauthorizedOperationException {
        Feature feature = createFeatureByThomas();
        assertEquals(FeatureState.PENDING, feature.getFeatureState());

        feature.authenticate(fredAuthToken);
        feature.addContribution(new BigDecimal("100"), "plop");
        assertEquals(FeatureState.PENDING, feature.getFeatureState());

        feature.authenticate(tomAuthToken);
        feature.addOffer(tomAuthToken.getMember(), new BigDecimal("120"), "description", Locale.FRENCH, DateUtils.tomorrow(), 0);
        assertEquals(FeatureState.PREPARING, feature.getFeatureState());

        feature.authenticate(yoAuthToken);
        feature.addContribution(new BigDecimal("20"), "plip");
        assertEquals(FeatureState.PREPARING, feature.getFeatureState());

        feature = passeIntoDev(feature);

        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
    }

    public void testRemoveOffer() throws NotEnoughMoneyException, UnauthorizedOperationException, NotFoundException {
        final Feature feature = createFeatureByThomas();
        final DaoMember admin = DaoMember.createAndPersist("admin1", "admin1", "admin1", Locale.FRANCE);
        admin.setActivationState(ActivationState.ACTIVE);
        admin.setRole(Role.ADMIN);
        assertEquals(FeatureState.PENDING, feature.getFeatureState());

        feature.authenticate(fredAuthToken);
        feature.addContribution(new BigDecimal("100"), "plop");
        assertEquals(FeatureState.PENDING, feature.getFeatureState());

        feature.authenticate(tomAuthToken);

        feature.addOffer(tomAuthToken.getMember(), new BigDecimal("120"), "description", Locale.FRENCH, DateUtils.tomorrow(), 0);
        assertEquals(FeatureState.PREPARING, feature.getFeatureState());

        assertNotNull(feature.getSelectedOffer());

        try {
            feature.authenticate(tomAuthToken);
            feature.removeOffer(feature.getSelectedOffer());
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            feature.authenticate(new AuthToken("admin", "admin"));
            feature.removeOffer(feature.getSelectedOffer());
            assertTrue(true);
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(0, feature.getOffers().size());
    }

    public void testCancelDevelopment() throws NotEnoughMoneyException, UnauthorizedOperationException {
        final Feature feature = createFeatureAddOffer120AddContribution120BeginDev();

        try {
            feature.cancelDevelopment();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertEquals(UnauthorizedOperationException.SpecialCode.AUTHENTICATION_NEEDED, e.getCode());
        }

        try {
            feature.authenticate(yoAuthToken);
            feature.cancelDevelopment();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertEquals(UnauthorizedOperationException.SpecialCode.NON_DEVELOPER_CANCEL_FEATURE, e.getCode());
        }

        feature.authenticate(tomAuthToken);

        assertEquals(120, feature.getContribution().intValue());

        feature.cancelDevelopment();

        assertEquals(0, feature.getContribution().intValue());

        assertEquals(FeatureState.DISCARDED, feature.getFeatureState());

    }

    private Feature createFeatureAddOffer120AddContribution120BeginDev() throws NotEnoughMoneyException, UnauthorizedOperationException {
        Feature feature = createFeatureByThomas();
        assertEquals(FeatureState.PENDING, feature.getFeatureState());

        feature.authenticate(fredAuthToken);
        feature.addContribution(new BigDecimal("100"), "plop");
        assertEquals(FeatureState.PENDING, feature.getFeatureState());

        feature.authenticate(tomAuthToken);

        feature.addOffer(tomAuthToken.getMember(), new BigDecimal("120"), "description", Locale.FRENCH, DateUtils.tomorrow(), 0);

        assertEquals(FeatureState.PREPARING, feature.getFeatureState());

        feature.authenticate(yoAuthToken);
        feature.addContribution(new BigDecimal("20"), "plip");
        assertEquals(FeatureState.PREPARING, feature.getFeatureState());

        feature = passeIntoDev(feature);

        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        return feature;
    }

    public void testFinishedDevelopment() throws NotEnoughMoneyException, UnauthorizedOperationException {
        // final Feature feature =
        // createFeatureAddOffer120AddContribution120BeginDev();
        //
        // try {
        // feature.getSelectedOffer().;
        // fail();
        // } catch (final UnauthorizedOperationException e) {
        // assertEquals(UnauthorizedOperationException.SpecialCode.AUTHENTICATION_NEEDED,
        // e.getCode());
        // }
        //
        // try {
        // feature.authenticate(yoAuthToken);
        // feature.releaseCurrentBatch();
        // fail();
        // } catch (final UnauthorizedOperationException e) {
        // assertEquals(UnauthorizedOperationException.SpecialCode.NON_DEVELOPER_FINISHED_FEATURE,
        // e.getCode());
        // }
        //
        // feature.authenticate(tomAuthToken);
        // feature.releaseCurrentBatch();
        //
        // assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        // assertEquals(120, feature.getContribution().intValue());
        // TODO
    }

    public void testOfferWithALotOfBatch() throws UnauthorizedOperationException, NotEnoughMoneyException {
        Feature feature = createFeatureByThomas();

        feature.authenticate(tomAuthToken);
        final Offer offer = feature.addOffer(tomAuthToken.getMember(),
                                            BigDecimal.TEN,
                                            "description",
                                            Locale.FRENCH,
                                            DateUtils.tomorrow(),
                                            DateUtils.SECOND_PER_WEEK);

        offer.addBatch(BigDecimal.TEN, "description", Locale.FRENCH, DateUtils.tomorrow(), DateUtils.SECOND_PER_WEEK);
        offer.addBatch(BigDecimal.TEN, "description", Locale.FRENCH, DateUtils.nowPlusSomeDays(2), DateUtils.SECOND_PER_WEEK);
        offer.addBatch(BigDecimal.TEN, "description", Locale.FRENCH, DateUtils.nowPlusSomeDays(4), DateUtils.SECOND_PER_WEEK);
        offer.addBatch(BigDecimal.TEN, "description", Locale.FRENCH, DateUtils.nowPlusSomeDays(8), DateUtils.SECOND_PER_WEEK);

        feature.authenticate(yoAuthToken);
        feature.addContribution(new BigDecimal("12"), null);
        feature.addContribution(new BigDecimal("13"), null);
        feature.authenticate(fredAuthToken);
        feature.addContribution(new BigDecimal("14"), null);
        feature.addContribution(new BigDecimal("15"), null);
        feature.authenticate(tomAuthToken);
        feature.addContribution(new BigDecimal("16"), null);

        feature = passeIntoDev(feature);

        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());

        // feature.authenticate(tomAuthToken);
        // feature.releaseCurrentBatch();
        //
        // assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        // assertTrue(feature.validateCurrentBatch(true));
        // assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        //
        // feature.releaseCurrentBatch();
        // assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        // assertTrue(feature.validateCurrentBatch(true));
        // assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        //
        // feature.releaseCurrentBatch();
        // assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        // assertTrue(feature.validateCurrentBatch(true));
        // assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        //
        // feature.releaseCurrentBatch();
        // assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        // assertTrue(feature.validateCurrentBatch(true));
        // assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        //
        // feature.releaseCurrentBatch();
        // assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        // assertTrue(feature.validateCurrentBatch(true));
        // assertEquals(FeatureState.FINISHED, feature.getFeatureState());
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
    // So you have to reload from the db the feature. (So it return it ...)
    private Feature passeIntoDev(final Feature feature) {

        ModelAccessor.close();
        ModelAccessor.open();

        Mockit.setUpMock(DaoFeature.class, new MockFeatureValidationTimeOut());

        new TaskUpdateDevelopingState(feature.getId(), new Date());
        try {
            Thread.sleep(1000);
        } catch (final InterruptedException e) {
            fail();
        }
        Mockit.tearDownMocks();

        // Some times has been spent. Model must have been closed and reopened.
        ModelAccessor.close();
        ModelAccessor.open();

        return FeatureManager.getFeatureById(feature.getId());

    }
}
