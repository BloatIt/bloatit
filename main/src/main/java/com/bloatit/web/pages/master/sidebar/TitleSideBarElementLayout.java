package com.bloatit.web.pages.master.sidebar;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;

public class TitleSideBarElementLayout extends SideBarElementLayout {

    private final HtmlTitle title;
    private final HtmlDiv body;
    private final PlaceHolderElement floatRight;

    public TitleSideBarElementLayout() {
        super();

        title = new HtmlTitle(1);
        title.setCssClass("side_bar_element_title");
        body = new HtmlDiv("side_bar_element_body");
        floatRight = new PlaceHolderElement();
        super.add(floatRight);
        super.add(title);
        super.add(body);
    }

    public void setTitle(String title) {
        this.title.addText(title);
    }

    @Override
    public HtmlBranch add(XmlNode element) {
        body.add(element);
        return this;
    }

    public HtmlBranch setFloatRight(HtmlElement element) {
        floatRight.add(new HtmlDiv("float_right").add(element));
        return this;
    }

}
