package com.bloatit.model;


public abstract class Identifiable extends Unlockable{
    
    protected abstract int getDaoId();

    public Integer getId() {
        return getDaoId();
    }
}
