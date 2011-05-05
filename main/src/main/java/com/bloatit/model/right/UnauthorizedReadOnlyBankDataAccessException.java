package com.bloatit.model.right;

public class UnauthorizedReadOnlyBankDataAccessException extends UnauthorizedOperationException {
    private static final long serialVersionUID = 5639908838565712073L;

    public UnauthorizedReadOnlyBankDataAccessException(final Action action) {
        super(action);
    }

}
