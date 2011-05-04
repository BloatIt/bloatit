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
import com.bloatit.model.right.Action;

public class ElveosAuthenticatedUser implements ElveosUser {
    Member member;

    public ElveosAuthenticatedUser(Member member) {
        super();
        this.member = member;
    }

    @Override
    public final String getLogin() {
        return member.getLogin();
    }

    @Override
    public final Date getDateCreation() throws UnauthorizedPublicReadOnlyAccessException {
        return member.getDateCreation();
    }

    @Override
    public final InternalAccount getInternalAccount() throws UnauthorizedPrivateAccessException {
        return member.getInternalAccount();
    }

    @Override
    public final Integer getId() {
        return member.getId();
    }

    @Override
    public int hashCode() {
        return member.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return member.equals(obj);
    }

    @Override
    public final ExternalAccount getExternalAccount() throws UnauthorizedPrivateAccessException {
        return member.getExternalAccount();
    }

    @Override
    public final PageIterable<BankTransaction> getBankTransactions() throws UnauthorizedPrivateAccessException {
        return member.getBankTransactions();
    }

    @Override
    public PageIterable<Contribution> getContributions() throws UnauthorizedOperationException {
        return member.getContributions();
    }

    @Override
    public PageIterable<MoneyWithdrawal> getMoneyWithdrawals() throws UnauthorizedOperationException {
        return member.getMoneyWithdrawals();
    }

    @Override
    public void sendInvitation(Member member, Team team) throws UnauthorizedOperationException {
        member.sendInvitation(member, team);
    }

    @Override
    public final boolean canAccessDateCreation() {
        return member.canAccessDateCreation();
    }

    @Override
    public final boolean canGetInternalAccount() {
        return member.canGetInternalAccount();
    }

    @Override
    public boolean acceptInvitation(JoinTeamInvitation invitation) throws UnauthorizedOperationException {
        return member.acceptInvitation(invitation);
    }

    @Override
    public final boolean canGetExternalAccount() {
        return member.canGetExternalAccount();
    }

    @Override
    public final boolean canGetBankTransactionAccount() {
        return member.canGetBankTransactionAccount();
    }

    @Override
    public final boolean canGetContributions() {
        return member.canGetContributions();
    }

    @Override
    public void refuseInvitation(JoinTeamInvitation invitation) throws UnauthorizedOperationException {
        member.refuseInvitation(invitation);
    }

    @Override
    public void kickFromTeam(Team aTeam, Member actor) throws UnauthorizedOperationException {
        member.kickFromTeam(aTeam, actor);
    }

    @Override
    public void addToPublicTeam(Team team) throws UnauthorizedOperationException {
        member.addToPublicTeam(team);
    }

    @Override
    public boolean checkPassword(String password) {
        return member.checkPassword(password);
    }

    @Override
    public boolean activate(String activationKey) {
        return member.activate(activationKey);
    }

    @Override
    public ActivationState getActivationState() {
        return member.getActivationState();
    }

    @Override
    public String getActivationKey() {
        return member.getActivationKey();
    }

    @Override
    public PageIterable<JoinTeamInvitation> getReceivedInvitation(State state) {
        return member.getReceivedInvitation(state);
    }

    @Override
    public PageIterable<Team> getTeams() throws UnauthorizedOperationException {
        return member.getTeams();
    }

    @Override
    public int getKarma() throws UnauthorizedOperationException {
        return member.getKarma();
    }

    @Override
    public PageIterable<UserContent<? extends DaoUserContent>> getActivity() {
        return member.getActivity();
    }

    @Override
    public String getDisplayName() {
        return member.getDisplayName();
    }

    @Override
    public String getEmail() throws UnauthorizedOperationException {
        return member.getEmail();
    }

    @Override
    public Locale getLocale() {
        return member.getLocale();
    }

    @Override
    public String getFullname() {
        return member.getFullname();
    }

    @Override
    public Image getAvatar() {
        return member.getAvatar();
    }

    @Override
    public long getInvitationCount() {
        return member.getInvitationCount();
    }

    @Override
    public PageIterable<Feature> getFeatures(boolean asMemberOnly) {
        return member.getFeatures(asMemberOnly);
    }

    @Override
    public PageIterable<Kudos> getKudos() throws UnauthorizedPrivateAccessException {
        return member.getKudos();
    }

    @Override
    public PageIterable<Contribution> doGetContributions() throws UnauthorizedOperationException {
        return member.doGetContributions();
    }

    @Override
    public PageIterable<MoneyWithdrawal> doGetMoneyWithdrawals() throws UnauthorizedOperationException {
        return member.doGetMoneyWithdrawals();
    }

    @Override
    public PageIterable<Contribution> getContributions(boolean asMemberOnly) {
        return member.getContributions(asMemberOnly);
    }

    @Override
    public PageIterable<Comment> getComments(boolean asMemberOnly) {
        return member.getComments(asMemberOnly);
    }

    @Override
    public PageIterable<Offer> getOffers(boolean asMemberOnly) {
        return member.getOffers(asMemberOnly);
    }

    @Override
    public PageIterable<Translation> getTranslations(boolean asMemberOnly) {
        return member.getTranslations(asMemberOnly);
    }

    @Override
    public Role getRole() {
        return member.getRole();
    }

    @Override
    public String getResetKey() {
        return member.getResetKey();
    }

    @Override
    public boolean canGetKarma() {
        return member.canGetKarma();
    }

    @Override
    public boolean canSetPassword() {
        return member.canSetPassword();
    }

    @Override
    public boolean canAccessEmail(Action action) {
        return member.canAccessEmail(action);
    }

    @Override
    public boolean canBeKickFromTeam(Team aTeam, Member actor) {
        return member.canBeKickFromTeam(aTeam, actor);
    }

    @Override
    public boolean hasTeamRight(Team aTeam, UserTeamRight aRight) {
        return member.hasTeamRight(aTeam, aRight);
    }

    @Override
    public Set<UserTeamRight> getTeamRights(Team g) {
        return member.getTeamRights(g);
    }

    @Override
    public boolean hasConsultTeamRight(Team aTeam) {
        return member.hasConsultTeamRight(aTeam);
    }

    @Override
    public boolean hasTalkTeamRight(Team aTeam) {
        return member.hasTalkTeamRight(aTeam);
    }

    @Override
    public boolean hasInviteTeamRight(Team aTeam) {
        return member.hasInviteTeamRight(aTeam);
    }

    @Override
    public boolean hasModifyTeamRight(Team aTeam) {
        return member.hasModifyTeamRight(aTeam);
    }

    @Override
    public boolean hasPromoteTeamRight(Team aTeam) {
        return member.hasPromoteTeamRight(aTeam);
    }

    @Override
    public boolean hasBankTeamRight(Team aTeam) {
        return member.hasBankTeamRight(aTeam);
    }

    @Override
    public boolean isInTeam(Team team) {
        return member.isInTeam(team);
    }

    @Override
    public PageIterable<JoinTeamInvitation> getSentInvitation(State state) {
        return member.getSentInvitation(state);
    }

}
