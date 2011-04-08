package com.bloatit.web.pages.master.sidebar;

import java.util.ArrayList;

import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.web.linkable.meta.bugreport.SideBarBugReportBlock;

public class PluginBox extends PlaceHolderElement {
    private final ArrayList<HtmlElement> plugins = new ArrayList<HtmlElement>();
    private final PlaceHolderElement pageContent;

    public PluginBox(Url url) {
        pageContent = new PlaceHolderElement();
        plugins.add(pageContent);
        plugins.add(new SideBarBugReportBlock(url));
        for (HtmlElement plugin : plugins) {
            add(plugin);
        }
    }

    public void addPageContent(SideBarElementLayout content) {
        pageContent.add(content);
    }
}
