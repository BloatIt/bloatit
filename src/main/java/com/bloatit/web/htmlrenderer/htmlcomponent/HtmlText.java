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

public class HtmlText extends HtmlComponent {

    final private String text;
    final private String cssClass;

    public HtmlText(String text) {
        this.text = text;
        cssClass = null;
    }

    public HtmlText(String cssClass, String text) {
        this.text = text;
        this.cssClass = cssClass;
    }

    @Override
    public void generate(HtmlResult htmlResult) {
        if (cssClass == null) {
            htmlResult.write("<p>" + text + "</p>");
        } else {
            htmlResult.write("<p class=\"" + cssClass + "\">" + text + "</p>");
        }
    }
}
