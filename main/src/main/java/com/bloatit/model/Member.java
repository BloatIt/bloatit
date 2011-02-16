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
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.demand.DemandList;
import com.bloatit.model.lists.CommentList;
import com.bloatit.model.lists.ContributionList;
import com.bloatit.model.lists.GroupList;
import com.bloatit.model.lists.JoinGroupInvitationtList;
import com.bloatit.model.lists.KudosList;
import com.bloatit.model.lists.OfferList;
import com.bloatit.model.lists.TranslationList;
import com.bloatit.model.right.MemberRight;
import com.bloatit.model.right.RightManager.Action;

public final class Member extends Actor<DaoMember> {

    /**
     * Create a new member using its Dao version.
     *
     * @param dao
     *            a DaoMember
     * @return the new member or null if dao is null.
     */
    public static Member create(final DaoMember dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoMember> created = CacheManager.get(dao);
            if (created == null) {
                return new Member(dao);
            }
            return (Member) created;
        }
        return null;
    }

    public Member(final String login, final String password, final String email, final Locale locale) {
        super(DaoMember.createAndPersist(login, password, email, locale));
    }

    private Member(final DaoMember dao) {
        super(dao);
    }

    /**
     * Tells if a user can access the group property. You have to unlock this
     * Member using the {@link Member#authenticate(AuthToken)} method.
     *
     * @param action
     *            can be read/write/delete. for example use READ to know if you
     *            can use {@link Member#getGroups()}.
     * @return true if you can use the method.
     */
    public boolean canAccessGroups(final Action action) {
        return new MemberRight.GroupList().canAccess(calculateRole(this), action);
    }

    /**
     * To add a user into a public group, you have to make sure you can access
     * the groups with the {@link Action#WRITE} action.
     *
     * @param group
     *            must be a public group.
     * @throws UnauthorizedOperationException
     *             if the authenticated member do not have the right to use this
     *             methods.
     * @see Member#canAccessGroups(Action)
     */
    public void addToPublicGroup(final Group group) throws UnauthorizedOperationException {
        if (group.getRight() != Right.PUBLIC) {
            throw new UnauthorizedOperationException(SpecialCode.GROUP_NOT_PUBLIC);
        }
        new MemberRight.GroupList().tryAccess(calculateRole(this), Action.WRITE);
        getDao().addToGroup(group.getDao());
    }

    /**
     * <p>
     * Gives some new rights to a user in a groups
     * </p>
     *
     * @param the
     *            role in which the new role will be set
     * @param newRole
     *            the new role of the user
     * @throws MemberNotInGroupException
     *             when <code>this</code> is not part of <code>group</code>
     * @throws UnauthorizedOperationException
     *             if the authenticated user is not <code>ADMIN</code> of
     *             <code>group</code>
     */
    public void setGroupRole(final Group group, TeamRole newRole) throws UnauthorizedOperationException, MemberNotInGroupException {
        if (!this.isInGroup(group)) {
            throw new MemberNotInGroupException();
        }

        new MemberRight.GroupList().tryAccess(calculateRole(this), Action.WRITE);
        setGroupRoleUnprotected(group, newRole);
    }
    

    /**
     * Give some right to the user to a group without checking if the user can
     * get these rights
     *
     * @param group
     *            the group to add rights to the user
     * @param newRight
     *            the new new role of the user
     */
    protected void setGroupRoleUnprotected(Group group, TeamRole newRole) {
        for (UserGroupRight r : newRole.getRights()) {
            getDao().addGroupRight(group.getDao(), r);
        }
    }

    /**
     * Adds a user to a group without checking if the group is Public or not
     *
     * @param group
     *            the group to which the user will be added
     */
    protected void addToGroupUnprotected(Group group) {
        getDao().addToGroup(group.getDao());
    }

    /**
     * Tells if a user can access the property "invite".
     *
     * @param group
     *            the group in which you want to invite somebody
     * @param action
     *            WRITE for create a new invitation, DELETED to accept/refuse
     *            it, READ to list the invitations you have recieved.
     * @return true if you can invite/accept/refuse.
     */
    public boolean canInvite(final Group group, final Action action) {
        return new MemberRight.InviteInGroup().canAccess(calculateRole(this, group), action);
    }

    /**
     * To invite a member into a group you have to have the WRITE right on the
     * "invite" property.
     *
     * @param member
     *            The member you want to invite
     * @param group
     *            The group in which you invite a member.
     * @throws UnauthorizedOperationException
     */
    public void invite(final Member member, final Group group) throws UnauthorizedOperationException {
        new MemberRight.InviteInGroup().tryAccess(calculateRole(this, group), Action.WRITE);
        DaoJoinGroupInvitation.createAndPersist(getDao(), member.getDao(), group.getDao());
    }

    /**
     * @param state
     *            can be PENDING, ACCEPTED or REFUSED
     * @return all the received invitation with the specified state.
     */
    public PageIterable<JoinGroupInvitation> getReceivedInvitation(final State state) {
        return new JoinGroupInvitationtList(getDao().getReceivedInvitation(state));
    }

    /**
     * @param state
     *            can be PENDING, ACCEPTED or REFUSED
     * @return all the sent invitation with the specified state.
     */
    public PageIterable<DaoJoinGroupInvitation> getSentInvitation(final State state) {
        return getDao().getSentInvitation(state);
    }

    /**
     * To accept an invitation you must have the DELETED right on the "invite"
     * property. If the invitation is not in PENDING state then nothing is done.
     *
     * @param invitation
     *            the authenticate member must be receiver of the invitation.
     * @throws UnauthorizedOperationException
     */
    public void acceptInvitation(final JoinGroupInvitation invitation) throws UnauthorizedOperationException {
        if (invitation.getReciever().getId() != getAuthToken().getMember().getId()) {
            throw new UnauthorizedOperationException(SpecialCode.INVITATION_RECIEVER_MISMATCH);
        }
        new MemberRight.InviteInGroup().tryAccess(calculateRole(this, invitation.getGroup()), Action.DELETE);
        invitation.accept();
    }

    /**
     * To refuse an invitation you must have the DELETED right on the "invite"
     * property. If the invitation is not in PENDING state then nothing is done.
     *
     * @param invitation
     *            the authenticate member must be receiver of the invitation.
     * @throws UnauthorizedOperationException
     */
    public void refuseInvitation(final JoinGroupInvitation invitation) throws UnauthorizedOperationException {
        if (invitation.getReciever().getId() != getAuthToken().getMember().getId()) {
            throw new UnauthorizedOperationException(SpecialCode.INVITATION_RECIEVER_MISMATCH);
        }
        new MemberRight.InviteInGroup().tryAccess(calculateRole(this, invitation.getGroup()), Action.DELETE);
        invitation.refuse();
    }

    /**
     * To remove this member from a group you have to have the DELETED right on
     * the "group" property. If the member is not in the "group", nothing is
     * done. (Although it should be considered as an error and will be logged)
     *
     * @param group
     *            is the group from which the user will be removed.
     * @throws UnauthorizedOperationException
     */
    public void removeFromGroup(final Group group) throws UnauthorizedOperationException {
        new MemberRight.GroupList().tryAccess(calculateRole(this), Action.DELETE);
        getDao().removeFromGroup(group.getDao());
    }

    /**
     * To get the groups you have the have the READ right on the "group"
     * property.
     *
     * @return all the group in which this member is.
     * @throws UnauthorizedOperationException
     */
    public PageIterable<Group> getGroups() throws UnauthorizedOperationException {
        new MemberRight.GroupList().tryAccess(calculateRole(this), Action.READ);
        return new GroupList(getDao().getGroups());
    }

    public boolean canGetKarma() {
        return new MemberRight.Karma().canAccess(calculateRole(this), Action.READ);
    }

    public int getKarma() throws UnauthorizedOperationException {
        new MemberRight.Karma().tryAccess(calculateRole(this), Action.READ);
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

    public boolean canAccessName(final Action action) {
        return new MemberRight.Name().canAccess(calculateRole(this), action);
    }

    public String getDisplayName() throws UnauthorizedOperationException {
        new MemberRight.Name().tryAccess(calculateRole(this), Action.READ);
        if (getDao().getFullname() != null && getDao().getFullname().isEmpty()) {
            return getLogin();
        }
        return getFullname();
    }

    public String getFullname() throws UnauthorizedOperationException {
        new MemberRight.Name().tryAccess(calculateRole(this), Action.READ);
        return getDao().getFullname();
    }

    public void setFullname(final String fullname) throws UnauthorizedOperationException {
        new MemberRight.Name().tryAccess(calculateRole(this), Action.WRITE);
        getDao().setFullname(fullname);
    }

    public boolean canSetPassword() {
        return new MemberRight.Password().canAccess(calculateRole(this), Action.WRITE);
    }

    public void setPassword(final String password) throws UnauthorizedOperationException {
        new MemberRight.Password().tryAccess(calculateRole(this), Action.WRITE);
        getDao().setPassword(password);
    }

    public boolean canAccessLocale(final Action action) {
        return new MemberRight.Locale().canAccess(calculateRole(this), action);
    }

    public Locale getLocaleUnprotected() {
        return getDao().getLocale();
    }

    public Locale getLocale() throws UnauthorizedOperationException {
        new MemberRight.Locale().tryAccess(calculateRole(this), Action.READ);
        return getDao().getLocale();
    }

    public void setLocal(final Locale loacle) throws UnauthorizedOperationException {
        new MemberRight.Locale().tryAccess(calculateRole(this), Action.WRITE);
        getDao().setLocale(loacle);
    }

    public void setRole(Role role) {
        getDao().setRole(role);
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
     * Returns the status of the member in a given <code>group</code>
     *
     * @param group
     *            the group in whoch we want to know member status
     * @return a <code>Set</code> containing all the roles of the member for
     *         <code>group</code> or <code>null</code> if the member is not part
     *         of this group. <br />
     *         Note the set can be empty if the member has no preset role
     *         (standard member).
     */
    protected TeamRole getRoleUnprotected(final Group group) {
        return new TeamRole(group.getDao().getMemberStatus(getDao()));
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

    public Image getAvatar() {
        // TODO : Do it properly
        return new Image("none.png", Image.ImageType.LOCAL);
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



}
