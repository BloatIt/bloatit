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

package test.pages.components;

import test.HtmlElement;

import com.bloatit.common.Image;

/**
 * Used to display an image
 */
public class HtmlImage extends HtmlElement {
    private final static String IMAGE_DIRECTORY = "/resources/img";

    public HtmlImage(Image image) {
        super("img");
        String URI = "";
        if (image.isLocal()) {
            URI = HtmlImage.IMAGE_DIRECTORY + "/" + image.getIdentifier();
        } else {
            URI = image.getIdentifier();
        }
        addAttribute("src", URI);
    }

    public HtmlImage(Image image, String cssClass) {
        this(image);
        addAttribute("class", cssClass);
    }

}
