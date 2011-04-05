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
package com.bloatit.framework.exceptions.highlevel;

/**
 * An exception used when a user right is lower than expected
 * <p>
 * This exception should be thrown whenever we try to perform an action for
 * which the user <b>should</b> have access but he doesn't.
 * </p>
 * <p>
 * Result of throwing this exception should display an error page to the user
 * informing him it doesn't work because of a bug, and server admin should be
 * warned immediatly.
 * </p>
 * <p>
 * Do <b>not</b> use this when the user doesn't have access, but it was to be
 * expected.
 * </p>
 */
public class ShallNotPassException extends RuntimeException {
    private static final long serialVersionUID = 6337131650418050251L;

    /**
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public ShallNotPassException(final String string, final Throwable cause) {
        super(string, cause);
    }

    /**
     * @see RuntimeException#RuntimeException(String)
     */
    public ShallNotPassException(final String message) {
        super(message);
    }

    /**
     * @see RuntimeException#RuntimeException(Throwable)
     */
    public ShallNotPassException(final Throwable cause) {
        super(cause);
    }

}
