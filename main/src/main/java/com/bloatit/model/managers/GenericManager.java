package com.bloatit.model.managers;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.bloatit.data.DaoIdentifiable;
import com.bloatit.data.SessionManager;
import com.bloatit.model.ConstructorVisitor;
import com.bloatit.model.Identifiable;

public final class GenericManager {
    private GenericManager() {
        // TODO Auto-generated constructor stub
    }

    public static Identifiable<?> getById(Integer id) {
        Criteria criteria = SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoIdentifiable.class);
        criteria.add(Restrictions.eq("id", id));
        return ((DaoIdentifiable) criteria.uniqueResult()).accept(new ConstructorVisitor());
    }
}
