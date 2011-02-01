package com.bloatit.data;

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

import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * A DaoAccount generalize the idea of bank account for our system. This class is mapped
 * as a joined table. So there is a table for DaoAccount, and a table for each of its
 * children. Each time you want to access a DaoAccount, there is a SQL join done, between
 * the daoAccount and its child.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DaoAccount implements IdentifiableInterface{

    /**
     * Because of the different inheritance strategy we cannot inherit from identifiable.
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
     * The amount is the quantity of money you has in your account. WARNING: For now there
     * is no ?devise? ($,€, ...)
     */
    @Basic(optional = false)
    private BigDecimal amount;

    /**
     * This constructor initialize the creation and modification dates. The amount is set
     * to 0
     *
     * @param actor is the owner of this account
     * @throws NonOptionalParameterException if the actor == null
     */
    public DaoAccount(final DaoActor actor) {
        if (actor == null) {
            throw new NonOptionalParameterException();
        }
        this.actor = actor;
        this.creationDate = new Date();
        this.lastModificationDate = creationDate;
        this.amount = BigDecimal.ZERO;
    }

    /**
     * WARNING: the order is not specified yet. Maybe it will be ordered by date (if
     * needed)
     *
     * @return all the transactions that are from/to this account.
     */
    public final PageIterable<DaoTransaction> getTransactions() {
        return new QueryCollection<DaoTransaction>("from DaoTransaction as t where t.from = :this or t.to = :this").setEntity("this", this);
    }

    /**
     * If you want to take away from this account some money, you have to know if there is
     * enough money in it.
     *
     * @param amount The quantity of money you want to get.
     * @return true if this operation is allowed.
     */
    protected abstract boolean hasEnoughMoney(BigDecimal amount);

    public final Date getLastModificationDate() {
        return (Date) lastModificationDate.clone();
    }

    public final BigDecimal getAmount() {
        return amount;
    }

    @Override
    public final Integer getId() {
        return id;
    }

    public final DaoActor getActor() {
        return actor;
    }

    public final Date getCreationDate() {
        return (Date) creationDate.clone();
    }

    /**
     * To modify the value of the amount, you have to create a transaction. This method is
     * protected to be used by transaction only !
     *
     * @param value the quantity of money to add to the amount of this account. (May be a
     *        negative value)
     */
    protected final void addToAmountValue(final BigDecimal value) {
        resetModificationDate();
        lastModificationDate = new Date();
        amount = amount.add(value);
    }

    /**
     * To modify the value of the amount, you have to create a transaction. This method is
     * protected to be used by transaction only !
     *
     * @param value the quantity of money to subtract to the amount of this account. (May
     *        be a negative value)
     */
    protected final void substractToAmountValue(final BigDecimal value) {
        resetModificationDate();
        lastModificationDate = new Date();
        amount = amount.subtract(value);
    }

    /**
     * Used internally or by subclasses to every time the Amount is changed. It reset the
     * modification date to now.
     */
    protected final void resetModificationDate() {
        lastModificationDate = new Date();
    }

    /**
     * This is for hibernate only. The amount must be modified by some higher level
     * methods. For test purpose it is protected, but it will be private.
     *
     * @see DaoTransaction
     * @param amount the new amount to set.
     */
    protected final void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoAccount() {
        super();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((actor == null) ? 0 : actor.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj.getClass().equals(getClass()))) {
            return false;
        }
        final DaoAccount other = (DaoAccount) obj;
        if (actor == null) {
            if (other.actor != null) {
                return false;
            }
        } else if (!actor.equals(other.actor)) {
            return false;
        }
        if (creationDate == null) {
            if (other.creationDate != null) {
                return false;
            }
        } else if (!creationDate.equals(other.creationDate)) {
            return false;
        }
        return true;
    }

}
