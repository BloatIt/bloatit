package com.bloatit.model.data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

@Entity
public class DaoSpecification extends DaoUserContent {

    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String content;
    @OneToOne(optional = true)
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
