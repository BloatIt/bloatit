package com.bloatit.framework.right;

import java.util.EnumSet;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.framework.right.RightManager.Role;

public abstract class Accessor {

    protected abstract boolean can(EnumSet<Role> role, Action action);
    
    public boolean canAccess(EnumSet<Role> role, Action action){
        if (role.contains(Role.ADMIN)){
            return true;
        }
        return can(role, action);
    }

    public boolean canAccess(Action action) {
        return canAccess(EnumSet.of(Role.ADMIN), action);
    }

    public void tryAccess(Action action) {
        tryAccess(EnumSet.of(Role.ADMIN), action);
    }

    public void tryAccess(EnumSet<Role> role, Action action) {
        if (!canAccess(role, action)) {
            throw new UnauthorizedOperationException();
        }
    }
}
