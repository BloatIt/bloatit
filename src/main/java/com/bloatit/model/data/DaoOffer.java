package com.bloatit.model.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

@Entity
public class DaoOffer extends DaoKudosable {

    @ManyToOne
    private DaoDemand demand;
    
    @OneToOne
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    @IndexedEmbedded
    private DaoDescription description;
    
    @Basic(optional = false)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    private Date dateExpire;

    protected DaoOffer() {
        super();
    }

    public DaoOffer(DaoMember member, DaoDemand Demand, DaoDescription text, Date dateExpire) {
        super(member);
        this.demand = Demand;
        this.description = text;
        this.dateExpire = dateExpire;
    }

    public Date getDateExpire() {
        return dateExpire;
    }

    public void setDateExpire(Date dateExpire) {
        this.dateExpire = dateExpire;
    }

    public DaoDemand getDemand() {
        return demand;
    }

    public DaoDescription getDescription() {
        return description;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setDemand(DaoDemand Demand) {
        this.demand = Demand;
    }

    protected void setDescription(DaoDescription text) {
        this.description = text;
    }
}
