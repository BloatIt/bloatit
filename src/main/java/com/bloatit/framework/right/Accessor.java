package com.bloatit.framework.right;

import java.util.EnumSet;

import com.bloatit.common.Log;
import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.framework.right.RightManager.Role;

/**
 * The class Accessor is class that test if a member as the right to access some feature.
 * 
 * It implements two methods : canAccess that return a boolean if you can access the
 * feature and tryAccess that throw an {@link UnauthorizedOperationException} if you
 * cannot.
 * 
 * This class implement the template method pattern so that child classes just have to
 * implement the can method.
 * 
 * You have to create an Accessor child class for each feature. For example you could
 * have:
 * 
 * <pre>
 * class MemberCanAccessFullname extends Accessor {
 *     &#064;Override
 *     protected boolean can(EnumSet&lt;Role&gt; role, Action action) {
 *         return role == OWNER || (action == READ);
 *     }
 * }
 * </pre>
 * 
 * There are some useful functions and code organization to manage the creation of
 * Accessor classes see {@link RightManager}.
 * 
 * @see RighManager
 */
public abstract class Accessor {

    protected abstract boolean can(EnumSet<Role> role, Action action);

    public boolean canAccess(EnumSet<Role> role, Action action) {
        if (role.contains(Role.ADMIN)) {
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
            Log.framework().error("UnauthorizedOperationException - " + role + " - " + action + " - " + getClass().getName());
            throw new UnauthorizedOperationException();
        }
    }
}
