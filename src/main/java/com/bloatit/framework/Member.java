package com.bloatit.framework;

import com.bloatit.common.PageIterable;
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
import com.bloatit.model.data.DaoMember;

public class Member extends Actor {

    private DaoMember dao;

    public static Member create(DaoMember dao){
        if (dao == null){
            return null;
        }
        return new Member(dao);
    }
    
    private Member(DaoMember dao) {
        super();
        this.dao = dao;
    }

    public boolean canAccessGroups(Action action) {
        return new MemberRight.GroupList().canAccess(calculateRole(this), action);
    }

    public void addToGroup(Group aGroup, boolean isAdmin) {
        new MemberRight.GroupList().tryAccess(calculateRole(this), Action.WRITE);
        dao.addToGroup(aGroup.getDao(), isAdmin);
    }

    public void removeFromGroup(Group aGroup) {
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

    public boolean canAccessName(Action action) {
        return new MemberRight.Name().canAccess(calculateRole(this), action);
    }

    public String getFullname() {
        new MemberRight.Name().tryAccess(calculateRole(this), Action.READ);
        return dao.getFullname();
    }

    public void setFullname(String fullname) {
        new MemberRight.Name().tryAccess(calculateRole(this), Action.WRITE);
        dao.setFullname(fullname);
    }

    public boolean canSetPassword() {
        return new MemberRight.Password().canAccess(calculateRole(this), Action.WRITE);
    }

    public void setPassword(String password) {
        new MemberRight.Password().tryAccess(calculateRole(this), Action.WRITE);
        dao.setPassword(password);
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

    @Override
    protected DaoActor getDaoActor() {
        return dao;
    }

    protected boolean isInGroupUnprotected(Group group) {
        return dao.isInGroup(group.getDao());
    }

    public boolean isInGroup(Group group) {
        return isInGroupUnprotected(group);
    }

    protected DaoMember getDao() {
        return dao;
    }

    protected void addToKarma(int value) {
        dao.addToKarma(value);
    }

    protected String getPassword() {
        return dao.getPassword();
    }
}
