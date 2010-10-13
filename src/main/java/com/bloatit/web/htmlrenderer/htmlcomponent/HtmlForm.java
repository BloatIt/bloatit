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
import com.bloatit.web.server.Action;

public class HtmlForm extends HtmlContainer{
    private final Action action;

    public HtmlForm(Action action){
        this.action = action;
    }

    @Override
    public void generate(HtmlResult htmlResult) {
        htmlResult.write("<form action=\""+this.action.getUrl()+"\" method=\"POST\">");
        htmlResult.indent();

        super.generate(htmlResult);
        htmlResult.unindent();
        htmlResult.write("</form>");
    }
}