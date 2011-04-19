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
package com.bloatit.model;

import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

import com.bloatit.data.DaoFileMetadata;
import com.bloatit.data.DaoJoinTeamInvitation;
import com.bloatit.data.DaoJoinTeamInvitation.State;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoTeam.Right;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.exceptions.lowlevel.MemberNotInTeamException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException.SpecialCode;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.SecuredHash;
import com.bloatit.framework.webprocessor.context.User;
import com.bloatit.model.feature.FeatureList;
import com.bloatit.model.lists.CommentList;
import com.bloatit.model.lists.ContributionList;
import com.bloatit.model.lists.JoinTeamInvitationList;
import com.bloatit.model.lists.KudosList;
import com.bloatit.model.lists.OfferList;
import com.bloatit.model.lists.TeamList;
import com.bloatit.model.lists.TranslationList;
import com.bloatit.model.lists.UserContentList;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.MemberRight;

public final class Member extends Actor<DaoMember> implements User {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final int PASSWORD_SALT_LENGTH = 50;

    private static final class MyCreator extends Creator<DaoMember, Member> {
        @SuppressWarnings("synthetic-access")
        @Override
        public Member doCreate(final DaoMember dao) {
            return new Member(dao);
        }
    }

    /**
     * Create a new member using its Dao version.
     * 
     * @param dao a DaoMember
     * @return the new member or null if dao is null.
     */
    @SuppressWarnings("synthetic-access")
    public static Member create(final DaoMember dao) {
        return new MyCreator().create(dao);
    }

    private static DaoMember createDaoMember(final String login, final String password, final String email, final Locale locale) {
        final String salt = RandomStringUtils.randomAscii(PASSWORD_SALT_LENGTH);
        final String passwd = SecuredHash.calculateHash(password, salt);
        return DaoMember.createAndPersist(login, passwd, salt, email, locale);
    }

    public Member(final String login, final String password, final String email, final Locale locale) {
        super(createDaoMember(login, password, email, locale));

    }

    public Member(final String login, final String password, final String email, final String fullname, final Locale locale) {
        this(login, password, email, locale);
        getDao().setFullname(fullname);
    }

    private Member(final DaoMember dao) {
        super(dao);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Accessors
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Tells if a user can access the team property. You have to unlock this
     * Member using the {@link Member#authenticate(AuthToken)} method.
     * 
     * @param action can be read/write/delete. for example use READ to know if
     *            you can use {@link Member#getTeams()}.
     * @return true if you can use the method.
     */
    public boolean canAccessTeams(final Action action) {
        return canAccess(new MemberRight.TeamList(), action);
    }

    /**
     * Tells if a user can access the property "invite".
     * 
     * @param team the team in which you want to invite somebody
     * @return true if you can invite/accept/refuse.
     */
    public boolean canSendInvitation(final Team team) {
        return canAccess(new MemberRight.SendInvitation(), Action.WRITE);
    }

    public boolean canGetKarma() {
        return canAccess(new MemberRight.Karma(), Action.READ);
    }

    public boolean canAccessName(final Action action) {
        return canAccess(new MemberRight.Name(), action);
    }

    public boolean canSetPassword() {
        return canAccess(new MemberRight.Password(), Action.WRITE);
    }

    public boolean canAccessLocale(final Action action) {
        return canAccess(new MemberRight.Locale(), action);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Setter / modification
    // /////////////////////////////////////////////////////////////////////////////////////////

    // / TEAM RIGHTS

    /**
     * <p>
     * Gives some new rights to a user in a teams
     * </p>
     * 
     * @param newRole the new role of the user
     * @throws MemberNotInTeamException when <code>this</code> is not part of
     *             <code>team</code>
     * @throws UnauthorizedOperationException if the authenticated user is not
     *             <code>ADMIN</code> of <code>team</code>
     */
    public void setTeamRole(final Team team, final TeamRole newRole) throws UnauthorizedOperationException, MemberNotInTeamException {
        if (!isInTeam(team)) {
            throw new MemberNotInTeamException();
        }

        tryAccess(new MemberRight.TeamList(), Action.WRITE);
        setTeamRoleUnprotected(team, newRole);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Accessors
    // /////////////////////////////////////////////////////////////////////////////////////////

    public Set<UserTeamRight> getTeamRights(final Team g) {
        return getDao().getTeamRights(g.getDao());
    }

    public void addTeamRight(final Team aTeam, final UserTeamRight aRight) {
        getDao().addTeamRight(aTeam.getDao(), aRight);
    }

    public void removeTeamRight(final Team aTeam, final UserTeamRight removeRight) {
        getDao().removeTeamRight(aTeam.getDao(), removeRight);
    }

    public boolean canInTeam(final Team aTeam, final UserTeamRight aRight) {
        if (getTeamRights(aTeam) == null) {
            return false;
        }
        return getTeamRights(aTeam).contains(aRight);
    }

    public boolean canConsult(final Team aTeam) {
        return canInTeam(aTeam, UserTeamRight.CONSULT);
    }

    public boolean canTalk(final Team aTeam) {
        return canInTeam(aTeam, UserTeamRight.TALK);
    }

    public boolean canInvite(final Team aTeam) {
        return canInTeam(aTeam, UserTeamRight.INVITE);
    }

    public boolean canModify(final Team aTeam) {
        return canInTeam(aTeam, UserTeamRight.MODIFY);
    }

    public boolean canPromote(final Team aTeam) {
        return canInTeam(aTeam, UserTeamRight.PROMOTE);
    }

    public boolean canBank(final Team aTeam) {
        return canInTeam(aTeam, UserTeamRight.BANK);
    }

    // / END TEAM RIGHTS

    /**
     * Give some right to the user to a team without checking if the user can
     * get these rights
     * 
     * @param team the team to add rights to the user
     * @param newRight the new new role of the user
     */
    protected void setTeamRoleUnprotected(final Team team, final TeamRole newRole) {
        for (final UserTeamRight r : newRole.getRights()) {
            getDao().addTeamRight(team.getDao(), r);
        }
    }

    /**
     * Adds a user to a team without checking if the team is Public or not
     * 
     * @param team the team to which the user will be added
     */
    protected void addToTeamUnprotected(final Team team) {
        getDao().addToTeam(team.getDao());
    }

    /**
     * To invite a member into a team you have to have the WRITE right on the
     * "invite" property.
     * 
     * @param member The member you want to invite
     * @param team The team in which you invite a member.
     * @throws UnauthorizedOperationException
     */
    public void sendInvitation(final Member member, final Team team) throws UnauthorizedOperationException {
        tryAccess(new MemberRight.SendInvitation(), Action.WRITE);
        DaoJoinTeamInvitation.createAndPersist(getDao(), member.getDao(), team.getDao());
    }

    /**
     * To accept an invitation you must have the DELETED right on the "invite"
     * property. If the invitation is not in PENDING state then nothing is done.
     * 
     * @param invitation the authenticate member must be receiver of the
     *            invitation.
     * @throws UnauthorizedOperationException
     */
    public void acceptInvitation(final JoinTeamInvitation invitation) throws UnauthorizedOperationException {
        if (!invitation.getReciever().getId().equals(getAuthToken().getMember().getId())) {
            throw new UnauthorizedOperationException(SpecialCode.INVITATION_RECIEVER_MISMATCH);
        }
        tryAccess(new MemberRight.SendInvitation(), Action.DELETE);

        // Accept the invitation
        invitation.accept();

        // discard all other invitation to join the same team
        final Team team = invitation.getTeam();
        final PageIterable<JoinTeamInvitation> receivedInvitation = this.getReceivedInvitation(State.PENDING, team);
        for (final JoinTeamInvitation invite : receivedInvitation) {
            invite.discard();
        }
    }

    /**
     * To refuse an invitation you must have the DELETED right on the "invite"
     * property. If the invitation is not in PENDING state then nothing is done.
     * 
     * @param invitation the authenticate member must be receiver of the
     *            invitation.
     * @throws UnauthorizedOperationException
     */
    public void refuseInvitation(final JoinTeamInvitation invitation) throws UnauthorizedOperationException {
        if (!invitation.getReciever().getId().equals(getAuthToken().getMember().getId())) {
            throw new UnauthorizedOperationException(SpecialCode.INVITATION_RECIEVER_MISMATCH);
        }
        tryAccess(new MemberRight.SendInvitation(), Action.DELETE);
        invitation.refuse();
    }

    /**
     * To remove this member from a team you have to have the DELETED right on
     * the "team" property. If the member is not in the "team", nothing is done.
     * (Although it should be considered as an error and will be logged)
     * 
     * @param team is the team from which the user will be removed.
     * @throws UnauthorizedOperationException
     */
    public void removeFromTeam(final Team team) throws UnauthorizedOperationException {
        tryAccess(new MemberRight.TeamList(), Action.DELETE);
        getDao().removeFromTeam(team.getDao());
    }

    public void setPassword(final String password) throws UnauthorizedOperationException {
        tryAccess(new MemberRight.Password(), Action.WRITE);
        getDao().setPassword(SecuredHash.calculateHash(password, getDao().getSalt()));
    }

    public void setLocal(final Locale loacle) throws UnauthorizedOperationException {
        tryAccess(new MemberRight.Locale(), Action.WRITE);
        getDao().setLocale(loacle);
    }

    // TODO Right management
    public void setRole(final Role role) {
        getDao().setRole(role);
    }

    public void activate() {
        getDao().setActivationState(ActivationState.ACTIVE);
    }

    /**
     * To add a user into a public team, you have to make sure you can access
     * the teams with the {@link Action#WRITE} action.
     * 
     * @param team must be a public team.
     * @throws UnauthorizedOperationException if the authenticated member do not
     *             have the right to use this methods.
     * @see Member#canAccessTeams(Action)
     */
    public void addToPublicTeam(final Team team) throws UnauthorizedOperationException {
        if (team.getRight() != Right.PUBLIC) {
            throw new UnauthorizedOperationException(SpecialCode.TEAM_NOT_PUBLIC);
        }
        getDao().addToTeam(team.getDao());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param state can be PENDING, ACCEPTED or REFUSED
     * @return all the received invitation with the specified state.
     */
    public PageIterable<JoinTeamInvitation> getReceivedInvitation(final State state) {
        return new JoinTeamInvitationList(getDao().getReceivedInvitation(state));
    }

    /**
     * @param state can be PENDING, ACCEPTED or REFUSED
     * @param team the team invited to join
     * @return all the received invitation with the specified state and team
     */
    public PageIterable<JoinTeamInvitation> getReceivedInvitation(final State state, final Team team) {
        return new JoinTeamInvitationList(getDao().getReceivedInvitation(state, team.getDao()));
    }

    /**
     * @param state can be PENDING, ACCEPTED or REFUSED
     * @return all the sent invitation with the specified state.
     */
    public PageIterable<DaoJoinTeamInvitation> getSentInvitation(final State state) {
        return getDao().getSentInvitation(state);
    }

    /**
     * To get the teams you have the have the READ right on the "team" property.
     * 
     * @return all the team in which this member is.
     * @throws UnauthorizedOperationException
     */
    public PageIterable<Team> getTeams() throws UnauthorizedOperationException {
        tryAccess(new MemberRight.TeamList(), Action.READ);
        return new TeamList(getDao().getTeams());
    }

    public int getKarma() throws UnauthorizedOperationException {
        tryAccess(new MemberRight.Karma(), Action.READ);
        return getDao().getKarma();
    }

    public PageIterable<UserContent<? extends DaoUserContent>> getActivity() {
        return new UserContentList(getDao().getActivity());
    }

    private static final float INFLUENCE_MULTIPLICATOR = 2;
    private static final float INFLUENCE_DIVISER = 100;
    private static final float INFLUENCE_BASE = 1;

    protected int calculateInfluence() {
        final int karma = getDao().getKarma();
        if (karma > 0) {
            return (int) (Math.log10((INFLUENCE_DIVISER + karma) / INFLUENCE_DIVISER) * INFLUENCE_MULTIPLICATOR + INFLUENCE_BASE);
        } else if (karma == 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public String getDisplayName() throws UnauthorizedOperationException {
        tryAccess(new MemberRight.Name(), Action.READ);
        if (getDao().getFullname() != null && !getDao().getFullname().isEmpty()) {
            return getFullname();
        }
        return getLogin();
    }

    @Override
    public String getUserLogin() {
        return getLoginUnprotected();
    }

    @Override
    public Locale getUserLocale() {
        return getLocaleUnprotected();
    }

    public String getFullname() throws UnauthorizedOperationException {
        tryAccess(new MemberRight.Name(), Action.READ);
        return getDao().getFullname();
    }

    public void setFullname(final String fullname) throws UnauthorizedOperationException {
        tryAccess(new MemberRight.Name(), Action.WRITE);
        getDao().setFullname(fullname);
    }

    public Locale getLocaleUnprotected() {
        return getDao().getLocale();
    }

    public Locale getLocale() throws UnauthorizedOperationException {
        tryAccess(new MemberRight.Locale(), Action.READ);
        return getDao().getLocale();
    }

    public PageIterable<Feature> getFeatures(final boolean asMemberOnly) {
        return new FeatureList(getDao().getFeatures(asMemberOnly));
    }

    public PageIterable<Kudos> getKudos() {
        return new KudosList(getDao().getKudos());
    }

    @Override
    public PageIterable<Contribution> getContributions() throws UnauthorizedOperationException {
        return getContributions(true);
    }

    public PageIterable<Contribution> getContributions(final boolean asMemberOnly) throws UnauthorizedOperationException {
        tryAccess(new MemberRight.Contributions(), Action.READ);
        return new ContributionList(getDao().getContributions(asMemberOnly));
    }

    public PageIterable<Comment> getComments(final boolean asMemberOnly) {
        return new CommentList(getDao().getComments(asMemberOnly));
    }

    public PageIterable<Offer> getOffers(final boolean asMemberOnly) {
        return new OfferList(getDao().getOffers(asMemberOnly));
    }

    public PageIterable<Translation> getTranslations(final boolean asMemberOnly) {
        return new TranslationList(getDao().getTranslations(asMemberOnly));
    }

    public boolean isInTeam(final Team team) {
        return isInTeamUnprotected(team);
    }

    /**
     * Returns the status of the member in a given <code>team</code> <<<<<<<
     * Updated upstream
     * 
     * @param team the team in which we want to know member status =======
     * @param team the team in which we want to know member status >>>>>>>
     *            Stashed changes
     * @return a <code>Set</code> containing all the roles of the member for
     *         <code>team</code> or <code>null</code> if the member is not part
     *         of this team. <br />
     *         Note the set can be empty if the member has no preset role
     *         (standard member).
     */
    protected TeamRole getRoleUnprotected(final Team team) {
        final Set<UserTeamRight> memberStatus = team.getDao().getUserTeamRight(getDao());
        if (memberStatus != null) {
            return new TeamRole(memberStatus);
        }
        return null;
    }

    protected boolean isInTeamUnprotected(final Team team) {
        return getDao().isInTeam(team.getDao());
    }

    protected void addToKarma(final int value) {
        getDao().addToKarma(value);
    }

    public Role getRole() {
        return getDao().getRole();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.User#getActivationState()
     */
    @Override
    public ActivationState getActivationState() {
        return getDao().getActivationState();
    }

    public String getActivationKey() {
        final DaoMember m = getDao();
        final String digest = "" + m.getId() + m.getContact() + m.getFullname() + m.getPassword();
        return DigestUtils.sha256Hex(digest);
    }

    @Override
    public Image getAvatar() {
        DaoFileMetadata avatar = getDao().getAvatar();
        if (avatar != null) {
            return new Image(FileMetadata.create(avatar));
        }
        String libravatar = null;
        libravatar = libravatar(getDao().getContact().toLowerCase().trim());
        if (libravatar == null) {
            return null;
        }
        return new Image(libravatar);
    }

    private String libravatar(String email) {
        String digest = DigestUtils.md5Hex(email.toLowerCase());
        // return "http://cdn.libravatar.org/avatar/" + digest +
        // "?d=http://elveos.org/resources/commons/img/none.png&s=64";
        return digest;
    }

    public void setAvatar(final FileMetadata fileImage) {
        // TODO: right management
        getDao().setAvatar(fileImage.getDao());
    }

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    /**
     * Checks if an inputed password matches the user password
     * 
     * @param password the password to match
     * @return <i>true</i> if the inputed password matches the password in the
     *         database, <i>false</i> otherwise
     */
    public boolean checkPassword(String password) {
        final String digestedPassword = SecuredHash.calculateHash(password, getDao().getSalt());
        return getDao().passwordEquals(digestedPassword);
    }
}
