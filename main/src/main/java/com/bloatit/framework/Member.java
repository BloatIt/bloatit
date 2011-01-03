package com.bloatit.framework;

import java.util.Locale;

import com.bloatit.common.Image;
import com.bloatit.common.PageIterable;
import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.lists.CommentList;
import com.bloatit.framework.lists.ContributionList;
import com.bloatit.framework.lists.DemandList;
import com.bloatit.framework.lists.GroupList;
import com.bloatit.framework.lists.KudosList;
import com.bloatit.framework.lists.OfferList;
import com.bloatit.framework.lists.SpecificationList;
import com.bloatit.framework.lists.TranslationList;
import com.bloatit.framework.right.MemberRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoActor;
import com.bloatit.model.data.DaoGroup.MemberStatus;
import com.bloatit.model.data.DaoGroup.Right;
import com.bloatit.model.data.DaoJoinGroupInvitation;
import com.bloatit.model.data.DaoMember;
import com.bloatit.model.data.DaoMember.Role;

public final class Member extends Actor {

    private final DaoMember dao;

    public static Member create(final DaoMember dao) {
        if (dao == null) {
            return null;
        }
        return new Member(dao);
    }

    private Member(final DaoMember dao) {
        super();
        this.dao = dao;
    }

    public boolean canAccessGroups(final Action action) {
        return new MemberRight.GroupList().canAccess(calculateRole(this), action);
    }

    public void addToPublicGroup(final Group group) {
        if (group.getRight() != Right.PUBLIC) {
            throw new UnauthorizedOperationException();
        }
        new MemberRight.GroupList().tryAccess(calculateRole(this), Action.WRITE);
        dao.addToGroup(group.getDao(), false);
    }

    /**
     * @param group the group in which you want to invite somebody
     * @param action write for create a new invitation and read to accept/refuse it
     * @return true if you can invite/accept/refuse.
     */
    public boolean canInvite(final Group group, final Action action) {
        return new MemberRight.InviteInGroup().canAccess(calculateRole(this, group), action);
    }

    public void invite(final Member member, final Group group) {
        new MemberRight.InviteInGroup().tryAccess(calculateRole(this, group), Action.WRITE);
        DaoJoinGroupInvitation.createAndPersist(getDao(), member.getDao(), group.getDao());
    }

    public void acceptInvitation(final JoinGroupInvitation invitation) {
        invitation.authenticate(getToken());
        invitation.accept();
    }

    public void refuseInvitation(final JoinGroupInvitation demand) {
        new MemberRight.InviteInGroup().tryAccess(calculateRole(this, demand.getGroup()), Action.READ);
        demand.refuse();
    }

    public void removeFromGroup(final Group aGroup) {
        new MemberRight.GroupList().tryAccess(calculateRole(this), Action.DELETE);
        dao.removeFromGroup(aGroup.getDao());
    }

    public PageIterable<Group> getGroups() {
        new MemberRight.GroupList().tryAccess(calculateRole(this), Action.READ);
        return new GroupList(dao.getGroups());
    }

    public boolean canGetKarma() {
        return new MemberRight.Karma().canAccess(calculateRole(this), Action.READ);
    }

    public int getKarma() {
        new MemberRight.Karma().tryAccess(calculateRole(this), Action.READ);
        return dao.getKarma();
    }

    private static final int INFLUENCE_MULTIPLICATOR = 10;

    protected int calculateInfluence() {
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

    public String getFullname() {
        new MemberRight.Name().tryAccess(calculateRole(this), Action.READ);
        return dao.getFullname();
    }

    public void setFullname(final String fullname) {
        new MemberRight.Name().tryAccess(calculateRole(this), Action.WRITE);
        dao.setFullname(fullname);
    }

    public boolean canSetPassword() {
        return new MemberRight.Password().canAccess(calculateRole(this), Action.WRITE);
    }

    public void setPassword(final String password) {
        new MemberRight.Password().tryAccess(calculateRole(this), Action.WRITE);
        dao.setPassword(password);
    }

    public boolean canAccessLocale(final Action action) {
        return new MemberRight.Locale().canAccess(calculateRole(this), action);
    }

    public Locale getLocal() {
        new MemberRight.Locale().tryAccess(calculateRole(this), Action.READ);
        return dao.getLocale();
    }

    public void setLocal(final Locale loacle) {
        new MemberRight.Locale().tryAccess(calculateRole(this), Action.WRITE);
        dao.setLocale(loacle);
    }

    public PageIterable<Demand> getDemands() {
        return new DemandList(dao.getDemands());
    }

    public PageIterable<Kudos> getKudos() {
        return new KudosList(dao.getKudos());
    }

    public PageIterable<Specification> getSpecifications() {
        return new SpecificationList(dao.getSpecifications());
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
