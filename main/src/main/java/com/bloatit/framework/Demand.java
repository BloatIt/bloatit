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

	public boolean canAccessComment(Action action) {
		return new DemandRight.Comment().canAccess(calculateRole(this), action);
	}

	public boolean canAccessContribution(Action action) {
		return new DemandRight.Contribute().canAccess(calculateRole(this), action);
	}

	public boolean canAccessOffer(Action action) {
		return new DemandRight.Offer().canAccess(calculateRole(this), action);
	}

	public void addComment(final String text) {
		new DemandRight.Comment().tryAccess(calculateRole(this), Action.WRITE);
		dao.addComment(DaoComment.createAndPersist(getToken().getMember().getDao(), text));
	}

	public void addContribution(final BigDecimal amount, final String comment) throws NotEnoughMoneyException {
		new DemandRight.Contribute().tryAccess(calculateRole(this), Action.WRITE);
		dao.addContribution(getToken().getMember().getDao(), amount, comment);
	}

	public Offer addOffer(final BigDecimal amount, final Locale locale, final String title, final String text, final Date dateExpir) {
		new DemandRight.Offer().tryAccess(calculateRole(this), Action.WRITE);
		return new Offer(dao.addOffer(getToken().getMember().getDao(), amount, new Description(getToken().getMember(), locale, title, text).getDao(),
				dateExpir));
	}

	public void createSpecification(final String content) {
		new DemandRight.Offer().tryAccess(calculateRole(this), Action.WRITE);
		dao.createSpecification(getToken().getMember().getDao(), content);
	}

	public PageIterable<Comment> getComments() {
		new DemandRight.Comment().tryAccess(calculateRole(this), Action.READ);
		return new CommentList(dao.getCommentsFromQuery());
	}

	public PageIterable<Contribution> getContributions() {
		new DemandRight.Contribute().tryAccess(calculateRole(this), Action.READ);
		return new ContributionList(dao.getContributionsFromQuery());
	}

	// TODO comment
	private static final int PROGRESSION_COEF = 42;
	private static final int PROGRESSION_CONTRIBUTION_DIVISOR = 200;
	private static final int PROGRESSION_PERCENT_MULTIPLICATOR = 100;

	/**
	 * Return the progression in percent. It compare the amount of contribution
	 * to the amount of the current offer.
	 * 
	 * @return a percentage. It can be > 100 if the amount of contributions is
	 *         greater than the amount for the current offer. If the offer
	 *         amount is 0 then it return Float.POSITIVE_INFINITY.
	 */
	public float getProgression() {
		new DemandRight.Contribute().tryAccess(calculateRole(this), Action.READ);
		if (dao.getOffers().isEmpty()) {
			return PROGRESSION_COEF * (1 - 1 / (1 + dao.getContribution().floatValue() / PROGRESSION_CONTRIBUTION_DIVISOR));
		} else {
			final DaoOffer currentOffer = dao.getCurrentOffer();
			if (currentOffer.getAmount().floatValue() != 0) {
				return (dao.getContribution().floatValue() * PROGRESSION_PERCENT_MULTIPLICATOR) / currentOffer.getAmount().floatValue();
			} else {
				return Float.POSITIVE_INFINITY;
			}
		}
	}

	public DaoDemand getDao() {
		return dao;
	}

	public BigDecimal getContribution() {
		new DemandRight.Contribute().tryAccess(calculateRole(this), Action.READ);
		return dao.getContribution();
	}

	public BigDecimal getContributionMax() {
		new DemandRight.Contribute().tryAccess(calculateRole(this), Action.READ);
		return dao.getContributionMax();
	}

	public BigDecimal getContributionMin() {
		new DemandRight.Contribute().tryAccess(calculateRole(this), Action.READ);
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
		new DemandRight.Offer().tryAccess(calculateRole(this), Action.READ);
		return new OfferList(dao.getOffersFromQuery());
	}

	/**
	 * The current offer is the offer with the max popularity then the min
	 * amount.
	 *
	 * @return the current offer for this demand, or null if there is no offer.
	 */
	public Offer getCurrentOffer() {
		return Offer.create(dao.getCurrentOffer());
	}

	public Specification getSpecification() {
		return new Specification(dao.getSpecification());
	}

	public String getTitle() {
		return getDescription().getDefaultTranslation().getTitle();
	}

	// TODO right management
	public void removeOffer(final Offer offer) {
		new DemandRight.Offer().tryAccess(calculateRole(this), Action.DELETE);
		dao.removeOffer(offer.getDao());
	}

	@Deprecated
	public boolean canContribute() {
		return new DemandRight.Contribute().canAccess(calculateRole(this), Action.WRITE);
	}

}
