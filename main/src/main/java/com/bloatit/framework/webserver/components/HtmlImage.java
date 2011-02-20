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

package com.bloatit.framework.webserver.components;

import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webserver.components.meta.HtmlLeaf;
import com.bloatit.framework.webserver.url.Url;

/**
 * Used to display an image
 */
public class HtmlImage extends HtmlLeaf {
    private static final String IMAGE_DIRECTORY = "/resources/img";

    public HtmlImage(final Image image) {
        super("img");
        String uri = "";
        if (image.isLocal()) {
            uri = HtmlImage.IMAGE_DIRECTORY + "/" + image.getIdentifier();
        } else {
            uri = image.getIdentifier();
        }
        addAttribute("src", uri);
    }

    public HtmlImage(final Image image, final String cssClass) {
        this(image);
        addAttribute("class", cssClass);
    }

    public HtmlImage(final Url imageUrl) {
        super("img");
        addAttribute("src", imageUrl.urlString());
    }

    public HtmlImage(final Url imageUrl, final String cssClass) {
        this(imageUrl);
        addAttribute("class", cssClass);
    }
}
