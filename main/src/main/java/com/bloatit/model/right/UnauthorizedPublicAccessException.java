package com.bloatit.model.right;


public class UnauthorizedPublicAccessException extends UnauthorizedOperationException {

    private static final long serialVersionUID = -2718286565324742260L;

    public UnauthorizedPublicAccessException(Action action) {
        super(action);
    }

}
