package com.bloatit.model;

import java.util.Date;
import java.util.Locale;
import java.util.Set;

import com.bloatit.data.DaoJoinTeamInvitation.State;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPrivateAccessException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPublicReadOnlyAccessException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.context.User;
import com.bloatit.model.right.Action;

public interface ElveosUser extends User {

    public abstract String getLogin();

    public abstract Date getDateCreation() throws UnauthorizedPublicReadOnlyAccessException;

    public abstract InternalAccount getInternalAccount() throws UnauthorizedPrivateAccessException;

    public abstract Integer getId();

    public abstract int hashCode();

    public abstract boolean equals(Object obj);

    public abstract ExternalAccount getExternalAccount() throws UnauthorizedPrivateAccessException;

    public abstract PageIterable<BankTransaction> getBankTransactions() throws UnauthorizedPrivateAccessException;

    public abstract PageIterable<Contribution> getContributions() throws UnauthorizedOperationException;

    public abstract PageIterable<MoneyWithdrawal> getMoneyWithdrawals() throws UnauthorizedOperationException;

    public abstract void sendInvitation(Member member, Team team) throws UnauthorizedOperationException;

    public abstract boolean canAccessDateCreation();

    public abstract boolean canGetInternalAccount();

    public abstract boolean acceptInvitation(JoinTeamInvitation invitation) throws UnauthorizedOperationException;

    public abstract boolean canGetExternalAccount();

    public abstract boolean canGetBankTransactionAccount();

    public abstract boolean canGetContributions();

    public abstract void refuseInvitation(JoinTeamInvitation invitation) throws UnauthorizedOperationException;

    public abstract void kickFromTeam(Team aTeam, Member actor) throws UnauthorizedOperationException;

    public abstract void addToPublicTeam(Team team) throws UnauthorizedOperationException;

    public abstract boolean checkPassword(String password);

    public abstract boolean activate(String activationKey);

    public abstract ActivationState getActivationState();

    public abstract String getActivationKey();

    public abstract PageIterable<JoinTeamInvitation> getReceivedInvitation(State state);

    public abstract PageIterable<JoinTeamInvitation> getSentInvitation(State state);

    public abstract PageIterable<Team> getTeams() throws UnauthorizedOperationException;

    public abstract int getKarma() throws UnauthorizedOperationException;

    public abstract PageIterable<UserContent<? extends DaoUserContent>> getActivity();

    public abstract String getDisplayName();

    public abstract String getEmail() throws UnauthorizedOperationException;

    public abstract Locale getLocale();

    public abstract String getFullname();

    public abstract Image getAvatar();

    public abstract long getInvitationCount();

    public abstract PageIterable<Feature> getFeatures(boolean asMemberOnly);

    public abstract PageIterable<Kudos> getKudos() throws UnauthorizedPrivateAccessException;

    public abstract PageIterable<Contribution> doGetContributions() throws UnauthorizedOperationException;

    public abstract PageIterable<MoneyWithdrawal> doGetMoneyWithdrawals() throws UnauthorizedOperationException;

    public abstract PageIterable<Contribution> getContributions(boolean asMemberOnly);

    public abstract PageIterable<Comment> getComments(boolean asMemberOnly);

    public abstract PageIterable<Offer> getOffers(boolean asMemberOnly);

    public abstract PageIterable<Translation> getTranslations(boolean asMemberOnly);

    public abstract Role getRole();

    public abstract String getResetKey();

    public abstract boolean canGetKarma();

    public abstract boolean canSetPassword();

    public abstract boolean canAccessEmail(Action action);

    public abstract boolean canBeKickFromTeam(Team aTeam, Member actor);

    public abstract boolean hasTeamRight(Team aTeam, UserTeamRight aRight);

    public abstract Set<UserTeamRight> getTeamRights(Team g);

    public abstract boolean hasConsultTeamRight(Team aTeam);

    public abstract boolean hasTalkTeamRight(Team aTeam);

    public abstract boolean hasInviteTeamRight(Team aTeam);

    public abstract boolean hasModifyTeamRight(Team aTeam);

    public abstract boolean hasPromoteTeamRight(Team aTeam);

    public abstract boolean hasBankTeamRight(Team aTeam);

    public abstract boolean isInTeam(Team team);

}
