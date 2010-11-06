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

package com.bloatit.web.htmlrenderer.htmlcomponent;

import com.bloatit.web.htmlrenderer.HtmlResult;
import com.bloatit.web.server.Request;

public class HtmlForm extends HtmlContainer {
    private final Request action;
    private Method method = Method.POST;

    public enum Method {
        GET, POST
    }

    public HtmlForm(Request action) {
        this.action = action;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public void generate(HtmlResult htmlResult) {
        String methodString = "";
        if (method == Method.GET) {
            methodString = "GET";
        } else {
            methodString = "POST";
        }

        htmlResult.write("<form action=\"" + action.getUrl() + "\" method=\"" + methodString + "\">");
        htmlResult.indent();

        super.generate(htmlResult);
        htmlResult.unindent();
        htmlResult.write("</form>");
    }
}
