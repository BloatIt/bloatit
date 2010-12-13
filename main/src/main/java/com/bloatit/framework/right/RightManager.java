package com.bloatit.framework.right;

import java.util.EnumSet;

import com.bloatit.framework.Unlockable;

/**
 * The RightManager class contains some useful methods to create the Accessor
 * classes.
 * 
 * The Role and Action are here to have an extended unix like right system. Role
 * represent
 * the group / other / owner and Action is the like RWX.
 * 
 * You have to create an {@link Accessor} sub class to set the right of an
 * attribute. The
 * Accessor sub classes are really small classes. To store them correctly I use
 * classes has namespace.
 * For example the {@link MemberRight} class is used to store every Accessor for
 * the Member class.
 * 
 * @see Accessor
 * 
 */
public abstract class RightManager {

    /**
     * REMEMBER: The order is important: first the group related roles, then the
     * other
     * roles, from the less privileged to the more.
     * 
     * You can calculate the role of a user using the {@link Unlockable} object
     * (and calculateRole.)
     * 
     * For now some of the roles are not used.
     * 
     * @see Unlockable
     */
    public enum Role {
        GROUP_ADMIN, IN_GROUP, OTHER, OWNER, PRIVILEGED, REVIEWER, MODERATOR, ADMIN
    }

    /**
     * This is the action you want to do on an attribute.
     */
    public enum Action {
        READ, WRITE, DELETE
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean ownerCanRead(final EnumSet<Role> role, final Action action) {
        return role.contains(Role.OWNER) && Action.READ == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean ownerCanWrite(final EnumSet<Role> role, final Action action) {
        return role.contains(Role.OWNER) && Action.WRITE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean ownerCanDelete(final EnumSet<Role> role, final Action action) {
        return role.contains(Role.OWNER) && Action.DELETE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean otherCanRead(final EnumSet<Role> role, final Action action) {
        return role.contains(Role.OTHER) && Action.READ == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean otherCanWrite(final EnumSet<Role> role, final Action action) {
        return role.contains(Role.OTHER) && Action.WRITE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean otherCanDelete(final EnumSet<Role> role, final Action action) {
        return role.contains(Role.OTHER) && Action.DELETE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean modoCanRead(final EnumSet<Role> role, final Action action) {
        return role.contains(Role.MODERATOR) && Action.READ == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean modoCanWrite(final EnumSet<Role> role, final Action action) {
        return role.contains(Role.MODERATOR) && Action.WRITE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean modoCanDelete(final EnumSet<Role> role, final Action action) {
        return role.contains(Role.MODERATOR) && Action.DELETE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean canRead(final Action action) {
        return Action.READ == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean canWrite(final Action action) {
        return Action.WRITE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean canDelete(final Action action) {
        return Action.DELETE == action;
    }

    /**
     * Already overloaded Accessor. Use it when you have a readable by all
     * attribute.
     */
    protected static class ReadOnly extends Accessor {
        @Override
        protected boolean can(final EnumSet<Role> role, final Action action) {
            return Action.READ == action;
        }
    }

    /**
     * Already overloaded Accessor. Use it when you have a r/w by owner
     * attribute.
     */
    protected static class Private extends Accessor {
        @Override
        protected boolean can(final EnumSet<Role> role, final Action action) {
            return ownerCanRead(role, action) || ownerCanWrite(role, action);
        }
    }

    /**
     * Already overloaded Accessor. Use it when you have a readable by all and
     * writable by
     * owner attribute.
     */
    protected static class Public extends Accessor {
        @Override
        protected boolean can(final EnumSet<Role> role, final Action action) {
            return canRead(action) || ownerCanWrite(role, action);
        }
    }

    /**
     * Already overloaded Accessor. Use it when you have a readable by all and
     * writable by
     * owner attribute
     */
    protected static class PublicModerable extends Accessor {
        @Override
        protected boolean can(final EnumSet<Role> role, final Action action) {
            return canRead(action) || ownerCanWrite(role, action) || modoCanDelete(role, action) || modoCanWrite(role, action);
        }
    }

    /**
     * Already overloaded Accessor.
     */
    protected static class PrivateReadOnly extends Accessor {
        @Override
        protected boolean can(final EnumSet<Role> role, final Action action) {
            return ownerCanRead(role, action);
        }
    }

}
