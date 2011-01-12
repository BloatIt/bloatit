package com.bloatit.framework.right;

import java.util.EnumSet;

public class MemberRight extends RightManager {

    public static class GroupList extends Accessor {
        @Override
        protected final boolean can(final EnumSet<Role> role, final Action action) {
            boolean can = false;
            can = can || canRead(action);
            can = can || ownerCanWrite(role, action);
            can = can || ownerCanDelete(role, action);
            can = can || modoCanWrite(role, action);
            can = can || modoCanDelete(role, action);
            return can;
        }
    }

    // Delete for accept/refuse
    // write to create a new
    public static class InviteInGroup extends Accessor {
        @Override
        protected final boolean can(final EnumSet<Role> role, final Action action) {
            boolean returnValue = false;
            returnValue = role.contains(Role.IN_GROUP) && (action == Action.WRITE || action == Action.READ);
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
