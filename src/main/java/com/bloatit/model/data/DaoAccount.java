package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import com.bloatit.common.PageIterable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DaoAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne
    private DaoActor actor;
    private Date creationDate;

    @Basic(optional = false)
    private Date lastModificationDate;

    @Basic(optional = false)
    private BigDecimal amount;

    protected DaoAccount() {
        super();
    }

    public DaoAccount(DaoActor actor) {
        if (actor == null) {
            throw new NullPointerException();
        }
        this.actor = actor;
        this.creationDate = new Date();
        this.lastModificationDate = new Date();
        this.amount = new BigDecimal("0");
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    protected void addToAmountValue(BigDecimal blocked) {
        resetModificationDate();
        lastModificationDate = new Date();
        amount = amount.add(blocked);
    }

    protected void substractToAmountValue(BigDecimal blocked) {
        resetModificationDate();
        lastModificationDate = new Date();
        amount = amount.subtract(blocked);
    }

    public PageIterable<DaoTransaction> getTransactions() {
        return new QueryCollection<DaoTransaction>("from DaoTransaction as t where t.from = :this or t.to = :this")
                .setEntity("this", this);
    }

    protected void resetModificationDate() {
        lastModificationDate = new Date();
    }

    public Integer getId() {
        return id;
    }

    public DaoActor getActor() {
        return actor;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    protected void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    protected void setActor(DaoActor Actor) {
        actor = Actor;
    }

    protected void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
