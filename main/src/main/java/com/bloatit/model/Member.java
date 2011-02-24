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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Set;

import com.bloatit.data.DaoGroup.Right;
import com.bloatit.data.DaoGroupRight.UserGroupRight;
import com.bloatit.data.DaoJoinGroupInvitation;
import com.bloatit.data.DaoJoinGroupInvitation.State;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMember.ActivationState;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.MemberNotInGroupException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException.SpecialCode;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.demand.DemandList;
import com.bloatit.model.lists.CommentList;
import com.bloatit.model.lists.ContributionList;
import com.bloatit.model.lists.GroupList;
import com.bloatit.model.lists.JoinGroupInvitationtList;
import com.bloatit.model.lists.KudosList;
import com.bloatit.model.lists.OfferList;
import com.bloatit.model.lists.TranslationList;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.MemberRight;
import com.bloatit.rest.resources.ModelClassVisitor;

public final class Member extends Actor<DaoMember> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoMember, Member> {
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
    public static Member create(final DaoMember dao) {
        return new MyCreator().create(dao);
    }

    public Member(final String login, final String password, final String email, final Locale locale) {
        super(DaoMember.createAndPersist(login, password, email, locale));
    }

    private Member(final DaoMember dao) {
        super(dao);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Accessors
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Tells if a user can access the group property. You have to unlock this
     * Member using the {@link Member#authenticate(AuthToken)} method.
     *
     * @param action can be read/write/delete. for example use READ to know if
     *            you can use {@link Member#getGroups()}.
     * @return true if you can use the method.
     */
    public boolean canAccessGroups(final Action action) {
        return canAccess(new MemberRight.GroupList(), action);
    }

    /**
     * Tells if a user can access the property "invite".
     *
     * @param group the group in which you want to invite somebody
     * @param action WRITE for create a new invitation, DELETED to accept/refuse
     *            it, READ to list the invitations you have recieved.
     * @return true if you can invite/accept/refuse.
     */
    public boolean canSendInvitation(final Group group, final Action action) {
        return canAccess(new MemberRight.SendInvitation(), action);
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

    /**
     * <p>
     * Gives some new rights to a user in a groups
     * </p>
     *
     * @param newRole the new role of the user
     * @throws MemberNotInGroupException when <code>this</code> is not part of
     *             <code>group</code>
     * @throws UnauthorizedOperationException if the authenticated user is not
     *             <code>ADMIN</code> of <code>group</code>
     */
    public void setGroupRole(final Group group, final TeamRole newRole) throws UnauthorizedOperationException, MemberNotInGroupException {
        if (!isInGroup(group)) {
            throw new MemberNotInGroupException();
        }

        tryAccess(new MemberRight.GroupList(), Action.WRITE);
        setGroupRoleUnprotected(group, newRole);
    }

    /**
     * Give some right to the user to a group without checking if the user can
     * get these rights
     *
     * @param group the group to add rights to the user
     * @param newRight the new new role of the user
     */
    protected void setGroupRoleUnprotected(final Group group, final TeamRole newRole) {
        for (final UserGroupRight r : newRole.getRights()) {
            getDao().addGroupRight(group.getDao(), r);
        }
    }

    /**
     * Adds a user to a group without checking if the group is Public or not
     *
     * @param group the group to which the user will be added
     */
    protected void addToGroupUnprotected(final Group group) {
        getDao().addToGroup(group.getDao());
    }

    /**
     * To invite a member into a group you have to have the WRITE right on the
     * "invite" property.
     *
     * @param member The member you want to invite
     * @param group The group in which you invite a member.
     * @throws UnauthorizedOperationException
     */
    public void sendInvitation(final Member member, final Group group) throws UnauthorizedOperationException {
        tryAccess(new MemberRight.SendInvitation(), Action.WRITE);
        DaoJoinGroupInvitation.createAndPersist(getDao(), member.getDao(), group.getDao());
    }

    /**
     * To accept an invitation you must have the DELETED right on the "invite"
     * property. If the invitation is not in PENDING state then nothing is done.
     *
     * @param invitation the authenticate member must be receiver of the
     *            invitation.
     * @throws UnauthorizedOperationException
     */
    public void acceptInvitation(final JoinGroupInvitation invitation) throws UnauthorizedOperationException {
        if (invitation.getReciever().getId() != getAuthToken().getMember().getId()) {
            throw new UnauthorizedOperationException(SpecialCode.INVITATION_RECIEVER_MISMATCH);
        }
        tryAccess(new MemberRight.SendInvitation(), Action.DELETE);

        // Accept the invitation
        invitation.accept();

        // discard all other invitation to join the same group
        final Group group = invitation.getGroup();
        final PageIterable<JoinGroupInvitation> receivedInvitation = this.getReceivedInvitation(State.PENDING, group);
        for (final JoinGroupInvitation invite : receivedInvitation) {
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
    public void refuseInvitation(final JoinGroupInvitation invitation) throws UnauthorizedOperationException {
        if (invitation.getReciever().getId() != getAuthToken().getMember().getId()) {
            throw new UnauthorizedOperationException(SpecialCode.INVITATION_RECIEVER_MISMATCH);
        }
        tryAccess(new MemberRight.SendInvitation(), Action.DELETE);
        invitation.refuse();
    }

    /**
     * To remove this member from a group you have to have the DELETED right on
     * the "group" property. If the member is not in the "group", nothing is
     * done. (Although it should be considered as an error and will be logged)
     *
     * @param group is the group from which the user will be removed.
     * @throws UnauthorizedOperationException
     */
    public void removeFromGroup(final Group group) throws UnauthorizedOperationException {
        tryAccess(new MemberRight.GroupList(), Action.DELETE);
        getDao().removeFromGroup(group.getDao());
    }

    public void setPassword(final String password) throws UnauthorizedOperationException {
        tryAccess(new MemberRight.Password(), Action.WRITE);
        getDao().setPassword(password);
    }

    public void setLocal(final Locale loacle) throws UnauthorizedOperationException {
        tryAccess(new MemberRight.Locale(), Action.WRITE);
        getDao().setLocale(loacle);
    }

    // TODO Right management
    public void setRole(final Role role) {
        getDao().setRole(role);
    }

    public static String sha1(final String digest) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (final NoSuchAlgorithmException ex) {
            throw new FatalErrorException("Algorithm Sha1 not available", ex);
        }
        md.update(digest.getBytes());
        final byte byteData[] = md.digest();

        final StringBuilder sb = new StringBuilder();
        for (final byte element : byteData) {
            sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public void activate() {
        getDao().setActivationState(ActivationState.ACTIVE);
    }

    /**
     * To add a user into a public group, you have to make sure you can access
     * the groups with the {@link Action#WRITE} action.
     *
     * @param group must be a public group.
     * @throws UnauthorizedOperationException if the authenticated member do not
     *             have the right to use this methods.
     * @see Member#canAccessGroups(Action)
     */
    public void addToPublicGroup(final Group group) throws UnauthorizedOperationException {
        if (group.getRight() != Right.PUBLIC) {
            throw new UnauthorizedOperationException(SpecialCode.GROUP_NOT_PUBLIC);
        }
        tryAccess(new MemberRight.GroupList(), Action.WRITE);
        getDao().addToGroup(group.getDao());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param state can be PENDING, ACCEPTED or REFUSED
     * @return all the received invitation with the specified state.
     */
    public PageIterable<JoinGroupInvitation> getReceivedInvitation(final State state) {
        return new JoinGroupInvitationtList(getDao().getReceivedInvitation(state));
    }

    /**
     * @param state can be PENDING, ACCEPTED or REFUSED
     * @param group the group invited to join
     * @return all the received invitation with the specified state and group
     */
    public PageIterable<JoinGroupInvitation> getReceivedInvitation(final State state, final Group group) {
        return new JoinGroupInvitationtList(getDao().getReceivedInvitation(state, group.getDao()));
    }

    /**
     * @param state can be PENDING, ACCEPTED or REFUSED
     * @return all the sent invitation with the specified state.
     */
    public PageIterable<DaoJoinGroupInvitation> getSentInvitation(final State state) {
        return getDao().getSentInvitation(state);
    }

    /**
     * To get the groups you have the have the READ right on the "group"
     * property.
     *
     * @return all the group in which this member is.
     * @throws UnauthorizedOperationException
     */
    public PageIterable<Group> getGroups() throws UnauthorizedOperationException {
        tryAccess(new MemberRight.GroupList(), Action.READ);
        return new GroupList(getDao().getGroups());
    }

    public int getKarma() throws UnauthorizedOperationException {
        tryAccess(new MemberRight.Karma(), Action.READ);
        return getDao().getKarma();
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

    public String getDisplayName() throws UnauthorizedOperationException {
        tryAccess(new MemberRight.Name(), Action.READ);
        if (getDao().getFullname() != null && getDao().getFullname().isEmpty()) {
            return getLogin();
        }
        return getFullname();
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

    public PageIterable<Demand> getDemands() {
        return new DemandList(getDao().getDemands());
    }

    public PageIterable<Kudos> getKudos() {
        return new KudosList(getDao().getKudos());
    }

    public PageIterable<Contribution> getContributions() {
        return new ContributionList(getDao().getTransactions());
    }

    public PageIterable<Comment> getComments() {
        return new CommentList(getDao().getComments());
    }

    public PageIterable<Offer> getOffers() {
        return new OfferList(getDao().getOffers());
    }

    public PageIterable<Translation> getTranslations() {
        return new TranslationList(getDao().getTranslations());
    }

    public boolean isInGroup(final Group group) {
        return isInGroupUnprotected(group);
    }

    /**
     * Returns the status of the member in a given <code>group</code> <<<<<<<
     * Updated upstream
     *
     * @param group the group in which we want to know member status =======
     * @param group the group in which we want to know member status >>>>>>>
     *            Stashed changes
     * @return a <code>Set</code> containing all the roles of the member for
     *         <code>group</code> or <code>null</code> if the member is not part
     *         of this group. <br />
     *         Note the set can be empty if the member has no preset role
     *         (standard member).
     */
    protected TeamRole getRoleUnprotected(final Group group) {
        final Set<UserGroupRight> memberStatus = group.getDao().getUserGroupRight(getDao());
        if (memberStatus != null) {
            return new TeamRole(memberStatus);
        }
        return null;
    }

    protected boolean isInGroupUnprotected(final Group group) {
        return getDao().isInGroup(group.getDao());
    }

    protected void addToKarma(final int value) {
        getDao().addToKarma(value);
    }

    protected String getPassword() {
        return getDao().getPassword();
    }

    public Role getRole() {
        return getDao().getRole();
    }

    public ActivationState getActivationState() {
        return getDao().getActivationState();
    }

    public String getActivationKey() {
        final DaoMember m = getDao();
        final String digest = "" + m.getId() + m.getContact() + m.getFullname() + m.getPassword();

        return sha1(digest);
    }

    public FileMetadata getAvatar() {
        return FileMetadata.create(getDao().getAvatar());
    }

    public void setAvatar(FileMetadata fileImage) {
        // TODO: right management
        getDao().setAvatar(fileImage.getDao());
    }

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
