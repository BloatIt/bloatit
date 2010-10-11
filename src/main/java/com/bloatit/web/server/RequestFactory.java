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

package com.bloatit.web.server;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


class RequestFactory<T extends Request> {

    final private Class<T> type;

    private RequestFactory() {
        type = null;
    };

    public RequestFactory(Class<T> type) {
        this.type = type;
    }

    public T build(Session session, Map<String, String> parameters) {

        T request = null;

        try {
            request = type.newInstance();
            request.init(session, parameters);
            
        } catch (InstantiationException ex) {
            Logger.getLogger(RequestFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(RequestFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        return request;
    }

}
