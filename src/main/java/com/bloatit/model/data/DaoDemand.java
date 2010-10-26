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
public class DaoDemand extends DaoKudosable {

    @OneToOne(optional = false)
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private DaoDescription description;

    @OneToOne(mappedBy = "demand", optional = true)
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private DaoSpecification specification;

    @OneToMany(mappedBy = "demand")
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    @OrderBy(clause = "DaoOffer.popularity")
    // TODO test me !
    private Set<DaoOffer> offers = new HashSet<DaoOffer>(0);

    // TODO make sure it is read only !
    @OneToMany(mappedBy = "demand")
    @Cascade(value = { CascadeType.ALL })
    private Set<DaoContribution> contributions = new HashSet<DaoContribution>(0);

    /**
     * It is automatically in validated state (temporary)
     * 
     * @param Actor the author of the demand
     * @param description
     */
    public static DaoDemand createAndPersist(DaoActor Actor, DaoDescription Description) {
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        DaoDemand Demand = new DaoDemand(Actor, Description);
        try {
            session.save(Demand);
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return Demand;
    }

    protected DaoDemand(DaoActor Actor, DaoDescription Description) {
        super(Actor);
        setValidated();
        this.description = Description;
        this.specification = null;
    }

    protected DaoDemand() {
        super();
    }

    public void delete() {
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        session.delete(this);
    }

    public void createSpecification(DaoActor Actor, String content) {
        specification = new DaoSpecification(Actor, content, this);
    }

    public DaoOffer addOffer(DaoActor Actor, DaoDescription Description, Date dateExpir) {
        DaoOffer Offer = new DaoOffer(Actor, this, Description, dateExpir);
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

    // TODO create a Throwable type
    public void addContribution(DaoActor Actor, BigDecimal amount) throws Throwable {
        if (amount.compareTo(new BigDecimal("0")) <= 0) {
            throw new Throwable();
        }
        contributions.add(new DaoContribution(Actor, amount));
    }

    public DaoSpecification getSpecification() {
        return specification;
    }

    public DaoDescription getDescription() {
        return description;
    }

    // TODO create a query ?
    public Set<DaoOffer> getOffers() {
        return offers;
    }

    // TODO create a query ?
    public Set<DaoContribution> getContributions() {
        return contributions;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setSpecification(DaoSpecification Specification) {
        this.specification = Specification;
    }

    protected void setDescription(DaoDescription Description) {
        this.description = Description;
    }

    protected void setOffers(Set<DaoOffer> Offers) {
        this.offers = Offers;
    }

    protected void setContributions(Set<DaoContribution> Contributions) {
        this.contributions = Contributions;
    }
}
