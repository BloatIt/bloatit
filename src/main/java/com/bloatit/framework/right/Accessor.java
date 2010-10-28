package com.bloatit.framework.right;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.framework.right.RightManager.Role;

public abstract class Accessor {

    protected abstract boolean can(Role role, Action action);
    
    public boolean canAccess(Role role, Action action){
        if (role == Role.ADMIN){
            return true;
        }
        return can(role, action);
    }

    public boolean canAccess(Action action) {
        return canAccess(Role.OTHER, action);
    }

    public void tryAccess(Action action) {
        tryAccess(Role.OTHER, action);
    }

    public void tryAccess(Role role, Action action) {
        if (!canAccess(role, action)) {
            throw new UnauthorizedOperationException();
        }
    }
}
