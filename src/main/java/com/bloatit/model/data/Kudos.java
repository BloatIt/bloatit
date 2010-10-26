package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class Kudos extends UserContent {

    @Basic(optional = false)
    private int value;

    public Kudos() {}

    public Kudos(Actor actor, int value) {
        super(actor);
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
