package com.bloatit.model.data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Specification extends UserContent {

    private String content;
    @OneToOne(optional = true)
    private Demand demand;

    protected Specification() {
        super();
    }

    public Specification(Member member, String content, Demand demand) {
        super(member);
        this.content = content;
        this.demand = demand;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected Demand getDemand() {
        return demand;
    }

    protected void setDemand(Demand demand) {
        this.demand = demand;
    }

}
