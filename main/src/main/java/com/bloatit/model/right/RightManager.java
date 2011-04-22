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

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPrivateAccessException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPrivateReadOnlyAccessException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPublicAccessException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPublicReadOnlyAccessException;
import com.bloatit.model.Team;

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
    protected static boolean ownerCanRead(final RestrictedInterface role, final Action action) {
        return Action.READ == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean ownerCanWrite(final RestrictedInterface role, final Action action) {
        return Action.WRITE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean ownerCanDelete(final RestrictedInterface role, final Action action) {
        return Action.DELETE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean authentifiedCanRead(final RestrictedInterface role, final Action action) {
        return role.isAuthenticated() && Action.READ == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean authentifiedCanWrite(final RestrictedInterface role, final Action action) {
        return role.isAuthenticated() && Action.WRITE == action;
    }

    /**
     * Helper function, use it in the overloading of the
     * {@link GenericAccessor#can(EnumSet, Action)} method
     */
    protected static boolean authentifiedCanDelete(final RestrictedInterface role, final Action action) {
        return role.isAuthenticated() && Action.DELETE == action;
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
        protected final boolean can(final RestrictedInterface role, final Action action) {
            if (role.getAuthenticatedMember().hasConsultTeamRight(role.getAsTeam())) {
                return canRead(action) || canWrite(action);
            }
            return ownerCanRead(role, action) || ownerCanWrite(role, action);
        }

        @Override
        protected UnauthorizedPrivateAccessException exception(Action action) {
            return new UnauthorizedPrivateAccessException(action);
        }
    }

    // TODO change the UnauthorizedPrivateAccessException
    public static class BankData extends GenericAccessor<UnauthorizedPrivateAccessException> {
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            if (role.getAuthenticatedMember().hasBankTeamRight(role.getAsTeam())) {
                return canRead(action) || canWrite(action);
            }
            return ownerCanRead(role, action) || ownerCanWrite(role, action);
        }

        @Override
        protected UnauthorizedPrivateAccessException exception(Action action) {
            return new UnauthorizedPrivateAccessException(action);
        }
    }

    // TODO change the UnauthorizedPrivateAccessException
    public static class ReadOnlyBankData extends GenericAccessor<UnauthorizedPrivateAccessException> {
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            if (role.getAuthenticatedMember().hasBankTeamRight(role.getAsTeam())) {
                return canRead(action);
            }
            return ownerCanRead(role, action);
        }

        @Override
        protected UnauthorizedPrivateAccessException exception(Action action) {
            return new UnauthorizedPrivateAccessException(action);
        }
    }

    /**
     * Already overloaded Accessor. Use it when you have a readable by all and
     * writable by owner attribute.
     */
    public static class Public extends GenericAccessor<UnauthorizedPublicAccessException> {
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            return canRead(action) || ownerCanWrite(role, action);
        }

        @Override
        protected UnauthorizedPublicAccessException exception(Action action) {
            return new UnauthorizedPublicAccessException(action);
        }
    }

    /**
     * Already overloaded Accessor. Use it when you have a readable by all.
     */
    public static class PublicReadOnly extends GenericAccessor<UnauthorizedPublicReadOnlyAccessException> {
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            return canRead(action);
        }

        @Override
        protected UnauthorizedPublicReadOnlyAccessException exception(Action action) {
            return new UnauthorizedPublicReadOnlyAccessException(action);
        }
    }

    /**
     * Already overloaded Accessor.
     */
    public static class PrivateReadOnly extends GenericAccessor<UnauthorizedPrivateReadOnlyAccessException> {
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            return ownerCanRead(role, action);

        }

        @Override
        protected UnauthorizedPrivateReadOnlyAccessException exception(Action action) {
            return new UnauthorizedPrivateReadOnlyAccessException(action);
        }
    }

    public static abstract class Accessor extends GenericAccessor<UnauthorizedOperationException> {

        @Override
        protected UnauthorizedOperationException exception(Action action) {
            return new UnauthorizedOperationException(action);
        }
    }

}
