package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
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
import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.SessionManager;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

/**
 * A DaoDemand is a kudosable content. It has a translatable description, and can have a
 * specification and some offers.
 * 
 * The state of the demand is managed bu its super class DaoKudosable. On a demand we can
 * add some comment and some contriutions.
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

    // TODO make sure it is read only !
    @OneToMany(mappedBy = "demand")
    @OrderBy(clause = "creationDate desc")
    @Cascade(value = { CascadeType.ALL })
    private Set<DaoContribution> contributions = new HashSet<DaoContribution>(0);

    @OneToMany
    @Cascade(value = { CascadeType.ALL })
    // @OrderBy(clause = "creationDate desc") // TODO find how to make this
    // works
    @IndexedEmbedded
    private Set<DaoComment> comments = new HashSet<DaoComment>(0);

    /**
     * It is automatically in validated state (temporary)
     * 
     * @param member the author of the demand
     * @param description
     */
    public static DaoDemand createAndPersist(DaoMember member, DaoDescription Description) {
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
     *  // TODO FINISH ME
     * @param member
     * @param description
     * @throws NullPointerException if any of the parameter is null.
     */
    protected DaoDemand(DaoMember member, DaoDescription description) {
        super(member);
        if (description == null) {
            throw new NullPointerException();
        }
        setState(State.VALIDATED);
        this.description = description;
        this.specification = null;
        this.contribution = new BigDecimal("0");
    }

    protected DaoDemand() {
        super();
    }

    public void delete() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        session.delete(this);
    }

    public void createSpecification(DaoMember member, String content) {
        specification = new DaoSpecification(member, content, this);
    }

    public DaoOffer addOffer(DaoMember member, BigDecimal amount, DaoDescription description, Date dateExpir) {
        final DaoOffer Offer = new DaoOffer(member, this, amount, description, dateExpir);
        offers.add(Offer);
        return Offer;
    }

    /**
     * delete offer from this demand AND FROM DB !
     * 
     * @param Offer the offer we want to delete.
     */
    public void removeOffer(DaoOffer Offer) {
        offers.remove(Offer);
    }

    /**
     * Add a contribution to a demand.
     * 
     * @param member the author of the contribution
     * @param amount the > 0 amount of euros on this contribution
     * @param comment a <= 144 char comment on this contribution
     * @throws NotEnoughMoneyException
     */
    public void addContribution(DaoMember member, BigDecimal amount, String comment) throws NotEnoughMoneyException {
        if (amount.compareTo(new BigDecimal("0")) <= 0) {
            throw new FatalErrorException("The amount of a contribution cannot be <= 0.", null);
        }
        if (comment.length() > 144) {
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

    public PageIterable<DaoOffer> getOffersFromQuery() {
        return new QueryCollection<DaoOffer>("from DaoOffer as f where f.demand = :this").setEntity("this", this);
    }

    public Set<DaoOffer> getOffers() {
        return offers;
    }

    public PageIterable<DaoContribution> getContributionsFromQuery() {
        return new QueryCollection<DaoContribution>("from DaoContribution as f where f.demand = :this").setEntity("this", this);
    }

    public Set<DaoContribution> getContributions() {
        return contributions;
    }

    public PageIterable<DaoComment> getCommentsFromQuery() {
        return new QueryCollection<DaoComment>(SessionManager.getSessionFactory().getCurrentSession().createFilter(getComments(), ""),
                                               SessionManager.getSessionFactory().getCurrentSession().createFilter(getComments(), "select count(*)"));
    }

    public Set<DaoComment> getComments() {
        return comments;
    }

    public void addComment(DaoComment comment) {
        comments.add(comment);
    }

    public BigDecimal getContribution() {
        return contribution;
    }

    public BigDecimal getContributionMin() {
        return (BigDecimal) SessionManager.createQuery("select min(f.amount) from DaoContribution as f where f.demand = :this")
                                          .setEntity("this", this)
                                          .uniqueResult();
    }

    public BigDecimal getContributionMax() {
        return (BigDecimal) SessionManager.createQuery("select max(f.amount) from DaoContribution as f where f.demand = :this")
                                          .setEntity("this", this)
                                          .uniqueResult();
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setSpecification(DaoSpecification Specification) {
        specification = Specification;
    }

    protected void setDescription(DaoDescription Description) {
        description = Description;
    }

    protected void setOffers(Set<DaoOffer> Offers) {
        offers = Offers;
    }

    protected void setContributions(Set<DaoContribution> Contributions) {
        contributions = Contributions;
    }

    protected void setComments(Set<DaoComment> comments) {
        this.comments = comments;
    }

    protected void setContribution(BigDecimal contribution) {
        this.contribution = contribution;
    }

}
