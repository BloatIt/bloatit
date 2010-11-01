package com.bloatit.framework.right;

import java.util.EnumSet;

public abstract class RightManager {

    // WARNING order is important
    public enum Role {
        GROUP, OTHER, OWNER, PRIVILEGED, REVIEWER, MODERATOR, ADMIN
    }

    public enum Action {
        READ, WRITE, DELETE
    }

    protected static boolean ownerCanRead(EnumSet<Role> role, Action action) {
        return role.contains(Role.OWNER) && Action.READ == action;
    }

    protected static boolean ownerCanWrite(EnumSet<Role> role, Action action) {
        return role.contains(Role.OWNER) && Action.WRITE == action;
    }

    protected static boolean ownerCanDelete(EnumSet<Role> role, Action action) {
        return role.contains(Role.OWNER) && Action.DELETE == action;
    }

    protected static boolean otherCanRead(EnumSet<Role> role, Action action) {
        return role.contains(Role.OTHER) && Action.READ == action;
    }

    protected static boolean otherCanWrite(EnumSet<Role> role, Action action) {
        return role.contains(Role.OTHER) && Action.WRITE == action;
    }

    protected static boolean otherCanDelete(EnumSet<Role> role, Action action) {
        return role.contains(Role.OTHER) && Action.DELETE == action;
    }

    protected static boolean modoCanRead(EnumSet<Role> role, Action action) {
        return role.contains(Role.MODERATOR) && Action.READ == action;
    }

    protected static boolean modoCanWrite(EnumSet<Role> role, Action action) {
        return role.contains(Role.MODERATOR) && Action.WRITE == action;
    }

    protected static boolean modoCanDelete(EnumSet<Role> role, Action action) {
        return role.contains(Role.MODERATOR) && Action.DELETE == action;
    }

    protected static boolean canRead(Action action) {
        return Action.READ == action;
    }

    protected static boolean canWrite(Action action) {
        return Action.WRITE == action;
    }

    protected static boolean canDelete(Action action) {
        return Action.DELETE == action;
    }

    public static class ReadOnly extends Accessor {
        @Override
        protected boolean can(EnumSet<Role> role, Action action) {
            return Action.READ == action;
        }
    }

    public static class Private extends Accessor {
        @Override
        protected boolean can(EnumSet<Role> role, Action action) {
            return ownerCanRead(role, action) || ownerCanWrite(role, action);
        }
    }

    public static class Public extends Accessor {
        @Override
        protected boolean can(EnumSet<Role> role, Action action) {
            return canRead(action) || ownerCanWrite(role, action);
        }
    }

    public static class PublicModerable extends Accessor {
        @Override
        protected boolean can(EnumSet<Role> role, Action action) {
            return canRead(action) || ownerCanWrite(role, action) || modoCanDelete(role, action) || modoCanWrite(role, action);
        }
    }

    public static class PrivateReadOnly extends Accessor {
        @Override
        protected boolean can(EnumSet<Role> role, Action action) {
            return ownerCanRead(role, action);
        }
    }

}
