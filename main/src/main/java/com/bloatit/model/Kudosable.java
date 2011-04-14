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

import java.util.EnumSet;

import com.bloatit.common.Log;
import com.bloatit.data.DaoKudosable;
import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException.SpecialCode;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.KudosableRight;

public abstract class Kudosable<T extends DaoKudosable> extends UserContent<T> implements KudosableInterface<T> {

    private static final int TURN_VALID = ModelConfiguration.getKudosableDefaultTurnValid();
    private static final int TURN_REJECTED = ModelConfiguration.getKudosableDefaultTurnRejected();
    private static final int TURN_HIDDEN = ModelConfiguration.getKudosableDefaultTurnHidden();
    private static final int TURN_PENDING = ModelConfiguration.getKudosableDefaultTurnPending();

    private static final int MIN_INFLUENCE_TO_UNKUDOS = ModelConfiguration.getKudosableMinInfluenceToUnkudos();
    private static final int MIN_INFLUENCE_TO_KUDOS = ModelConfiguration.getKudosableMinInfluenceToKudos();

    public Kudosable(final T dao) {
        super(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.KudosableInterface#canKudos()
     */
    @Override
    public EnumSet<SpecialCode> canVoteUp() {
        return canVote(1);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.KudosableInterface#canUnkudos()
     */
    @Override
    public EnumSet<SpecialCode> canVoteDown() {
        return canVote(-1);
    }

    private final EnumSet<SpecialCode> canVote(final int sign) {
        final EnumSet<SpecialCode> errors = EnumSet.noneOf(SpecialCode.class);

        // See if we can kudos.
        if (!canAccess(new KudosableRight.Kudos(), Action.WRITE)) {
            errors.add(SpecialCode.NOTHING_SPECIAL);
        }

        if (getDao().isPopularityLocked()) {
            errors.add(SpecialCode.KUDOSABLE_LOCKED);
        }

        if (getAuthTokenUnprotected() == null) {
            errors.add(SpecialCode.AUTHENTICATION_NEEDED);
            // Stop tests here: the other tests need an AuthToken
            return errors;
        }

        if (getAuthTokenUnprotected().getMember().getRole() == Role.ADMIN) {
            // Stop here. The member is an admin. He must be able to kudos
            // everything.
            return errors;
        }

        // I cannot kudos my own content.
        if (isOwner()) {
            errors.add(SpecialCode.OWNED_BY_ME);
        }

        // Only one kudos per person
        if (getDao().hasKudosed(getAuthTokenUnprotected().getMember().getDao())) {
            errors.add(SpecialCode.ALREADY_VOTED);
        }

        // Make sure we are in the right position
        final Member member = getAuthTokenUnprotected().getMember();
        final int influence = member.calculateInfluence();
        if (sign == -1 && influence < MIN_INFLUENCE_TO_UNKUDOS) {
            errors.add(SpecialCode.INFLUENCE_LOW_ON_VOTE_DOWN);
        }
        if (sign == 1 && influence < MIN_INFLUENCE_TO_KUDOS) {
            errors.add(SpecialCode.INFLUENCE_LOW_ON_VOTE_UP);
        }
        return errors;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.KudosableInterface#unkudos()
     */
    @Override
    public final int voteDown() throws UnauthorizedOperationException {
        final int vote = vote(-1);
        notifyKudos(false);
        return vote;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.KudosableInterface#kudos()
     */
    @Override
    public final int voteUp() throws UnauthorizedOperationException {
        final int vote = vote(1);
        notifyKudos(true);
        return vote;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.KudosableInterface#getState()
     */
    @Override
    public final PopularityState getState() {
        return getDao().getState();
    }

    private int vote(final int sign) throws UnauthorizedOperationException {
        final EnumSet<SpecialCode> canKudos = canVote(sign);
        if (!canKudos.isEmpty()) {
            throw new UnauthorizedOperationException(canKudos.iterator().next());
        }

        // Make sure we are in the right position
        final Member member = getAuthToken().getMember();
        final int influence = member.calculateInfluence();

        if (influence > 0) {
            getMember().addToKarma(influence);
            calculateNewState(getDao().addKudos(member.getDao(), sign * influence));
        }

        return influence * sign;
    }

    private void calculateNewState(final int newPop) {
        switch (getState()) {
            case PENDING:
                if (newPop >= turnValid()) {
                    setStateUnprotected(PopularityState.VALIDATED);
                    notifyValid();
                } else if (newPop <= turnHidden() && newPop > turnRejected()) {
                    setStateUnprotected(PopularityState.HIDDEN);
                    notifyHidden();
                } else if (newPop <= turnRejected()) {
                    setStateUnprotected(PopularityState.REJECTED);
                    notifyRejected();
                }
                // NO BREAK IT IS OK !!
            case VALIDATED:
                if (newPop <= turnPending()) {
                    setStateUnprotected(PopularityState.PENDING);
                    notifyPending();
                }
                break;
            case HIDDEN:
            case REJECTED:
                if (newPop >= turnPending() && newPop < turnValid()) {
                    setStateUnprotected(PopularityState.PENDING);
                    notifyPending();
                } else if (newPop >= turnValid()) {
                    setStateUnprotected(PopularityState.VALIDATED);
                    notifyValid();
                } else if (newPop <= turnHidden() && newPop > turnRejected()) {
                    setStateUnprotected(PopularityState.VALIDATED);
                    notifyValid();
                } else if (newPop <= turnRejected()) {
                    setStateUnprotected(PopularityState.REJECTED);
                    notifyRejected();
                }
                break;
            default:
                assert false;
                break;
        }
    }

    public void setState(final PopularityState newState) throws UnauthorizedOperationException {
        if (!hasUserPrivilege(Role.ADMIN)) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        setStateUnprotected(newState);
    }

    private void setStateUnprotected(final PopularityState newState) {
        if (getState() != newState) {
            Log.model().info("Kudosable: " + getId() + " change from state: " + this.getState() + ", to: " + newState);
        }
        getDao().setState(newState);
    }

    // TODO right management
    @Override
    public void lockPopularity() throws UnauthorizedOperationException {
        if (!hasUserPrivilege(Role.ADMIN)) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        getDao().lockPopularity();
    }

    // TODO right management
    @Override
    public void unlockPopularity() throws UnauthorizedOperationException {
        if (!hasUserPrivilege(Role.ADMIN)) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        getDao().unlockPopularity();
    }

    // TODO right management
    @Override
    public boolean isPopularityLocked() throws UnauthorizedOperationException {
        if (!hasUserPrivilege(Role.ADMIN)) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        return getDao().isPopularityLocked();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.KudosableInterface#getPopularity()
     */
    @Override
    public final int getPopularity() {
        return getDao().getPopularity();
    }

    @Override
    public int getUserVoteValue() {
        final AuthToken authToken = getAuthTokenUnprotected();

        if (getAuthTokenUnprotected() == null) {
            return 0;
        }

        return getDao().getVote(authToken.getMember().getDao());
    }

    /**
     * You can redefine me if you want to customize the state calculation
     * limits. Default value is {@value #TURN_PENDING}.
     * 
     * @return The popularity to for a Kudosable to reach to turn to
     *         {@link PopularityState#PENDING} state.
     */
    protected int turnPending() {
        return TURN_PENDING;
    }

    /**
     * You can redefine me if you want to customize the state calculation
     * limits. Default value is {@value #TURN_VALID}.
     * 
     * @return The popularity to for a Kudosable to reach to turn to
     *         {@link PopularityState#VALIDATED} state.
     */
    protected int turnValid() {
        return TURN_VALID;
    }

    /**
     * You can redefine me if you want to customize the state calculation
     * limits. Default value is {@value #TURN_REJECTED}.
     * 
     * @return The popularity to for a Kudosable to reach to turn to
     *         {@link PopularityState#REJECTED} state.
     */
    protected int turnRejected() {
        return TURN_REJECTED;
    }

    /**
     * You can redefine me if you want to customize the state calculation
     * limits. Default value is {@value #TURN_HIDDEN}.
     * 
     * @return The popularity to for a Kudosable to reach to turn to
     *         {@link PopularityState#HIDDEN} state.
     */
    protected int turnHidden() {
        return TURN_HIDDEN;
    }

    /**
     * This method is called each time this Kudosable is kudosed.
     * 
     * @param positif true if it is a kudos false if it is an unKudos.
     */
    protected void notifyKudos(final boolean positif) {
        // Implement me if you wish
    }

    /**
     * This method is called each time this Kudosable change its state to valid.
     */
    protected void notifyValid() {
        // Implement me if you wish
    }

    /**
     * This method is called each time this Kudosable change its state to
     * pending.
     */
    protected void notifyPending() {
        // Implement me if you wish
    }

    /**
     * This method is called each time this Kudosable change its state to
     * rejected.
     */
    protected void notifyRejected() {
        // Implement me if you wish
    }

    /**
     * This method is called each time this Kudosable change its state to
     * Hidden.
     */
    protected void notifyHidden() {
        // Implement me if you wish
    }

}
