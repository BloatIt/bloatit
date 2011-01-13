/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.exceptions;

import com.bloatit.web.utils.url.IndexPageUrl;
import com.bloatit.web.utils.url.Url;

// No serialization possible because IndexPageUrl is not serialisable.
@SuppressWarnings("serial")
public class RedirectException extends Exception {

    private final Url url;

    public RedirectException(final Url url) {
        if (url == null) {
            this.url = new IndexPageUrl();
        } else {
            this.url = url;
        }
    }

    public final Url getUrl() {
        return url;
    }

}
