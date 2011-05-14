package com.bloatit.model.right;

public class UnauthorizedBankDataAccessException extends UnauthorizedOperationException {

    private static final long serialVersionUID = 237700510498542246L;

    public UnauthorizedBankDataAccessException(final Action action) {
        super(action);
    }

}
