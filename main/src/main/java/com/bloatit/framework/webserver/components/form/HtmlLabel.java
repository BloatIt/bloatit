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

package com.bloatit.framework.webserver.components.form;

import com.bloatit.framework.webserver.components.meta.HtmlLeaf;

/**
 * Internal class used to represend text displayed next to a form field (legend
 * of the form field)
 */
public final class HtmlLabel extends HtmlLeaf {
    /**
     * Creates a label with displayed text
     * 
     * @param content the displayed text
     */
    protected HtmlLabel(final String content) {
        super("label");
        addText(content);
    }

    /**
     * Links a label with a formfield
     * 
     * @param forId the Id of the formfield
     */
    protected void setFor(final String forId) {
        addAttribute("for", forId);
    }
}
