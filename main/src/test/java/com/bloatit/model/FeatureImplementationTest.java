//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import javassist.NotFoundException;
import mockit.Mock;
import mockit.Mockit;

import org.junit.Test;

import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoSoftware;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.model.ModelAccessor;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.webprocessor.context.User.ActivationState;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.model.feature.TaskUpdateDevelopingState;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedOperationException;

public class FeatureImplementationTest extends ModelTestUnit {

    @Test
    public void testCreate() {
        final Feature feature = FeatureImplementation.create(DaoFeature.createAndPersist(memberTom.getDao(),
                                                                                         null,
                                                                                         DaoDescription.createAndPersist(memberTom
                                                                                                                                     .getDao(),
                                                                                                                         null,
                                                                                                                         Locale.FRANCE,
                                                                                                                         "title",
                                                                                                                         "description"),
                                                                                         DaoSoftware.getByName("VLC")));
        assertNotNull(feature);
        assertNull(FeatureImplementation.create(null));
    }

    private Feature createFeatureByThomas() {
        return FeatureImplementation.create(DaoFeature.createAndPersist(memberTom.getDao(),
                                                                        null,
                                                                        DaoDescription.createAndPersist(memberTom.getDao(),
                                                                                                        null,
                                                                                                        Locale.FRANCE,
                                                                                                        "title",
                                                                                                        "description"),
                                                                        DaoSoftware.getByName("VLC")));
    }

    @Test
    public void testFeature() {
        final Feature feature = new FeatureImplementation(memberTom,
                                                          null,
                                                          Locale.FRANCE,
                                                          "title",
                                                          "Description",
                                                          Software.create(DaoSoftware.getByName("VLC")));
        assertEquals(feature.getAuthor(), memberTom);
        assertEquals(feature.getDescription().getDefaultLocale(), Locale.FRANCE);
        assertEquals(feature.getDescription().getDefaultTranslation().getTitle(), "title");
        assertEquals(feature.getDescription().getDefaultTranslation().getText(), "Description");
    }

    @Test
    public void testCanAccessComment() {
        final Feature feature = createFeatureByThomas();
        assertTrue(feature.canAccessComment(Action.READ));
        assertFalse(feature.canAccessComment(Action.WRITE));
        assertFalse(feature.canAccessComment(Action.DELETE));

        // other
        AuthToken.authenticate(memeberFred);
        assertTrue(feature.canAccessComment(Action.READ));
        assertTrue(feature.canAccessComment(Action.WRITE));
        assertFalse(feature.canAccessComment(Action.DELETE));

        // feature creator
        AuthToken.authenticate(memberTom);
        assertTrue(feature.canAccessComment(Action.READ));
        assertTrue(feature.canAccessComment(Action.WRITE));
        assertFalse(feature.canAccessComment(Action.DELETE));

        // For now nobody can delete a comment
    }

    @Test
    public void testCanAccessContribution() {
        final Feature feature = createFeatureByThomas();
        assertTrue(feature.canAccessContribution(Action.READ));
        assertFalse(feature.canAccessContribution(Action.WRITE));
        assertFalse(feature.canAccessContribution(Action.DELETE));

        // owner of feature.
        AuthToken.authenticate(memberTom);
        assertTrue(feature.canAccessContribution(Action.READ));
        assertTrue(feature.canAccessContribution(Action.WRITE));
        assertFalse(feature.canAccessContribution(Action.DELETE));

        // other
        AuthToken.authenticate(memeberFred);
        assertTrue(feature.canAccessContribution(Action.READ));
        assertTrue(feature.canAccessContribution(Action.WRITE));
        assertFalse(feature.canAccessContribution(Action.DELETE));
    }

    @Test
    public void testCanAccessOffer() {
        final Feature feature = createFeatureByThomas();
        assertTrue(feature.canAccessOffer(Action.READ));
        assertFalse(feature.canAccessOffer(Action.WRITE));
        assertFalse(feature.canAccessOffer(Action.DELETE));

        // other
        AuthToken.authenticate(memeberFred);
        assertTrue(feature.canAccessOffer(Action.READ));
        assertTrue(feature.canAccessOffer(Action.WRITE));
        assertFalse(feature.canAccessOffer(Action.DELETE));

        // owner of feature.
        AuthToken.authenticate(memberTom);
        assertTrue(feature.canAccessOffer(Action.READ));
        assertTrue(feature.canAccessOffer(Action.WRITE));
        assertFalse(feature.canAccessOffer(Action.DELETE));
    }

    @Test
    public void testAddContribution() {
        final Feature feature = createFeatureByThomas();

        assertEquals(FeatureState.PENDING, feature.getFeatureState());

        AuthToken.authenticate(memeberFred);
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
        } catch (final BadProgrammerException e) {
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
        } catch (final BadProgrammerException e) {
            assertTrue(true);
        }

        assertEquals(feature.getContribution(), new BigDecimal("30"));
        AuthToken.unAuthenticate();;

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
        AuthToken.authenticate(memberTom);
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

    @Test
    public void testAddOffer() {
        final Feature feature = createFeatureByThomas();

        assertEquals(FeatureState.PENDING, feature.getFeatureState());
        AuthToken.authenticate(memeberFred);

        assertNull(feature.getSelectedOffer());
        assertEquals(0, feature.getOffers().getPageSize());

        try {
            AuthToken.authenticate(memeberFred);
            feature.addOffer(new BigDecimal("120"), "description", "GNU GPL V3", Locale.FRENCH, DateUtils.tomorrow(), 0);
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(FeatureState.PREPARING, feature.getFeatureState());

        assertNotNull(feature.getSelectedOffer());
        assertEquals(FeatureState.PREPARING, feature.getFeatureState());
    }

    public static class MockFeatureValidationTimeOut {
        @Mock
        public Date getValidationDate() {
            return DateUtils.yesterday();
        }
    }

    @Test
    public void testBeginDevelopment() throws NotEnoughMoneyException, UnauthorizedOperationException {
        Feature feature = createFeatureByThomas();
        assertEquals(FeatureState.PENDING, feature.getFeatureState());

        AuthToken.authenticate(memeberFred);
        feature.addContribution(new BigDecimal("100"), "plop");
        assertEquals(FeatureState.PENDING, feature.getFeatureState());

        AuthToken.authenticate(memberTom);
        feature.addOffer(new BigDecimal("120"), "description", "GNU GPL V3", Locale.FRENCH, DateUtils.tomorrow(), 0);
        assertEquals(FeatureState.PREPARING, feature.getFeatureState());

        AuthToken.authenticate(memberYo);
        feature.addContribution(new BigDecimal("20"), "plip");
        assertEquals(FeatureState.PREPARING, feature.getFeatureState());

        feature = passeIntoDev(feature);

        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
    }

    @Test
    public void testRemoveOffer() throws NotEnoughMoneyException, UnauthorizedOperationException, NotFoundException {
        final Feature feature = createFeatureByThomas();
        final DaoMember admin = DaoMember.createAndPersist("admin1", "admin1", "salt", "admin1", Locale.FRANCE);
        admin.setActivationState(ActivationState.ACTIVE);
        admin.setRole(Role.ADMIN);
        assertEquals(FeatureState.PENDING, feature.getFeatureState());

        AuthToken.authenticate(memeberFred);
        feature.addContribution(new BigDecimal("100"), "plop");
        assertEquals(FeatureState.PENDING, feature.getFeatureState());

        AuthToken.authenticate(memberTom);

        feature.addOffer(new BigDecimal("120"), "description", "GNU GPL V3", Locale.FRENCH, DateUtils.tomorrow(), 0);
        assertEquals(FeatureState.PREPARING, feature.getFeatureState());

        assertNotNull(feature.getSelectedOffer());

        try {
            AuthToken.authenticate(memberTom);
            feature.removeOffer(feature.getSelectedOffer());
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            AuthToken.authenticate(memberAdmin);
            feature.removeOffer(feature.getSelectedOffer());
            assertTrue(true);
        } catch (final UnauthorizedOperationException e) {
            fail();
        }

        assertEquals(0, feature.getOffers().size());
    }

    @Test
    public void testCancelDevelopment() throws NotEnoughMoneyException, UnauthorizedOperationException {
        final Feature feature = createFeatureAddOffer120AddContribution120BeginDev();

        try {
            feature.cancelDevelopment();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        try {
            AuthToken.authenticate(memberYo);
            feature.cancelDevelopment();
            fail();
        } catch (final UnauthorizedOperationException e) {
            assertTrue(true);
        }

        AuthToken.authenticate(memberTom);

        assertEquals(120, feature.getContribution().intValue());

        feature.cancelDevelopment();

        assertEquals(0, feature.getContribution().intValue());

        assertEquals(FeatureState.DISCARDED, feature.getFeatureState());
    }

    private Feature createFeatureAddOffer120AddContribution120BeginDev() throws NotEnoughMoneyException, UnauthorizedOperationException {
        Feature feature = createFeatureByThomas();
        assertEquals(FeatureState.PENDING, feature.getFeatureState());

        AuthToken.authenticate(memeberFred);
        feature.addContribution(new BigDecimal("100"), "plop");
        assertEquals(FeatureState.PENDING, feature.getFeatureState());

        AuthToken.authenticate(memberTom);

        feature.addOffer(new BigDecimal("120"), "description", "GNU GPL V3", Locale.FRENCH, DateUtils.tomorrow(), 0);

        assertEquals(FeatureState.PREPARING, feature.getFeatureState());

        AuthToken.authenticate(memberYo);
        feature.addContribution(new BigDecimal("20"), "plip");
        assertEquals(FeatureState.PREPARING, feature.getFeatureState());

        feature = passeIntoDev(feature);

        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        return feature;
    }

    @Test
    public void testOfferWithALotOfMilestone() throws UnauthorizedOperationException, NotEnoughMoneyException {
        Feature feature = createFeatureByThomas();

        AuthToken.authenticate(memberTom);
        final Offer offer = feature.addOffer(BigDecimal.TEN,
                                             "description",
                                             "GNU GPL V3",
                                             Locale.FRENCH,
                                             DateUtils.tomorrow(),
                                             DateUtils.SECOND_PER_WEEK);

        AuthToken.authenticate(memberTom);
        offer.addMilestone(BigDecimal.TEN, "description", Locale.FRENCH, DateUtils.tomorrow(), DateUtils.SECOND_PER_WEEK);
        offer.addMilestone(BigDecimal.TEN, "description", Locale.FRENCH, DateUtils.nowPlusSomeDays(2), DateUtils.SECOND_PER_WEEK);
        offer.addMilestone(BigDecimal.TEN, "description", Locale.FRENCH, DateUtils.nowPlusSomeDays(4), DateUtils.SECOND_PER_WEEK);
        offer.addMilestone(BigDecimal.TEN, "description", Locale.FRENCH, DateUtils.nowPlusSomeDays(8), DateUtils.SECOND_PER_WEEK);

        AuthToken.authenticate(memberYo);
        feature.addContribution(new BigDecimal("12"), null);
        feature.addContribution(new BigDecimal("13"), null);
        AuthToken.authenticate(memeberFred);
        feature.addContribution(new BigDecimal("14"), null);
        feature.addContribution(new BigDecimal("15"), null);
        AuthToken.authenticate(memberTom);
        feature.addContribution(new BigDecimal("16"), null);

        feature = passeIntoDev(feature);

        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());

        AuthToken.authenticate(memberTom);
        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        feature.getSelectedOffer().validateCurrentMilestone(true);
        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());

        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        feature.getSelectedOffer().validateCurrentMilestone(true);
        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());

        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        feature.getSelectedOffer().validateCurrentMilestone(true);
        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());

        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        feature.getSelectedOffer().validateCurrentMilestone(true);
        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());

        assertEquals(FeatureState.DEVELOPPING, feature.getFeatureState());
        feature.getSelectedOffer().validateCurrentMilestone(true);
        assertEquals(FeatureState.FINISHED, feature.getFeatureState());
    }

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

    /**
     * Test the creation of a complete feature test set (including offer,
     * milestones, contributions, kudos ...) and then delete the feature and
     * check if eveything is correctly delete.
     * 
     * @throws UnauthorizedOperationException
     * @throws NotEnoughMoneyException
     */
    @Test
    public void testDeleteFeature() throws NotEnoughMoneyException, UnauthorizedOperationException {
        final Feature feature = createFeatureByThomas();

        AuthToken.authenticate(memberTom);
        feature.addContribution(new BigDecimal(50), "Woohoo");
        final Comment comm1 = feature.addComment("You shouldn't do it this way");
        AuthToken.authenticate(memberTom);
        final Comment reply11 = comm1.addComment("Mmmh in fact you should ... I was wrong !");

        AuthToken.authenticate(memeberFred);
        feature.addContribution(new BigDecimal(75), "Plop");
        AuthToken.authenticate(memeberFred);
        final Comment reply12 = comm1.addComment("Indeed you were wrong");

        AuthToken.authenticate(memberYo);
        feature.addContribution(new BigDecimal(200), "Yeahh");
        final Comment comm2 = feature.addComment("Do it your way, ignore tom he's obviously wrong !");

        AuthToken.authenticate(loser);
        final Offer offer = feature.addOffer(new BigDecimal("300"), "Beautiful offer", "AGPL", new Locale("fr", "fr"), DateUtils.tomorrow(), 100);
        final Milestone milestone = offer.getCurrentMilestone();
        final Release release = milestone.addRelease("Beta 1", "0.1", new Locale("fr", "fr"), null);

        final Bug bug1 = milestone.addBug("A new bug 1", "This is a new bug description 1", new Locale("en", "us"), Level.FATAL);
        AuthToken.authenticate(memeberFred);
        final Comment bugComm11 = bug1.addComment("plop");
        AuthToken.authenticate(memberTom);
        final Comment bugComm111 = bugComm11.addComment("plip");
        AuthToken.authenticate(memberYo);
        final Comment bugComm12 = bug1.addComment("plap");
        AuthToken.authenticate(memeberFred);
        final Bug bug2 = milestone.addBug("A new bug 2", "This is a new bug description 2", new Locale("en", "us"), Level.MAJOR);
        AuthToken.authenticate(memberYo);
        final Bug bug3 = milestone.addBug("A new bug 3", "This is a new bug description 3", new Locale("en", "us"), Level.MINOR);
        AuthToken.authenticate(memberTom);
        final Bug bug4 = milestone.addBug("A new bug 4", "This is a new bug description 4", new Locale("en", "us"), Level.FATAL);
        AuthToken.authenticate(memeberFred);
        final Comment bugComm41 = bug4.addComment("plop");
        AuthToken.authenticate(memberTom);
        final Comment bugComm411 = bugComm41.addComment("plip");
        AuthToken.authenticate(memeberFred);
        final Comment bugComm42 = bug4.addComment("plap");
        AuthToken.authenticate(memberYo);

        AuthToken.authenticate(memberAdmin);
        feature.delete();

        assertTrue(feature.isDeleted());
        assertTrue(comm1.isDeleted());
        assertTrue(reply11.isDeleted());
        assertTrue(reply12.isDeleted());
        assertTrue(comm2.isDeleted());
        assertTrue(release.isDeleted());
        assertTrue(offer.isDeleted());
        assertTrue(bug1.isDeleted());
        assertTrue(bug2.isDeleted());
        assertTrue(bug3.isDeleted());
        assertTrue(bug4.isDeleted());
        assertTrue(bugComm11.isDeleted());
        assertTrue(bugComm111.isDeleted());
        assertTrue(bugComm12.isDeleted());
        assertTrue(bugComm41.isDeleted());
        assertTrue(bugComm411.isDeleted());
        assertTrue(bugComm42.isDeleted());
    }
}
