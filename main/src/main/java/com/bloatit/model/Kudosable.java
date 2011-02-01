package com.bloatit.model;

import com.bloatit.data.DaoKudosable;
import com.bloatit.data.DaoKudosable.State;
import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException.SpecialCode;
import com.bloatit.model.right.KudosableRight;
import com.bloatit.model.right.RightManager.Action;

public abstract class Kudosable<T extends DaoKudosable> extends UserContent<T> {

    private static final int TURN_VALID = 100;
    private static final int TURN_REJECTED = -100;
    private static final int TURN_HIDDEN = -10;
    private static final int TURN_PENDING = 10;

    public Kudosable(final T dao) {
        super(dao);
    }

    protected abstract DaoKudosable getDaoKudosable();

    public final boolean canKudos() {
        if (!new KudosableRight.Kudos().canAccess(calculateRole(this), Action.WRITE)) {
            return false;
        }
        return !getDaoKudosable().hasKudosed(getAuthToken().getMember().getDao());
    }

    private void tryKudos() throws UnauthorizedOperationException {
        if (!canKudos()) {
            throw new UnauthorizedOperationException(calculateRole(this), Action.WRITE, SpecialCode.ALREADY_KUDOSED);
        }
    }

    public final void unkudos() throws UnauthorizedOperationException {
        tryKudos();
        addKudos(-1);
        notifyKudos(false);
    }

    public final void kudos() throws UnauthorizedOperationException {
        tryKudos();
        addKudos(1);
        notifyKudos(true);
    }

    public final State getState() {
        return getDaoKudosable().getState();
    }

    @Override
    protected final DaoUserContent getDaoUserContent() {
        return getDaoKudosable();
    }

    private void addKudos(final int signe) throws UnauthorizedOperationException {
        final Member member = getAuthToken().getMember();
        final int influence = member.calculateInfluence();
        if (influence > 0) {
            getAuthor().addToKarma(influence);
            calculateNewState(getDaoKudosable().addKudos(member.getDao(), signe * influence));
        }
    }

    private void calculateNewState(final int newPop) {
        switch (getState()) {
        case PENDING:
            if (newPop >= turnValid()) {
                setState(State.VALIDATED);
                notifyValid();
            } else if (newPop <= turnHidden() && newPop > turnRejected()) {
                setState(State.HIDDEN);
                notifyHidden();
            } else if (newPop <= turnRejected()) {
                setState(State.REJECTED);
                notifyRejected();
            }
            // NO BREAK IT IS OK !!
        case VALIDATED:
            if (newPop <= TURN_PENDING) {
                setState(State.PENDING);
                notifyPending();
            }
            break;
        case HIDDEN:
        case REJECTED:
            if (newPop >= turnPending() && newPop < turnValid()) {
                setState(State.PENDING);
                notifyPending();
            } else if (newPop >= turnValid()) {
                setState(State.VALIDATED);
                notifyValid();
            } else if (newPop <= turnHidden() && newPop > turnRejected()) {
                setState(State.VALIDATED);
                notifyValid();
            } else if (newPop <= turnRejected()) {
                setState(State.REJECTED);
                notifyRejected();
            }
            break;
        default:
            assert false;
            break;
        }
    }

    private void setState(final State newState) {
        getDaoKudosable().setState(newState);
    }

    protected int turnPending() {
        return TURN_PENDING;
    }

    protected int turnValid() {
        return TURN_VALID;
    }

    protected int turnRejected() {
        return TURN_REJECTED;
    }

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

    public final int getPopularity() {
        return getDaoKudosable().getPopularity();
    }

}
