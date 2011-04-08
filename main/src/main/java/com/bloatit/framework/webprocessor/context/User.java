package com.bloatit.framework.webprocessor.context;

import java.util.Locale;

public interface User {

    public enum ActivationState {
        VALIDATING, ACTIVE, DELETED
    }
    
    public abstract String getUserLogin();

    public abstract Locale getUserLocale();

    public abstract ActivationState getActivationState();
    
    public abstract Integer getId();
}
