/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.exceptions.general;

/**
 * An exception to throw whenever the user is messing with input parameters.
 * <p>
 * Throwing this exception should be restricted to events where the user input
 * does not match the expected input. Result of throwing this exception will be
 * displaying an error page informing the user he made a mistake, and inviting
 * him to retry. <br/>
 * The error page should also provide tool to report a bug, if the input was
 * indeed correct but we misinterpreted it.
 * </p>
 */
public class MeanUserException extends RuntimeException {
    private static final long serialVersionUID = -5680372902136130104L;

    /**
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public MeanUserException(final String string, final Throwable cause) {
        super(string, cause);
    }

    /**
     * @see RuntimeException#RuntimeException(String)
     */
    public MeanUserException(final String message) {
        super(message);
    }

    /**
     * @see RuntimeException#RuntimeException(Throwable)
     */
    public MeanUserException(final Throwable cause) {
        super(cause);
    }
}
