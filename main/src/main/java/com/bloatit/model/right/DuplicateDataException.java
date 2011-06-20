package com.bloatit.model.right;


public class DuplicateDataException extends UnauthorizedOperationException {

    private static final long serialVersionUID = -2718286565324742260L;

    public DuplicateDataException(Action action) {
        super(action);
    }

}
