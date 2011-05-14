package com.bloatit.model.right;


public class UnauthorizedPrivateAccessException extends UnauthorizedOperationException {

    private static final long serialVersionUID = -2718286565324742260L;

    public UnauthorizedPrivateAccessException(Action action) {
        super(action);
    }

}
