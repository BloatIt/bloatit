package com.bloatit.web.pages.master;

import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.XmlNode;

public class SideBarElementLayout extends HtmlDiv {


    private final HtmlTitle title;
    private final HtmlDiv body;

    public SideBarElementLayout() {
        super("side_bar_element");

        title = new HtmlTitle(2);
        title.setCssClass("side_bar_element_title");
        body = new HtmlDiv("side_bar_element_body");
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

}
