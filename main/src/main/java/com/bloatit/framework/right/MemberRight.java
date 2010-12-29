package com.bloatit.framework.right;

import java.util.EnumSet;

public class MemberRight extends RightManager {

    public static class GroupList extends Accessor {
        @Override
        protected final boolean can(final EnumSet<Role> role, final Action action) {
            return canRead(action) || ownerCanWrite(role, action) || ownerCanDelete(role, action) || modoCanWrite(role, action)
                    || modoCanDelete(role, action);
        }
    }

    // Read for accept/refuse
    // write to create a new
    public static class InviteInGroup extends Accessor {
        @Override
        protected final boolean can(final EnumSet<Role> role, final Action action) {
            return role.contains(Role.IN_GROUP) && action == Action.WRITE || ownerCanRead(role, action);
        }
    }

    public static class Karma extends Public {
    }

    public static class Password extends Private {
    }

    public static class Name extends Public {
    }

}
