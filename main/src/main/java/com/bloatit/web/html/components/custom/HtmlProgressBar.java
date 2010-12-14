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

package com.bloatit.web.html.components.custom;

import com.bloatit.web.html.HtmlText;

public class HtmlProgressBar extends HtmlText {

    public HtmlProgressBar(final float progress) {
        super("<div class=\"progress_bar_block\"><div class=\"progress_bar\"><div class=\"progress_bar_state\" style=\"width: " + progress
                + "%;\"></div></div></div>");
    }
}
