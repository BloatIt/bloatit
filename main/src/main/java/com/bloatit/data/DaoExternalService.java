package com.bloatit.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.Hash;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                           name = "externalservice.getByToken",
                           query = "FROM DaoExternalService " +
                           		   "WHERE token = :token "),
                       }
             )
//@formatter:on
public final class DaoExternalService extends DaoUserContent {

    @ManyToOne(optional = false)
    private DaoDescription description;

    @ManyToOne(optional = true)
    private DaoFileMetadata logo;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToMany(mappedBy = "service", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private final List<DaoExternalServiceMembership> membership = new ArrayList<DaoExternalServiceMembership>();

    // ======================================================================
    // Static operations
    // ======================================================================

    public static DaoExternalService getByToken(final String authorizationToken) {
        final DaoExternalService service = (DaoExternalService) SessionManager.getNamedQuery("externalservice.getByToken")
                                                                              .setString("token", authorizationToken)
                                                                              .uniqueResult();
        return service;
    }

    // ======================================================================
    // Construction
    // ======================================================================

    public static DaoExternalService createAndPersist(final DaoMember author, final DaoTeam asTeam, final DaoDescription description) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoExternalService theMember = new DaoExternalService(author, asTeam, description);
        try {
            session.save(theMember);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return theMember;
    }

    private DaoExternalService(final DaoMember author, final DaoTeam asTeam, final DaoDescription description) {
        super(author, asTeam);
        if (description == null) {
            throw new NonOptionalParameterException();
        }
        this.description = description;
        this.token = Hash.generateUniqueToken(32);
    }

    public void setLogo(final DaoFileMetadata fileImage) {
        logo = fileImage;
    }

    public void addMembership(final DaoExternalServiceMembership membership) {
        this.membership.add(membership);
    }

    // ======================================================================
    // Getters
    // ======================================================================

    public DaoFileMetadata getLogo() {
        return logo;
    }

    public DaoDescription getDescription() {
        return description;
    }

    public String getToken() {
        return token;
    }

    // ======================================================================
    // equals and hashCode.
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((logo == null) ? 0 : logo.hashCode());
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
        final DaoExternalService other = (DaoExternalService) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (logo == null) {
            if (other.logo != null) {
                return false;
            }
        } else if (!logo.equals(other.logo)) {
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

    protected DaoExternalService() {
        super();
    }

}
