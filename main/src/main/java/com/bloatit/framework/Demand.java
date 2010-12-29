package com.bloatit.framework;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.CommentList;
import com.bloatit.framework.lists.ContributionList;
import com.bloatit.framework.lists.OfferList;
import com.bloatit.framework.right.DemandRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoComment;
import com.bloatit.model.data.DaoDemand;
import com.bloatit.model.data.DaoDescription;
import com.bloatit.model.data.DaoKudosable;
import com.bloatit.model.data.DaoOffer;
import com.bloatit.model.data.util.SessionManager;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

public final class Demand extends Kudosable {
    private final DaoDemand dao;

    public static Demand create(final DaoDemand dao) {
        if (dao == null || !SessionManager.getSessionFactory().getCurrentSession().contains(dao)) {
            return null;
        }
        return new Demand(dao);
    }

    public Demand(final Member author, final Locale locale, final String title, final String description) {
        dao = DaoDemand.createAndPersist(author.getDao(), DaoDescription.createAndPersist(author.getDao(), locale, title, description));
    }

    private Demand(final DaoDemand dao) {
        super();
        this.dao = dao;
    }

    public void addComment(final String text) {
        dao.addComment(DaoComment.createAndPersist(getToken().getMember().getDao(), text));
    }

    public void addContribution(final BigDecimal amount, final String comment) throws NotEnoughMoneyException {
        // TODO : Vérifier que l'utilisateur a le droit de contribuer
        dao.addContribution(getToken().getMember().getDao(), amount, comment);
    }

    public Offer addOffer(final BigDecimal amount, final Locale locale, final String title, final String text, final Date dateExpir) {
        return new Offer(dao.addOffer(getToken().getMember().getDao(),
                amount,
                new Description(getToken().getMember(), locale, title, text).getDao(),
                dateExpir));
    }

    public void createSpecification(final String content) {
        dao.createSpecification(getToken().getMember().getDao(), content);
    }

    public PageIterable<Comment> getComments() {
        return new CommentList(dao.getCommentsFromQuery());
    }

    public PageIterable<Contribution> getContributions() {
        return new ContributionList(dao.getContributionsFromQuery());
    }

    /**
     * Return the progression in percent. It compare the amount of contribution to the
     * amount of the current offer.
     * 
     * @return a percentage. It can be > 100 if the amount of contributions is greater
     *         than the amount for the current offer. If the offer amount is 0 then it
     *         return Float.POSITIVE_INFINITY.
     */
    public float getProgression() {
        if (dao.getOffers().isEmpty()) {
            return 42 * (1 - 1 / (1 + dao.getContribution().floatValue() / 200));
        } else {
            final DaoOffer currentOffer = dao.getCurrentOffer();
            if (currentOffer.getAmount().floatValue() != 0) {
                return (dao.getContribution().floatValue() * 100) / currentOffer.getAmount().floatValue();
            } else {
                return Float.POSITIVE_INFINITY;
            }
        }
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

    public Specification getSpecification() {
        return new Specification(dao.getSpecification());
    }

    public String getTitle() {
        return getDescription().getDefaultTranslation().getTitle();
    }

    // TODO right management
    public void removeOffer(final Offer offer) {
        dao.removeOffer(offer.getDao());
    }

    public boolean canContribute() {
        return new DemandRight.Contribute().canAccess(calculateRole(this), Action.WRITE);
    }

}
