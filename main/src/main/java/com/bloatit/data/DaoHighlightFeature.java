package com.bloatit.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.specific.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * List of hightlighted features with the reason, the position and the hightlight
 * date
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                           name = "highlightFeature.byIsActivated",
                           query = "FROM DaoHighlightFeature " +
                                   "WHERE activationDate < now() " +
                                   "AND desactivationDate > now()"),
                       @NamedQuery(
                           name = "highlightFeature.byIsActivated.size",
                           query = "SELECT count(*) " +
                                   "FROM DaoHighlightFeature " +
                                   "WHERE activationDate < now() " +
                                   "AND desactivationDate > now()")
                       }
             )
// @formatter:on
public class DaoHighlightFeature extends DaoIdentifiable {

    @Basic(optional = false)
    private int position;

    @Basic(optional = true)
    private String reason;

    @ManyToOne(optional = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private DaoFeature feature;

    @Column(updatable = false, nullable = false)
    private Date activationDate;

    @Column(updatable = false, nullable = false)
    private Date desactivationDate;

    // ======================================================================
    // Construction
    // ======================================================================

    /**
     * Create a DaoHighlightFeature and add it into the db.
     *
     * @param feature the feature
     * @param position the position
     * @param reason the reason
     * @param activationDate the activation date
     * @param desactivationDate the desactivation date
     * @return the dao highlight feature
     */
    public static DaoHighlightFeature createAndPersist(final DaoFeature feature,
                                                      final int position,
                                                      final String reason,
                                                      final Date activationDate,
                                                      final Date desactivationDate) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoHighlightFeature hightlightFeature = new DaoHighlightFeature(feature, position, reason, activationDate, desactivationDate);
        try {
            session.save(hightlightFeature);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return hightlightFeature;
    }

    /**
     * Create a DaoHighlightFeature
     *
     * @param feature
     * @param position
     * @param reason
     * @param activationDate
     * @param desactivationDate
     */
    public DaoHighlightFeature(final DaoFeature feature, final int position, final String reason, final Date activationDate, final Date desactivationDate) {
        if (feature == null || activationDate == null || desactivationDate == null) {
            throw new NonOptionalParameterException();
        }
        this.feature = feature;
        this.position = position;
        this.reason = reason;
        this.activationDate = activationDate;
        this.desactivationDate = desactivationDate;

    }

    // ======================================================================
    // Getters
    // ======================================================================

    /**
     * @return all the member in this team. (Use a HQL query).
     */
    public PageIterable<DaoHighlightFeature> getActiveHightlightFeatures() {
        return new QueryCollection<DaoHighlightFeature>("highlightFeature.byIsActivated");
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

    public DaoFeature getFeature() {
        return this.feature;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.activationDate == null) ? 0 : this.activationDate.hashCode());
        result = prime * result + ((this.feature == null) ? 0 : this.feature.hashCode());
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
        final DaoHighlightFeature other = (DaoHighlightFeature) obj;
        if (this.activationDate == null) {
            if (other.activationDate != null) {
                return false;
            }
        } else if (!this.activationDate.equals(other.activationDate)) {
            return false;
        }
        if (this.feature == null) {
            if (other.feature != null) {
                return false;
            }
        } else if (!this.feature.equals(other.feature)) {
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

    protected DaoHighlightFeature() {
        super();
    }

}
