package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class DaoKudos extends DaoUserContent {

    @Basic(optional = false)
    private int value;

    public DaoKudos() {}

    public DaoKudos(DaoMember member, int value) {
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
