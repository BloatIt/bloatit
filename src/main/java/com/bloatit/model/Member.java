package com.bloatit.model;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.CommentList;
import com.bloatit.framework.lists.ContributionList;
import com.bloatit.framework.lists.DemandList;
import com.bloatit.framework.lists.GroupList;
import com.bloatit.framework.lists.KudosList;
import com.bloatit.framework.lists.OfferList;
import com.bloatit.framework.lists.SpecificationList;
import com.bloatit.framework.lists.TranslationList;
import com.bloatit.model.data.DaoActor;
import com.bloatit.model.data.DaoMember;

public class Member extends Actor {

    private DaoMember dao;

    public Member(DaoMember dao) {
        super();
        this.dao = dao;
    }

    public String getFullName() {
        return getFirstname() + " " + getLastname();
    }

    protected DaoMember getDao() {
        return dao;
    }

    public void addToGroup(Group aGroup, boolean isAdmin) {
        dao.addToGroup(aGroup.getDao(), isAdmin);
    }

    public void removeFromGroup(Group aGroup) {
        dao.removeFromGroup(aGroup.getDao());
    }

    public PageIterable<Group> getGroups() {
        return new GroupList(dao.getGroups());
    }

    public int getKarma() {
        return dao.getKaram();
    }

    public void addToKarma(int value) {
        dao.addToKarama(value);
    }

    public String getFirstname() {
        return dao.getFirstname();
    }

    public void setFirstname(String firstname) {
        dao.setFirstname(firstname);
    }

    public String getPassword() {
        return dao.getPassword();
    }

    public void setPassword(String password) {
        dao.setPassword(password);
    }

    public String getLastname() {
        return dao.getLastname();
    }

    public void setLastname(String name) {
        dao.setLastname(name);
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
        return dao.isInGroup(group);
    }
    
    public boolean isInGroup(Group group) {
        return isInGroupUnprotected(group);
    }

}
