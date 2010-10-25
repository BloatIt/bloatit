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

import com.bloatit.model.data.util.SessionManger;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne
    private Member member;
    private Date creationDate;

    @Basic(optional = false)
    private Date lastModificationDate;

    @Basic(optional = false)
    private BigDecimal amount;

    protected Account() {
        super();
    }

    public Account(Member member) {
        this.member = member;
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
        this.lastModificationDate = new Date();
        this.amount = this.amount.add(blocked);
    }

    protected void substractToAmountValue(BigDecimal blocked) {
        this.lastModificationDate = new Date();
        this.amount = this.amount.subtract(blocked);
    }

    public QueryCollection<Transaction> getTransactions() {
        return new QueryCollection<Transaction>(SessionManger.getSessionFactory()
                                                             .getCurrentSession()
                                                             .createQuery("from Transaction as t where t.from = this or t.to = this"));
    }

    protected void resetModificationDate() {
        lastModificationDate = new Date();
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
