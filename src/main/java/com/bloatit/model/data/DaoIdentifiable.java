package com.bloatit.model.data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

/**
 * Base class to use with Hibernate. (A persistent class do not need to inherit
 * from DaoIdentifiable)
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DaoIdentifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    protected void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
