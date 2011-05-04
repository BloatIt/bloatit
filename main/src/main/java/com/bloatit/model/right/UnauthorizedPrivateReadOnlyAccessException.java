package com.bloatit.model.right;


public class UnauthorizedPrivateReadOnlyAccessException extends UnauthorizedOperationException {

    private static final long serialVersionUID = -2718286565324742260L;

    public UnauthorizedPrivateReadOnlyAccessException(Action action) {
        super(action);
    }

}
