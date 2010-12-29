package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.model.data.util.SessionManager;

/**
 * A kudos is a positive or negative appreciation of a Kudosable content. [ Yes there is a
 * 's' at the end of kudos even when there is only one. ] The kudos is an internal storing
 * class. You should never have to use it in other package.
 * 
 * @see DaoKudosable#addKudos(DaoMember, int)
 */
@Entity
public final class DaoKudos extends DaoUserContent {

    /**
     * The value can be positive or negative. The value should never be equals zero.
     */
    @Basic(optional = false)
    private int value;

    protected static DaoKudos createAndPersist(final DaoMember member, final int value) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoKudos kudos = new DaoKudos(member, value);
        try {
            session.save(kudos);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return kudos;
    }

    /**
     * Create a new kudos.
     * 
     * @param member is the person creating the kudos.
     * @param value is value of the kudos.
     */
    private DaoKudos(final DaoMember member, final int value) {
        super(member);
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoKudos() {
        super();
    }

    @SuppressWarnings("unused")
    private void setValue(final int value) {
        this.value = value;
    }
}
