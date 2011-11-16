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
import com.bloatit.model.lists.KudosList;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.RgtKudosable;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedOperationException.SpecialCode;

public abstract class Kudosable<T extends DaoKudosable> extends UserContent<T> implements KudosableInterface {

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
        if (!canAccess(new RgtKudosable.Kudos(), Action.WRITE)) {
            errors.add(SpecialCode.NOTHING_SPECIAL);
        }

        if (getDao().isPopularityLocked()) {
            errors.add(SpecialCode.KUDOSABLE_LOCKED);
        }

        if (!AuthToken.isAuthenticated()) {
            errors.add(SpecialCode.AUTHENTICATION_NEEDED);
            // Stop tests here: the other tests need an AuthToken
            return errors;
        }

        if (AuthToken.getMember().getRole() == Role.ADMIN) {
            // Stop here. The member is an admin. He must be able to kudos
            // everything.
            return errors;
        }

        // I cannot kudos my own content.
        if (getRights().isOwner()) {
            errors.add(SpecialCode.OWNED_BY_ME);
        }

        // Only one kudos per person
        if (getDao().hasKudosed(AuthToken.getMember().getDao())) {
            errors.add(SpecialCode.ALREADY_VOTED);
        }

        // Make sure we are in the right position
        final Member member = AuthToken.getMember();
        final int influence = member.calculateInfluence();
        if (sign == -1 && influence < ModelConfiguration.getKudosableMinKarmaToUnkudos()) {
            errors.add(SpecialCode.INFLUENCE_LOW_ON_VOTE_DOWN);
        }
        if (sign == 1 && influence < ModelConfiguration.getKudosableMinKarmaToKudos()) {
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

    private int vote(final int sign) throws UnauthorizedOperationException {
        final EnumSet<SpecialCode> canKudos = canVote(sign);
        if (!canKudos.isEmpty()) {
            throw new UnauthorizedOperationException(canKudos.iterator().next());
        }

        // Make sure we are in the right position
        final Member member = AuthToken.getMember();
        final int influence = member.calculateInfluence();

        if (influence > 0) {
            //getMember().addToKarma(sign * influence);
            calculateNewState(getPopularity(), getDao().addKudos(member.getDao(), DaoGetter.get(AuthToken.getAsTeam()), sign * influence));
        }

        return influence * sign;
    }

    private void calculateNewState(final int oldPop, final int newPop) {
        switch (getState()) {
            case PENDING:
                if (newPop >= turnValid()) {
                    setStateUnprotected(PopularityState.VALIDATED);
                    getMember().addToKarma(1);
                    notifyValid();
                } else if (newPop <= turnHidden() && newPop > turnRejected()) {
                    setStateUnprotected(PopularityState.HIDDEN);
                    getMember().addToKarma(-1);
                    notifyHidden();
                } else if (newPop <= turnRejected()) {
                    setStateUnprotected(PopularityState.REJECTED);
                    getMember().addToKarma(-2);
                    notifyRejected();
                }
                // NO BREAK IT IS OK !!
            case VALIDATED:
                if (newPop <= turnPending()) {
                    setStateUnprotected(PopularityState.PENDING);
                    getMember().addToKarma(-1);
                    notifyPending();
                } else {
                    int oldKarma = (oldPop - turnValid())/ModelConfiguration.getKudosableStepToGainKarma();
                    int newKarma = (oldPop - turnValid())/ModelConfiguration.getKudosableStepToGainKarma();
                    getMember().addToKarma(newKarma - oldKarma);
                }
                break;
            case HIDDEN:
            case REJECTED:
                if (newPop >= turnPending() && newPop < turnValid()) {
                    setStateUnprotected(PopularityState.PENDING);
                    getMember().addToKarma(2);
                    notifyPending();
                } else if (newPop >= turnValid()) {
                    setStateUnprotected(PopularityState.VALIDATED);
                    getMember().addToKarma(3);
                    notifyValid();
                } else if (newPop <= turnPending() && newPop > turnHidden()) {
                    setStateUnprotected(PopularityState.HIDDEN);
                    getMember().addToKarma(1);
                    notifyValid();
                }
                break;
            default:
                assert false;
                break;
        }
    }

    private void setStateUnprotected(final PopularityState newState) {
        if (getState() != newState) {
            Log.model().info("Kudosable: " + getId() + " change from state: " + this.getState() + ", to: " + newState);
        }
        getDao().setState(newState);
    }

    public void setState(final PopularityState newState) throws UnauthorizedOperationException {
        if (!getRights().hasAdminUserPrivilege()) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        setStateUnprotected(newState);
    }

    @Override
    protected void delete(boolean delOrder) throws UnauthorizedOperationException {
        for (final Kudos kudos : getKudos()) {
            kudos.delete(delOrder);
        }
        super.delete(delOrder);
    }

    private KudosList getKudos() {
        return new KudosList(getDao().getKudos());
    }

    @Override
    public void lockPopularity() throws UnauthorizedOperationException {
        if (!getRights().hasAdminUserPrivilege()) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        getDao().lockPopularity();
    }

    @Override
    public void unlockPopularity() throws UnauthorizedOperationException {
        if (!getRights().hasAdminUserPrivilege()) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        getDao().unlockPopularity();
    }

    @Override
    public final PopularityState getState() {
        return getDao().getState();
    }

    @Override
    public boolean isPopularityLocked() throws UnauthorizedOperationException {
        return getDao().isPopularityLocked();
    }

    @Override
    public final int getPopularity() {
        return getDao().getPopularity();
    }

    @Override
    public int getUserVoteValue() {
        if (!AuthToken.isAuthenticated()) {
            return 0;
        }
        return getDao().getVote(AuthToken.getMember().getDao());
    }

    /**
     * You can redefine me if you want to customize the state calculation
     * limits.
     * 
     * @return The popularity to for a Kudosable to reach to turn to
     *         {@link PopularityState#PENDING} state.
     */
    protected int turnPending() {
        return ModelConfiguration.getKudosableDefaultTurnPending();
    }

    /**
     * You can redefine me if you want to customize the state calculation
     * limits.
     * 
     * @return The popularity to for a Kudosable to reach to turn to
     *         {@link PopularityState#VALIDATED} state.
     */
    protected int turnValid() {
        return ModelConfiguration.getKudosableDefaultTurnValid();
    }

    /**
     * You can redefine me if you want to customize the state calculation
     * limits.
     * 
     * @return The popularity to for a Kudosable to reach to turn to
     *         {@link PopularityState#REJECTED} state.
     */
    protected int turnRejected() {
        return ModelConfiguration.getKudosableDefaultTurnRejected();
    }

    /**
     * You can redefine me if you want to customize the state calculation
     * limits.
     * 
     * @return The popularity to for a Kudosable to reach to turn to
     *         {@link PopularityState#HIDDEN} state.
     */
    protected int turnHidden() {
        return ModelConfiguration.getKudosableDefaultTurnHidden();
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
    private void notifyHidden() {
        // Implement me if you wish
    }

}
