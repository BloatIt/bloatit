package com.bloatit.model;

import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

import com.bloatit.data.DaoJoinTeamInvitation.State;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.data.DaoUserContent;
import com.bloatit.data.queries.EmptyPageIterable;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPrivateAccessException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPublicReadOnlyAccessException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.right.Action;

public class ElveosAnonymousUser implements ElveosUser {

    public static ElveosAnonymousUser ANONYMOUS = new ElveosAnonymousUser();

    private ElveosAnonymousUser() {
        super();
    }

    @Override
    public String getLogin() {
        return "";
    }

    @Override
    public Date getDateCreation() throws UnauthorizedPublicReadOnlyAccessException {
        throw new BadProgrammerException("No creation date");
    }

    @Override
    public InternalAccount getInternalAccount() throws UnauthorizedPrivateAccessException {
        throw new UnauthorizedPrivateAccessException(Action.READ);
    }

    @Override
    public Integer getId() {
        return -1;
    }

    @Override
    public ExternalAccount getExternalAccount() throws UnauthorizedPrivateAccessException {
        throw new UnauthorizedPrivateAccessException(Action.READ);
    }

    @Override
    public PageIterable<BankTransaction> getBankTransactions() throws UnauthorizedPrivateAccessException {
        throw new UnauthorizedPrivateAccessException(Action.READ);
    }

    @Override
    public PageIterable<Contribution> getContributions() throws UnauthorizedOperationException {
        throw new UnauthorizedOperationException(Action.READ);
    }

    @Override
    public PageIterable<MoneyWithdrawal> getMoneyWithdrawals() throws UnauthorizedOperationException {
        throw new UnauthorizedOperationException(Action.READ);
    }

    @Override
    public void sendInvitation(Member member, Team team) throws UnauthorizedOperationException {
        throw new UnauthorizedOperationException(Action.WRITE);
    }

    @Override
    public boolean canAccessDateCreation() {
        return false;
    }

    @Override
    public boolean canGetInternalAccount() {
        return false;
    }

    @Override
    public boolean acceptInvitation(JoinTeamInvitation invitation) throws UnauthorizedOperationException {
        throw new UnauthorizedOperationException(Action.WRITE);
    }

    @Override
    public boolean canGetExternalAccount() {
        return false;
    }

    @Override
    public boolean canGetBankTransactionAccount() {
        return false;
    }

    @Override
    public boolean canGetContributions() {
        return false;
    }

    @Override
    public void refuseInvitation(JoinTeamInvitation invitation) throws UnauthorizedOperationException {
        throw new UnauthorizedOperationException(Action.WRITE);
    }

    @Override
    public void kickFromTeam(Team aTeam, Member actor) throws UnauthorizedOperationException {
        throw new UnauthorizedOperationException(Action.WRITE);
    }

    @Override
    public void addToPublicTeam(Team team) throws UnauthorizedOperationException {
        throw new UnauthorizedOperationException(Action.WRITE);
    }

    @Override
    public boolean checkPassword(String password) {
        return false;
    }

    @Override
    public boolean activate(String activationKey) {
        return false;
    }

    @Override
    public ActivationState getActivationState() {
        return ActivationState.VALIDATING;
    }

    @Override
    public String getActivationKey() {
        return "";
    }

    @Override
    public PageIterable<JoinTeamInvitation> getReceivedInvitation(State state) {
        return new EmptyPageIterable<JoinTeamInvitation>();
    }

    @Override
    public PageIterable<JoinTeamInvitation> getSentInvitation(State state) {
        return new EmptyPageIterable<JoinTeamInvitation>();
    }

    @Override
    public PageIterable<Team> getTeams() throws UnauthorizedOperationException {
        throw new UnauthorizedOperationException(Action.READ);
    }

    @Override
    public int getKarma() throws UnauthorizedOperationException {
        throw new UnauthorizedOperationException(Action.READ);
    }

    @Override
    public PageIterable<UserContent<? extends DaoUserContent>> getActivity() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "";
    }

    @Override
    public String getEmail() throws UnauthorizedOperationException {
        throw new UnauthorizedOperationException(Action.READ);
    }

    @Override
    public Locale getLocale() {
        return Locale.ENGLISH;
    }

    @Override
    public String getFullname() {
        return "";
    }

    @Override
    public Image getAvatar() {
        return null;
    }

    @Override
    public long getInvitationCount() {
        return 0;
    }

    @Override
    public PageIterable<Feature> getFeatures(boolean asMemberOnly) {
        return new EmptyPageIterable<Feature>();
    }

    @Override
    public PageIterable<Kudos> getKudos() throws UnauthorizedPrivateAccessException {
        throw new UnauthorizedPrivateAccessException(Action.READ);
    }

    @Override
    public PageIterable<Contribution> doGetContributions() throws UnauthorizedOperationException {
        throw new UnauthorizedOperationException(Action.READ);
    }

    @Override
    public PageIterable<MoneyWithdrawal> doGetMoneyWithdrawals() throws UnauthorizedOperationException {
        throw new UnauthorizedOperationException(Action.READ);
    }

    @Override
    public PageIterable<Contribution> getContributions(boolean asMemberOnly) {
        return new EmptyPageIterable<Contribution>();
    }

    @Override
    public PageIterable<Comment> getComments(boolean asMemberOnly) {
        return new EmptyPageIterable<Comment>();
    }

    @Override
    public PageIterable<Offer> getOffers(boolean asMemberOnly) {
        return new EmptyPageIterable<Offer>();
    }

    @Override
    public PageIterable<Translation> getTranslations(boolean asMemberOnly) {
        return new EmptyPageIterable<Translation>();
    }

    @Override
    public Role getRole() {
        return Role.NORMAL;
    }

    @Override
    public String getResetKey() {
        return null;
    }

    @Override
    public boolean canGetKarma() {
        return false;
    }

    @Override
    public boolean canSetPassword() {
        return false;
    }

    @Override
    public boolean canAccessEmail(Action action) {
        return false;
    }

    @Override
    public boolean canBeKickFromTeam(Team aTeam, Member actor) {
        return false;
    }

    @Override
    public boolean hasTeamRight(Team aTeam, UserTeamRight aRight) {
        return false;
    }

    @Override
    public Set<UserTeamRight> getTeamRights(Team g) {
        return EnumSet.noneOf(UserTeamRight.class);
    }

    @Override
    public boolean hasConsultTeamRight(Team aTeam) {
        return false;
    }

    @Override
    public boolean hasTalkTeamRight(Team aTeam) {
        return false;
    }

    @Override
    public boolean hasInviteTeamRight(Team aTeam) {
        return false;
    }

    @Override
    public boolean hasModifyTeamRight(Team aTeam) {
        return false;
    }

    @Override
    public boolean hasPromoteTeamRight(Team aTeam) {
        return false;
    }

    @Override
    public boolean hasBankTeamRight(Team aTeam) {
        return false;
    }

    @Override
    public boolean isInTeam(Team team) {
        return false;
    }

}
