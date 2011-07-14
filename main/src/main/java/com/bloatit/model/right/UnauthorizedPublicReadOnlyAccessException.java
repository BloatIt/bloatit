package com.bloatit.model.right;

public class UnauthorizedPublicReadOnlyAccessException extends UnauthorizedOperationException {

    private static final long serialVersionUID = -2718286565324742260L;

    public UnauthorizedPublicReadOnlyAccessException(final Action action) {
        super(action);
    }

}
