package com.bloatit.framework.exceptions;

import java.util.EnumSet;

import com.bloatit.model.JoinGroupInvitation;
import com.bloatit.model.Unlockable;
import com.bloatit.model.right.RightManager.Action;
import com.bloatit.model.right.RightManager.Role;

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
    private static final long serialVersionUID = -3668632178618592431L;

    public enum SpecialCode {
        /**
         * You try to access a method that require authentication without authenticating
         * the object.
         *
         * @see Unlockable#authenticate(com.bloatit.model.AuthToken)
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

        /**
         * Influence too low to unkudos a kudosable
         */
        INFLUENCE_LOW_ON_VOTE_DOWN,

        /**
         * Influence too low to kudos a kudosable
         */
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

        /**
         * You try to vote but you are the author
         */
        OWNED_BY_ME,
        
        /**
         * You tried to do an action reserved for the admin.
         */
        ADMIN_ONLY

    }

    private final EnumSet<Role> roles;
    private final Action action;
    private final SpecialCode code;

    public UnauthorizedOperationException(final EnumSet<Role> roles, final Action action, final SpecialCode code) {
        super();
        this.roles = roles;
        this.action = action;
        this.code = code;
    }

    public UnauthorizedOperationException(final EnumSet<Role> roles, final Action action) {
        this(roles, action, SpecialCode.NOTHING_SPECIAL);
    }

    public UnauthorizedOperationException(final SpecialCode code) {
        super();
        this.roles = null;
        this.action = null;
        this.code = code;
    }

    public final EnumSet<Role> getRoles() {
        return roles;
    }

    public final Action getAction() {
        return action;
    }

    public final SpecialCode getCode() {
        return code;
    }

}
