package com.bloatit.model;

import java.util.EnumSet;

import com.bloatit.common.Log;
import com.bloatit.data.DaoKudosable;
import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException.SpecialCode;
import com.bloatit.model.right.KudosableRight;
import com.bloatit.model.right.RightManager.Action;

public abstract class Kudosable<T extends DaoKudosable> extends UserContent<T> implements KudosableInterface<T> {

    private static final int TURN_VALID = KudosableConfiguration.getDefaultTurnValid();
    private static final int TURN_REJECTED = KudosableConfiguration.getDefaultTurnRejected();
    private static final int TURN_HIDDEN = KudosableConfiguration.getDefaultTurnHidden();
    private static final int TURN_PENDING = KudosableConfiguration.getDefaultTurnPending();

    private static final int MIN_INFLUENCE_TO_UNKUDOS = KudosableConfiguration.getMinInfluenceToUnkudos();
    private static final int MIN_INFLUENCE_TO_KUDOS = KudosableConfiguration.getMinInfluenceToKudos();

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

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.KudosableInterface#unkudos()
     */
    @Override
    public final int voteDown() throws UnauthorizedOperationException {
        int vote = vote(-1);
        notifyKudos(false);
        return vote;
    }

    @Override
    public int getUserVoteValue() {
        AuthToken authToken = getAuthTokenUnprotected();

        if (getAuthTokenUnprotected() == null) {
            return 0;
        }

        return getDao().getVote(authToken.getMember().getDao());
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.KudosableInterface#kudos()
     */
    @Override
    public final int voteUp() throws UnauthorizedOperationException {
        int vote = vote(1);
        notifyKudos(true);
        return vote;
    }

    private final EnumSet<SpecialCode> canVote(int sign) {
        EnumSet<SpecialCode> errors = EnumSet.noneOf(SpecialCode.class);

        // See if we can kudos.
        if (!new KudosableRight.Kudos().canAccess(calculateRole(this), Action.WRITE)) {
            errors.add(SpecialCode.NOTHING_SPECIAL);
        }

        if (getAuthTokenUnprotected() == null) {
            errors.add(SpecialCode.AUTHENTICATION_NEEDED);
            // Stop tests here: the other tests need an AuthToken
            return errors;
        }

        if (getAuthTokenUnprotected().getMember().getRole() == Role.ADMIN) {
            // Stop here. The member is an admin. He must be able to kudos everything.
            return errors;
        }

        // I cannot kudos my own content.
        if (isOwnedByMe()) {
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

    @Override
    public boolean isOwnedByMe() {
        if(getAuthTokenUnprotected() == null) {
            return false;
        }
        return getAuthor().equals(getAuthTokenUnprotected().getMember());
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.KudosableInterface#getState()
     */
    @Override
    public final PopularityState getState() {
        return getDao().getState();
    }

    @Override
    protected final DaoUserContent getDaoUserContent() {
        return getDao();
    }

    private int vote(final int sign) throws UnauthorizedOperationException {
        EnumSet<SpecialCode> canKudos = canVote(sign);
        if (!canKudos.isEmpty()) {
            throw new UnauthorizedOperationException(canKudos.iterator().next());
        }

        // Make sure we are in the right position
        final Member member = getAuthToken().getMember();
        final int influence = member.calculateInfluence();

        if (influence > 0) {
            getAuthor().addToKarma(influence);
            calculateNewState(getDao().addKudos(member.getDao(), sign * influence));
        }

        return influence * sign;
    }

    private void calculateNewState(final int newPop) {
        switch (getState()) {
        case PENDING:
            if (newPop >= turnValid()) {
                setState(PopularityState.VALIDATED);
                notifyValid();
            } else if (newPop <= turnHidden() && newPop > turnRejected()) {
                setState(PopularityState.HIDDEN);
                notifyHidden();
            } else if (newPop <= turnRejected()) {
                setState(PopularityState.REJECTED);
                notifyRejected();
            }
            // NO BREAK IT IS OK !!
        case VALIDATED:
            if (newPop <= turnPending()) {
                setState(PopularityState.PENDING);
                notifyPending();
            }
            break;
        case HIDDEN:
        case REJECTED:
            if (newPop >= turnPending() && newPop < turnValid()) {
                setState(PopularityState.PENDING);
                notifyPending();
            } else if (newPop >= turnValid()) {
                setState(PopularityState.VALIDATED);
                notifyValid();
            } else if (newPop <= turnHidden() && newPop > turnRejected()) {
                setState(PopularityState.VALIDATED);
                notifyValid();
            } else if (newPop <= turnRejected()) {
                setState(PopularityState.REJECTED);
                notifyRejected();
            }
            break;
        default:
            assert false;
            break;
        }
    }

    private void setState(final PopularityState newState) {
        Log.model().info("Kudosable: " + getId() + " change from state: " + this.getState() + ", to: " + newState);
        getDao().setState(newState);
    }

    /**
     * You can redefine me if you want to customize the state calculation limits. Default
     * value is {@value #TURN_PENDING}.
     *
     * @return The popularity to for a Kudosable to reach to turn to {@link PopularityState#PENDING}
     *         state.
     */
    protected int turnPending() {
        return TURN_PENDING;
    }

    /**
     * You can redefine me if you want to customize the state calculation limits. Default
     * value is {@value #TURN_VALID}.
     *
     * @return The popularity to for a Kudosable to reach to turn to
     *         {@link PopularityState#VALIDATED} state.
     */
    protected int turnValid() {
        return TURN_VALID;
    }

    /**
     * You can redefine me if you want to customize the state calculation limits. Default
     * value is {@value #TURN_REJECTED}.
     *
     * @return The popularity to for a Kudosable to reach to turn to
     *         {@link PopularityState#REJECTED} state.
     */
    protected int turnRejected() {
        return TURN_REJECTED;
    }

    /**
     * You can redefine me if you want to customize the state calculation limits. Default
     * value is {@value #TURN_HIDDEN}.
     *
     * @return The popularity to for a Kudosable to reach to turn to {@link PopularityState#HIDDEN}
     *         state.
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
     * This method is called each time this Kudosable change its state to pending.
     */
    protected void notifyPending() {
        // Implement me if you wish
    }

    /**
     * This method is called each time this Kudosable change its state to rejected.
     */
    protected void notifyRejected() {
        // Implement me if you wish
    }

    /**
     * This method is called each time this Kudosable change its state to Hidden.
     */
    protected void notifyHidden() {
        // Implement me if you wish
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.KudosableInterface#getPopularity()
     */
    @Override
    public final int getPopularity() {
        return getDao().getPopularity();
    }

}
