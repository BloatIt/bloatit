package com.bloatit.model;

import java.util.EnumSet;

import com.bloatit.data.DaoKudosable;
import com.bloatit.data.DaoKudosable.State;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException.SpecialCode;

public interface KudosableInterface<T extends DaoKudosable> extends UserContentInterface<T> {

    EnumSet<SpecialCode> canKudos();

    EnumSet<SpecialCode> canUnkudos();

    void unkudos() throws UnauthorizedOperationException;

    void kudos() throws UnauthorizedOperationException;

    State getState();

    int getPopularity();

}