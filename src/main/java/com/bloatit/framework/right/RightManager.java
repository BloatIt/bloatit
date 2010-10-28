package com.bloatit.framework.right;

public abstract class RightManager {

    public enum Role {
        OWNER, GROUP, OTHER, ADMIN
    }

    public enum Action {
        READ, WRITE, DELETE
    }

    protected static boolean ownerCanRead(Role role, Action action) {
        return role == Role.OWNER && Action.READ == action;
    }

    protected static boolean ownerCanWrite(Role role, Action action) {
        return role == Role.OWNER && Action.WRITE == action;
    }

    protected static boolean ownerCanDelete(Role role, Action action) {
        return role == Role.OWNER && Action.DELETE == action;
    }

    protected static boolean otherCanRead(Role role, Action action) {
        return role == Role.OTHER && Action.READ == action;
    }

    protected static boolean otherCanWrite(Role role, Action action) {
        return role == Role.OTHER && Action.WRITE == action;
    }

    protected static boolean otherCanDelete(Role role, Action action) {
        return role == Role.OTHER && Action.DELETE == action;
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
        protected boolean can(Role role, Action action) {
            return Action.READ == action;
        }
    }

    public static class Private extends Accessor {
        @Override
        protected boolean can(Role role, Action action) {
            return ownerCanRead(role, action) || ownerCanWrite(role, action);
        }
    }
    
    public static class Public extends Accessor {
        @Override
        protected boolean can(Role role, Action action) {
            return canRead(action) || ownerCanWrite(role, action);
        }
    }

    public static class PrivateReadOnly extends Accessor {
        @Override
        protected boolean can(Role role, Action action) {
            return ownerCanRead(role, action);
        }
    }

}
