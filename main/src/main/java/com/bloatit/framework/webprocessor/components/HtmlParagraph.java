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
package com.bloatit.framework.webprocessor.components;

import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;

public class HtmlParagraph extends HtmlBranch {

    public HtmlParagraph() {
        super("p");
    }

    /**
     * Creates an HtmlParagraph with a preset text
     * 
     * @param text the text that will be displayed
     */
    public HtmlParagraph(final String text) {
        this(new HtmlText(text));
    }

    /**
     * Creates an HtmlParagraph with a preset text and a given css style
     * 
     * @param cssClass the name of the css class applied
     * @param text the text that will be displayed
     */
    public HtmlParagraph(final String text, final String cssClass) {
        super("p");
        addAttribute("class", cssClass);
        add(new com.bloatit.framework.webprocessor.components.meta.HtmlText(text));
    }

    public HtmlParagraph(final HtmlText htmlText) {
        super("p");
        add(htmlText);
    }

    public HtmlParagraph(final HtmlElement content) {
        super("p");
        add(content);
    }
}
