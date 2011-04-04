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

import java.io.IOException;

/**
 * An exception thrown when a component out of the application caused an error.
 * <p>
 * This exception is mainly used to encapsulate {@link IOException}.
 * </p>
 * <p>
 * When such an exception is caught, the server admin should be notified
 * immediately, as it can have very serious effect on the service. User should
 * be notified with an error page.
 * </p>
 */
public class ExternalErrorException extends RuntimeException {
    private static final long serialVersionUID = -5891157791160586955L;

    /**
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public ExternalErrorException(final String string, final Throwable cause) {
        super(string, cause);
    }

    /**
     * @see RuntimeException#RuntimeException(String)
     */
    public ExternalErrorException(final String message) {
        super(message);
    }

    /**
     * @see RuntimeException#RuntimeException(Throwable)
     */
    public ExternalErrorException(final Throwable cause) {
        super(cause);
    }
}
