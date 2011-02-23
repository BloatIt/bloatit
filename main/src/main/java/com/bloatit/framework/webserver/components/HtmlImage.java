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

package com.bloatit.framework.webserver.components;

import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webserver.components.meta.HtmlLeaf;
import com.bloatit.framework.webserver.url.Url;

/**
 * Used to display an image
 */
public class HtmlImage extends HtmlLeaf {
    private static final String IMAGE_DIRECTORY = "/resources/img";

    public HtmlImage(final Image image, String alt) {
        super("img");
        String uri = "";
        if (image.isLocal()) {
            uri = HtmlImage.IMAGE_DIRECTORY + "/" + image.getIdentifier();
        } else {
            uri = image.getIdentifier();
        }
        addAttribute("src", uri);
        addAttribute("alt", alt);
    }

    public HtmlImage(final Image image, String alt, final String cssClass) {
        this(image, alt);
        addAttribute("class", cssClass);
    }

    public HtmlImage(final Url imageUrl, String alt) {
        super("img");
        addAttribute("src", imageUrl.urlString());
        addAttribute("alt", alt);
    }

    public HtmlImage(final Url imageUrl, String alt, final String cssClass) {
        this(imageUrl, alt);
        addAttribute("class", cssClass);
    }
}
