/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with
 * BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package test.html.components.standard;


/**
 * Used to display block of texts
 */
public class HtmlText extends HtmlParagraph {
    /**
     * Creates an HtmlText with a preset text
     * 
     * @param text the text that will be displayed
     */
    public HtmlText(final String text) {
        super();
        add(new test.html.HtmlText(text));
    }

    /**
     * Creates an HtmlText with a preset text and a given css style
     * 
     * @param cssClass the name of the css class applied
     * @param text the texit that will be displayed
     */
    public HtmlText(final String text, final String cssClass) {
        super();
        addAttribute("class", cssClass);
        add(new test.html.HtmlText(text));
    }

}
