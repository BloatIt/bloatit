package com.bloatit.model.right;

import java.util.EnumSet;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.right.RightManager.Action;
import com.bloatit.model.right.RightManager.Role;

/**
 * The class Accessor is class that test if a member as the right to access some feature.
 * It implements two methods : canAccess that return a boolean if you can access the
 * feature and tryAccess that throw an {@link UnauthorizedOperationException} if you
 * cannot. This class implement the template method pattern so that child classes just
 * have to implement the can method. You have to create an Accessor child class for each
 * feature. For example you could have:
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

    public final boolean canAccess(final EnumSet<Role> role, final Action action) {
        if (role.contains(Role.ADMIN)) {
            Log.model().trace("Admin access");
            return true;
        }
        return can(role, action);
    }

    public final void tryAccess(final EnumSet<Role> role, final Action action) throws UnauthorizedOperationException {
        if (!canAccess(role, action)) {
            Log.model().error("UnauthorizedOperationException - " + role + " - " + action + " - " + getClass().getName());
            throw new UnauthorizedOperationException(role, action);
        }
    }
}
