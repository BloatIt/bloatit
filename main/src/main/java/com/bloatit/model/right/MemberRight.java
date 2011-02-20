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

public class MemberRight extends RightManager {

    public static class GroupList extends Accessor {
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            boolean can = false;
            can = can || canRead(action);
            can = can || role.hasGroupPrivilege(UserGroupRight.PROMOTE) && ownerCanWrite(role, action);
            can = can || role.hasGroupPrivilege(UserGroupRight.PROMOTE) && ownerCanDelete(role, action);
            return can;
        }
    }

    // Delete for accept/refuse
    // write to create a new
    public static class SendInvitation extends Accessor {
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            boolean returnValue = false;
            returnValue = role.hasGroupPrivilege(UserGroupRight.INVITE) && (action == Action.WRITE);
            returnValue = returnValue || ownerCanRead(role, action) || ownerCanDelete(role, action);
            return returnValue;
        }
    }

    public static class Karma extends Public {
        // nothing this is just a rename.
    }

    public static class Password extends Private {
        // nothing this is just a rename.
    }

    public static class Locale extends Private {
        // nothing this is just a rename.
    }

    public static class Name extends Public {
        // nothing this is just a rename.
    }
}
