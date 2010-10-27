package com.bloatit.model;


public abstract class Identifiable {
    
    protected abstract int getDaoId();

    public Integer getId() {
        return getDaoId();
    }
}
