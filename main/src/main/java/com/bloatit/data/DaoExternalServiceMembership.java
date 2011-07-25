package com.bloatit.data;

import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.data.exceptions.ElementNotFoundException;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.datetime.DateUtils;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "member_id", "service_id" }) })
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                           name = "externalServiceMembership.getByToken",
                           query = "FROM DaoExternalServiceMembership " +
                           		   "WHERE token = :token "),
                   		@NamedQuery(
                                    name = "externalServiceMembership.byServicetokenMember",
                                    query = "FROM DaoExternalServiceMembership s " +
                                            "WHERE s.service.token = :token " +
                                            "AND s.member = :member"),
                       }
             )
//@formatter:on
public final class DaoExternalServiceMembership extends DaoIdentifiable {

    public enum RightLevel {
        CREATE_FEATURE, CREATE_OFFER, COMMENT, KUDOS, CONTRIBUTE,
    }

    @Basic(optional = false)
    private String token;

    @Basic(optional = false)
    private boolean authorized;

    private String refreshToken;
    private Date expirationDate;

    @Basic(optional = false)
    private boolean canCreateFeature;
    @Basic(optional = false)
    private boolean canCreateOffer;
    @Basic(optional = false)
    private boolean canComment;
    @Basic(optional = false)
    private boolean canKudos;
    @Basic(optional = false)
    private boolean canContribute;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private DaoExternalService service;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private DaoMember member;

    // ======================================================================
    // Static operations
    // ======================================================================

    public static void authorizeService(final String authorizationToken,
                                        final String accessToken,
                                        final String refreshToken,
                                        final Date expirationDate) throws ElementNotFoundException {
        if (authorizationToken == null || accessToken == null || refreshToken == null || expirationDate == null) {
            throw new NonOptionalParameterException();
        }
        final DaoExternalServiceMembership service = (DaoExternalServiceMembership) SessionManager.getNamedQuery("externalServiceMembership.getByToken")
                                                                                                  .setString("token", authorizationToken)
                                                                                                  .uniqueResult();
        if (service == null) {
            throw new ElementNotFoundException("The service with the specified token is not found.");
        }
        if (service.isAuthorized()) {
            throw new ElementNotFoundException("The service with the specified token is already authorized.");
        }
        service.authorize(accessToken, refreshToken, expirationDate);
    }

    public static DaoExternalServiceMembership getByToken(final String authorizationToken) {
        final DaoExternalServiceMembership service = (DaoExternalServiceMembership) SessionManager.getNamedQuery("externalServiceMembership.getByToken")
                                                                                                  .setString("token", authorizationToken)
                                                                                                  .uniqueResult();
        return service;
    }

    public static DaoExternalServiceMembership getByServicetokenMember(final String serviceToken, final DaoMember member) {
        final DaoExternalServiceMembership service = (DaoExternalServiceMembership) SessionManager.getNamedQuery("externalServiceMembership.byServicetokenMember")
                                                                                                  .setString("token", serviceToken)
                                                                                                  .setEntity("member", member)
                                                                                                  .uniqueResult();
        return service;
    }

    // ======================================================================
    // Construction
    // ======================================================================

    protected static DaoExternalServiceMembership createAndPersist(final DaoMember member,
                                                                   final DaoExternalService service,
                                                                   final String token,
                                                                   final EnumSet<RightLevel> level) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoExternalServiceMembership theMember = new DaoExternalServiceMembership(member, service, token, level);
        try {
            session.save(theMember);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return theMember;
    }

    private DaoExternalServiceMembership(final DaoMember member, final DaoExternalService service, final String token, final EnumSet<RightLevel> level) {
        super();
        if (member == null || service == null || token == null || level == null) {
            throw new NonOptionalParameterException();
        }
        if (token.isEmpty()) {
            throw new NonOptionalParameterException();
        }
        this.member = member;
        this.service = service;
        this.service.addMembership(this);
        this.token = token;
        this.authorized = false;
        this.refreshToken = null;
        this.expirationDate = null;

        this.canCreateFeature = false;
        this.canCreateOffer = false;
        this.canComment = false;
        this.canKudos = false;
        this.canContribute = false;

        setLevel(level);
    }

    private void setLevel(final EnumSet<RightLevel> level) {
        for (final RightLevel rightLevel : level) {
            switch (rightLevel) {
                case CREATE_FEATURE:
                    this.canCreateFeature = true;
                    break;
                case CREATE_OFFER:
                    this.canCreateOffer = true;
                    break;
                case COMMENT:
                    this.canComment = true;
                    break;
                case KUDOS:
                    this.canKudos = true;
                    break;
                case CONTRIBUTE:
                    this.canContribute = true;
                    break;
                default:
                    break;
            }
        }
    }

    public final void authorize(final String accessToken, final String refreshToken, final Date expirationDate) {
        if (accessToken == null || refreshToken == null || expirationDate == null) {
            throw new NonOptionalParameterException();
        }
        if (accessToken.isEmpty() || refreshToken.isEmpty()) {
            throw new NonOptionalParameterException();
        }
        if (!DateUtils.isInTheFuture(expirationDate)) {
            throw new BadProgrammerException("Make sure the expiration date is in the future.");
        }
        this.authorized = true;
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.expirationDate = expirationDate;
    }

    public void reset(String token, EnumSet<RightLevel> level) {
        authorized = false;
        refreshToken = null;
        this.expirationDate = null;
        setLevel(level);
        this.token = token;
    }

    // ======================================================================
    // Getters
    // ======================================================================

    public final boolean isValid() {
        return authorized && expirationDate != null && DateUtils.isInTheFuture(expirationDate);
    }

    public final DaoExternalService getService() {
        return service;
    }

    public final String getToken() {
        return token;
    }

    public final boolean isAuthorized() {
        return authorized;
    }

    public final String getRefreshToken() {
        return refreshToken;
    }

    public final Date getExpirationDate() {
        return expirationDate;
    }

    public final DaoMember getMember() {
        return member;
    }

    public final EnumSet<RightLevel> getLevels() {
        final EnumSet<RightLevel> levels = EnumSet.noneOf(RightLevel.class);
        if (canCreateFeature) {
            levels.add(RightLevel.CREATE_FEATURE);
        }
        if (canCreateOffer) {
            levels.add(RightLevel.CREATE_OFFER);
        }
        if (canComment) {
            levels.add(RightLevel.COMMENT);
        }
        if (canContribute) {
            levels.add(RightLevel.CONTRIBUTE);
        }
        if (canKudos) {
            levels.add(RightLevel.KUDOS);
        }
        return levels;
    }

    // ======================================================================
    // equals and hashCode.
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (authorized ? 1231 : 1237);
        result = prime * result + (canComment ? 1231 : 1237);
        result = prime * result + (canContribute ? 1231 : 1237);
        result = prime * result + (canCreateFeature ? 1231 : 1237);
        result = prime * result + (canCreateOffer ? 1231 : 1237);
        result = prime * result + (canKudos ? 1231 : 1237);
        result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
        result = prime * result + ((service == null) ? 0 : service.hashCode());
        result = prime * result + ((refreshToken == null) ? 0 : refreshToken.hashCode());
        result = prime * result + ((token == null) ? 0 : token.hashCode());
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
        final DaoExternalServiceMembership other = (DaoExternalServiceMembership) obj;
        if (authorized != other.authorized) {
            return false;
        }
        if (canComment != other.canComment) {
            return false;
        }
        if (canContribute != other.canContribute) {
            return false;
        }
        if (canCreateFeature != other.canCreateFeature) {
            return false;
        }
        if (canCreateOffer != other.canCreateOffer) {
            return false;
        }
        if (canKudos != other.canKudos) {
            return false;
        }
        if (expirationDate == null) {
            if (other.expirationDate != null) {
                return false;
            }
        } else if (!expirationDate.equals(other.expirationDate)) {
            return false;
        }
        if (service == null) {
            if (other.service != null) {
                return false;
            }
        } else if (!service.equals(other.service)) {
            return false;
        }
        if (refreshToken == null) {
            if (other.refreshToken != null) {
                return false;
            }
        } else if (!refreshToken.equals(other.refreshToken)) {
            return false;
        }
        if (token == null) {
            if (other.token != null) {
                return false;
            }
        } else if (!token.equals(other.token)) {
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

    /**
     * Instantiates a new dao member.
     */
    protected DaoExternalServiceMembership() {
        super();
    }
}
