package com.bloatit.framework.restprocessor;

import com.bloatit.model.IdentifiableInterface;

public abstract class RestElement<M extends IdentifiableInterface> {
    public abstract boolean isNull();
}
