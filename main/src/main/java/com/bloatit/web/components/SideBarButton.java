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
package com.bloatit.web.components;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Image;
import com.bloatit.web.linkable.master.sidebar.SideBarElementLayout;

public class SideBarButton extends SideBarElementLayout {
    private final HtmlLink link;

    public SideBarButton(final String text, final Url to, final String imgUri) {
        this(text, to, imgUri, true);
    }

    public SideBarButton(final String text, final Url to, final String imgUri, boolean decorated) {
        final SideBarElementLayout createBox = new SideBarElementLayout();
        final HtmlDiv buttonMasterDiv;
        if (decorated) {
            buttonMasterDiv = new HtmlDiv("side_bar_button");
        } else {
            buttonMasterDiv = new HtmlDiv("side_bar_button_undecorated");
        }
        link = new HtmlLink(to.urlString(), buttonMasterDiv);
        link.setCssClass("side_bar_button_link");

        { // Box to hold feature creating button content
            final HtmlImage img = new HtmlImage(new Image(imgUri), text);
            img.setCssClass("side_bar_button_img");
            buttonMasterDiv.add(img);
            final HtmlDiv textBlock = new HtmlDiv("side_bar_button_text_box");
            { // Box to hold text of the button
                HtmlDiv textDiv;
                if (decorated) {
                    textDiv = new HtmlDiv("button side_bar_button_text");
                } else {
                    textDiv = new HtmlDiv("side_bar_button_text");
                }
                textBlock.add(textDiv);
                textDiv.addText(text);
            }
            buttonMasterDiv.add(textBlock);
        }
        createBox.add(link);
        add(link);
    }

    public HtmlElement asElement() {
        return link;
    }
}
