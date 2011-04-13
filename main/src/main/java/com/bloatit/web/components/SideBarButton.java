package com.bloatit.web.components;

import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.web.pages.master.sidebar.SideBarElementLayout;
import com.bloatit.web.url.CreateFeaturePageUrl;

public class SideBarButton extends SideBarElementLayout {
    public SideBarButton(String text, String imgUri) {
        // Display of a button to create a feature
        SideBarElementLayout createBox = new SideBarElementLayout();
        HtmlDiv buttonMasterDiv = new HtmlDiv("side_bar_button");
        HtmlLink link = new HtmlLink(new CreateFeaturePageUrl().urlString(), buttonMasterDiv);
        { // Box to hold feature creating button content
            HtmlImage img = new HtmlImage(new Image(imgUri), text);
            img.setCssClass("side_bar_button_img");
            buttonMasterDiv.add(img);
            HtmlDiv textBlock = new HtmlDiv("side_bar_button_text_box");
            { // Box to hold text of the button
                HtmlDiv textDiv = new HtmlDiv("side_bar_button_text");
                textBlock.add(textDiv);
                textDiv.addText(text);
            }
            buttonMasterDiv.add(textBlock);
        }
        createBox.add(link);
        
        add(link);
    }
}
