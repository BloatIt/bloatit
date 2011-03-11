package com.bloatit.web.pages.master;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.meta.XmlNode;

public class Breadcrumb {

    List<HtmlLink> linkList = new ArrayList<HtmlLink>();


    public void pushLink(HtmlLink link) {
        linkList.add(link);
    }

    public XmlNode toXmlNode() {
        HtmlDiv breadcrumb = new HtmlDiv("breadcrumb");


        boolean first = true;

        for(HtmlLink link: linkList) {
            if(!first) {
                breadcrumb.addText(" > ");
            }
            breadcrumb.add(link);

            first = false;
        }

        return breadcrumb;

    }

}
