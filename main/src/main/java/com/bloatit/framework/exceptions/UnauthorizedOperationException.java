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
package com.bloatit.framework.exceptions;

import com.bloatit.model.JoinGroupInvitation;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.RestrictedObject;

/**
 * This exception is thrown when you try to access a property without the having right.
 * <p>
 * An {@link UnauthorizedOperationException} should give you some informations about why
 * you failed to access a property. To do so, tree different informations can be
 * available:
 * <li>The role in which the user was when trying to access the property.</li>
 * <li>The action the user try to do.</li>
 * <li>A special code for every possible special access error.</li>
 * </p>
 */
public class UnauthorizedOperationException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3668632178618592431L;

    /**
     * A specialCode is the return code describing the.
     * 
     * {@link UnauthorizedOperationException}.
     */
    public enum SpecialCode {
        /**
         * You try to access a method that require authentication without authenticating
         * the object.
         * 
         * @see RestrictedObject#authenticate(com.bloatit.model.AuthToken)
         */
        AUTHENTICATION_NEEDED,

        /**
         * No special code. See the roles/action.
         */
        NOTHING_SPECIAL,

        /**
         * You try to (un)kudos a kudosable that has already been (un)kudosed by you.
         */
        ALREADY_VOTED,

        /**
         * You try to (un)kudos a locked kudosable.
         */
        KUDOSABLE_LOCKED,

        /** Influence too low to unkudos a kudosable. */
        INFLUENCE_LOW_ON_VOTE_DOWN,

        /** Influence too low to kudos a kudosable. */
        INFLUENCE_LOW_ON_VOTE_UP,

        /**
         * You try to add somebody in a group, but this group is not public. You have to
         * use {@link JoinGroupInvitation} object to join a non public group.
         */
        GROUP_NOT_PUBLIC,

        /**
         * You try to accept/refuse an invitation, but you are not the receiver.
         */
        INVITATION_RECIEVER_MISMATCH,

        /**
         * You try to delete a demand, but you are not the current developer.
         */
        NON_DEVELOPER_CANCEL_DEMAND,

        /**
         * You try to finish a demand, but you are not the current developer.
         */
        NON_DEVELOPER_FINISHED_DEMAND,

        /**
         * You create an object and insert it with different person. For example Tom
         * create an Offer and Yo insert it in the demand.
         */
        CREATOR_INSERTOR_MISMATCH,

        /** You try to vote but you are the author. */
        OWNED_BY_ME,

        /**
         * You tried to do an action reserved for the admin.
         */
        ADMIN_ONLY

    }

    /** The action. */
    private final Action action;

    /** The code. */
    private final SpecialCode code;

    /**
     * Instantiates a new unauthorized operation exception.
     * 
     * @param action the action when trying to access a property.
     * @param code the code describing more precisely what went wrong.
     */
    public UnauthorizedOperationException(final Action action, final SpecialCode code) {
        super();
        this.action = action;
        this.code = code;
    }

    /**
     * Instantiates a new unauthorized operation exception.
     * 
     * @param action the action when trying to access a property.
     */
    public UnauthorizedOperationException(final Action action) {
        this(action, SpecialCode.NOTHING_SPECIAL);
    }

    /**
     * Instantiates a new unauthorized operation exception.
     * 
     * @param code the code describing more precisely what went wrong.
     */
    public UnauthorizedOperationException(final SpecialCode code) {
        super();
        this.action = null;
        this.code = code;
    }

    /**
     * Gets the action.
     * 
     * @return the action that has been forbidden.
     */
    public final Action getAction() {
        return action;
    }

    /**
     * Gets the code.
     * 
     * @return the code describing what went wrong.
     */
    public final SpecialCode getCode() {
        return code;
    }

}
