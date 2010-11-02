package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.model.data.util.SessionManager;

@Entity
public class DaoKudos extends DaoUserContent {

    @Basic(optional = false)
    private int value;

    public DaoKudos() {}

    public static DaoKudos createAndPersist(DaoMember member, int value) {
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

    private DaoKudos(DaoMember member, int value) {
        super(member);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setValue(int value) {
        this.value = value;
    }
}
