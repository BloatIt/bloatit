package com.bloatit.framework;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.model.data.DaoKudosable;
import com.bloatit.model.data.DaoKudosable.State;
import com.bloatit.model.data.DaoUserContent;

public abstract class Kudosable extends UserContent {

    protected abstract DaoKudosable getDaoKudosable();

    public boolean canKudos(Member member) {
        return !getDaoKudosable().hasKudosed(member.getDao());
    }

    public void unkudos(Member member) {
        if (getDaoKudosable().hasKudosed(member.getDao())) {
            throw new UnauthorizedOperationException();
        }
        int influence = member.calculateInfluence();
        if (influence > 0) {
            getAuthor().addToKarma(-influence);
            getDaoKudosable().addKudos(member.getDao(), -influence);
        }
    }

    public void kudos(Member member) {
        if (getDaoKudosable().hasKudosed(member.getDao())) {
            throw new UnauthorizedOperationException();
        }
        int influence = member.calculateInfluence();
        if (influence > 0) {
            getAuthor().addToKarma(influence);
            getDaoKudosable().addKudos(member.getDao(), influence);
        }
    }

    public State getState() {
        return getDaoKudosable().getState();
    }

    protected void setValidated() {
        getDaoKudosable().setValidated();
    }

    protected void setRejected() {
        getDaoKudosable().setRejected();
    }

    @Override
    protected final DaoUserContent getDaoUserContent() {
        return getDaoKudosable();
    }

}
