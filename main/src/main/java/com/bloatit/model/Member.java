package com.bloatit.model;

import java.util.Locale;

import com.bloatit.data.DaoActor;
import com.bloatit.data.DaoGroup.MemberStatus;
import com.bloatit.data.DaoGroup.Right;
import com.bloatit.data.DaoJoinGroupInvitation;
import com.bloatit.data.DaoJoinGroupInvitation.State;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException.SpecialCode;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.demand.Demand;
import com.bloatit.model.demand.DemandList;
import com.bloatit.model.lists.CommentList;
import com.bloatit.model.lists.ContributionList;
import com.bloatit.model.lists.GroupList;
import com.bloatit.model.lists.KudosList;
import com.bloatit.model.lists.OfferList;
import com.bloatit.model.lists.TranslationList;
import com.bloatit.model.right.MemberRight;
import com.bloatit.model.right.RightManager.Action;

public final class Member extends Actor {
    private final DaoMember dao;

    /**
     * Create a new member using its Dao version.
     * 
     * @param dao a DaoMember
     * @return the new member or null if dao is null.
     */
    public static Member create(final DaoMember dao) {
        if (dao == null) {
            return null;
        }
        return new Member(dao);
    }

    public Member(final String login, final String password, final String email, final Locale locale) {
        super();
        dao = DaoMember.createAndPersist(login, password, email, locale);
    }

    private Member(final DaoMember dao) {
        super();
        this.dao = dao;
    }

    /**
     * Tells if a user can access the group property. You have to unlock this Member using
     * the {@link Member#authenticate(AuthToken)} method.
     * 
     * @param action can be read/write/delete. for example use READ to know if you can use
     *        {@link Member#getGroups()}.
     * @return true if you can use the method.
     */
    public boolean canAccessGroups(final Action action) {
        return new MemberRight.GroupList().canAccess(calculateRole(this), action);
    }

    /**
     * To add a user into a public group, you have to make sure you can access the groups
     * with the {@link Action#WRITE} action.
     * 
     * @param group must be a public group.
     * @throws UnauthorizedOperationException if the authenticated member do not have the
     *         right to use this methods.
     * @see Member#canAccessGroups(Action)
     */
    public void addToPublicGroup(final Group group) throws UnauthorizedOperationException {
        if (group.getRight() != Right.PUBLIC) {
            throw new UnauthorizedOperationException(SpecialCode.GROUP_NOT_PUBLIC);
        }
        new MemberRight.GroupList().tryAccess(calculateRole(this), Action.WRITE);
        dao.addToGroup(group.getDao(), false);
    }

    /**
     * Tells if a user can access the property "invite".
     * 
     * @param group the group in which you want to invite somebody
     * @param action WRITE for create a new invitation, DELETE to accept/refuse it, READ
     *        to list the invitations you have recieved.
     * @return true if you can invite/accept/refuse.
     */
    public boolean canInvite(final Group group, final Action action) {
        return new MemberRight.InviteInGroup().canAccess(calculateRole(this, group), action);
    }

    /**
     * To invite a member into a group you have to have the WRITE right on the "invite"
     * property.
     * 
     * @param member The member you want to invite
     * @param group The group in which you invite a member.
     * @throws UnauthorizedOperationException
     */
    public void invite(final Member member, final Group group) throws UnauthorizedOperationException {
        new MemberRight.InviteInGroup().tryAccess(calculateRole(this, group), Action.WRITE);
        DaoJoinGroupInvitation.createAndPersist(getDao(), member.getDao(), group.getDao());
    }

    /**
     * @param state can be PENDING, ACCEPTED or REFUSED
     * @return all the received invitation with the specified state.
     */
    public PageIterable<DaoJoinGroupInvitation> getReceivedInvitation(final State state) {
        return dao.getReceivedInvitation(state);
    }

    /**
     * @param state can be PENDING, ACCEPTED or REFUSED
     * @return all the sent invitation with the specified state.
     */
    public PageIterable<DaoJoinGroupInvitation> getSentInvitation(final State state) {
        return dao.getSentInvitation(state);
    }

    /**
     * To accept an invitation you must have the DELETE right on the "invite" property. If
     * the invitation is not in PENDING state then nothing is done.
     * 
     * @param invitation the authenticate member must be receiver of the invitation.
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
     * To refuse an invitation you must have the DELETE right on the "invite" property. If
     * the invitation is not in PENDING state then nothing is done.
     * 
     * @param invitation the authenticate member must be receiver of the invitation.
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
     * To remove this member from a group you have to have the DELETE right on the "group"
     * property. If the member is not in the "group", nothing is done. (Although it should
     * be considered as an error and will be logged)
     * 
     * @param group is the group from which the user will be removed.
     * @throws UnauthorizedOperationException
     */
    public void removeFromGroup(final Group group) throws UnauthorizedOperationException {
        new MemberRight.GroupList().tryAccess(calculateRole(this), Action.DELETE);
        dao.removeFromGroup(group.getDao());
    }

    /**
     * To get the groups you have the have the READ right on the "group" property.
     * 
     * @return all the group in which this member is.
     * @throws UnauthorizedOperationException
     */
    public PageIterable<Group> getGroups() throws UnauthorizedOperationException {
        new MemberRight.GroupList().tryAccess(calculateRole(this), Action.READ);
        return new GroupList(dao.getGroups());
    }

    public boolean canGetKarma() {
        return new MemberRight.Karma().canAccess(calculateRole(this), Action.READ);
    }

    public int getKarma() throws UnauthorizedOperationException {
        new MemberRight.Karma().tryAccess(calculateRole(this), Action.READ);
        return dao.getKarma();
    }

    private static final int INFLUENCE_MULTIPLICATOR = 10;

    protected int calculateInfluence() throws UnauthorizedOperationException {
        final int karma = getKarma();
        if (karma > 0) {
            return (int) (Math.log10(karma) * INFLUENCE_MULTIPLICATOR + 1);
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
        if (dao.getFullname() != null && dao.getFullname().isEmpty()) {
            return getLogin();
        }
        return getFullname();
    }

    public String getFullname() throws UnauthorizedOperationException {
        new MemberRight.Name().tryAccess(calculateRole(this), Action.READ);
        return dao.getFullname();
    }

    public void setFullname(final String fullname) throws UnauthorizedOperationException {
        new MemberRight.Name().tryAccess(calculateRole(this), Action.WRITE);
        dao.setFullname(fullname);
    }

    public boolean canSetPassword() {
        return new MemberRight.Password().canAccess(calculateRole(this), Action.WRITE);
    }

    public void setPassword(final String password) throws UnauthorizedOperationException {
        new MemberRight.Password().tryAccess(calculateRole(this), Action.WRITE);
        dao.setPassword(password);
    }

    public boolean canAccessLocale(final Action action) {
        return new MemberRight.Locale().canAccess(calculateRole(this), action);
    }

    public Locale getLocaleUnprotected() {
        return dao.getLocale();
    }

    public Locale getLocale() throws UnauthorizedOperationException {
        new MemberRight.Locale().tryAccess(calculateRole(this), Action.READ);
        return dao.getLocale();
    }

    public void setLocal(final Locale loacle) throws UnauthorizedOperationException {
        new MemberRight.Locale().tryAccess(calculateRole(this), Action.WRITE);
        dao.setLocale(loacle);
    }

    public PageIterable<Demand> getDemands() {
        return new DemandList(dao.getDemands());
    }

    public PageIterable<Kudos> getKudos() {
        return new KudosList(dao.getKudos());
    }

    public PageIterable<Contribution> getContributions() {
        return new ContributionList(dao.getTransactions());
    }

    public PageIterable<Comment> getComments() {
        return new CommentList(dao.getComments());
    }

    public PageIterable<Offer> getOffers() {
        return new OfferList(dao.getOffers());
    }

    public PageIterable<Translation> getTranslations() {
        return new TranslationList(dao.getTranslations());
    }

    public boolean isInGroup(final Group group) {
        return isInGroupUnprotected(group);
    }

    @Override
    protected DaoActor getDaoActor() {
        return dao;
    }

    protected MemberStatus getStatusUnprotected(final Group group) {
        return group.getDao().getMemberStatus(dao);
    }

    protected boolean isInGroupUnprotected(final Group group) {
        return dao.isInGroup(group.getDao());
    }

    @Override
    public DaoMember getDao() {
        return dao;
    }

    protected void addToKarma(final int value) {
        dao.addToKarma(value);
    }

    protected String getPassword() {
        return dao.getPassword();
    }

    public Role getRole() {
        return dao.getRole();
    }

    public Image getAvatar() {
        // TODO : Do it properly
        return new Image("none.png", Image.ImageType.LOCAL);
    }
}
