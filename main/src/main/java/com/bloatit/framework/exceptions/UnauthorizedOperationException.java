package com.bloatit.framework.exceptions;

import java.util.EnumSet;

import com.bloatit.model.JoinGroupInvitation;
import com.bloatit.model.right.RightManager.Action;
import com.bloatit.model.right.RightManager.Role;

public class UnauthorizedOperationException extends Exception {
    private static final long serialVersionUID = -3668632178618592431L;

    public enum SpecialCode {
        /**
         * No special code. See the roles/action.
         */
        NOTHING_SPECIAL,

        /**
         * You try to (un)kudos a kudosable that has already been (un)kudosed by you.
         */
        ALREADY_KUDOSED,

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
        NON_DEVELOPER_FINISHED_DEMAND
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
