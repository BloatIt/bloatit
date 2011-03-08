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

import com.bloatit.data.DaoGroupRight.UserGroupRight;
import com.bloatit.model.Member;

/**
 * The Class MemberRight store the properties accessor for the {@link Member}
 * class.
 */
public class MemberRight extends RightManager {

    /**
     * The Class GroupList is an accessor for the GroupList property.
     */
    public static class GroupList extends Accessor {

        /*
         * (non-Javadoc)
         * @see com.bloatit.model.right.Accessor#can(com.bloatit.model.right.
         * RestrictedInterface , com.bloatit.model.right.Action)
         */
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            boolean can = false;
            can = can || canRead(action);
            can = can || role.hasGroupPrivilege(UserGroupRight.PROMOTE) && ownerCanWrite(role, action);
            can = can || ownerCanDelete(role, action);
            return can;
        }
    }

    // Delete for accept/refuse
    // write to create a new
    /**
     * The Class SendInvitation is an accessor for the SendInvitation property.
     */
    public static class SendInvitation extends Accessor {

        /*
         * (non-Javadoc)
         * @see com.bloatit.model.right.Accessor#can(com.bloatit.model.right.
         * RestrictedInterface , com.bloatit.model.right.Action)
         */
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            boolean returnValue = false;
            returnValue = role.hasGroupPrivilege(UserGroupRight.INVITE) && (action == Action.WRITE);
            returnValue = returnValue || ownerCanRead(role, action) || ownerCanDelete(role, action);
            return returnValue;
        }
    }

    /**
     * The Class Karma is a {@link Public} accessor for the Karma property.
     */
    public static class Karma extends Public {
        // nothing this is just a rename.
    }

    /**
     * The Class Password is a {@link Private} accessor for the Password
     * property.
     */
    public static class Password extends Private {
        // nothing this is just a rename.
    }

    /**
     * The Class Locale is a {@link Private} accessor for the Locale property.
     */
    public static class Locale extends Private {
        // nothing this is just a rename.
    }

    /**
     * The Class Name is a {@link Public} accessor for the Name property.
     */
    public static class Name extends Public {
        // nothing this is just a rename.
    }
}
