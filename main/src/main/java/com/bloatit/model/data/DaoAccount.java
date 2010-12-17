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

import com.bloatit.common.Log;
import com.bloatit.common.PageIterable;

/**
 * A DaoAccount generalize the idea of bank account for our system. This class
 * is mapped
 * as a joined table. So there is a table for DaoAccount, and a table for each
 * of its
 * children. Each time you want to access a DaoAccount, there is a SQL join
 * done, between
 * the daoAccount and its child.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DaoAccount {

    /**
     * Because of the different inheritance strategy we cannot inherit from
     * identifiable.
     * So we have to have an id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * The DaoActor is the person that own this account.
     */
    @OneToOne
    private DaoActor actor;

    @Basic(optional = false)
    private Date creationDate;

    @Basic(optional = false)
    private Date lastModificationDate;

    /**
     * The amount is the quantity of money you has in your account. WARNING: For
     * now there
     * is no ?devise? ($,€, ...)
     */
    @Basic(optional = false)
    private BigDecimal amount;

    /**
     * This constructor initialize the creation and modification dates. The
     * amount is set
     * to 0
     * 
     * @param actor is the owner of this account
     * @throws NullPointerException if the actor == null
     */
    public DaoAccount(final DaoActor actor) {
        if (actor == null) {
            Log.data().fatal("Cannot create account with a null actor.");
            throw new NullPointerException();
        }
        this.actor = actor;
        this.creationDate = new Date();
        this.lastModificationDate = new Date();
        this.amount = new BigDecimal("0");
    }

    /**
     * WARNING: the order is not specified yet. Maybe it will be ordered by date
     * (if
     * needed)
     * 
     * @return all the transactions that are from/to this account.
     */
    public PageIterable<DaoTransaction> getTransactions() {
        return new QueryCollection<DaoTransaction>("from DaoTransaction as t where t.from = :this or t.to = :this").setEntity("this", this);
    }

    /**
     * If you want to take away from this account some money, you have to know
     * if there is enough money in it.
     * 
     * @param amount The quantity of money you want to get.
     * @return true if this operation is allowed.
     */
    protected abstract boolean hasEnoughMoney(BigDecimal amount);

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public BigDecimal getAmount() {
        return amount;
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

    /**
     * To modify the value of the amount, you have to create a transaction. This
     * method is
     * protected to be used by transaction only !
     * 
     * @param value the quantity of money to add to the amount of this account.
     *        (May be a
     *        negative value)
     */
    protected void addToAmountValue(final BigDecimal value) {
        resetModificationDate();
        lastModificationDate = new Date();
        amount = amount.add(value);
    }

    /**
     * To modify the value of the amount, you have to create a transaction. This
     * method is
     * protected to be used by transaction only !
     * 
     * @param value the quantity of money to subtract to the amount of this
     *        account. (May
     *        be a negative value)
     */
    protected void substractToAmountValue(final BigDecimal value) {
        resetModificationDate();
        lastModificationDate = new Date();
        amount = amount.subtract(value);
    }

    /**
     * Used internally or by subclasses to every time the Amount is changed. It
     * reset the
     * modification date to now.
     */
    protected void resetModificationDate() {
        lastModificationDate = new Date();
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected DaoAccount() {
        super();
    }

    /**
     * This is for hibernate only. The amount must be modified by some higher
     * level
     * methods.
     * 
     * @see DaoTransaction
     * @param amount the new amount to set.
     */
    protected void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setLastModificationDate(final Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setId(final Integer id) {
        this.id = id;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setActor(final DaoActor Actor) {
        actor = Actor;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }
}
