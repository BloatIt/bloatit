/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.htmlrenderer.htmlcomponent;

import com.bloatit.common.Image;
import com.bloatit.web.htmlrenderer.HtmlResult;

/**
 * Used to display an image
 */
public class HtmlImage extends HtmlComponent {
    private final Image image;

    /**
     * The directory where the images are stored
     */
    private final static String IMAGE_DIRECTORY = "/resources/img";
    private final String cssClass;

    public HtmlImage(Image image){
        this(image,"");
    }
    
    public HtmlImage(Image image, String cssClass){
        this.image = image;
        this.cssClass = cssClass;
    }

    @Override
    public void generate(HtmlResult htmlResult) {
        String URI = "";
        if(this.image.isLocal()){
            URI = HtmlImage.IMAGE_DIRECTORY + "/" + this.image.getIdentifier();
        }else{
            URI = image.getIdentifier();
        }

        String css = "";
        if(this.cssClass != null ){
            css = "class=\"" + this.cssClass + "\"";
        }
        htmlResult.write("<img src=\"" + URI +"\" " + css +" />");
    }
}