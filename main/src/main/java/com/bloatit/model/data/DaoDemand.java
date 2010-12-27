package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
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
import com.bloatit.model.data.util.SessionManager;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

/**
 * A DaoDemand is a kudosable content. It has a translatable description, and can have a
 * specification and some offers. The state of the demand is managed by its super class
 * DaoKudosable. On a demand we can add some comment and some contriutions.
 */
@Entity
@Indexed
public class DaoDemand extends DaoKudosable {

    /**
     * This is a calculated value with the sum of the value of all contributions.
     */
    @Basic(optional = false)
    private BigDecimal contribution;

    /**
     * A description is a translatable text with an title.
     */
    @OneToOne(optional = false)
    @Cascade(value = { CascadeType.ALL })
    @IndexedEmbedded
    private DaoDescription description;

    @OneToOne(mappedBy = "demand")
    @Cascade(value = { CascadeType.ALL })
    @IndexedEmbedded
    private DaoSpecification specification;

    @OneToMany(mappedBy = "demand")
    @Cascade(value = { CascadeType.ALL })
    @OrderBy(clause = "popularity desc")
    @IndexedEmbedded
    private Set<DaoOffer> offers = new HashSet<DaoOffer>(0);

    @OneToMany(mappedBy = "demand")
    @OrderBy(clause = "creationDate DESC")
    @Cascade(value = { CascadeType.ALL })
    private Set<DaoContribution> contributions = new HashSet<DaoContribution>(0);

    @OneToMany
    @Cascade(value = { CascadeType.ALL })
    // @OrderBy(clause = "creationDate DESC") // TODO find why this is not working
    @IndexedEmbedded
    private Set<DaoComment> comments = new HashSet<DaoComment>(0);

    /**
     * It is automatically in validated state (temporary)
     * 
     * @param member the author of the demand
     * @param description
     */
    public static DaoDemand createAndPersist(final DaoMember member, final DaoDescription Description) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoDemand Demand = new DaoDemand(member, Description);
        try {
            session.save(Demand);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return Demand;
    }

    /**
     * Create a DaoDemand and set it to the state validated.
     * 
     * @param member is the author of the demand
     * @param description is the description ...
     * @throws NullPointerException if any of the parameter is null.
     */
    protected DaoDemand(final DaoMember member, final DaoDescription description) {
        super(member);
        if (description == null) {
            throw new NullPointerException();
        }
        setState(State.VALIDATED);
        this.description = description;
        this.specification = null;
        this.contribution = BigDecimal.ZERO;
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
     * Create a specification.
     * 
     * @param member author (must be non null).
     * @param content a string contain the specification (WARNING : UNTESTED)(must be non
     *        null).
     */
    public void createSpecification(final DaoMember member, final String content) {
        specification = new DaoSpecification(member, content, this);
    }

    /**
     * Add a new offer for this demand.
     * 
     * @param member the author of the offer
     * @param amount the amount that the author want to make the offer
     * @param description this is a description of the offer
     * @param dateExpir this is when the offer should be finish ?
     * @return the newly created offer.
     */
    public DaoOffer addOffer(final DaoMember member, final BigDecimal amount, final DaoDescription description, final Date dateExpir) {
        final DaoOffer Offer = new DaoOffer(member, this, amount, description, dateExpir);
        offers.add(Offer);
        return Offer;
    }

    /**
     * delete offer from this demand AND FROM DB !
     * 
     * @param Offer the offer we want to delete.
     */
    public void removeOffer(final DaoOffer offer) {
        // TODO test me !
        offers.remove(offer);
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
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            Log.data().fatal("Cannot create a contribution with this amount " + amount.toEngineeringString() + " by member " + member.getId());
            throw new FatalErrorException("The amount of a contribution cannot be <= 0.", null);
        }
        if (comment.length() > 144) {
            Log.data().fatal("The comment of a contribution must be <= 144 chars long.");
            throw new FatalErrorException("Comments lenght of Contribution must be < 144.", null);
        }

        contributions.add(new DaoContribution(member, this, amount, comment));
        contribution = contribution.add(amount);
    }

    public DaoSpecification getSpecification() {
        return specification;
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
    public DaoOffer getCurrentOffer() {
        // TODO test me !

        // First try to find a validated offer.
        final String validatedQueriStr = "FROM DaoOffer " + //
                "WHERE demand = :this AND state = :state " + //
                "ORDER BY amount ASC, creationDate DESC ";

        final DaoOffer validatedOffer = (DaoOffer) SessionManager.createQuery(validatedQueriStr).setEntity("this", this)
                .setEntity("state", DaoKudosable.State.VALIDATED).iterate().next();
        if (validatedOffer != null) {
            return validatedOffer;
        }

        // If there is no validated offer then we try to find a pending offer
        final String queryString = "FROM DaoOffer " + //
                "WHERE demand = :this " + //
                "AND state == :state " + //
                "AND popularity = (select max(popularity) from DaoOffer where demand = :this) " + //
                "ORDER BY amount ASC, creationDate DESC";
        try {
            return (DaoOffer) SessionManager.createQuery(queryString).setEntity("this", this).setEntity("state", DaoKudosable.State.PENDING)
                    .iterate().next();
        } catch (final NoSuchElementException e) {
            return null;
        }
    }

    public Set<DaoOffer> getOffers() {
        return offers;
    }

    /**
     * Use a HQL query to get the contributions as a PageIterable collection
     */
    public PageIterable<DaoContribution> getContributionsFromQuery() {
        return new QueryCollection<DaoContribution>("from DaoContribution as f where f.demand = :this").setEntity("this", this);
    }

    public Set<DaoContribution> getContributions() {
        return contributions;
    }

    /**
     * Use a HQL query to get the first level comments as a PageIterable collection
     */
    public PageIterable<DaoComment> getCommentsFromQuery() {
        return new QueryCollection<DaoComment>(SessionManager.getSessionFactory().getCurrentSession().createFilter(getComments(), ""), SessionManager
                .getSessionFactory().getCurrentSession().createFilter(getComments(), "select count(*)"));
    }

    public Set<DaoComment> getComments() {
        return comments;
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

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected DaoDemand() {
        super();
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setSpecification(final DaoSpecification Specification) {
        specification = Specification;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setDescription(final DaoDescription Description) {
        description = Description;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setOffers(final Set<DaoOffer> Offers) {
        offers = Offers;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setContributions(final Set<DaoContribution> Contributions) {
        contributions = Contributions;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setComments(final Set<DaoComment> comments) {
        this.comments = comments;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setContribution(final BigDecimal contribution) {
        this.contribution = contribution;
    }
}
