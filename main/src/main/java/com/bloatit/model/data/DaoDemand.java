package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OrderBy;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.bloatit.common.FatalErrorException;
import com.bloatit.common.Log;
import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.NonOptionalParameterException;
import com.bloatit.model.data.util.SessionManager;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

/**
 * A DaoDemand is a kudosable content. It has a translatable description, and can have a
 * specification and some offers. The state of the demand is managed by its super class
 * DaoKudosable. On a demand we can add some comment and some contriutions.
 */
@Entity
@Indexed
public final class DaoDemand extends DaoKudosable {

    /**
     * This is the state of the demand. It's used in the workflow modeling. The order is
     * important !
     */
    public enum DemandState {
        PENDING, PREPARING, DEVELOPPING, INCOME, DISCARDED, FINISHED
    }

    /**
     * This is a calculated value with the sum of the value of all contributions.
     */
    @Basic(optional = false)
    private BigDecimal contribution;

    @Basic(optional = false)
    @Enumerated
    private DemandState demandState;

    /**
     * A description is a translatable text with an title.
     */
    @OneToOne(optional = false)
    @Cascade(value = { CascadeType.ALL })
    @IndexedEmbedded
    private DaoDescription description;

    @OneToMany(mappedBy = "demand")
    @Cascade(value = { CascadeType.ALL })
    @OrderBy(clause = "popularity desc")
    @IndexedEmbedded
    private final Set<DaoOffer> offers = new HashSet<DaoOffer>(0);

    @OneToMany(mappedBy = "demand")
    @OrderBy(clause = "creationDate DESC")
    @Cascade(value = { CascadeType.ALL })
    private final Set<DaoContribution> contributions = new HashSet<DaoContribution>(0);

    @OneToMany
    @Cascade(value = { CascadeType.ALL })
    @IndexedEmbedded
    private final Set<DaoComment> comments = new HashSet<DaoComment>(0);

    /**
     * The selected offer is the offer that is most likely to be validated and used. If an
     * offer is selected and has enough money and has a elapse time done then this offer
     * go into dev.
     */
    @ManyToOne(optional = true)
    @Cascade(value = { CascadeType.ALL })
    @IndexedEmbedded
    private DaoOffer selectedOffer;

    @Basic(optional = true)
    private Date validationDate;

    /**
     * @see #DaoDemand(DaoMember, DaoDescription)
     */
    public static DaoDemand createAndPersist(final DaoMember member, final DaoDescription description) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoDemand demand = new DaoDemand(member, description);
        try {
            session.save(demand);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return demand;
    }

    /**
     * @see #DaoDemand(DaoMember, DaoDescription, DaoOffer)
     */
    public static DaoDemand createAndPersist(final DaoMember member, final DaoDescription description, final DaoOffer offer) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoDemand demand = new DaoDemand(member, description, offer);
        try {
            session.save(demand);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return demand;
    }

    /**
     * Create a DaoDemand and set its state to the state PENDING.
     *
     * @param member is the author of the demand
     * @param description is the description ...
     * @throws NonOptionalParameterException if any of the parameter is null.
     */
    private DaoDemand(final DaoMember member, final DaoDescription description) {
        super(member);
        if (description == null) {
            throw new NonOptionalParameterException();
        }
        this.description = description;
        this.validationDate = null;
        setSelectedOffer(null);
        this.contribution = BigDecimal.ZERO;
        setDemandState(DemandState.PENDING);
    }

    /**
     * Create a DaoDemand, add an offer and set its state to the state
     * {@link DemandState#PREPARING}.
     *
     * @param member is the author of the demand
     * @param description is the description ...
     * @param offer
     * @throws NonOptionalParameterException if any of the parameter is null.
     */
    private DaoDemand(final DaoMember member, final DaoDescription description, final DaoOffer offer) {
        this(member, description);
        if (offer == null) {
            throw new NonOptionalParameterException();
        }
        this.offers.add(offer);
        setDemandState(DemandState.PREPARING);
    }

    /**
     * Delete this DaoDemand from the database. "this" will remain, but unmapped. (You
     * shoudn't use it then)
     */
    public void delete() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        session.delete(this);
    }

    /**
     * Add a new offer for this demand.
     */
    public void addOffer(DaoOffer offer) {
        offers.add(offer);
    }

    /**
     * delete offer from this demand AND FROM DB !
     *
     * @param Offer the offer we want to delete.
     */
    public void removeOffer(final DaoOffer offer) {
        offers.remove(offer);
        if (offer.equals(selectedOffer)) {
            selectedOffer = null;
        }
        SessionManager.getSessionFactory().getCurrentSession().delete(offer);
    }

    /**
     * Add a contribution to a demand.
     *
     * @param member the author of the contribution
     * @param amount the > 0 amount of euros on this contribution
     * @param comment a <= 144 char comment on this contribution
     * @throws NotEnoughMoneyException
     */
    public void addContribution(final DaoMember member, final BigDecimal amount, final String comment) throws NotEnoughMoneyException {
        if (amount == null) {
            throw new NonOptionalParameterException();
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            Log.data().fatal("Cannot create a contribution with this amount " + amount.toEngineeringString() + " by member " + member.getId());
            throw new FatalErrorException("The amount of a contribution cannot be <= 0.", null);
        }
        if (comment != null && comment.length() > DaoContribution.COMMENT_MAX_LENGTH) {
            Log.data().fatal("The comment of a contribution must be <= 144 chars long.");
            throw new FatalErrorException("Comments lenght of Contribution must be < 144.", null);
        }

        contributions.add(new DaoContribution(member, this, amount, comment));
        contribution = contribution.add(amount);
    }

    public DaoDescription getDescription() {
        return description;
    }

    /**
     * Use a HQL query to get the offers as a PageIterable collection
     */
    public PageIterable<DaoOffer> getOffersFromQuery() {
        return new QueryCollection<DaoOffer>("from DaoOffer as f where f.demand = :this").setEntity("this", this);
    }

    /**
     * The current offer is the offer with the max popularity then the min amount.
     *
     * @return the current offer for this demand, or null if there is no offer.
     */
    private DaoOffer getCurrentOffer() {
        // If there is no validated offer then we try to find a pending offer
        final String queryString = "FROM DaoOffer " + //
                "WHERE demand = :this " + //
                "AND state <= :state " + // <= pending means also validated.
                "AND popularity = (select max(popularity) from DaoOffer where demand = :this) " + //
                "ORDER BY amount ASC, creationDate DESC";
        try {
            return (DaoOffer) SessionManager.createQuery(queryString).setEntity("this", this).setParameter("state", DaoKudosable.State.PENDING)
                    .iterate().next();
        } catch (final NoSuchElementException e) {
            return null;
        }
    }

    public Set<DaoOffer> getOffers() {
        return offers;
    }

    public void setDemandState(final DemandState demandState) {
        this.demandState = demandState;
    }

    public DemandState getDemandState() {
        return demandState;
    }

    /**
     * Use a HQL query to get the contributions as a PageIterable collection
     */
    public PageIterable<DaoContribution> getContributionsFromQuery() {
        return new QueryCollection<DaoContribution>("from DaoContribution as f where f.demand = :this").setEntity("this", this);
    }

    /**
     * Use a HQL query to get the first level comments as a PageIterable collection
     */
    public PageIterable<DaoComment> getCommentsFromQuery() {
        return new QueryCollection<DaoComment>(SessionManager.getSessionFactory().getCurrentSession().createFilter(comments, ""), SessionManager
                .getSessionFactory().getCurrentSession().createFilter(comments, "select count(*)"));
    }

    public DaoOffer getSelectedOffer() {
        return selectedOffer;
    }

    public void setSelectedOffer(final DaoOffer selectedOffer) {
        this.selectedOffer = selectedOffer;
    }

    public void addComment(final DaoComment comment) {
        comments.add(comment);
    }

    public BigDecimal getContribution() {
        return contribution;
    }

    /**
     * @return the minimum value of the contribution on this demand.
     */
    public BigDecimal getContributionMin() {
        return (BigDecimal) SessionManager.createQuery("select min(f.amount) from DaoContribution as f where f.demand = :this")
                .setEntity("this", this).uniqueResult();
    }

    /**
     * @return the maximum value of the contribution on this demand.
     */
    public BigDecimal getContributionMax() {
        return (BigDecimal) SessionManager.createQuery("select max(f.amount) from DaoContribution as f where f.demand = :this")
                .setEntity("this", this).uniqueResult();
    }

    public void computeSelectedOffer() {
        selectedOffer = getCurrentOffer();
    }

    public void setValidationDate(Date validationDate) {
        this.validationDate = validationDate;
    }

    public Date getValidationDate() {
        return validationDate;
    }

    /**
     * Called by contribution when canceled.
     *
     * @param amount
     */
    void cancelContribution(BigDecimal amount) {
        this.contribution = this.contribution.subtract(amount);
    }

    public void validateContributions(int percent) {
        if (selectedOffer == null) {
            throw new FatalErrorException("The selectedOffer shouldn't be null here !");
        }
        if (percent == 0) {
            return;
        }
        for (DaoContribution contribution : getContributionsFromQuery()) {
            try {
                if (contribution.getState() == DaoContribution.State.PENDING) {
                    contribution.validate(selectedOffer, percent);
                }
            } catch (NotEnoughMoneyException e) {
                Log.data().fatal(e);
            }
        }
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoDemand() {
        super();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DaoDemand other = (DaoDemand) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        return true;
    }
}
