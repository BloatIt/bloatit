package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OrderBy;

/**
 * 
 */
@Entity
public class Demand extends Kudosable {
    public enum State {
        CONSTRUCTING, VALIDATED, DEVELOPED, ACCEPTED, REJECTED;
    }

    @Basic(optional = false)
    @Enumerated
    private State state;

    @OneToOne(mappedBy = "demand", optional = false)
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private Translatable description;
    @OneToOne(mappedBy = "demand", optional = false)
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private Translatable title;

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
    private Set<Transaction> contributions = new HashSet<Transaction>(0);

    /**
     * It is automatically in validated state (temporary)
     * 
     * @param member the author of the demand
     * @param description
     */
    public Demand(Member member, Translatable title, Translatable description) {
        super(member);
        this.title = title;
        this.state = State.VALIDATED;
        this.description = description;
        this.specification = null;
    }

    protected Demand() {
        super();
    }

    public void createSpecification(Member member, String content, Demand demand) {
        specification = new Specification(member, content, demand);
    }

    public void addOffer(Member author, Translatable description, Date dateExpir) {
        offers.add(new Offer(author, this, description, dateExpir));
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
        contributions.add(new Transaction(member, amount));
    }

    public Specification getSpecification() {
        return specification;
    }

    public State getState() {
        return state;
    }

    public Translatable getDescription() {
        return description;
    }

    protected Set<Offer> getOffers() {
        return offers;
    }

    protected Set<Transaction> getContributions() {
        return contributions;
    }

    public Translatable getTitle() {
        return title;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setSpecification(Specification specification) {
        this.specification = specification;
    }

    protected void setState(State state) {
        this.state = state;
    }

    protected void setDescription(Translatable description) {
        this.description = description;
    }

    protected void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }

    protected void setContributions(Set<Transaction> contributions) {
        this.contributions = contributions;
    }

    public void setTitle(Translatable title) {
        this.title = title;
    }

}
