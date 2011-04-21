/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.exceptions.lowlevel;

import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.url.IndexPageUrl;

/**
 * A redirect exception can occurs when generating a page. It will be handled by
 * the webServer to send a redirect on the right url.
 */
// No serialization possible because IndexPageUrl is not serializable.
public class RedirectException extends Exception {
    private static final long serialVersionUID = -5875435339130019317L;
    private final Url url;

    /**
     * Create a {@link RedirectException} that redirect to <code>url</code>.
     * 
     * @param url is where the user will be redirect to.
     */
    public RedirectException(final Url url) {
        if (url == null) {
            this.url = new IndexPageUrl();
        } else {
            this.url = url;
        }
    }

    /**
     * @return the redirection url.
     */
    public final Url getUrl() {
        return url;
    }

}
