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

import java.util.EnumSet;

import com.bloatit.data.DaoExternalServiceMembership.RightLevel;
import com.bloatit.model.Kudosable;
import com.bloatit.model.Rights;

/**
 * The Class KudosableRight store the properties accessor for the
 * {@link Kudosable} class.
 */
public class RgtKudosable extends RightManager {

    /**
     * The Class Kudos is a accessor for the Kudos property.
     */
    public static class Kudos extends Accessor {

        /*
         * (non-Javadoc)
         * @see com.bloatit.model.right.Accessor#can(com.bloatit.model.right.
         * RestrictedInterface , com.bloatit.model.right.Action)
         */
        @Override
        protected final boolean can(final Rights role, final Action action) {
            return authentifiedCanWrite(role, action);
        }
        
        @Override
        protected boolean authorizeWeakAccess(EnumSet<RightLevel> rights, final Action action) {
            return rights.contains(RightLevel.KUDOS);
        }
    }
}
