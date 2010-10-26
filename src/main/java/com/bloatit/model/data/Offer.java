package com.bloatit.model.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class Offer extends Kudosable {

    @ManyToOne
    private Demand demand;
    @OneToOne
    @Cascade(value={CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    private Description description;
    @Basic(optional = false)
    private Date dateExpire;

    protected Offer() {
        super();
    }

    public Offer(Actor actor, Demand demand, Description text, Date dateExpire) {
        super(actor);
        this.demand = demand;
        this.description = text;
        this.dateExpire = dateExpire;
    }

    public Date getDateExpire() {
        return dateExpire;
    }

    public void setDateExpire(Date dateExpire) {
        this.dateExpire = dateExpire;
    }

    public Demand getDemand() {
        return demand;
    }

    public Description getDescription() {
        return description;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setDemand(Demand demand) {
        this.demand = demand;
    }

    protected void setDescription(Description text) {
        this.description = text;
    }
}
