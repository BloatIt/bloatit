package com.bloatit.web.pages.master;

import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.XmlNode;

public class SideBarElementLayout extends HtmlDiv {

    private final HtmlTitle title;
    private final HtmlDiv body;
    private final PlaceHolderElement floatRight;

    public SideBarElementLayout() {
        super("side_bar_element");

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
