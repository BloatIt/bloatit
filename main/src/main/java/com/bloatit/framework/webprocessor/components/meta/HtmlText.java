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
package com.bloatit.framework.webprocessor.components.meta;

import com.bloatit.web.HtmlTools;

/**
 * <p>
 * Class used to directly escaped text into a page
 * </p>
 * <p>
 * Usage :
 * 
 * <pre>
 * {@code another_component.add(new HtmlText("A lot of beautiful text here"));}
 * </pre>
 * 
 * </p>
 * <p>
 * All the text inputed into an HtmlText will be escaped using the
 * <code>HtmlTools.escape(text)</code> method. It is therefore safe to display
 * using this method, but cannot be used to display an html tag.<br />
 * Html Tags should be displayed using {@link XmlText}
 * </p>
 */
public class HtmlText extends XmlText {

    /**
     * Creates a component to add text to a page
     * 
     * @param text the Html text to add to add
     */
    public HtmlText(final String text) {
        super(HtmlTools.escape(text));
    }
}
