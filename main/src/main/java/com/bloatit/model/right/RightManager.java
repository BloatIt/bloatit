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
import com.bloatit.model.Rights;

/**
 * <p>
 * The RightManager class contains some useful methods to create the
 * {@link GenericAccessor} classes.
 * </p>
 * 
 * @see GenericAccessor
 */
public abstract class RightManager {

    protected RightManager() {
        // nothing
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean ownerCanRead(final Rights role, final Action action) {
        return role.isOwner() && Action.READ == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean ownerCanWrite(final Rights role, final Action action) {
        return role.isOwner() && Action.WRITE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean ownerCanDelete(final Rights role, final Action action) {
        return role.isOwner() && Action.DELETE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean teamOwnerCanRead(final Rights role, final Action action) {
        return role.isTeamOwner() && Action.READ == action && role.hasConsultTeamRight();
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean teamOwnerCanWrite(final Rights role, final Action action) {
        return role.isTeamOwner() && Action.WRITE == action && role.hasModifyTeamRight();
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean teamOwnerCanDelete(final Rights role, final Action action) {
        return role.isTeamOwner() && Action.DELETE == action && role.hasModifyTeamRight();
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean authentifiedCanRead(final Rights role, final Action action) {
        return role.isAuthenticated() && Action.READ == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean authentifiedCanWrite(final Rights role, final Action action) {
        return role.isAuthenticated() && Action.WRITE == action;
    }
    
    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean activedCanWrite(final Rights role, final Action action) {
        return role.isAuthenticated() && Action.WRITE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean authentifiedCanDelete(final Rights role, final Action action) {
        return role.isAuthenticated() && Action.DELETE == action;
    }
    
    protected static boolean withKarmaToCommentCanWrite(Rights role, final Action action) {
        return role.hasKarmaToComment() && Action.WRITE == action;
    }


    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean canRead(final Action action) {
        return Action.READ == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean canWrite(final Action action) {
        return Action.WRITE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean canDelete(final Action action) {
        return Action.DELETE == action;
    }

    /**
     * Already overloaded Accessor. Use it when you have a r/w by owner
     * attribute.
     */
    public static class Private extends GenericAccessor<UnauthorizedPrivateAccessException> {
        @Override
        protected final boolean can(final Rights role, final Action action) {
            if (teamOwnerCanRead(role, action) || teamOwnerCanWrite(role, action)) {
                return true;
            }
            return ownerCanRead(role, action) || ownerCanWrite(role, action);
        }

        @Override
        protected UnauthorizedPrivateAccessException exception(final Action action) {
            return new UnauthorizedPrivateAccessException(action);
        }
    }

    public static class BankData extends GenericAccessor<UnauthorizedBankDataAccessException> {
        @Override
        protected final boolean can(final Rights role, final Action action) {
            if (teamOwnerCanRead(role, action) && role.hasBankTeamRight()) {
                return true;
            }
            if (teamOwnerCanWrite(role, action) && role.hasBankTeamRight()) {
                return true;
            }
            return ownerCanRead(role, action) || ownerCanWrite(role, action);
        }

        @Override
        protected UnauthorizedBankDataAccessException exception(final Action action) {
            return new UnauthorizedBankDataAccessException(action);
        }
    }

    public static class ReadOnlyBankData extends GenericAccessor<UnauthorizedReadOnlyBankDataAccessException> {
        @Override
        protected final boolean can(final Rights role, final Action action) {
            if (teamOwnerCanRead(role, action) && role.hasBankTeamRight()) {
                return true;
            }
            return ownerCanRead(role, action);
        }

        @Override
        protected UnauthorizedReadOnlyBankDataAccessException exception(final Action action) {
            return new UnauthorizedReadOnlyBankDataAccessException(action);
        }
    }

    /**
     * Already overloaded Accessor. Use it when you have a readable by all and
     * writable by owner attribute.
     */
    public static class Public extends GenericAccessor<UnauthorizedPublicAccessException> {
        @Override
        protected final boolean can(final Rights role, final Action action) {
            return canRead(action) || ownerCanWrite(role, action) || teamOwnerCanWrite(role, action);
        }

        @Override
        protected UnauthorizedPublicAccessException exception(final Action action) {
            return new UnauthorizedPublicAccessException(action);
        }

        @Override
        protected boolean authorizeWeakAccess(EnumSet<RightLevel> rights, final Action action) {
            return action == Action.READ;
        }
    }

    /**
     * Already overloaded Accessor. Use it when you have a readable by all.
     */
    public static class PublicReadOnly extends GenericAccessor<UnauthorizedPublicReadOnlyAccessException> {
        @Override
        protected final boolean can(final Rights role, final Action action) {
            return canRead(action);
        }

        @Override
        protected UnauthorizedPublicReadOnlyAccessException exception(final Action action) {
            return new UnauthorizedPublicReadOnlyAccessException(action);
        }

        @Override
        protected boolean authorizeWeakAccess(EnumSet<RightLevel> rights, final Action action) {
            return true;
        }
    }

    /**
     * Already overloaded Accessor.
     */
    public static class PrivateReadOnly extends GenericAccessor<UnauthorizedPrivateReadOnlyAccessException> {
        @Override
        protected final boolean can(final Rights role, final Action action) {
            return ownerCanRead(role, action) && teamOwnerCanRead(role, action);
        }

        @Override
        protected UnauthorizedPrivateReadOnlyAccessException exception(final Action action) {
            return new UnauthorizedPrivateReadOnlyAccessException(action);
        }

        @Override
        protected boolean authorizeWeakAccess(EnumSet<RightLevel> rights, final Action action) {
            return action == Action.READ;
        }
    }

    public static abstract class Accessor extends GenericAccessor<UnauthorizedOperationException> {

        @Override
        protected UnauthorizedOperationException exception(final Action action) {
            return new UnauthorizedOperationException(action);
        }
    }

    public static class AdminOnly extends Accessor {
        @Override
        protected boolean can(final Rights object, final Action action) {
            return false;
        }
    }

    public static class Authenticated extends Accessor {
        @Override
        protected boolean can(final Rights object, final Action action) {
            return object.isAuthenticated();
        }

        @Override
        protected boolean authorizeWeakAccess(EnumSet<RightLevel> rights, final Action action) {
            return action == Action.READ;
        }
    }
}
