package com.bloatit.model;

import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.webprocessor.context.AbstractAuthToken;
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
    private Team currentTeam;

    public Rights(final AuthToken token, final IdentifiableInterface identifiable) {
        this.token = token;
        currentTeam = null;
        if (token == null || token.isAnonymous()) {
            owningState = OwningState.NOBODY;
        } else {
            if (token.getAsTeam() != null || identifiable.accept(new GetCreatedByTeamVisitor()) != null) {
                if (token.getAsTeam() != null) {
                    currentTeam = token.getAsTeam();
                } else {
                    currentTeam = identifiable.accept(new GetCreatedByTeamVisitor());
                }
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
        return token != null && currentTeam != null && hasModifyTeamRight(token.getMember(), currentTeam);
    }

    public boolean hasConsultTeamRight() {
        if (isTeamOwner()) {
            return token != null && currentTeam != null && hasConsultTeamRight(token.getMember(), currentTeam);
        }
        return false;
    }

    public boolean hasBankTeamRight() {
        if (isTeamOwner()) {
            return token != null && currentTeam != null && hasBankTeamRight(token.getMember(), currentTeam);
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
        return token != null && token.getMember().getRole() == role;
    }

    // ///////////////////////////////////////
    // Visitors

    private class GetCreatedByTeamVisitor extends HighLevelModelVisitor<Team> {

        @Override
        public Team visitAbstract(final Account<?> model) {
            return visitAbstract(model.getActorUnprotected());
        }

        @Override
        public Team visitAbstract(final Actor<?> model) {
            if (model instanceof Team) {
                return (Team) model;
            }
            return null;
        }

        @Override
        public Team visitAbstract(final UserContentInterface model) {
            return model.getAsTeam();
        }

        @Override
        public Team visitAbstract(final BankTransaction model) {
            return visitAbstract(model.getAuthorUnprotected());
        }

        @Override
        public Team visitAbstract(final Milestone model) {
            return visitAbstract(model.getOffer());
        }

        @Override
        public Team visitAbstract(final Description model) {
            return null;
        }

        @Override
        public Team visitAbstract(final HighlightFeature model) {
            return null;
        }

        @Override
        public Team visitAbstract(final JoinTeamInvitation model) {
            return null;
        }

        @Override
        public Team visitAbstract(final Software model) {
            return null;
        }

        @Override
        public Team visitAbstract(final Transaction model) {
            // FIXME !!
            return null;
        }

        @Override
        public Team visitAbstract(MoneyWithdrawal model) {
            return visitAbstract(model.getActorUnprotected());
        }

    }

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
            return visitAbstract(model.getAuthor());
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

        @Override
        public Boolean visitAbstract(MoneyWithdrawal model) {
            return visitAbstract(model.getActorUnprotected());
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

        @Override
        public Boolean visitAbstract(MoneyWithdrawal model) {
            return visitAbstract(model.getActorUnprotected());
        }
    }
}
