package com.bloatit.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * List of hightlighted demands with the reason, the position and the hightlight
 * date
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DaoHighlightDemand extends DaoIdentifiable {

    @Basic(optional = false)
    private int position;

    @Basic(optional = true)
    private String reason;

    @ManyToOne(optional = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private DaoDemand demand;

    @Column(updatable = false, nullable = false)
    private Date activationDate;

    @Column(updatable = false, nullable = false)
    private Date desactivationDate;

    // ======================================================================
    // Construction
    // ======================================================================

    /**
     * Create a DaoHighlightDemand and add it into the db.
     * 
     * @param demand the demand
     * @param position the position
     * @param reason the reason
     * @param activationDate the activation date
     * @param desactivationDate the desactivation date
     * @return the dao highlight demand
     */
    public static DaoHighlightDemand createAndPersist(final DaoDemand demand,
                                                      final int position,
                                                      final String reason,
                                                      final Date activationDate,
                                                      final Date desactivationDate) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoHighlightDemand hightlightDemand = new DaoHighlightDemand(demand, position, reason, activationDate, desactivationDate);
        try {
            session.save(hightlightDemand);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return hightlightDemand;
    }

    /**
     * Create a DaoHighlightDemand
     * 
     * @param demand
     * @param position
     * @param reason
     * @param activationDate
     * @param desactivationDate
     */
    public DaoHighlightDemand(final DaoDemand demand, final int position, final String reason, final Date activationDate, final Date desactivationDate) {
        if (demand == null || activationDate == null || desactivationDate == null) {
            throw new NonOptionalParameterException();
        }
        this.demand = demand;
        this.position = position;
        this.reason = reason;
        this.activationDate = activationDate;
        this.desactivationDate = desactivationDate;

    }

    // ======================================================================
    // Getters
    // ======================================================================

    /**
     * @return all the member in this group. (Use a HQL query).
     */
    public PageIterable<DaoHighlightDemand> getActiveHightlightDemands() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();

        final Query query = session.createQuery("from DaoHighlightDemand " + //
                "where activationDate < :now " + //
                "and desactivationDate > :now");//

        query.setDate("now", new Date());

        final QueryCollection<DaoHighlightDemand> queryCollection = new QueryCollection<DaoHighlightDemand>(query);
        return queryCollection;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(final int position) {
        this.position = position;
    }

    public Date getActivationDate() {
        return this.activationDate;
    }

    public void setActivationDate(final Date activationDate) {
        this.activationDate = activationDate;
    }

    public Date getDesactivationDate() {
        return this.desactivationDate;
    }

    public void setDesactivationDate(final Date desactivationDate) {
        this.desactivationDate = desactivationDate;
    }

    public String getReason() {
        return this.reason;
    }

    public DaoDemand getDemand() {
        return this.demand;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.activationDate == null) ? 0 : this.activationDate.hashCode());
        result = prime * result + ((this.demand == null) ? 0 : this.demand.hashCode());
        result = prime * result + ((this.desactivationDate == null) ? 0 : this.desactivationDate.hashCode());
        result = prime * result + this.position;
        result = prime * result + ((this.reason == null) ? 0 : this.reason.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoHighlightDemand other = (DaoHighlightDemand) obj;
        if (this.activationDate == null) {
            if (other.activationDate != null) {
                return false;
            }
        } else if (!this.activationDate.equals(other.activationDate)) {
            return false;
        }
        if (this.demand == null) {
            if (other.demand != null) {
                return false;
            }
        } else if (!this.demand.equals(other.demand)) {
            return false;
        }
        if (this.desactivationDate == null) {
            if (other.desactivationDate != null) {
                return false;
            }
        } else if (!this.desactivationDate.equals(other.desactivationDate)) {
            return false;
        }
        if (this.position != other.position) {
            return false;
        }
        if (this.reason == null) {
            if (other.reason != null) {
                return false;
            }
        } else if (!this.reason.equals(other.reason)) {
            return false;
        }
        return true;
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoHighlightDemand() {
        super();
    }

}
