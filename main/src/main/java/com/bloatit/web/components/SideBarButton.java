package com.bloatit.web.components;

import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.pages.master.sidebar.SideBarElementLayout;

public class SideBarButton extends SideBarElementLayout {
    private final HtmlLink link;
    
    public SideBarButton(final String text, final Url to, final String imgUri) {
        // Display of a button to create a feature
        final SideBarElementLayout createBox = new SideBarElementLayout();
        final HtmlDiv buttonMasterDiv = new HtmlDiv("side_bar_button");
        link = new HtmlLink(to.urlString(), buttonMasterDiv);

        { // Box to hold feature creating button content
            final HtmlImage img = new HtmlImage(new Image(imgUri), text);
            img.setCssClass("side_bar_button_img");
            buttonMasterDiv.add(img);
            final HtmlDiv textBlock = new HtmlDiv("side_bar_button_text_box");
            { // Box to hold text of the button
                final HtmlDiv textDiv = new HtmlDiv("side_bar_button_text");
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
