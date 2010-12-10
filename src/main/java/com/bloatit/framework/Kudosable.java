package com.bloatit.framework;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.model.data.DaoKudosable;
import com.bloatit.model.data.DaoKudosable.State;
import com.bloatit.model.data.DaoUserContent;

public abstract class Kudosable extends UserContent {

    private static final int TURN_VALIDE = 100;
    private static final int TURN_REJECTED = -100;
    private static final int TURN_HIDDEN = -10;
    private static final int TURN_PENDING = 10;

    protected abstract DaoKudosable getDaoKudosable();

    public boolean canKudos() {
        return !getDaoKudosable().hasKudosed(getToken().getMember().getDao());
    }

    public void unkudos() {
        addKudos(-1);
    }

    public void kudos() {
        addKudos(1);
    }

    public State getState() {
        return getDaoKudosable().getState();
    }

    protected void setState(final State newState) {
        getDaoKudosable().setState(newState);
    }

    @Override
    protected final DaoUserContent getDaoUserContent() {
        return getDaoKudosable();
    }

    private void addKudos(final int signe) {
        final Member member = getToken().getMember();
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
        }
    }

    public int getPopularity() {
        return getDaoKudosable().getPopularity();
    }
}
