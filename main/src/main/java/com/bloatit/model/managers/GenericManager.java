//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.model.managers;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.bloatit.data.DaoIdentifiable;
import com.bloatit.data.SessionManager;
import com.bloatit.model.DataVisitorConstructor;
import com.bloatit.model.Identifiable;

/**
 * The Class GenericManager is a utility class with static method to load data
 * from the DB. It is generic because the methods are not specific for a class.
 */
public final class GenericManager {

    /**
     * Desactivate constructor on utility class.
     */
    private GenericManager() {
        // desactivate CTOR
    }

    /**
     * Create an identifiable using its Id. If the identifiable is found in the
     * cache then no new object is created (It will return the one from the
     * cache).
     *
     * @param id the id
     * @return the {@link Identifiable}, or <code>null</code> if not found.
     */
    public static Identifiable<?> getById(final Integer id) {
        final Criteria criteria = SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoIdentifiable.class);
        criteria.add(Restrictions.eq("id", id));
        DaoIdentifiable daoIdentifiable = (DaoIdentifiable) criteria.uniqueResult();
        if (daoIdentifiable == null) {
            return null;
        }
        return daoIdentifiable.accept(new DataVisitorConstructor());
    }
}
