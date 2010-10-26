package com.bloatit.model.data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class DaoSpecification extends DaoUserContent {

    private String content;
    @OneToOne(optional = true)
    private DaoDemand demand;

    protected DaoSpecification() {
        super();
    }

    public DaoSpecification(DaoActor Actor, String content, DaoDemand Demand) {
        super(Actor);
        this.content = content;
        this.demand = Demand;
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

    protected DaoDemand getDemand() {
        return demand;
    }

    protected void setDemand(DaoDemand Demand) {
        this.demand = Demand;
    }

}
