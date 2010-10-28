package com.bloatit.framework;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.CommentList;
import com.bloatit.framework.lists.ContributionList;
import com.bloatit.framework.lists.OfferList;
import com.bloatit.model.data.DaoDemand;
import com.bloatit.model.data.DaoKudosable;

public class Demand extends Kudosable {
    private DaoDemand dao;

    public Demand(DaoDemand dao) {
        super();
        this.dao = dao;
    }

    public DaoDemand getDao() {
        return dao;
    }
    
    public String getTitle(){
        return getDescription().getDefaultTranslation().getTitle();
    }

    public void createSpecification(Member member, String content) {
        dao.createSpecification(member.getDao(), content);
    }

    public Offer addOffer(Member member, Description description, Date dateExpir) {
        return new Offer(dao.addOffer(member.getDao(), description.getDao(), dateExpir));
    }

    public void removeOffer(Offer offer) {
        dao.removeOffer(offer.getDao());
    }

    public void addContribution(Member member, BigDecimal amount) throws Throwable {
        dao.addContribution(member.getDao(), amount);
    }

    public Specification getSpecification() {
        return new Specification(dao.getSpecification());
    }

    public Description getDescription() {
        return new Description(dao.getDescription());
    }

    public PageIterable<Offer> getOffers() {
        return new OfferList(dao.getOffersFromQuery());
    }

    public PageIterable<Contribution> getContributions() {
        return new ContributionList(dao.getContributionsFromQuery());
    }

    public PageIterable<Comment> getComments() {
        return new CommentList(dao.getCommentsFromQuery());
    }

    public void addComment(Comment comment) {
        dao.addComment(comment.getDao());
    }

    @Override
    protected DaoKudosable getDaoKudosable() {
        return dao;
    }

}
