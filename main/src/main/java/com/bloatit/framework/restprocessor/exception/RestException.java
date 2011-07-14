//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework.restprocessor.exception;

import com.bloatit.framework.xcgiserver.HttpResponse.StatusCode;

/**
 * <p>
 * An exception thrown whenever there is an error in the <code>ReST</code>
 * process. When this exception is thrown, the <code>status</code> of the error
 * has to be set.
 * </p>
 * <p>
 * The server is then in charge of handling these exceptions and will generate
 * the correct message to the user based on this.
 * </p>
 * <p>
 * Statuses are :
 * <li><code>ERROR_301_MOVED_PERMANENTLY</code>: When the ReST resource moved</li>
 * <li><code>ERROR_302_FOUND</code>: When the resource exists but can't be
 * accessed at the moment (temporary error)</li>
 * <li><code>ERROR_401_UNAUTHORIZED</code>: When the user is not allowed to
 * access this ReST resource. Usually authenticating or getting new user rights
 * will be sufficient</li>
 * <li><code>ERROR_403_FORBIDDEN</code>: When the ReST resource shouldn't be
 * access whatever happens.</li>
 * <li><code>ERROR_404_NOT_FOUND</code>: When the resource doesn't exist. Note:
 * this is a permanent state contraryli to 302</li>
 * <li><code>ERROR_405_METHOD_NOT_ALLOWED</code>: When the method (GET, POST,
 * PUT, DELETE) is not allowed for this resource</li>
 * <li><code>ERROR_500_INTERNAL_SERVER_ERROR</code>: When an unexpected error
 * occurs on the server side</li>
 * <li><code>ERROR_501_NOT_IMPLEMENTED</code>: When this should be accessible
 * but has not been developed yet</li>
 * <li><code>ERROR_503_SERVICE_UNAVAILABLE</code>: When the service is
 * temporarily unavailable</li>
 * <li><code>OK_200</code>: <b>NEVER SET</b> an exception cannot throw an OK
 * return code</li>
 * </p>
 * <p>
 * The semantics of <code>ERROR_302_FOUND</code>,
 * <code>ERROR_500_INTERNAL_SERVER_ERROR</code> and
 * <code>ERROR_503_SERVICE_UNAVAILABLE</code> are very close and they can
 * usually be used one for the other.
 * </p>
 */
public class RestException extends Exception {
    private static final long serialVersionUID = 1789724106068640033L;
    private final StatusCode status;

    public RestException(final StatusCode status, final String message) {
        super(message);
        this.status = status;
    }

    public RestException(final StatusCode status, final String message, final Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public StatusCode getStatus() {
        return status;
    }
}
