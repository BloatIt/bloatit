package com.bloatit.framework.rest;

import com.bloatit.data.IdentifiableInterface;

public abstract class RestElement<M extends IdentifiableInterface> {
    public abstract boolean isNull();
}
