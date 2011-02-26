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
package com.bloatit.data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.search.annotations.DocumentId;

/**
 * Base class to use with Hibernate. (A persistent class do not need to inherit
 * from DaoIdentifiable) When using DaoIdentifiable as a superClass, you ensure
 * to have a id column in your table. There is no DaoIdentifiable Table.
 */
@MappedSuperclass
public abstract class DaoIdentifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @DocumentId
    private Integer id;

    public Integer getId() {
        return this.id;
    }

    public abstract <ReturnType> ReturnType accept(DataClassVisitor<ReturnType> visitor);

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoIdentifiable() {
        super();
    }

    // ======================================================================
    // equals and hashcode
    // ======================================================================

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

}
