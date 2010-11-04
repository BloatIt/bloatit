package com.bloatit.framework;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.CommentList;
import com.bloatit.framework.lists.ContributionList;
import com.bloatit.framework.lists.OfferList;
import com.bloatit.model.data.DaoComment;
import com.bloatit.model.data.DaoDemand;
import com.bloatit.model.data.DaoDescription;
import com.bloatit.model.data.DaoKudosable;
import com.bloatit.model.data.util.SessionManager;

public class Demand extends Kudosable {
    private final DaoDemand dao;

    public static Demand create(DaoDemand dao) {
        if (dao == null || !SessionManager.getSessionFactory().getCurrentSession().contains(dao)) {
            return null;
        }
        return new Demand(dao);
    }

    public Demand(Member member, Locale locale, String title, String description) {
        dao = DaoDemand.createAndPersist(member.getDao(), new DaoDescription(member.getDao(), locale, title, description));
    }

    private Demand(DaoDemand dao) {
        super();
        this.dao = dao;
    }

    public void addComment(String text) {
        dao.addComment(DaoComment.createAndPersist(getToken().getMember().getDao(), text));
    }

    public void addContribution(BigDecimal amount, String comment) throws Throwable {
        dao.addContribution(getToken().getMember().getDao(), amount, comment);
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

    public BigDecimal getContribution() {
        return dao.getContribution();
    }

    public BigDecimal getContributionMax() {
        return dao.getContributionMax();
    }

    public BigDecimal getContributionMin() {
        return dao.getContributionMin();
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

    @Override
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
