package com.bloatit.framework.exceptions.specific;

/**
 * Throw this exception when a method received a null or empty argument and was
 * expecting a value.
 */
public class NonOptionalParameterException extends IllegalArgumentException {
    private static final long serialVersionUID = 684365471928810874L;

    /**
     * @see IllegalArgumentException#IllegalArgumentException(String, Throwable)
     */
    public NonOptionalParameterException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @see IllegalArgumentException#IllegalArgumentException(String)
     */
    public NonOptionalParameterException(final String message) {
        super(message);
    }

    /**
     * @see IllegalArgumentException#IllegalArgumentException(Throwable)
     */
    public NonOptionalParameterException(final Throwable cause) {
        super(cause);
    }

    /**
     * @see IllegalArgumentException#IllegalArgumentException()
     */
    public NonOptionalParameterException() {
        super();
    }
}
