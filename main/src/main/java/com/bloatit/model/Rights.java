package com.bloatit.model;

import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.RestrictedObject;
import com.bloatit.model.visitor.HighLevelModelVisitor;

public class Rights {

    /**
     * The Enum OwningState is the state of a user regarding to this
     * {@link RestrictedObject}.
     */
    private enum OwningState {

        /** NOBODY means the user is not authenticated. */
        NOBODY,
        /**
         * AUTHENTICATED means the user is authenticated but he is not the
         * author of this content
         */
        AUTHENTICATED,
        /**
         * OWNER means the user is authenticated and is the author of this
         * content
         */
        OWNER,

        /**
         * TEAM_OWNER means the user is authenticated and is in the team that
         * has created the content.
         */
        TEAM_OWNER,
    }

    private final OwningState owningState;
    private final AuthToken token;

    public Rights(final AuthToken token, final IdentifiableInterface identifiable) {
        this.token = token;
        if (token.isAnonymous()) {
            owningState = OwningState.NOBODY;
        } else {
            if (token.getAsTeam() != null) {
                if (identifiable.accept(new IsTeamOwnerVisitor(token.getMember()))) {
                    owningState = OwningState.TEAM_OWNER;
                } else {
                    owningState = OwningState.AUTHENTICATED;
                }
            } else {
                if (identifiable.accept(new IsOwnerVisitor(token.getMember()))) {
                    owningState = OwningState.OWNER;
                } else {
                    owningState = OwningState.AUTHENTICATED;
                }
            }
        }
    }

    // ///////////////////////////////////////
    // owning state

    public final boolean isAuthenticated() {
        return owningState != OwningState.NOBODY;
    }

    public final boolean isOwner() {
        return owningState == OwningState.OWNER;
    }

    public final boolean isNobody() {
        return owningState == OwningState.NOBODY;
    }

    public final boolean isTeamOwner() {
        return owningState == OwningState.TEAM_OWNER;
    }

    // ///////////////////////////////////////
    // Team Rights

    public boolean hasModifyTeamRight() {
        return hasModifyTeamRight(token.getMember(), token.getAsTeam());
    }

    public boolean hasConsultTeamRight() {
        if (isTeamOwner()) {
            return hasConsultTeamRight(token.getMember(), token.getAsTeam());
        }
        return false;
    }

    public boolean hasBankTeamRight() {
        if (isTeamOwner()) {
            return hasBankTeamRight(token.getMember(), token.getAsTeam());
        }
        return false;
    }

    public boolean hasConsultTeamRight(final Member member, final Team aTeam) {
        return hasTeamRight(member, aTeam, UserTeamRight.CONSULT);
    }

    public boolean hasTalkTeamRight(final Member member, final Team aTeam) {
        return hasTeamRight(member, aTeam, UserTeamRight.TALK);
    }

    public boolean hasInviteTeamRight(final Member member, final Team aTeam) {
        return hasTeamRight(member, aTeam, UserTeamRight.INVITE);
    }

    public boolean hasModifyTeamRight(final Member member, final Team aTeam) {
        return hasTeamRight(member, aTeam, UserTeamRight.MODIFY);
    }

    public boolean hasPromoteTeamRight(final Member member, final Team aTeam) {
        return hasTeamRight(member, aTeam, UserTeamRight.PROMOTE);
    }

    public boolean hasBankTeamRight(final Member member, final Team aTeam) {
        return hasTeamRight(member, aTeam, UserTeamRight.BANK);
    }

    private boolean hasTeamRight(final Member member, final Team team, final UserTeamRight aRight) {
        if (team == null) {
            return false;
        }
        if (team.getUserTeamRight(member) == null) {
            return false;
        }
        return member.getTeamRights(team).contains(aRight);
    }

    // ///////////////////////////////////////
    // User privilege

    public final boolean hasNormalUserPrivilege() {
        return hasUserPrivilege(Role.NORMAL) || hasPrivilegedUserPrivilege();
    }

    public final boolean hasPrivilegedUserPrivilege() {
        return hasUserPrivilege(Role.PRIVILEGED) || hasReviewerUserPrivilege();
    }

    public final boolean hasReviewerUserPrivilege() {
        return hasUserPrivilege(Role.REVIEWER) || hasModeratorUserPrivilege();
    }

    public final boolean hasModeratorUserPrivilege() {
        return hasUserPrivilege(Role.MODERATOR) || hasAdminUserPrivilege();
    }

    public final boolean hasAdminUserPrivilege() {
        return hasUserPrivilege(Role.ADMIN);
    }

    private final boolean hasUserPrivilege(final DaoMember.Role role) {
        return token.getMember().getRole() == role;
    }

    // ///////////////////////////////////////
    // Visitors

    private class IsTeamOwnerVisitor extends HighLevelModelVisitor<Boolean> {
        private final Member member;

        public IsTeamOwnerVisitor(final Member member) {
            this.member = member;
        }

        @Override
        public Boolean visitAbstract(final Account<?> model) {
            return visitAbstract(model.getActorUnprotected());
        }

        @Override
        public Boolean visitAbstract(final Actor<?> model) {
            if (model instanceof Team) {
                return member.isInTeam((Team) model);
            }
            return false;
        }

        @Override
        public Boolean visitAbstract(final UserContentInterface model) {
            return member.isInTeam(model.getAsTeam());
        }

        @Override
        public Boolean visitAbstract(final BankTransaction model) {
            return visitAbstract(model.getAuthorUnprotected());
        }

        @Override
        public Boolean visitAbstract(final Milestone model) {
            return visitAbstract(model.getOffer());
        }

        @Override
        public Boolean visitAbstract(final Description model) {
            return visitAbstract(model.getDefaultTranslation());
        }

        @Override
        public Boolean visitAbstract(final HighlightFeature model) {
            return false;
        }

        @Override
        public Boolean visitAbstract(final JoinTeamInvitation model) {
            return visitAbstract(model.getTeamUnprotected());
        }

        @Override
        public Boolean visitAbstract(final Software model) {
            return null;
        }

        @Override
        public Boolean visitAbstract(final Transaction model) {
            return visitAbstract(model.getToUnprotected()) || visitAbstract(model.getFromUnprotected());
        }
    }

    private class IsOwnerVisitor extends HighLevelModelVisitor<Boolean> {
        private final Member member;

        public IsOwnerVisitor(final Member member) {
            this.member = member;
        }

        @Override
        public Boolean visitAbstract(final Account<?> model) {
            return model.getActorUnprotected().equals(member);
        }

        @Override
        public Boolean visitAbstract(final Actor<?> model) {
            return model.equals(member);
        }

        @Override
        public Boolean visitAbstract(final UserContentInterface model) {
            return model.getAuthor().equals(member);
        }

        @Override
        public Boolean visitAbstract(final BankTransaction model) {
            if (model.getAuthorUnprotected().equals(member)) {
                return true;
            }
            return false;
        }

        @Override
        public Boolean visitAbstract(final Milestone model) {
            return visitAbstract(model.getOffer());
        }

        @Override
        public Boolean visitAbstract(final Description model) {
            return visitAbstract(model.getDefaultTranslation());
        }

        @Override
        public Boolean visitAbstract(final HighlightFeature model) {
            return false;
        }

        @Override
        public Boolean visitAbstract(final JoinTeamInvitation model) {
            return model.getSenderUnprotected().equals(member) || model.getReceiverUnprotected().equals(member);
        }

        @Override
        public Boolean visitAbstract(final Software model) {
            return false;
        }

        @Override
        public Boolean visitAbstract(final Transaction model) {
            return visitAbstract(model.getToUnprotected()) || visitAbstract(model.getFromUnprotected());
        }
    }
}
