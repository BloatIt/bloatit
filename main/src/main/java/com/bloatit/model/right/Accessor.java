//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.model.right;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.WithRights;
import com.bloatit.model.right.RightManager.Action;
import com.bloatit.model.right.RightManager.OwningState;

/**
 * The class Accessor is class that test if a member as the right to access some
 * feature. It implements two methods : canAccess that return a boolean if you
 * can access the feature and tryAccess that throw an
 * {@link UnauthorizedOperationException} if you cannot. This class implement
 * the template method pattern so that child classes just have to implement the
 * can method. You have to create an Accessor child class for each feature. For
 * example you could have:
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
 * There are some useful functions and code organization to manage the creation
 * of Accessor classes see {@link RightManager}.
 * 
 * @see RighManager
 */
public abstract class Accessor {

    protected abstract boolean can(WithRights rights, Action action);

    public final boolean canAccess(final WithRights rights, final Action action) {
        if (rights.getOwningState() != OwningState.NOBODY && rights.getRole() == com.bloatit.data.DaoMember.Role.ADMIN) {
            Log.model().trace("Admin access");
            return true;
        }
        return can(rights, action);
    }

    public final void tryAccess(final WithRights rights, final Action action) throws UnauthorizedOperationException {
        if (!canAccess(rights, action)) {
            throw new UnauthorizedOperationException(rights, action);
        }
    }
}
