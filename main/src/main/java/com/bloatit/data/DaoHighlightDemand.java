package com.bloatit.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * List of hightlighted demands with the reason, the position and the hightlight
 * date
 */
@Entity
public class DaoHighlightDemand extends DaoIdentifiable{

    @Basic(optional = false)
    private int position;

    @Basic(optional = true)
    private String reason;

    @ManyToOne(cascade = {  CascadeType.ALL })
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
     * @param demand
     * @param position
     * @param reason
     * @param activationDate
     * @param desactivationDate
     * @return
     */
    public static DaoHighlightDemand createAndPersist(final DaoDemand demand, int position, String reason, Date activationDate, Date desactivationDate) {
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
    public DaoHighlightDemand(DaoDemand demand, int position, String reason, Date activationDate, Date desactivationDate) {
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

        Query query = session.createQuery("from DaoHighlightDemand " + //
                        "where activationDate < :now " + //
                        "and desactivationDate > :now");//

        query.setDate("now", new Date());

        QueryCollection<DaoHighlightDemand> queryCollection = new QueryCollection<DaoHighlightDemand>(query);
        return queryCollection;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public Date getDesactivationDate() {
        return desactivationDate;
    }

    public void setDesactivationDate(Date desactivationDate) {
        this.desactivationDate = desactivationDate;
    }

    public String getReason() {
        return reason;
    }

    public DaoDemand getDemand() {
        return demand;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((activationDate == null) ? 0 : activationDate.hashCode());
        result = prime * result + ((demand == null) ? 0 : demand.hashCode());
        result = prime * result + ((desactivationDate == null) ? 0 : desactivationDate.hashCode());
        result = prime * result + position;
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DaoHighlightDemand other = (DaoHighlightDemand) obj;
        if (activationDate == null) {
            if (other.activationDate != null)
                return false;
        } else if (!activationDate.equals(other.activationDate))
            return false;
        if (demand == null) {
            if (other.demand != null)
                return false;
        } else if (!demand.equals(other.demand))
            return false;
        if (desactivationDate == null) {
            if (other.desactivationDate != null)
                return false;
        } else if (!desactivationDate.equals(other.desactivationDate))
            return false;
        if (position != other.position)
            return false;
        if (reason == null) {
            if (other.reason != null)
                return false;
        } else if (!reason.equals(other.reason))
            return false;
        return true;
    }
    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoHighlightDemand() {
        super();
    }

}
