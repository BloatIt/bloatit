package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class DaoKudos extends DaoUserContent {

    @Basic(optional = false)
    private int value;

    public DaoKudos() {}

    public DaoKudos(DaoActor Actor, int value) {
        super(Actor);
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
