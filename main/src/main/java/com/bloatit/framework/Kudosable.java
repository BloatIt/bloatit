package com.bloatit.framework;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.right.KudosableRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoKudosable;
import com.bloatit.model.data.DaoKudosable.State;
import com.bloatit.model.data.DaoUserContent;

public abstract class Kudosable extends UserContent {

    private static final int TURN_VALIDE = 100;
    private static final int TURN_REJECTED = -100;
    private static final int TURN_HIDDEN = -10;
    private static final int TURN_PENDING = 10;

    protected abstract DaoKudosable getDaoKudosable();

    public final boolean canKudos() {
        if (!new KudosableRight.Kudos().canAccess(calculateRole(this), Action.WRITE)) {
            return false;
        }
        return !getDaoKudosable().hasKudosed(getAuthToken().getMember().getDao());
    }

    public final void unkudos() throws UnauthorizedOperationException {
        if (!canKudos()) {
            throw new UnauthorizedOperationException();
        }
        addKudos(-1);
    }

    public final void kudos() throws UnauthorizedOperationException {
        if (!canKudos()) {
            throw new UnauthorizedOperationException();
        }
        addKudos(1);
    }

    public final State getState() {
        return getDaoKudosable().getState();
    }

    protected final void setState(final State newState) {
        getDaoKudosable().setState(newState);
    }

    @Override
    protected final DaoUserContent getDaoUserContent() {
        return getDaoKudosable();
    }

    private void addKudos(final int signe) throws UnauthorizedOperationException {
        final Member member = getAuthToken().getMember();
        if (getDaoKudosable().hasKudosed(member.getDao())) {
            throw new UnauthorizedOperationException();
        }
        final int influence = member.calculateInfluence();
        if (influence > 0) {
            getAuthor().addToKarma(influence);
            calculateNewState(getDaoKudosable().addKudos(member.getDao(), signe * influence));
        }
    }

    private void calculateNewState(final int newPop) {
        switch (getState()) {
        case PENDING:
            if (newPop >= TURN_VALIDE) {
                setState(State.VALIDATED);
            } else if (newPop <= TURN_HIDDEN && newPop > TURN_REJECTED) {
                setState(State.HIDDEN);
            } else if (newPop <= TURN_REJECTED) {
                setState(State.REJECTED);
            }
            // NO BREAK IT IS OK !!
        case VALIDATED:
            if (newPop <= TURN_PENDING) {
                setState(State.PENDING);
            }
            break;
        case HIDDEN:
        case REJECTED:
            if (newPop >= TURN_PENDING && newPop < TURN_VALIDE) {
                setState(State.PENDING);
            } else if (newPop >= TURN_VALIDE) {
                setState(State.VALIDATED);
            } else if (newPop <= TURN_HIDDEN && newPop > TURN_REJECTED) {
                setState(State.VALIDATED);
            } else if (newPop <= TURN_REJECTED) {
                setState(State.REJECTED);
            }
            break;
        default:
            assert false;
            break;
        }
    }

    public final int getPopularity() {
        return getDaoKudosable().getPopularity();
    }

}
