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

import com.bloatit.model.WithRights;

/**
 * The RightManager class contains some useful methods to create the Accessor
 * classes. The Role and Action are here to have an extended unix like right
 * system. Role represent the group / other / owner and Action is the like RWX.
 * You have to create an {@link Accessor} sub class to set the right of an
 * attribute. The Accessor sub classes are really small classes. To store them
 * correctly I use classes has namespace. For example the {@link MemberRight}
 * class is used to store every Accessor for the Member class.
 * 
 * @see Accessor
 */
public abstract class RightManager {

    protected RightManager() {
        // nothing
    }

    public enum OwningState {
        NOBODY, AUTHENTICATED, OWNER
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
    protected static boolean ownerCanRead(final WithRights role, final Action action) {
        return role.getOwningState() == OwningState.OWNER && Action.READ == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean ownerCanWrite(final WithRights role, final Action action) {
        return role.getOwningState() == OwningState.OWNER && Action.WRITE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean ownerCanDelete(final WithRights role, final Action action) {
        return role.getOwningState() == OwningState.OWNER && Action.DELETE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean authentifiedCanRead(final WithRights role, final Action action) {
        return role.getOwningState() != OwningState.NOBODY && Action.READ == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean authentifiedCanWrite(final WithRights role, final Action action) {
        return role.getOwningState() != OwningState.NOBODY && Action.WRITE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean authentifiedCanDelete(final WithRights role, final Action action) {
        return role.getOwningState() != OwningState.NOBODY && Action.DELETE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean otherCanRead(final WithRights role, final Action action) {
        return role.getOwningState() == OwningState.AUTHENTICATED && Action.READ == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean otherCanWrite(final WithRights role, final Action action) {
        return role.getOwningState() == OwningState.AUTHENTICATED && Action.WRITE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link Accessor#can(EnumSet, Action)} method
     */
    protected static boolean otherCanDelete(final WithRights role, final Action action) {
        return role.getOwningState() == OwningState.AUTHENTICATED && Action.DELETE == action;
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
        protected final boolean can(final WithRights role, final Action action) {
            return Action.READ == action;
        }
    }

    /**
     * Already overloaded Accessor. Use it when you have a r/w by owner
     * attribute.
     */
    protected static class Private extends Accessor {
        @Override
        protected final boolean can(final WithRights role, final Action action) {
            return ownerCanRead(role, action) || ownerCanWrite(role, action);
        }
    }

    /**
     * Already overloaded Accessor. Use it when you have a readable by all and
     * writable by owner attribute.
     */
    protected static class Public extends Accessor {
        @Override
        protected final boolean can(final WithRights role, final Action action) {
            return canRead(action) || ownerCanWrite(role, action);
        }
    }

    /**
     * Already overloaded Accessor. Use it when you have a readable by all.
     */
    protected static class PublicReadOnly extends Accessor {
        @Override
        protected final boolean can(final WithRights role, final Action action) {
            return canRead(action);
        }
    }

    /**
     * Already overloaded Accessor.
     */
    protected static class PrivateReadOnly extends Accessor {
        @Override
        protected final boolean can(final WithRights role, final Action action) {
            return ownerCanRead(role, action);
        }
    }

}
