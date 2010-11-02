package com.bloatit.model.data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class DaoSpecification extends DaoUserContent {

    private String content;
    @OneToOne(optional = true)
    @Cascade(value = { CascadeType.ALL})
    private DaoDemand demand;

    protected DaoSpecification() {
        super();
    }

    public DaoSpecification(DaoMember member, String content, DaoDemand Demand) {
        super(member);
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
