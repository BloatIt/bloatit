/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.html.components.standard;

import com.bloatit.common.Image;
import com.bloatit.web.html.HtmlLeaf;

/**
 * Used to display an image
 */
public class HtmlImage extends HtmlLeaf {
    private final static String IMAGE_DIRECTORY = "/resources/img";

    public HtmlImage(final Image image) {
        super("img");
        String URI = "";
        if (image.isLocal()) {
            URI = HtmlImage.IMAGE_DIRECTORY + "/" + image.getIdentifier();
        } else {
            URI = image.getIdentifier();
        }
        addAttribute("src", URI);
    }

    public HtmlImage(final Image image, final String cssClass) {
        this(image);
        addAttribute("class", cssClass);
    }
}
