package com.bloatit.framework;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.CommentList;
import com.bloatit.framework.lists.ContributionList;
import com.bloatit.framework.lists.OfferList;
import com.bloatit.model.data.DaoComment;
import com.bloatit.model.data.DaoDemand;
import com.bloatit.model.data.DaoKudosable;

public class Demand extends Kudosable {
    private DaoDemand dao;
    
    public static Demand create(DaoDemand dao) {
        if (dao == null) {
            return null;
        }
        return new Demand(dao);
    }


    private Demand(DaoDemand dao) {
        super();
        this.dao = dao;
    }

    public void addComment(String text) {
        dao.addComment(DaoComment.createAndPersist(getToken().getMember().getDao(), text));
    }

    public void addContribution(BigDecimal amount) throws Throwable {
        dao.addContribution(getToken().getMember().getDao(), amount);
    }

    public Offer addOffer(Description description, Date dateExpir) {
        return new Offer(dao.addOffer(getToken().getMember().getDao(), description.getDao(), dateExpir));
    }

    public void createSpecification(String content) {
        dao.createSpecification(getToken().getMember().getDao(), content);
    }

    public PageIterable<Comment> getComments() {
        return new CommentList(dao.getCommentsFromQuery());
    }

    public PageIterable<Contribution> getContributions() {
        return new ContributionList(dao.getContributionsFromQuery());
    }

    public DaoDemand getDao() {
        return dao;
    }

    @Override
    protected DaoKudosable getDaoKudosable() {
        return dao;
    }

    public Description getDescription() {
        return new Description(dao.getDescription());
    }

    public PageIterable<Offer> getOffers() {
        return new OfferList(dao.getOffersFromQuery());
    }

    public int getPopularity() {
        return dao.getPopularity();
    }

    public Specification getSpecification() {
        return new Specification(dao.getSpecification());
    }

    public String getTitle() {
        return getDescription().getDefaultTranslation().getTitle();
    }

    // TODO right management
    public void removeOffer(Offer offer) {
        dao.removeOffer(offer.getDao());
    }

}
