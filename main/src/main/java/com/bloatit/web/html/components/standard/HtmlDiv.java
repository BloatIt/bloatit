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

package com.bloatit.web.html.components.standard;

import com.bloatit.web.html.HtmlBranch;

/**
 * A class used to create a new html block (aka div)
 */
public class HtmlDiv extends HtmlBranch {

    public HtmlDiv() {
        super("div");
    }

    public HtmlDiv(final String cssClass) {
        super("div");
        addAttribute("class", cssClass);
    }

    public HtmlDiv(final String cssClass, final String id) {
        super("div");
        addAttribute("class", cssClass);
        addAttribute("id", id);
    }
}
