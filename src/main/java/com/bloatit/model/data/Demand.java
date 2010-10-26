package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OrderBy;

import com.bloatit.model.data.util.SessionManger;

/**
 * 
 */
@Entity
public class Demand extends Kudosable {

    @OneToOne(optional = false)
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private Description description;

    @OneToOne(mappedBy = "demand", optional = true)
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private Specification specification;

    @OneToMany(mappedBy = "demand")
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    @OrderBy(clause = "Offer.popularity")
    // TODO test me !
    private Set<Offer> offers = new HashSet<Offer>(0);

    // TODO make sure it is read only !
    @OneToMany(mappedBy = "demand")
    @Cascade(value = { CascadeType.ALL })
    private Set<Contribution> contributions = new HashSet<Contribution>(0);

    /**
     * It is automatically in validated state (temporary)
     * 
     * @param actor the author of the demand
     * @param description
     */
    public static Demand createAndPersist(Actor actor, Description description) {
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        Demand demand = new Demand(actor, description);
        try {
            session.save(demand);
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return demand;
    }

    protected Demand(Actor actor, Description description) {
        super(actor);
        setValidated();
        this.description = description;
        this.specification = null;
    }

    protected Demand() {
        super();
    }

    public void delete() {
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        session.delete(this);
    }

    public void createSpecification(Member member, String content) {
        specification = new Specification(member, content, this);
    }

    public Offer addOffer(Member author, Description description, Date dateExpir) {
        Offer offer = new Offer(author, this, description, dateExpir);
        offers.add(offer);
        return offer;
    }

    /**
     * delete offer from this demand AND FROM DB !
     * 
     * @param offer the offer we want to delete.
     */
    public void removeOffer(Offer offer) {
        offers.remove(offer);
    }

    // TODO create a Throwable type
    public void addContribution(Member member, BigDecimal amount) throws Throwable {
        if (amount.compareTo(new BigDecimal("0")) <= 0) {
            throw new Throwable();
        }
        contributions.add(new Contribution(member, amount));
    }

    public Specification getSpecification() {
        return specification;
    }

    public Description getDescription() {
        return description;
    }

    // TODO create a query ?
    public Set<Offer> getOffers() {
        return offers;
    }

    // TODO create a query ?
    public Set<Contribution> getContributions() {
        return contributions;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setSpecification(Specification specification) {
        this.specification = specification;
    }

    protected void setDescription(Description description) {
        this.description = description;
    }

    protected void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }

    protected void setContributions(Set<Contribution> contributions) {
        this.contributions = contributions;
    }
}
